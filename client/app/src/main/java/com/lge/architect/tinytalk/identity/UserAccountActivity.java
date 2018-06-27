package com.lge.architect.tinytalk.identity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.command.model.CreditCard;
import com.lge.architect.tinytalk.command.model.User;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;

import static android.support.v4.util.PatternsCompat.EMAIL_ADDRESS;

public class UserAccountActivity extends AppCompatActivity implements IdentificationListener, UserInfoListener {

  private AutoCompleteTextView emailView;
  private AutoCompleteTextView nameView;
  private PlacesAutocompleteTextView addressView;
  private View progressView;
  private View updateFormView;
  private CardForm cardForm;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_account_activity);
    setupActionBar();

    nameView = findViewById(R.id.name);
    nameView.setText(Identity.getInstance(this).getName());

    emailView = findViewById(R.id.email);
    nameView.setText(Identity.getInstance(this).getEmail());

    EditText oldPasswordView = findViewById(R.id.old_password);
    oldPasswordView.requestFocus();

    EditText newPasswordView = findViewById(R.id.new_password);
    EditText confirmPasswordView = findViewById(R.id.confirm_password);

    progressView = findViewById(R.id.update_progress);
    updateFormView = findViewById(R.id.update_form);

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

    cardForm = findViewById(R.id.card_form);
    cardForm.cardRequired(true)
        .maskCardNumber(true)
        .expirationRequired(true)
        .cvvRequired(true)
        .maskCvv(true)
        .actionLabel(getString(R.string.action_update))
        .setup(this);

    Button updateButton = findViewById(R.id.update_button);
    updateButton.setOnClickListener(view -> {
      final String oldPassword = oldPasswordView.getText().toString();
      final String newPassword = newPasswordView.getText().toString();
      final String confirmPassword = confirmPasswordView.getText().toString();
      final String name = nameView.getText().toString();
      final String email = emailView.getText().toString();
      final String address = addressView.getText().toString();
      final String cardNumber = cardForm.getCardNumber();
      final String expiryDate = cardForm.getExpirationDateEditText().getText().toString();
      final String cvvCode = cardForm.getCvv();

      if (!TextUtils.isEmpty(oldPassword) &&
          (TextUtils.isEmpty(newPassword) || (TextUtils.isEmpty(email) || isValidEmail(email)) ||
          (!TextUtils.isEmpty(newPassword) && newPassword.equals(confirmPassword)))) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UserAccountActivity.this)
            .setTitle(R.string.title_confirm_registration)
            .setMessage(
                (!TextUtils.isEmpty(name) ? getString(R.string.prompt_name) + ": " + name + "\n" : "") +
                (!TextUtils.isEmpty(email) ? getString(R.string.prompt_email) + ": " + email + "\n" : "") +
                (cardForm.isValid() ?
                    (getString(R.string.prompt_card_number) + ": " + cardNumber + "\n" +
                    getString(R.string.prompt_card_expiry_date) + ": " + expiryDate  + "\n" +
                    getString(R.string.prompt_card_cvv) + ": " + cvvCode + "\n") : ""))
            .setPositiveButton(R.string.action_confirm,
                (dialogInterface, i) -> {
                  RestApi.getInstance(this).updateUser(Identity.getInstance(this).getNumber(), oldPassword,
                      new User(name, email, newPassword, address, cardNumber, expiryDate, cvvCode), this);

                  dialogInterface.dismiss();

                  showProgress(true);
                })
            .setNegativeButton(android.R.string.cancel,
                (dialogInterface, i) -> dialogInterface.dismiss());
        alertBuilder.show();
      } else {
        Toast.makeText(UserAccountActivity.this, getString(R.string.prompt_complete_form), Toast.LENGTH_LONG).show();
      }
    });

    RestApi.getInstance(this).getUser(this, this);
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

    updateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    updateFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        updateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      }
    });
  }

  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public void onComplete(String name, String email, String number, String password) {
    showProgress(false);

    Identity.getInstance(this).save(this, name, number, password);

    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void onResponse(User user) {
    showProgress(false);

    nameView.setText(user.getName());
    emailView.setText(user.getEmail());
    addressView.setText(user.getAddress());

    CreditCard creditCard = user.getCreditCard();
    EditText cardNumber = findViewById(getResourceId("bt_card_form_card_number"));
    cardNumber.setText(creditCard.getNumber());

    EditText expiryDate = findViewById(getResourceId("bt_card_form_expiration"));
    expiryDate.setText(creditCard.getExpiryDate());

    EditText cardCvv = findViewById(getResourceId("bt_card_form_cvv"));
    cardCvv.setText(creditCard.getCvv());
  }

  public int getResourceId(String resId) {
    return getResources().getIdentifier(resId, "id", getPackageName());
  }

  @Override
  public void onFailure(String reason) {
    showProgress(false);

    Toast.makeText(this, getString(R.string.prompt_registration_failure) + ": " + reason, Toast.LENGTH_LONG).show();
  }
}
