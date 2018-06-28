package com.lge.architect.tinytalk.identity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;

import static android.support.v4.util.PatternsCompat.EMAIL_ADDRESS;

public abstract class BaseIdentityActivity extends AppCompatActivity {

  @BindView(android.R.id.content) View contentView;
  @BindView(android.R.id.progress) View progressView;

  protected void showProgress(final boolean show) {
    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
    progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      }
    });

    contentView.setVisibility(show ? View.GONE : View.VISIBLE);
    contentView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        contentView.setVisibility(show ? View.GONE : View.VISIBLE);
      }
    });
  }

  protected void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  public static boolean isValidEmail(CharSequence target) {
    return (target != null) && EMAIL_ADDRESS.matcher(target).matches();
  }
}
