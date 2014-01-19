package com.contactdialer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.database.Cursor;

public class ContactsBook implements Cloneable{
	private ArrayList<ContactsUnit> contacts=new ArrayList<ContactsUnit>();
	public void read(Cursor contactCursor) {
		contactCursor.moveToFirst();
		do {
			contacts.add(new ContactsUnit(contactCursor));
		}while (contactCursor.moveToNext());
	}
	public ContactsUnit get(int position) {
		return contacts.get(position);
	}
	public int size(){
		return contacts.size();
	}
	@SuppressWarnings("unchecked")
	public Object clone() {
		ContactsBook cb=new ContactsBook();
		cb.contacts=(ArrayList<ContactsUnit>) this.contacts.clone();
		return cb;
	}
	public ContactsBook filter(String filterString){
		ContactsBook db =new ContactsBook();
		Iterator<ContactsUnit> it =contacts.iterator();
		Pattern pattern= Pattern.compile(compileString(filterString));
		while(it.hasNext()) {
			ContactsUnit cu =it.next();
			if(pattern.matcher(cu.getSortKey()).find()){
				db.contacts.add(cu);
			}
		}
		return db;
	}
	
	//compileString create String for regex.Pattern to compile
	@SuppressLint("DefaultLocale")
	private String compileString(String filterString){
		String searchString=filterString.toUpperCase();
		int length=searchString.length();
		String returnString = "";
		for (int i=0;i<length;i++) {
			String start=" ";
			if (i==0){
				start="";
			}
			returnString=returnString+start+searchString.charAt(i)+"\\S* \\S*";
		}
		return returnString;
	}
}
