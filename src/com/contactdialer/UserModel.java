package com.contactdialer;

import java.util.LinkedList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserModel {
	public static final String TABLE_NAME = "Users";
	public static final int INDEX_NAME = 0;
	public static final int INDEX_COUNTRY = 1;
	public static final int INDEX_DISTRICT = 2;
	public static final int INDEX_GROUP = 3;
	public static final int INDEX_INGROUP = 4;
	public static final int INDEX_OUTGROUP = 5;
	public static final int INDEX_MPREFIX = 6;
	public static final String COLUMN_NAME = "Name";
	public static final String COLUMN_COUNTRY = "Country";
	public static final String COLUMN_DISTRICT = "District";
	public static final String COLUMN_GROUP = "uGroup";
	public static final String COLUMN_INGROUP = "InGroup";
	public static final String COLUMN_OUTGROUP = "OutGroup";
	public static final String COLUMN_MPREFIX = "MobilePrefix";

	public UserModel() {
		// TODO Auto-generated constructor stub
	}

	public User get(String name) {
		User user = new User();
		if (name==null) {
			return user;
		}
		SQLiteDatabase db = VarModel.getDateBase();
		Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NAME + "= ?",
				new String[] { name }, null, null, null);
		if (cursor.getCount() == 0) {
			return user;
		}
		cursor.moveToFirst();
		user.name=name;
		user.country = cursor.getString(INDEX_COUNTRY);
		user.district = cursor.getString(INDEX_DISTRICT);
		user.group = cursor.getString(INDEX_GROUP);
		user.inGroup = cursor.getString(INDEX_INGROUP);
		user.outGroup = cursor.getString(INDEX_OUTGROUP);
		user.mobilePrefix = cursor.getString(INDEX_MPREFIX);
		return user;
	}

	public LinkedList<String> list() {
		LinkedList<String> ll = new LinkedList<String>();
		SQLiteDatabase db = VarModel.getDateBase();
		Cursor cursor = db.query(TABLE_NAME, new String[] { COLUMN_NAME },
				null, null, null, null, null);
		if(cursor.getCount()!=0) {
			cursor.moveToFirst();
			do {
				ll.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		return ll;
	}

	public boolean update(User user, String name) {
		// replace the User name with new user into database
		SQLiteDatabase db = VarModel.getDateBase();
		if (name.equals(VariablesControl.getInstance().getCurrentUser())){
			VariablesControl.getInstance().setCurrentUser(user.name);
			VariablesControl.getInstance().updataCurrentUser();
		}
		db.update(TABLE_NAME, getContentValues(user), COLUMN_NAME + "= ?",
				new String[] { name });
		return true;
	}

	public boolean add(User user) {
		// add new user into database
		SQLiteDatabase db = VarModel.getDateBase();
		db.insert(TABLE_NAME, null, getContentValues(user));
		return true;
	}
	
	public boolean delete(String name) {
		// add new user into database
		SQLiteDatabase db = VarModel.getDateBase();
		db.delete(TABLE_NAME, COLUMN_NAME+"= ?", new String[] { name });
		return true;
	}

	private ContentValues getContentValues(User user) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_NAME, user.name);
		cv.put(COLUMN_COUNTRY, user.country);
		cv.put(COLUMN_DISTRICT, user.district);
		cv.put(COLUMN_GROUP, user.group);
		cv.put(COLUMN_INGROUP, user.inGroup);
		cv.put(COLUMN_OUTGROUP, user.outGroup);
		cv.put(COLUMN_MPREFIX, user.mobilePrefix);
		return cv;
	}

	public class User {
		private String name;
		public String getName() {
			return name;
		}

		private String country;
		private String district;
		private String group;
		private String inGroup;

		public User() {
			name = "HOME";
			country = "86";
			district = "27";
			group = "8199";
			inGroup = "3";
			outGroup = "9";
			mobilePrefix = "0";
		}

		public void set(String name, String country, String district,
				String group, String inGroup, String outGroup,
				String mobilePrefix) {
			this.name = name;
			this.country = country;
			this.district = district;
			this.group = group;
			this.inGroup = inGroup;
			this.outGroup = outGroup;
			this.mobilePrefix = mobilePrefix;

		}

		public String getInGroup() {
			return inGroup;
		}

		public String getOutGroup() {
			return outGroup;
		}

		public String getMobilePrefix() {
			return mobilePrefix;
		}

		private String outGroup;
		private String mobilePrefix;

		public String getCountry() {
			return country;
		}

		public String getDistrict() {
			return district;
		}

		public String getGroup() {
			return group;
		}

		public int getGroupWidth() {
			return getGroup().length();
		}
	}
}
