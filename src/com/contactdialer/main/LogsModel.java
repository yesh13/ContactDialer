package com.contactdialer.main;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.widget.Toast;

import com.contactdialer.common.VarProvider;

class LogsModel {
	public class LogItem implements Comparable<LogItem> {
		public static final int COLUMN_COUNT = 5;
		public static final int INDEX_TYPE = COLUMN_COUNT - 1;
		public static final int INDEX_NAME = COLUMN_COUNT - 2;
		public static final int INDEX_COUNT=COLUMN_COUNT-3;
		public static final int RESERVED_COLUMN=3;
		public static final int INDEX_DATE = 0;
		private String[] data = new String[COLUMN_COUNT];
		private int count;

		public LogItem(Cursor cursor, int type) {
			if (cursor.getColumnCount() == COLUMN_COUNT - RESERVED_COLUMN) {
				for (int i = 0; i < COLUMN_COUNT - RESERVED_COLUMN; i++) {
					data[i] = cursor.getString(i);
				}
			}
			data[INDEX_TYPE] = String.valueOf(type);
			count=1;
		}

		@Override
		public int compareTo(LogItem another) {
			long left = Long.parseLong(data[INDEX_DATE]);
			long right = Long.parseLong(another.data[INDEX_DATE]);
			if (left < right) {
				return 1;
			}
			return -1;
		}

		public long getDate() {
			return Long.parseLong(data[INDEX_DATE]);
		}

		public String getName() {
			if (data[INDEX_NAME] == null) {
				ContentResolver cv = mContext.getContentResolver();
				Cursor cursor = cv.query(Phone.CONTENT_URI,
						new String[] { Phone.DISPLAY_NAME }, Phone.NUMBER
								+ "=?", new String[] { getNumber() }, null);
				if (cursor.getCount() != 0) {
					cursor.moveToFirst();
					data[INDEX_NAME] = cursor.getString(0);
				}
				cursor.close();
			}
			return data[INDEX_NAME];
		}

		public String getNumber() {
			return data[1];
		}

		public int getType() {
			return Integer.parseInt(data[INDEX_TYPE]);
		}
		public int getCount(){
			return count;
		}
		public void addCount() {
			count++;
		}
	}

	public class LogsList {
		LinkedList<LogItem> list = new LinkedList<LogsModel.LogItem>();
		HashMap<String, LogItem> numberMap=new HashMap<String, LogsModel.LogItem>();

		public LogsList(Cursor[] cursors) {
			for (int type = 0; type < VarProvider.FILTER_TYPE_NUM; type++) {
				Cursor cursor = cursors[type];
				if (cursor == null) {
					continue;
				} else {
					if (cursor.getCount() != 0) {
						cursor.moveToFirst();
						do {
							list.add(new LogItem(cursor, type));
						} while (cursor.moveToNext());
					}
					cursor.close();

				}

			}
			Collections.sort(list);
			Iterator<LogItem> iterator=list.iterator();
			while (iterator.hasNext()) {
				LogItem item=iterator.next();
				String number=item.getNumber();
				LogItem itemExisted = numberMap.get(number);
				if (itemExisted==null) {
					numberMap.put(number, item);
					continue;
				} else {
					itemExisted.addCount();
					iterator.remove();
				}
			}
			
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

		public LogItem get(int index) {
			return list.get(index);
		}

		public int getCount() {
			return list.size();
		}
	}

	public static LogsModel getInstance(Context ctx) {
		return new LogsModel(ctx);
	}

	private Context mContext;

	private LogsModel() {
	}

	private LogsModel(Context ctx) {
		mContext = ctx;
	}

	public LogsList getLogsList() {
		Cursor[] cursors = getCursors(mContext, VarProvider.getInstance()
				.getLogFilter());

		return new LogsList(cursors);
	}
	public Cursor[] getCursors(Context ctx,boolean[] filterTag) {
		ContentResolver cr = ctx.getContentResolver();
		Cursor[] cursors = new Cursor[VarProvider.FILTER_TYPE_NUM];
		for (int type = 0; type < VarProvider.FILTER_TYPE_NUM; type++) {
			if (filterTag[type]) {
				switch (type) {
				case VarProvider.FILTER_TYPE_DIAL:
					DiallogModel dm = new DiallogModel();
					cursors[type] = dm.queryAll();
					break;
				case VarProvider.FILTER_TYPE_SMS_IN:
					cursors[type] = smsQuery(cr, 1);
					break;
				case VarProvider.FILTER_TYPE_SMS_OUT:
					cursors[type] = smsQuery(cr, 2);
					break;
				case VarProvider.FILTER_TYPE_OUT_GOING:
					cursors[type] = phoneQuery(cr, CallLog.Calls.OUTGOING_TYPE);
					Log.d("numberofcolumn",
							String.valueOf(cursors[type].getColumnCount()));
					break;
				case VarProvider.FILTER_TYPE_IN_COMING:
					cursors[type] = phoneQuery(cr, CallLog.Calls.INCOMING_TYPE);
					Log.d("numberofcolumn",
							String.valueOf(cursors[type].getColumnCount()));
					break;
				case VarProvider.FILTER_TYPE_MISSED:
					cursors[type] = phoneQuery(cr, CallLog.Calls.MISSED_TYPE);
					Log.d("numberofcolumn",
							String.valueOf(cursors[type].getColumnCount()));
					break;
				default:
					break;
				}
			} else {
				cursors[type] = null;
			}
		}
		return cursors;

	}
	private Cursor phoneQuery(ContentResolver cr, int type) {
		String[] projection = new String[] { CallLog.Calls.DATE,
				CallLog.Calls.NUMBER };
		return cr.query(CallLog.Calls.CONTENT_URI, projection,
				CallLog.Calls.TYPE + "= ?",
				new String[] { String.valueOf(type) }, null);
	}

	private Cursor smsQuery(ContentResolver cr, int type) {
		String[] projection = new String[] { "date", "address" };
		Uri uri = Uri.parse("content://sms");
		return cr.query(uri, projection, "type= ?",
				new String[] { String.valueOf(type) }, null);
	}

}
