package com.contactdialer;

import android.R.bool;
import android.R.integer;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Telephony;
import android.util.Log;
import android.provider.Telephony;

public class LogsFilter {
	public static final int TYPE_DIAL=0;
	public static final int TYPE_SMS_IN=1;
	public static final int TYPE_SMS_OUT=2;
	public static final int TYPE_OUT_GOING=3;
	public static final int TYPE_IN_COMING=4;
	public static final int TYPE_MISSED=5;
	public static final int NUM_TYPE=6;
	public static final String TABLE_NAME="Filter";
	private boolean[] filterTag=new boolean[NUM_TYPE];
	private LogsFilter(){
		
	}
	public LogsFilter(boolean[] filterTag){
		this.filterTag=filterTag;
	}
	public boolean[] getFilterTag() {
		return filterTag;
	}

	public void setFilter(boolean[] filterTag) {
		this.filterTag = filterTag;
	}
	public Cursor[] getCursors(Context ctx){
		ContentResolver cr=ctx.getContentResolver();
		Cursor[] cursors=new Cursor[NUM_TYPE];
		String[] projection;
		for (int type=0;type<NUM_TYPE;type++) {
			if (filterTag[type]) {
				switch (type) {
				case TYPE_DIAL:
					DiallogModel dm=new DiallogModel();
					cursors[type]=dm.queryAll();
					break;
				case TYPE_SMS_IN:
					cursors[type]=smsQuery(cr, 1);
					break;
				case TYPE_SMS_OUT:
					cursors[type]=smsQuery(cr, 2);
					break;
				case TYPE_OUT_GOING:
					cursors[type]=phoneQuery(cr,CallLog.Calls.OUTGOING_TYPE);
					Log.d("numberofcolumn", String.valueOf(cursors[type].getColumnCount()));
					break;
				case TYPE_IN_COMING:
					cursors[type]=phoneQuery(cr,CallLog.Calls.INCOMING_TYPE);
					Log.d("numberofcolumn", String.valueOf(cursors[type].getColumnCount()));
					break;
				case TYPE_MISSED:
					cursors[type]=phoneQuery(cr,CallLog.Calls.MISSED_TYPE);
					Log.d("numberofcolumn", String.valueOf(cursors[type].getColumnCount()));
					break;
				default:
					break;
				}
			} else {
				cursors[type]=null;
			}
		}
		return cursors;
		
	}

	private Cursor phoneQuery(ContentResolver cr,int type) {
		String[] projection = new String[] { CallLog.Calls.DATE,
				CallLog.Calls.NUMBER};
		return cr.query(CallLog.Calls.CONTENT_URI,
				projection, CallLog.Calls.TYPE + "= ?",
				new String[] { String
						.valueOf(type) },
				null);
	}
	private Cursor smsQuery(ContentResolver cr,int type) {
		String[] projection = new String[] { "date",
				"address" };
		Uri uri=Uri.parse("content://sms");
		return cr.query(uri,
				projection, "type= ?",
				new String[] { String
						.valueOf(type) },
				null);
	}
}