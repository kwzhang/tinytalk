package com.lge.architect.tinytalk.identity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.command.model.User;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.v4.util.PatternsCompat.EMAIL_ADDRESS;

public class RegistrationActivity extends AppCompatActivity implements IdentificationListener {

  private static final int REQUEST_READ_CONTACTS = 0;

  public static final int REQUEST_NEW_REGISTRATION = 0;

  private AutoCompleteTextView emailView;
  private AutoCompleteTextView nameView;
  private PlacesAutocompleteTextView addressView;
  private View progressView;
  private View registerFormView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.registration_activity);

    nameView = findViewById(R.id.name);
    emailView = findViewById(R.id.email);
    populateAutoComplete();

    EditText passwordView = findViewById(R.id.password);
    passwordView.setOnEditorActionListener((textView, id, keyEvent) -> {
      if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
        return true;
      }
      return false;
    });

    EditText confirmPasswordView = findViewById(R.id.confirm_password);

    progressView = findViewById(R.id.register_progress);
    registerFormView = findViewById(R.id.register_form);

    addressView = findViewById(R.id.address);
    addressView.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        addressView.showClearButton(s != null && s.length() > 0);
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });

    CardForm cardForm = findViewById(R.id.card_form);
    cardForm.cardRequired(true)
        .maskCardNumber(true)
        .expirationRequired(true)
        .cvvRequired(true)
        .maskCvv(true)
        .actionLabel(getString(R.string.action_register))
        .setup(this);

    Button registerButton = findViewById(R.id.register_button);
    registerButton.setOnClickListener(view -> {
      final String password = passwordView.getText().toString();
      final String confirmPassword = confirmPasswordView.getText().toString();
      final String name = nameView.getText().toString();
      final String email = emailView.getText().toString();
      final String address = addressView.getText().toString();
      final String cardNumber = cardForm.getCardNumber();
      final String expiryDate = cardForm.getExpirationDateEditText().getText().toString();
      final String cvvCode = cardForm.getCvv();

      if (cardForm.isValid() &&
          !TextUtils.isEmpty(name) && !TextUtils.isEmpty(address) &&
          !TextUtils.isEmpty(email) && isValidEmail(email) &&
          !TextUtils.isEmpty(password) && password.equals(confirmPassword)) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RegistrationActivity.this)
            .setTitle(R.string.title_confirm_registration)
            .setMessage(
                getString(R.string.prompt_name) + ": " + name + "\n" +
                getString(R.string.prompt_email) + ": " + email + "\n" +
                getString(R.string.prompt_card_number) + ": " + cardNumber + "\n" +
                getString(R.string.prompt_card_expiry_date) + ": " + expiryDate  + "\n" +
                getString(R.string.prompt_card_cvv) + ": " + cvvCode + "\n")
            .setPositiveButton(R.string.action_confirm,
                (dialogInterface, i) -> {
                  RestApi.getInstance(this).register(new User(name, email, password, address, cardNumber, expiryDate, cvvCode), this);

                  dialogInterface.dismiss();

                  showProgress(true);
                })
            .setNegativeButton(android.R.string.cancel,
                (dialogInterface, i) -> dialogInterface.dismiss());
        alertBuilder.show();
      } else {
        Toast.makeText(RegistrationActivity.this, getString(R.string.prompt_complete_form), Toast.LENGTH_LONG).show();
      }
    });
  }

  private void populateAutoComplete() {
    if (!mayRequestContacts()) {
      return;
    }
  }

  private boolean mayRequestContacts() {
    if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
      return true;
    }
    if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
      Snackbar.make(emailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
          .setAction(android.R.string.ok, v -> requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS));
    } else {
      requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
    }
    return false;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == REQUEST_READ_CONTACTS) {
      if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        populateAutoComplete();
      }
    }
  }

  public static boolean isValidEmail(CharSequence target) {
    return (target != null) && EMAIL_ADDRESS.matcher(target).matches();
  }

  private void showProgress(final boolean show) {
    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
    progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      }
    });

    registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    registerFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      }
    });
  }

  @Override
  public void onComplete(String name, String email, String number, String password) {
    showProgress(false);

    Identity.getInstance(this).save(this, name, number, password);

    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void onFailure(String reason) {
    showProgress(false);

    Toast.makeText(this, getString(R.string.prompt_registration_failure) + ": " + reason, Toast.LENGTH_LONG).show();
  }
}
