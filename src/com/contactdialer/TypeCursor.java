package com.contactdialer;

import android.database.Cursor;
import android.database.CursorWrapper;

public class TypeCursor extends CursorWrapper {
	public TypeCursor(Cursor cursor,int type) {
		super(cursor);
		// TODO Auto-generated constructor stub
	}

}
