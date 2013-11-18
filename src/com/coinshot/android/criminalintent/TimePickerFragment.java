package com.coinshot.android.criminalintent;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;


public class TimePickerFragment extends DialogFragment {

	public static final String EXTRA_DATE = "com.coinshot.android.criminalintent";
	
	private Date mDate;
	
	public static TimePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		
		TimePicker timePicker = (TimePicker) v.findViewById(R.id.dialog_time_timePicker);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hour, int minute) {
				Log.d("CrimeFragment", "onTimeSet called");
				Calendar cal = Calendar.getInstance();
				cal.set(
						cal.get(Calendar.YEAR),
						cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH),
						hour, minute
				);
				Log.d("CrimeFragment", cal.getTime().toString() + " -- " + EXTRA_DATE);
				mDate = cal.getTime();
				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});
		
		return new AlertDialog.Builder(getActivity())
			.setView(v)
			.setTitle(R.string.date_picker_title)
			.setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("CrimeFragment", "TimePickerFragment sendResult()");
						sendResult(Activity.RESULT_OK);
					}
				}
			)
			.create();
	}
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null) {
			return;
		}
		
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}

}
