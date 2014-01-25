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
	private String compileString(String filterString,boolean first) {
		String searchString = filterString.toUpperCase();
		int length = searchString.length();
		String returnString = "";
		for (int i = 0; i < length; i++) {
			String start = " ";
			if (i == 0) {
				start = "\b";
				if(first){
					start="^";
				}
			}
			returnString = returnString + start + searchString.charAt(i)
					+ "\\S* \\S*";
		}
		return returnString;
	}

	public ContactsBook filter(Cursor contactCursor, String filterString) {
		contacts = new ArrayList<ContactsUnit>();
		ArrayList<ContactsUnit> contactsNotFirst = new ArrayList<ContactsUnit>();
		Pattern patternFirst = Pattern.compile(compileString(filterString,true));
		Pattern patternNotFirstPattern = Pattern.compile(compileString(filterString,false));
		contactCursor.moveToFirst();
		do {
			ContactsUnit cu = new ContactsUnit(contactCursor);
			if (patternFirst.matcher(cu.getSortKey()).find()) {
				contacts.add(cu);
				continue;
			}
			if (patternNotFirstPattern.matcher(cu.getSortKey()).find()) {
				contactsNotFirst.add(cu);
			}
		} while (contactCursor.moveToNext());
		contacts.addAll(contactsNotFirst);
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
