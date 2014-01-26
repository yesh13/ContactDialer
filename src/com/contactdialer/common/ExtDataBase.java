package com.contactdialer.common;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Provide access to External Database stored in SDCard. If the database does
 * not exist, the class will create it automatically. This class should be
 * initiated by Activity Context in order to fetch resources.
 */
public class ExtDataBase {
	/**
	 * SQLiteOpenHelper help to get open ExtDataBase and create the DataBase
	 * when it does not exist.
	 */
	private class ExtSQLiteOpenHelper extends SQLiteOpenHelper {

		public ExtSQLiteOpenHelper(Context context, String name) {
			super(context, name, null, 1);
			// TODO Auto-generated constructor stub
		}

		/**
		 * This method is called when the database does not exist in the Storage
		 * in order to create tables and predefined data.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(String.format(
					"create table Variables (%s text,%s text)",
					(Object[]) new String[] { VarProvider.KEY_NAME,
							VarProvider.VALUE_NAME }));
			db.execSQL("insert into Variables values ('Duration' , '50')");
			db.execSQL("insert into Variables values ('Interval' , '50')");
			db.execSQL("insert into Variables values ('Volume' , '100')");
			db.execSQL("insert into Variables values ('CurrentUser' , 'Home')");
			db.execSQL("create table Users (Name text primary key,Country text,District text,uGroup text,InGroup text, OutGroup text, MobilePrefix text)");
			db.execSQL("insert into Users values ('COMPANY' , '86','27','8199',3,9,0)");
			db.execSQL("create table Filter (F0 boolean,F1 boolean, F2 boolean, F3 boolean , F4 boolean ,F5 boolean )");
			db.execSQL("insert into Filter values (1,1,1,1,1,1)");
			db.execSQL("create table DialLog (date text,number text )");
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * DataBase path template the first should be replaced by sdcard path and
	 * the second should be replaced by PackageName.
	 */
	private static final String dataBaseFileName = "%s/Android/data/%s/databases/pref.db";

	private static ExtDataBase singleton = new ExtDataBase();

	public static SQLiteDatabase getDateBase() {
		return singleton.mDB;
	}

	public static ExtDataBase getInstance() {
		return singleton;
	}

	private SQLiteDatabase mDB = null;

	private ExtDataBase() {
	}

	/**
	 * Initializing mDB. This method must be called before calling getDataBase.
	 * @param ctx
	 *            parameter is used to get resources PackageName and SDCard
	 *            Path.
	 */
	public void init(Context ctx) {
		// TODO Auto-generated method stub
		String[] dbplace = new String[] {
				Environment.getExternalStorageDirectory().getPath(),
				ctx.getPackageName() };
		File file = new File(
				String.format(dataBaseFileName, (Object[]) dbplace));
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		ExtSQLiteOpenHelper myHelper = new ExtSQLiteOpenHelper(ctx,
				file.getPath());
		mDB = myHelper.getWritableDatabase();
	}

}
