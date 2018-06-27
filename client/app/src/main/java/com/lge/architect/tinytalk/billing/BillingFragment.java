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
import java.util.Locale;

public class BillingFragment extends Fragment implements BillingListener {
  private static final String TAG = BillingFragment.class.getSimpleName();

  private int year;
  private int monthOfYear;
  private int dayOfMonth;

  private long minDateMillis;
  private long maxDateMillis;

  private TextView inCallTimeView;
  private TextView outCallTimeView;
  private TextView sentBytesView;
  private TextView receivedBytesView;
  private TextView totalCostView;

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

    DatePicker datePicker = view.findViewById(R.id.date_picker);
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

    inCallTimeView = view.findViewById(R.id.in_call_time_value);
    outCallTimeView = view.findViewById(R.id.out_call_time_value);
    sentBytesView = view.findViewById(R.id.sent_bytes_value);
    receivedBytesView = view.findViewById(R.id.received_bytes_value);
    totalCostView = view.findViewById(R.id.total_cost_value);

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
