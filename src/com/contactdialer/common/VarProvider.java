package com.contactdialer.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * <p>Provide simple connection to settings of this application. Using get* methods could
 * be called to get settings. Setting of this application includes variables,
 * filter, users.</p>
 * 
 * <p>Variables: variables for preference. These variable could be set in PrefFragment.</p>
 * 
 * <p>Filter: filter to choose whether a call log should be included. The
 * specification of each column of the filter could be interpreted by
 * FILTER_TYPE_*.</p>
 *
 */
public class VarProvider {
	public static final String TABLE_NAME = "Variables";
	public static final int FILTER_TYPE_NUM = 6;
	public static final String FILTER_TABLE_NAME = "Filter";
	public static final int FILTER_TYPE_DIAL = 0;
	public static final int FILTER_TYPE_SMS_IN = 1;
	public static final int FILTER_TYPE_SMS_OUT = 2;
	public static final int FILTER_TYPE_OUT_GOING = 3;
	public static final int FILTER_TYPE_IN_COMING = 4;
	public static final int FILTER_TYPE_MISSED = 5;
	public static final String KEY_NAME = "Item";
	public static final String VALUE_NAME = "ItemValue";
	private static final String DURATION_NAME = "Duration";
	private static final String INTERVAL_NAME = "Interval";
	private static final String VOLUME_NAME = "Volume";
	private static final String CURRENT_USER_NAME = "CurrentUser";
	public static int MAX = 100;

	public static VarProvider getInstance() {

		if (!singleton.valid) {
			singleton.queryAll();
		}
		return singleton;
	}

	private boolean valid = false;
	private int volume = -1;
	private int duration = -1;
	private int interval = -1;

	private String currentUser;

	private static VarProvider singleton = new VarProvider();

	private VarProvider() {
		volume = -1;
		duration = -1;
		interval = -1;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public int getDuration() {
		return duration;
	}

	public int getInterval() {
		return interval;
	}

	/**
	 * Get LogFilter which is used by LogsFragment in order to choose whether a
	 * calllog should be included.
	 * 
	 * @return array of filters
	 */
	public boolean[] getLogFilter() {
		SQLiteDatabase db = ExtDataBase.getDateBase();
		Cursor cursor = db.query(FILTER_TABLE_NAME, null, null, null, null,
				null, null);
		cursor.moveToFirst();
		boolean[] filterTag = new boolean[FILTER_TYPE_NUM];
		for (int i = 0; i < FILTER_TYPE_NUM; i++) {
			if (cursor.getInt(i) == 0) {
				filterTag[i] = false;
			} else {
				filterTag[i] = true;
			}
		}
		cursor.close();
		return filterTag;
	}

	public int getVolume() {
		return volume;
	}

	/**
	 * Get a value with key=item from Table Variables.
	 * 
	 * @param item
	 *            key of the selected row.
	 * @return value of the row. return null if no row in Table match item.
	 */
	public String query(final String item) {
		SQLiteDatabase db = ExtDataBase.getDateBase();
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

	/**
	 * Query all variables for Preference View.
	 * 
	 * @return return true if succeeding.
	 */
	public boolean queryAll() {
		SQLiteDatabase db = ExtDataBase.getDateBase();
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

	/**
	 * Set current user with user name. This method immediately update current
	 * user into DataBase.
	 * 
	 * @param userName
	 *            current user name to be set
	 */
	public void setCurrentUser(String userName) {
		currentUser = userName;
		updataCurrentUser();
	}

	public int setDuration(int time) {
		return duration = time;
	}

	public void setFilter(boolean[] filterTag) {
		ContentValues cv = new ContentValues();
		for (int i = 0; i < FILTER_TYPE_NUM; i++) {
			cv.put("F" + String.valueOf(i), filterTag[i]);
		}
		SQLiteDatabase db = ExtDataBase.getDateBase();
		db.update(FILTER_TABLE_NAME, cv, null, null);
	}

	public int setInterval(int time) {
		return interval = time;
	}

	public int setVolume(int time) {
		return volume = time;
	}

	public void updataCurrentUser() {
		update(CURRENT_USER_NAME, currentUser);
	}

	/**
	 * Update value of a variable with name key into argument value.
	 * 
	 * @param key
	 *            name of the variable
	 * @param value
	 *            value of the variable
	 * @return true if succeeding.
	 */
	public boolean update(String key, String value) {
		SQLiteDatabase db = ExtDataBase.getDateBase();
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

	/**
	 * Update all variable for Preference into Table.
	 * 
	 * @return true if succeeding.
	 */
	public boolean updateAll() {
		// save volume data into storage
		SQLiteDatabase db = ExtDataBase.getDateBase();
		if (db == null) {
			return false;
		}
		update(VOLUME_NAME, String.valueOf(volume));
		update(DURATION_NAME, String.valueOf(duration));
		update(INTERVAL_NAME, String.valueOf(interval));
		update(CURRENT_USER_NAME, currentUser);
		return true;
	}
}
