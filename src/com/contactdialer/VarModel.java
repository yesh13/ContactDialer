package com.contactdialer;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class VarModel {
	private String dataBaseFileName="%s/Android/data/%s/databases/pref.db";
	private VarModel() {
	}

	private static VarModel singleton = new VarModel();

	public static VarModel getInstance() {
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
		VarDataBase myHelper = new VarDataBase(ctx, file.getPath());
		mDB = myHelper.getWritableDatabase();
	}

}
