package com.contactdialer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	private Context mContext;

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table MobileTable(Mobile int,District int)");
		InputStream is;
		try {
			is = mContext.getAssets().open("mobile_table1.txt");
			InputStreamReader isr=new InputStreamReader(is);
			BufferedReader br=new BufferedReader(isr);
			String str = null;  
	        while ((str = br.readLine()) != null) {  
	            String[] pair=str.split(" ");
	            db.execSQL("insert into MobileTable values (? , ?)", pair);
	        }
	        br.close();
	        isr.close();
	        is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
