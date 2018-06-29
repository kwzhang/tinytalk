package com.lge.architect.tinytalk.identity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.command.model.CreditCard;
import com.lge.architect.tinytalk.command.model.User;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageAccountActivity extends BaseIdentityActivity implements IdentificationListener, UserInfoListener {

  @BindView(R.id.email) AutoCompleteTextView emailView;
  @BindView(R.id.name) AutoCompleteTextView nameView;
  @BindView(R.id.old_password) EditText oldPasswordView;
  @BindView(R.id.new_password) EditText newPasswordView;
  @BindView(R.id.confirm_password) EditText confirmPasswordView;
  @BindView(R.id.address) PlacesAutocompleteTextView addressView;
  @BindView(R.id.card_form) CardForm cardForm;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.manage_account_activity);
    ButterKnife.bind(this);
    setupActionBar();

    nameView.setText(Identity.getInstance(this).getName());
    emailView.setText(Identity.getInstance(this).getEmail());
    oldPasswordView.requestFocus();

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
        .actionLabel(getString(R.string.action_update))
        .setup(this);

    RestApi.getInstance(this).getUser(this, this);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.menu_manage_account, menu);

    super.onPrepareOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case R.id.action_logout:
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(R.string.action_confirm_logout)
            .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel())
            .setPositiveButton(R.string.action_logout, (dialog, which) -> {
              Identity.getInstance(this).clear(this);
              dialog.dismiss();

              setResult(RESULT_CANCELED);
              finish();
            });
        builder.show();
        return true;
    }

    return false;
  }

  @OnClick(R.id.update_button)
  public void onUpdate() {
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
      AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ManageAccountActivity.this)
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
      Toast.makeText(ManageAccountActivity.this, getString(R.string.prompt_complete_form), Toast.LENGTH_LONG).show();
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
