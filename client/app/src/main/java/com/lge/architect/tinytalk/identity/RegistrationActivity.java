package com.lge.architect.tinytalk.identity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.command.model.User;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends BaseIdentityActivity implements IdentificationListener {

  public static final int REQUEST_NEW_REGISTRATION = 0;

  @BindView(R.id.email) AutoCompleteTextView emailView;
  @BindView(R.id.name) AutoCompleteTextView nameView;
  @BindView(R.id.address) PlacesAutocompleteTextView addressView;
  @BindView(R.id.password) EditText passwordView;
  @BindView(R.id.confirm_password) EditText confirmPasswordView;
  @BindView(R.id.card_form) CardForm cardForm;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.registration_activity);
    ButterKnife.bind(this);

    passwordView.setOnEditorActionListener((textView, id, keyEvent) -> {
      if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
        return true;
      }
      return false;
    });

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

    cardForm.cardRequired(true)
        .maskCardNumber(true)
        .expirationRequired(true)
        .cvvRequired(true)
        .maskCvv(true)
        .actionLabel(getString(R.string.action_register))
        .setup(this);
  }

  @OnClick(R.id.register_button)
  public void onRegister() {
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
