package com.lge.architect.tinytalk.identity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;

public class ResetPasswordActivity extends AppCompatActivity implements IdentificationListener {

  public static final int REQUEST_RESET_PASSWORD = 304;

  private View progressView;
  private View resetFormView;
  private EditText phoneNumberView;
  private CardForm cardForm;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.reset_password_activity);
    setupActionBar();

    phoneNumberView = findViewById(R.id.phone_number);
    progressView = findViewById(R.id.reset_progress);
    resetFormView = findViewById(R.id.reset_form);

    cardForm = findViewById(R.id.card_form);
    cardForm.cardRequired(true)
        .maskCardNumber(true)
        .expirationRequired(true)
        .cvvRequired(true)
        .maskCvv(true)
        .actionLabel(getString(R.string.action_reset))
        .setup(this);

    Button updateButton = findViewById(R.id.reset_button);
    updateButton.setOnClickListener(view -> {
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
    });
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

    resetFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    resetFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        resetFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
