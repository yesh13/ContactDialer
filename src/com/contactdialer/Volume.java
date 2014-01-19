package com.contactdialer;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Volume {
	public static int max=100;
	private int volume;
	private Context mContext=null;
	private Volume(){
	}   
    private static final Volume singleton = new Volume();
    public static void init(Context ctx) {
    	singleton.mContext = ctx;
		SharedPreferences sp = ctx.getSharedPreferences("volume",Context.MODE_PRIVATE);		
		singleton.volume = sp.getInt("volume", max);
    }
    public static int getVolume(){return singleton.volume;} 
    public static int setVolume(int time){return singleton.volume=time;} 
	public static void commit(Context ctx) {
		//save volume data into storage
		SharedPreferences sp = ctx.getSharedPreferences("volume",Context.MODE_PRIVATE);		
		Editor editor = sp.edit();
		editor.putInt("volume", singleton.volume);
		if (singleton.mContext != null) {
			editor.commit();
		}
	}

}
