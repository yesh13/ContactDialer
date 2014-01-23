package com.contactdialer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class VariablesControl {
	public static final String TABLE_NAME = "Variables";
	public static final String KEY_NAME = "Item";
	public static final String VALUE_NAME = "ItemValue";
	private static final String DURATION_NAME = "Duration";
	private static final String INTERVAL_NAME = "Interval";
	private static final String VOLUME_NAME = "Volume";
	private static final String CURRENT_USER_NAME = "CurrentUser";
	private static final String FILTER = "Filter";
	public static int MAX = 100;
	private boolean valid = false;
	private int volume = -1;
	private int duration = -1;
	private int interval = -1;
	private String currentUser;

	public String getCurrentUser() {
		return currentUser;
	}

	private VariablesControl() {
		volume = -1;
		duration = -1;
		interval = -1;
	}

	private static VariablesControl singleton = new VariablesControl();

	public static VariablesControl getInstance() {
		if (!singleton.valid) {
			singleton.queryAll();
		}
		return singleton;
	}

	public int getVolume() {
		return volume;
	}

	public int setVolume(int time) {
		return volume = time;
	}

	public int getDuration() {
		return duration;
	}

	public int getInterval() {
		return interval;
	}

	public int setDuration(int time) {
		return duration = time;
	}

	public int setInterval(int time) {
		return interval = time;
	}

	public String setCurrentUser(String userName) {
		currentUser = userName;
		updataCurrentUser();
		return currentUser;
	}

	public LogsFilter getLogFilter() {
		SQLiteDatabase db = VarModel.getDateBase();
		Cursor cursor=db.query(LogsFilter.TABLE_NAME, null, 
				null, null, null, null, null);
		cursor.moveToFirst();
		boolean[] filterTag=new boolean[LogsFilter.NUM_TYPE];
		for (int i=0;i<LogsFilter.NUM_TYPE;i++){
			if (cursor.getInt(i)==0) {
				filterTag[i]=false;
			} else {
				filterTag[i]=true;
			}
		}
		LogsFilter filter=new LogsFilter(filterTag);
		cursor.close();
		return filter;
	}

	public void setFilter(LogsFilter filter) {
		ContentValues cv = new ContentValues();
		for (int i = 0; i < LogsFilter.NUM_TYPE; i++) {
			cv.put("F" + String.valueOf(i), filter.getFilterTag()[i]);
		}
		SQLiteDatabase db = VarModel.getDateBase();
		db.update(LogsFilter.TABLE_NAME, cv, null, null);
	}

	public boolean queryAll() {
		SQLiteDatabase db = VarModel.getDateBase();
		if (db == null) {
			return false;
		}
		volume = Integer.valueOf(query(VOLUME_NAME));
		duration = Integer.valueOf(query(DURATION_NAME));
		interval = Integer.valueOf(query(INTERVAL_NAME));
		currentUser = query(CURRENT_USER_NAME);
		valid = true;
		return true;

	}

	public String query(final String item) {
		SQLiteDatabase db = VarModel.getDateBase();
		if (db == null) {
			return null;
		}
		Cursor cursor = null;
		String ans = null;
		try {
			cursor = db.query(TABLE_NAME, null, KEY_NAME + " = ?",
					new String[] { item }, null, null, null);
			cursor.moveToFirst();
			ans = cursor.getString(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return ans;
	}

	public boolean updateAll() {
		// save volume data into storage
		SQLiteDatabase db = VarModel.getDateBase();
		if (db == null) {
			return false;
		}
		update(VOLUME_NAME, String.valueOf(volume));
		update(DURATION_NAME, String.valueOf(duration));
		update(INTERVAL_NAME, String.valueOf(interval));
		update(CURRENT_USER_NAME, currentUser);
		return true;
	}

	public boolean update(String key, String value) {
		SQLiteDatabase db = VarModel.getDateBase();
		if (db == null) {
			return false;
		}
		ContentValues cv = new ContentValues();
		cv.put(VALUE_NAME, value);
		int i = db.update(TABLE_NAME, cv, KEY_NAME + " =?",
				new String[] { key });
		Log.d("Volume", query(VOLUME_NAME));
		Log.d("num", String.valueOf(i));
		return true;
	}

	public void updataCurrentUser() {
		update(CURRENT_USER_NAME, currentUser);
	}
}
