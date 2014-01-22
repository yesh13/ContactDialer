package com.contactdialer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class NumberParserDataBase {
	private Context mContext;
	private String dbPath;
	private String backup;

	public NumberParserDataBase(Context ctx, String dbPath, String backup) {
		mContext = ctx;
		this.dbPath = dbPath;
		this.backup = backup;
	}

	public SQLiteDatabase getReadableDatabase() {
		SQLiteDatabase db = null;
		File file = new File(dbPath);
		// test whether it is absolute path e.g. test.db(no) or
		// /sdcard/test.db(yes)
		// yes then create the databases path
		if (!file.isAbsolute()) {
			file = mContext.getDatabasePath(dbPath);
		}
		try {
			db = SQLiteDatabase.openDatabase(file.getPath(), null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			copyAssetToDest(backup, file.getPath());
			try {
				db = SQLiteDatabase.openDatabase(file.getPath(), null,
						SQLiteDatabase.OPEN_READONLY);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return db;
	}

	private boolean copyAssetToDest(String backup, String dbPath) {
		InputStream istream = null;
		FileOutputStream ostream = null;
		File file = new File(dbPath);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			AssetManager am = mContext.getAssets();
			istream = am.open(backup);
			ostream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = istream.read(buffer)) > 0) {
				ostream.write(buffer, 0, length);
			}
			ostream.flush();
			ostream.close();
			istream.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (istream != null)
					istream.close();
				if (ostream != null)
					ostream.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return false;
		}
		return true;
	}
	// private String databasePath(){
	// String sdPath = Environment.getExternalStorageDirectory().getPath();
	// return sdPath+"/data/data/"+mContext.getPackageName()+"/database/";
	// }
}
