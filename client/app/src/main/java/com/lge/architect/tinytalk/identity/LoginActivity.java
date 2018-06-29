package com.lge.architect.tinytalk.identity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseIdentityActivity implements IdentificationListener {

  public static final int REQUEST_LOG_IN = 0;

  @BindView(R.id.phone_number) AutoCompleteTextView phoneNumberView;
  @BindView(R.id.password) EditText passwordView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.login_activity);
    ButterKnife.bind(this);

    passwordView.setOnEditorActionListener((textView, id, keyEvent) -> {
      if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
        return true;
      }
      return false;
    });

    String number = Identity.getInstance(this).getNumber();
    if (!TextUtils.isEmpty(number)) {
      phoneNumberView.setText(number);
      phoneNumberView.requestFocus();
    }
  }

  @OnClick(R.id.login_button)
  public void login() {
    final String password = passwordView.getText().toString();
    final String phoneNumber = phoneNumberView.getText().toString();

    if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password)) {
      showProgress(true);
      RestApi.getInstance(this).login(phoneNumber, password, this);
    } else {
      Toast.makeText(LoginActivity.this, getString(R.string.prompt_complete_form), Toast.LENGTH_LONG).show();
    }
  }

  @OnClick(R.id.register_button)
  public void register() {
    Intent intent = new Intent(this, RegistrationActivity.class);

    ActivityCompat.startActivityForResult(this, intent, RegistrationActivity.REQUEST_NEW_REGISTRATION,null);
  }

  @OnClick(R.id.reset_password_button)
  public void reset() {
    Intent intent = new Intent(this, ResetPasswordActivity.class);

    ActivityCompat.startActivityForResult(this, intent, ResetPasswordActivity.REQUEST_RESET_PASSWORD,null);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RegistrationActivity.REQUEST_NEW_REGISTRATION) {
      if (resultCode == RESULT_OK) {
        setResult(resultCode, data);
        finish();
      }
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

    Toast.makeText(this, getString(R.string.prompt_login_failure) + ": " + reason, Toast.LENGTH_LONG).show();
  }
}
