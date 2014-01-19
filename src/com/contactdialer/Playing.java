package com.contactdialer;

public class Playing {
	private boolean play;
	private Playing() {play=false;}
	private static final Playing singleton = new Playing();
	public static final boolean get() {
		return singleton.play;
	}
	public static final void set(boolean b) {
		singleton.play=b;
	}

}
