package com.contactdialer.main;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.contactdialer.common.ExtDataBase;

class DiallogModel {
	public class DialLogItem {
		public String date;
		public String number;

		public DialLogItem(String date, String number) {
			this.date = date;
			this.number = number;
		}
	}

	public static final String TABLE_NAME = "DialLog";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_NUMBER = "number";

	public void insert(DialLogItem dli) {
		SQLiteDatabase db = ExtDataBase.getDateBase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_DATE, dli.date);
		cv.put(COLUMN_NUMBER, dli.number);
		db.insert(TABLE_NAME, null, cv);
	}

	public Cursor queryAll() {
		SQLiteDatabase db = ExtDataBase.getDateBase();
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}
}
