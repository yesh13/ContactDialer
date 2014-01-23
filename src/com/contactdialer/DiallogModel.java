package com.contactdialer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DiallogModel {
	public static final String TABLE_NAME="DialLog";
	public static final String COLUMN_DATE="date";
	public static final String COLUMN_NUMBER="number";
	public void insert(DialLogItem dli) {
		SQLiteDatabase db = VarModel.getDateBase();
		ContentValues cv=new ContentValues();
		cv.put(COLUMN_DATE, dli.date);
		cv.put(COLUMN_NUMBER, dli.number);
		db.insert(TABLE_NAME, null, cv);
	}
	public class DialLogItem{
		public DialLogItem(String date, String number) {
			this.date = date;
			this.number = number;
		}
		public String date;
		public String number;
	}
	public Cursor queryAll(){
		SQLiteDatabase db = VarModel.getDateBase();
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}
}
