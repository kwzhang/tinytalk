package com.lge.architect.tinytalk.identity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;

public class ChangePasswordActivity extends AppCompatActivity implements IdentificationListener {

  public static final int REQUEST_RECOVER_PASSWORD = 305;

  public static final String EXTRA_PHONE_NUMBER = "phone_number";
  public static final String EXTRA_OLD_PASSWORD = "old_password";

  private String phoneNumber;
  private String oldPassword;
  private EditText newPasswordView;
  private EditText confirmPasswordView;
  private View changeFormView;
  private View progressView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.change_password_activity);
    setupActionBar();

    if (savedInstanceState != null) {
      phoneNumber = savedInstanceState.getString(EXTRA_PHONE_NUMBER);
      oldPassword = savedInstanceState.getString(EXTRA_OLD_PASSWORD);
    } else {
      phoneNumber = getIntent().getStringExtra(EXTRA_PHONE_NUMBER);
      oldPassword = getIntent().getStringExtra(EXTRA_OLD_PASSWORD);
    }

    progressView = findViewById(R.id.change_progress);
    changeFormView = findViewById(R.id.change_form);
    newPasswordView = findViewById(R.id.new_password);
    confirmPasswordView = findViewById(R.id.confirm_password);

    Button updateButton = findViewById(R.id.change_button);
    updateButton.setOnClickListener(view -> {
      final String newPassword = newPasswordView.getText().toString();
      final String confirmPassword = confirmPasswordView.getText().toString();

      if (!TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(newPassword) && newPassword.equals(confirmPassword)) {
        RestApi.getInstance(this).changePassword(phoneNumber, oldPassword, newPassword, this);
      } else {
        Toast.makeText(ChangePasswordActivity.this, getString(R.string.prompt_complete_form), Toast.LENGTH_LONG).show();
      }
    });
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putString(EXTRA_PHONE_NUMBER, phoneNumber);
    outState.putString(EXTRA_OLD_PASSWORD, oldPassword);
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

    changeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    changeFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        changeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void onFailure(String reason) {
    showProgress(false);

    Toast.makeText(this, getString(R.string.prompt_registration_failure) + ": " + reason, Toast.LENGTH_LONG).show();
  }
}
