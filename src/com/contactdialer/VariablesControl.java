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
	public static int MAX = 100;
	private int volume = -1;
	private int duration = -1;
	private int interval = -1;
	private String currentUser;

	private VariablesControl() {
		volume = -1;
		duration = -1;
		interval = -1;
	}

	private static VariablesControl singleton = new VariablesControl();

	public static VariablesControl getInstance() {
		if (singleton.volume < 0) {
			SQLiteDatabase db = PrefModel.getDateBase();
			if (db != null) {
				singleton.init(db);
			}
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
		return currentUser=userName;
	}

	private boolean init(SQLiteDatabase db) {
		volume = Integer.valueOf(query(db, VOLUME_NAME));
		duration = Integer.valueOf(query(db, DURATION_NAME));
		interval = Integer.valueOf(query(db, INTERVAL_NAME));
		currentUser=query(db, CURRENT_USER_NAME);
		return true;

	}

	public String query(final SQLiteDatabase db, final String item) {
		Cursor cursor = db.query(TABLE_NAME, null, KEY_NAME + " = ?",
				new String[]{item}, null, null, null);
		cursor.moveToFirst();
		return cursor.getString(1);
	}

	public void commit() {
		// save volume data into storage
		SQLiteDatabase db = PrefModel.getDateBase();
		update(db, VOLUME_NAME, String.valueOf(volume));
		update(db, DURATION_NAME, String.valueOf(duration));
		update(db, INTERVAL_NAME, String.valueOf(interval));
		update(db, CURRENT_USER_NAME, currentUser);

	}

	public boolean update(SQLiteDatabase db, String key, String value) {
		ContentValues cv = new ContentValues();
		cv.put(VALUE_NAME, value);
		int i=db.update(TABLE_NAME, cv, KEY_NAME+" =?",new String[]{key});
		Log.d("Volume", query(db, VOLUME_NAME));
		Log.d("num", String.valueOf(i));
		return true;
	}
}
