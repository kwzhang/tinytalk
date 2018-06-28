package com.lge.architect.tinytalk.identity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends BaseIdentityActivity implements IdentificationListener {

  public static final int REQUEST_RESET_PASSWORD = 304;

  @BindView(R.id.phone_number) EditText phoneNumberView;
  @BindView(R.id.card_form) CardForm cardForm;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.reset_password_activity);
    ButterKnife.bind(this);
    setupActionBar();

    cardForm.cardRequired(true)
        .maskCardNumber(true)
        .expirationRequired(true)
        .cvvRequired(true)
        .maskCvv(true)
        .actionLabel(getString(R.string.action_reset))
        .setup(this);
  }

  @OnClick(R.id.reset_button)
  public void onReset() {
    final String phoneNumber = phoneNumberView.getText().toString();
    final String cardNumber = cardForm.getCardNumber();
    final String expiryDate = cardForm.getExpirationDateEditText().getText().toString();
    final String cvvCode = cardForm.getCvv();

    if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(cardNumber) &&
        !TextUtils.isEmpty(expiryDate) && !TextUtils.isEmpty(cvvCode)) {
      RestApi.getInstance(this).resetPassword(phoneNumber, cardNumber, expiryDate, cvvCode, this);
    } else {
      Toast.makeText(ResetPasswordActivity.this, getString(R.string.prompt_complete_form), Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ChangePasswordActivity.REQUEST_RECOVER_PASSWORD) {
      if (resultCode == RESULT_OK) {
        setResult(resultCode, data);
        finish();
      }
    }
  }

  @Override
  public void onComplete(String name, String email, String number, String password) {
    showProgress(false);

    Intent intent = new Intent(this, ChangePasswordActivity.class);
    intent.putExtra(ChangePasswordActivity.EXTRA_PHONE_NUMBER, number);
    intent.putExtra(ChangePasswordActivity.EXTRA_OLD_PASSWORD, password);

    ActivityCompat.startActivityForResult(this, intent, ChangePasswordActivity.REQUEST_RECOVER_PASSWORD,null);
  }

  @Override
  public void onFailure(String reason) {
    showProgress(false);

    Toast.makeText(this, getString(R.string.prompt_registration_failure) + ": " + reason, Toast.LENGTH_LONG).show();
  }
}
