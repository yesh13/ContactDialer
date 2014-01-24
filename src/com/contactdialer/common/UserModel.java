package com.contactdialer.common;

import java.util.LinkedList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * This class helps to connect Users Table in the databases.
 */
public class UserModel {
	/**
	 * Table Name in ExtDataBase for Users.
	 */
	public static final String TABLE_NAME = "Users";
	/**
	 * project string array for getting User from Users Table.
	 */
	public static final String[] PROJECT = new String[] { "Name", "Country",
			"District", "uGroup", "InGroup", "OutGroup" ,"MobilePrefix"};
	/**
	 * number of column in Users Table
	 */
	public static final int COLUMN_NUM=7;
	public static final int INDEX_NAME = 0;
	public static final int INDEX_COUNTRY = 1;
	public static final int INDEX_DISTRICT = 2;
	public static final int INDEX_GROUP = 3;
	public static final int INDEX_INGROUP = 4;
	public static final int INDEX_OUTGROUP = 5;
	public static final int INDEX_MPREFIX = 6;

	public UserModel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Add a user to the Table for Users.
	 * @param user the user been added
	 * @return return true if succeeding.
	 */
	public boolean add(User user) {
		// add new user into database
		SQLiteDatabase db = ExtDataBase.getDateBase();
		db.insert(TABLE_NAME, null, getContentValues(user));
		return true;
	}

	/**
	 * Delete a user from Table for Users
	 * @param name the name of the user
	 * @return return true if succeeding.
	 */
	public boolean delete(String name) {
		// add new user into database
		SQLiteDatabase db = ExtDataBase.getDateBase();
		db.delete(TABLE_NAME,  PROJECT[INDEX_NAME] +"= ?", new String[] { name });
		return true;
	}

	/**
	 * Get a user instance from Table for Users.
	 * @param name the name of the user
	 * @return a user instance with name in Table for Users.
	 */
	public User get(String name) {
		User user = new User();
		if (name == null) {
			return user;
		}
		SQLiteDatabase db = ExtDataBase.getDateBase();
		Cursor cursor = db.query(TABLE_NAME, null, PROJECT[INDEX_NAME] +"= ?",
				new String[] { name }, null, null, null);
		if (cursor.getCount() == 0) {
			return user;
		}
		cursor.moveToFirst();
		for(int i=0;i<COLUMN_NUM;i++) {
			user.data[i]=cursor.getString(i);
		}
		user.data[INDEX_NAME] = name;
		return user;
	}

	private ContentValues getContentValues(User user) {
		ContentValues cv = new ContentValues();
		for(int i=0;i<COLUMN_NUM;i++) {
			cv.put(PROJECT[i], user.data[i]);
		}
		return cv;
	}

	/**
	 * Get list of names in Table for Users.
	 * @return list of names.
	 */
	public LinkedList<String> list() {
		LinkedList<String> ll = new LinkedList<String>();
		SQLiteDatabase db = ExtDataBase.getDateBase();
		Cursor cursor = db.query(TABLE_NAME, new String[] {  PROJECT[INDEX_NAME]  },
				null, null, null, null, null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				ll.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		return ll;
	}

	/**Update the user in Users Table with name into argument user.
	 * @param user the updating content
	 * @param name the name of the updated user.
	 * @return return true if succeeding.
	 */
	public boolean update(User user, String name) {
		// replace the User name with new user into database
		SQLiteDatabase db = ExtDataBase.getDateBase();
		if (name.equals(VarProvider.getInstance().getCurrentUser())) {
			VarProvider.getInstance().setCurrentUser(user.data[INDEX_NAME]);
			VarProvider.getInstance().updataCurrentUser();
		}
		db.update(TABLE_NAME, getContentValues(user), PROJECT[INDEX_NAME] + "= ?",
				new String[] { name });
		return true;
	}
	
	/**
	 * User class for a row in Users Table.
	 */
	public class User {
		public static final String CHINA = "86";
		public static final String MOBILEPREFIX = "0";
		String[] data=new String[COLUMN_NUM];

		public User() {
			data[INDEX_NAME] = "HOME";
			data[INDEX_COUNTRY] = CHINA;
			data[INDEX_DISTRICT] = "27";
			data[INDEX_GROUP] = "8199";
			data[INDEX_INGROUP] = "3";
			data[INDEX_OUTGROUP] = "9";
			data[INDEX_MPREFIX] = MOBILEPREFIX;
		}

		public String getCountry() {
			return data[INDEX_COUNTRY];
		}

		public String getDistrict() {
			return data[INDEX_DISTRICT];
		}

		public String getGroup() {
			return data[INDEX_GROUP];
		}

		public int getGroupWidth() {
			return getGroup().length();
		}

		public String getInGroup() {
			return data[INDEX_INGROUP];
		}

		public String getMobilePrefix() {
			return data[INDEX_MPREFIX];
		}

		public String getName() {
			return data[INDEX_NAME];
		}

		public String getOutGroup() {
			return data[INDEX_OUTGROUP];
		}

		public void set(String name, String country, String district,
				String group, String inGroup, String outGroup,
				String mobilePrefix) {
			data[INDEX_NAME] = name;
			data[INDEX_COUNTRY] = CHINA;
			data[INDEX_DISTRICT] = district;
			data[INDEX_GROUP] = group;
			data[INDEX_INGROUP] =inGroup;
			data[INDEX_OUTGROUP] = outGroup;
			data[INDEX_MPREFIX] = MOBILEPREFIX;
		}
	}
}
