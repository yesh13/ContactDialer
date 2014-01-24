package com.contactdialer.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

class NumberParserModel {
	static class Table {
		public static final String[] column = new String[] { "district" };
		public static final String select = "mobile = ?";
		public static final int DISTRICT_COLUMN = 0;
	}

	public static NumberParserModel getInstance() {
		return singleton;
	}

	private Context mContext;
	private HashSet<String> districts = new HashSet<String>();
	private HashSet<String> countries = new HashSet<String>();
	private SQLiteDatabase mDB = null;
	private static NumberParserModel singleton = new NumberParserModel();

	private NumberParserModel() {
	}

	public String district(String s) {
		if (mDB == null) {
			NumberParserDataBase myOpener = new NumberParserDataBase(mContext,
					"number.db", "number.db");
			mDB = myOpener.getReadableDatabase();
		}
		Cursor cursor = null;
		try {
			cursor = mDB.query("Mobile_Table", Table.column, Table.select,
					new String[] { s }, null, null, null);
			if (cursor.moveToFirst()) {
				return cursor.getString(Table.DISTRICT_COLUMN);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}

	public boolean init(Context ctx) {
		mContext = ctx;
		try {
			// read districts.txt
			InputStream is = ctx.getAssets().open("districts.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String str = null;
			while ((str = br.readLine()) != null) {
				districts.add(str);
			}
			is.close();
			isr.close();
			br.close();

			// read countries.txt
			is = ctx.getAssets().open("countries.txt");
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			while ((str = br.readLine()) != null) {
				countries.add(str);
			}
			is.close();
			isr.close();
			br.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public boolean isCountry(String s) {
		return countries.contains(s);
	}

	public boolean isDistrict(String s) {
		return districts.contains(s);
	}

}