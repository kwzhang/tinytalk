package com.lge.architect.tinytalk.identity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;

public class LoginActivity extends AppCompatActivity implements IdentificationListener {

  public static final int REQUEST_LOG_IN = 0;

  private AutoCompleteTextView phoneNumberView;
  private View progressView;
  private View loginFormView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.login_activity);

    phoneNumberView = findViewById(R.id.phone_number);
    EditText passwordView = findViewById(R.id.password);
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

    progressView = findViewById(R.id.login_progress);
    loginFormView = findViewById(R.id.login_form);

    Button loginButton = findViewById(R.id.login_button);
    loginButton.setOnClickListener(view -> {
      final String password = passwordView.getText().toString();
      final String phoneNumber = phoneNumberView.getText().toString();

      if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password)) {
        showProgress(true);
        RestApi.getInstance(this).login(phoneNumber, password, this);
      } else {
        Toast.makeText(LoginActivity.this, getString(R.string.prompt_complete_form), Toast.LENGTH_LONG).show();
      }
    });

    Button registerButton = findViewById(R.id.register_button);
    registerButton.setOnClickListener(view -> {
      Intent intent = new Intent(this, RegistrationActivity.class);

      ActivityCompat.startActivityForResult(this, intent, RegistrationActivity.REQUEST_NEW_REGISTRATION,null);
    });

    Button resetButton = findViewById(R.id.reset_password_button);
    resetButton.setOnClickListener(view -> {
      Intent intent = new Intent(this, ResetPasswordActivity.class);

      ActivityCompat.startActivityForResult(this, intent, ResetPasswordActivity.REQUEST_RESET_PASSWORD,null);
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

    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    loginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      }
    });
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
