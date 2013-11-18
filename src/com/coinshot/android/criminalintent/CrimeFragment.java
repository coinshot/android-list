package com.coinshot.android.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.coinshot.android.criminalintent.models.Crime;
import com.coinshot.android.criminalintent.models.CrimeLab;

public class CrimeFragment extends Fragment {
	
	public static final String EXTRA_CRIME_ID = "com.coinshot.android.criminalintent.crime_id";
	
	private static final String TAG = "CrimeFragment";
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private Button mTimeButton;
	private CheckBox mSolvedCheckBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(
			new TextWatcher() {
				public void onTextChanged(CharSequence c, int start, int before, int count) {
					mCrime.setTitle(c.toString());
				}
				public void beforeTextChanged(CharSequence c, int start, int count, int after) { }
				public void afterTextChanged(Editable c) { }
			}
		);
		
		mDateButton = (Button) v.findViewById(R.id.crime_date);
		updateDate();
		
		mDateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		mTimeButton = (Button) v.findViewById(R.id.crime_time);
		updateTime();
		
		mTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
				dialog.show(fm, DIALOG_TIME);
			}
		});
		
		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(
			new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					mCrime.setSolved(isChecked);
				}
			}
		);
		
		return v;
	}
	
	public static CrimeFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_DATE) {
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			Log.d(TAG, date.toString());
			setDate(date);
			updateDate();
		}
		if (requestCode == REQUEST_TIME) {
			Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_DATE);
			Log.d(TAG, "REQUEST_TIME");
			Log.d(TAG, date.toString());
			setTime(date);
			updateTime();
		}
	}
	
	public void setDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		cal.setTime(mCrime.getDate());
		cal.set(year, month, day);
		mCrime.setDate(cal.getTime());
	}
	
	public void setTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(mCrime.getDate());
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		
		cal.set(year, month, day, hour, minute);
		mCrime.setDate(cal.getTime());
	}
	
	public void updateDate() {
		mDateButton.setText(mCrime.getFormattedDate());
	}
	
	public void updateTime() {
		
		mTimeButton.setText(mCrime.getFormattedTime());
	}
}
