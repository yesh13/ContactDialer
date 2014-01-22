package com.contactdialer;

import java.util.LinkedList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserModel {
	public static final String TABLE_NAME="Users";
	public static final int INDEX_NAME=0;
	public static final int INDEX_COUNTRY=1;
	public static final int INDEX_DISTRICT=2;
	public static final int INDEX_GROUP=3;
	public static final String COLUMN_NAME="Name";
	public static final String COLUMN_COUNTRY="Country";
	public static final String COLUMN_DISTRICT="District";
	public static final String COLUMN_GROUP="mGroup";
	public UserModel() {
		// TODO Auto-generated constructor stub
	}
	public User get(String name){
		SQLiteDatabase db = PrefModel.getDateBase();
		Cursor cursor=db.query(TABLE_NAME, null, COLUMN_NAME+"= ?", new String[]{name},null,null,null);
		if (cursor.getCount()==0){
			return null;
		}
		cursor.moveToFirst();
		User user = new User();
		user.country=cursor.getString(INDEX_COUNTRY);
		user.district=cursor.getString(INDEX_DISTRICT);
		user.group=cursor.getString(INDEX_GROUP);
		return user;
	}
	public LinkedList<String> list(){
		LinkedList<String> ll = new LinkedList<String>();
		SQLiteDatabase db = PrefModel.getDateBase();
		Cursor cursor=db.query(TABLE_NAME, new String[]{COLUMN_NAME}, null, null,null,null,null);
		cursor.moveToFirst();
		do {
			ll.add(cursor.getString(0));
		}while (cursor.moveToNext());
		return ll;	
	}
	public boolean update(User user){
		SQLiteDatabase db = PrefModel.getDateBase();
		ContentValues cv=new ContentValues();
		cv.put(COLUMN_COUNTRY, user.country);
		cv.put(COLUMN_DISTRICT, user.district);
		cv.put(COLUMN_GROUP, user.group);
		db.update(TABLE_NAME, cv, COLUMN_NAME+"= ?", new String[]{user.name});
		return true;
	}
	public boolean add(User user){
		SQLiteDatabase db = PrefModel.getDateBase();
		ContentValues cv=new ContentValues();
		cv.put(COLUMN_NAME, user.name);
		cv.put(COLUMN_COUNTRY, user.country);
		cv.put(COLUMN_DISTRICT, user.district);
		cv.put(COLUMN_GROUP, user.group);
		db.insert(TABLE_NAME, null, cv);
		return true;
	}
	public class User {
		private String name;
		private String country;
		private String district;
		private String group;
		private String inGroup;
		public User() {
			name="Home";
			country="86";
			district="27";
			group="8199";
			inGroup="3";
			outGroup="9";
			mobilePrefix="0";
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
		public String getCountry(){
			return country;
		}
		public String getDistrict(){
			return district;
		}
		public String getGroup(){
			return group;
		}
		public int getGroupWidth() {
			return getGroup().length();
		}
	}
}
