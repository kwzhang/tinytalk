package com.lge.architect.tinytalk.identity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseIdentityActivity implements IdentificationListener {

  public static final int REQUEST_RECOVER_PASSWORD = 305;

  public static final String EXTRA_PHONE_NUMBER = "phone_number";
  public static final String EXTRA_OLD_PASSWORD = "old_password";

  private String phoneNumber;
  private String oldPassword;

  @BindView(R.id.new_password) EditText newPasswordView;
  @BindView(R.id.confirm_password) EditText confirmPasswordView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.change_password_activity);
    ButterKnife.bind(this);
    setupActionBar();

    if (savedInstanceState != null) {
      phoneNumber = savedInstanceState.getString(EXTRA_PHONE_NUMBER);
      oldPassword = savedInstanceState.getString(EXTRA_OLD_PASSWORD);
    } else {
      phoneNumber = getIntent().getStringExtra(EXTRA_PHONE_NUMBER);
      oldPassword = getIntent().getStringExtra(EXTRA_OLD_PASSWORD);
    }
  }

  @OnClick(R.id.change_button)
  public void onChange() {
    final String newPassword = newPasswordView.getText().toString();
    final String confirmPassword = confirmPasswordView.getText().toString();

    if (!TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(newPassword) && newPassword.equals(confirmPassword)) {
      RestApi.getInstance(this).changePassword(phoneNumber, oldPassword, newPassword, this);
    } else {
      Toast.makeText(ChangePasswordActivity.this, getString(R.string.prompt_complete_form), Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putString(EXTRA_PHONE_NUMBER, phoneNumber);
    outState.putString(EXTRA_OLD_PASSWORD, oldPassword);
  }

  @Override
  public void onComplete(String name, String email, String number, String password) {
    showProgress(false);

    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void onFailure(String reason) {
    showProgress(false);

    Toast.makeText(this, getString(R.string.prompt_registration_failure) + ": " + reason, Toast.LENGTH_LONG).show();
  }
}
