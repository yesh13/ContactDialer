package com.contactdialer.main;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.database.Cursor;

class ContactsBook implements Cloneable {
	private ArrayList<ContactsUnit> contacts = new ArrayList<ContactsUnit>();

	@SuppressWarnings("unchecked")
	public Object clone() {
		ContactsBook cb = new ContactsBook();
		cb.contacts = (ArrayList<ContactsUnit>) this.contacts.clone();
		return cb;
	}

	// compileString create String for regex.Pattern to compile
	@SuppressLint("DefaultLocale")
	private String compileString(String filterString) {
		String searchString = filterString.toUpperCase();
		int length = searchString.length();
		String returnString = "";
		for (int i = 0; i < length; i++) {
			String start = " ";
			if (i == 0) {
				start = "";
			}
			returnString = returnString + start + searchString.charAt(i)
					+ "\\S* \\S*";
		}
		return returnString;
	}

	public ContactsBook filter(Cursor contactCursor, String filterString) {
		contacts = new ArrayList<ContactsUnit>();
		Pattern pattern = Pattern.compile(compileString(filterString));
		contactCursor.moveToFirst();
		do {
			ContactsUnit cu = new ContactsUnit(contactCursor);
			if (pattern.matcher(cu.getSortKey()).find()) {
				contacts.add(cu);
			}
		} while (contactCursor.moveToNext());
		return this;
	}

	public ContactsUnit get(int position) {
		return contacts.get(position);
	}

	public void read(Cursor contactCursor) {
		contactCursor.moveToFirst();
		do {
			contacts.add(new ContactsUnit(contactCursor));
		} while (contactCursor.moveToNext());
	}

	public int size() {
		return contacts.size();
	}
}