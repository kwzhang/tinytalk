package com.lge.architect.tinytalk.billing;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;

import org.joda.time.DateTime;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillingFragment extends Fragment implements BillingListener {
  private static final String TAG = BillingFragment.class.getSimpleName();

  private int year;
  private int monthOfYear;
  private int dayOfMonth;

  private long minDateMillis;
  private long maxDateMillis;

  @BindView(R.id.in_call_time_value) TextView inCallTimeView;
  @BindView(R.id.out_call_time_value) TextView outCallTimeView;
  @BindView(R.id.sent_bytes_value) TextView sentBytesView;
  @BindView(R.id.received_bytes_value) TextView receivedBytesView;
  @BindView(R.id.total_cost_value) TextView totalCostView;
  @BindView(R.id.date_picker) DatePicker datePicker;

  public BillingFragment() {
    // Required empty public constructor
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle args = getArguments();
    if (args != null) {
    }

    DateTime dateTime = DateTime.now();
    year = dateTime.getYear();
    monthOfYear = dateTime.getMonthOfYear();
    dayOfMonth = dateTime.getDayOfMonth();

    minDateMillis = dateTime.minusYears(1).getMillis();
    maxDateMillis = dateTime.getMillis();

    RestApi.getInstance(getActivity()).getBilling(getActivity(), year, monthOfYear, this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    final View view = inflater.inflate(R.layout.billing_fragment, container, false);
    ButterKnife.bind(this, view);

    datePicker.init(year, monthOfYear, dayOfMonth, (picker, pickerYear, pickerMonthOfYear, pickerDayOfMonth) -> {
      year = pickerYear;
      monthOfYear = pickerMonthOfYear + 1;
      dayOfMonth = pickerDayOfMonth;

      RestApi.getInstance(getActivity()).getBilling(getActivity(), pickerYear, pickerMonthOfYear, this);
    });

    datePicker.setMinDate(minDateMillis);
    datePicker.setMaxDate(maxDateMillis);

    // hack to hide days
    datePicker.findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
    datePicker.refreshDrawableState();

    return view;
  }

  @Override
  public void onComplete(long inCallTime, long outCallTime, long sendMessageBytes, long receivedMessageBytes, float cost) {
    fillUsageView(inCallTime, outCallTime, sendMessageBytes, receivedMessageBytes, cost);
  }

  @Override
  public void onFailure(String reason) {
    Toast.makeText(getActivity(), getString(R.string.prompt_retrieve_failure) + ": " + reason, Toast.LENGTH_LONG).show();
  }

  public void fillUsageView(long inCallTime, long outCallTime, long sendMessageBytes, long receivedMessageBytes, float cost) {
    DecimalFormat valueFormatter = new DecimalFormat("#,###");
    DecimalFormat costFormatter = new DecimalFormat("#,###.##");

    inCallTimeView.setText(valueFormatter.format(inCallTime) + " seconds");
    outCallTimeView.setText(valueFormatter.format(outCallTime) + " seconds");
    sentBytesView.setText(valueFormatter.format(sendMessageBytes) + " bytes");
    receivedBytesView.setText(valueFormatter.format(receivedMessageBytes)+ " bytes");
    totalCostView.setText("$" + costFormatter.format(cost));
  }
}
