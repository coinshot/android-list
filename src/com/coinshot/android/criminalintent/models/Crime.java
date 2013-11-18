package com.coinshot.android.criminalintent.models;

import java.util.Date;
import java.util.UUID;

import android.text.format.DateFormat;
import android.text.format.Time;

public class Crime {

	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	
	public Crime() {
		mId = UUID.randomUUID();
		setDate(new Date());
	}
	
	@Override
	public String toString() {
		return mTitle;
	}

	public UUID getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public Date getDate() {
		return mDate;
	}
	
	public String getFormattedDate() {
		return DateFormat.format("yyyy-MM-dd", mDate).toString();
	}

	public void setDate(Date mDate) {
		this.mDate = mDate;
	}
		
	public String getFormattedTime() {
		return DateFormat.format("hh:mm A", mDate).toString();
	}
	
	public String getFormattedDateTime() {
		return DateFormat.format("yyyy-MM-dd hh:mm A", mDate).toString();
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean mSolved) {
		this.mSolved = mSolved;
	}
}
