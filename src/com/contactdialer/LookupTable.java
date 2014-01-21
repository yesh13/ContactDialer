package com.contactdialer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.style.UpdateAppearance;
import android.util.Log;

public class LookupTable {
	private Context mContext;
	private HashSet<String> districts=new HashSet<String>();
	private HashSet<String> countries=new HashSet<String>();
	private SQLiteDatabase dbMobileTable=null;
	private LookupTable(){
	}
	private static LookupTable singleton=new LookupTable();
	public static LookupTable getInstance(){
		return singleton;
	}
	public boolean init(Context ctx){
		mContext=ctx;
		try {
			//read districts.txt
			InputStream is=ctx.getAssets().open("districts.txt");
			InputStreamReader isr=new InputStreamReader(is);
			BufferedReader br=new BufferedReader(isr);
			String str = null;  
            while ((str = br.readLine()) != null) {  
                districts.add(str);
            }
            is.close();
            isr.close();
            br.close();
            
            //read countries.txt
            is=ctx.getAssets().open("countries.txt");
			isr=new InputStreamReader(is);
			br=new BufferedReader(isr);
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
	public boolean isCountry(String s){
		return countries.contains(s);
	}
	public boolean isDistrict(String s){
		return districts.contains(s);
	}
	public String district(String s){
		if(dbMobileTable==null) {
			DataBaseOpener myOpener = new DataBaseOpener(mContext,"number.db", "number.db");
			dbMobileTable = myOpener.getReadableDatabase();
		}
		Cursor cursor=dbMobileTable.query("Mobile_Table", Table.column, Table.select, new String[]{s}, null,null,null);
		if(cursor.moveToFirst()){
			return cursor.getString(Table.DISTRICT_COLUMN);
		}
		return null;
	}
	static class Table{
		public static final String[] column=new String[]{"district"};
		public static final String select="mobile = ?";
		public static final int DISTRICT_COLUMN=0;
	}
	
}