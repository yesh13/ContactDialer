package com.contactdialer;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class LogsModel {
	private static LogsModel singleton=new LogsModel();
	private Context mContext;
	public void init(Context ctx) {
		mContext=ctx;
		
	}
	public static LogsModel getInstance(){
		return singleton;
	}
	
	public LogsList getLogsList() {
		Cursor[] cursors = VariablesControl.getInstance().getLogFilter()
				.getCursors(mContext);
		
		return new LogsList(cursors);
	}
	private LogsModel(){}
	
	public class LogsList {
		LinkedList<LogItem> list=new LinkedList<LogsModel.LogItem>();
		HashSet<String> numberSet=new HashSet<String>();
		public LogsList(Cursor[] cursors) {
			for (int type=0;type<LogsFilter.NUM_TYPE;type++) {
				Cursor cursor=cursors[type];
				if (cursor==null) {
					continue;
				} else {
					if (cursor.getCount()!=0) {
						cursor.moveToFirst();
						do {
							list.add(new LogItem(cursor,type));
						} while (cursor.moveToNext());
					}
					cursor.close();
					
				}
				
			}
			Collections.sort(list);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Iterator<LogItem> it = list.iterator();
					while (it.hasNext()) {
						it.next().getName();
					}
				}
			}).start();
		}
		public int getCount() {
			return list.size();
		}
		public LogItem get(int index){
			return list.get(index);
		}
	}
	public class LogItem implements Comparable<LogItem>{
		public static final int COLUMN_COUNT=4;
		public static final int INDEX_TYPE=COLUMN_COUNT-1;
		public static final int INDEX_NAME=COLUMN_COUNT-2;
		public static final int INDEX_DATE=0;
		String[] data=new String[COLUMN_COUNT];
		
		public LogItem(Cursor cursor,int type) {
			if (cursor.getColumnCount()==COLUMN_COUNT-2) {
				for (int i=0;i<COLUMN_COUNT-2;i++) {
					data[i]=cursor.getString(i);
				}
			}
			data[INDEX_TYPE]=String.valueOf(type);
		}
		@Override
		public int compareTo(LogItem another) {
			long left=Long.parseLong(data[INDEX_DATE]);
			long right=Long.parseLong(another.data[INDEX_DATE]);
			if (left<right) {
				return 1;
			}
			return -1;
		}
		
		public long getDate(){
			return Long.parseLong(data[INDEX_DATE]);
		}

		public String getName() {
			if (data[INDEX_NAME]==null) {
				ContentResolver cv = mContext.getContentResolver();
				Cursor cursor=cv.query(Phone.CONTENT_URI, new String[] { Phone.DISPLAY_NAME },
						Phone.NUMBER + "=?", new String[] { getNumber() }, null);
				if (cursor.getCount()!=0) {
					cursor.moveToFirst();
					data[INDEX_NAME]=cursor.getString(0);
				}
				cursor.close();	
			}
			return data[INDEX_NAME];
		}
		public String getNumber(){
			return data[1];
		}
		public int getType(){
			return Integer.parseInt(data[INDEX_TYPE]);
		}
	}

}
