package com.contactdialer.main;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

@SuppressLint("InlinedApi")
class ContactsUnit {
	String number;
	String displayName;
	String sortKey;
	public static final String[] PHONES_PROJECTION = new String[] {
			Phone.SORT_KEY_PRIMARY, Phone.DISPLAY_NAME_PRIMARY, Phone.NUMBER };

	public ContactsUnit(final Cursor contactCursor) {
		number = contactCursor.getString(2);
		displayName = contactCursor.getString(1);
		sortKey = contactCursor.getString(0);
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getNumber() {
		return number;
	}

	public String getSortKey() {
		return sortKey;
	}
}
