package com.contactdialer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VarDataBase extends SQLiteOpenHelper {

	public VarDataBase(Context context, String name) {
		super(context, name, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(String.format("create table Variables (%s text,%s text)",
				(Object[])new String[] { VariablesControl.KEY_NAME, VariablesControl.VALUE_NAME }));
		db.execSQL("insert into Variables values ('Duration' , '50')");
		db.execSQL("insert into Variables values ('Interval' , '50')");
		db.execSQL("insert into Variables values ('Volume' , '100')");
		db.execSQL("insert into Variables values ('CurrentUser' , 'Home')");
		db.execSQL("create table Users (Name text primary key,Country text,District text,uGroup text,InGroup text, OutGroup text, MobilePrefix text)");
		db.execSQL("insert into Users values ('Home' , '86','27','8199',3,9,0)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
