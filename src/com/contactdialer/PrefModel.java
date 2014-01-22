package com.contactdialer;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class PrefModel {
	private String dataBaseFileName="%s/Android/data/%s/databases/pref.db";
	private PrefModel() {
	}

	private static PrefModel singleton = new PrefModel();

	public static PrefModel getInstance() {
		return singleton;
	}
	public static SQLiteDatabase getDateBase() {
		return singleton.mDB;
	}

	private SQLiteDatabase mDB = null;

	public void init(Context ctx) {
		// TODO Auto-generated method stub
		String[] dbplace = new String[] {
				Environment.getExternalStorageDirectory().getPath(),
				ctx.getPackageName() };
		File file=new File(String.format(
				dataBaseFileName, (Object[]) dbplace));
		File dir=file.getParentFile();
		if(!dir.exists()){
			dir.mkdirs();
		}
		PrefDataBase myHelper = new PrefDataBase(ctx, file.getPath());
		mDB = myHelper.getWritableDatabase();
	}

}
