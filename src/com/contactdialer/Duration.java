package com.contactdialer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Duration {
	private int duration=50;
	private int interval=50;
	private Context mContext=null;
	private Duration(){
	}   
    private static final Duration singleton = new Duration();
    public static void init(Context ctx) {
    	singleton.mContext = ctx;
		SharedPreferences sp = ctx.getSharedPreferences("duration",Context.MODE_PRIVATE);		
		singleton.duration = sp.getInt("duration", 200);
		singleton.interval = sp.getInt("interval", 200);
    }
    public static int getDuration(){return singleton.duration;} 
    public static int getInterval(){return singleton.interval;} 
    public static int setDuration(int time){return singleton.duration=time;} 
    public static int setInterval(int time){return singleton.interval=time;}
	public static void commit(Context ctx) {
		//save duration and interval data into storage
		SharedPreferences sp = ctx.getSharedPreferences("duration",Context.MODE_PRIVATE);		
		Editor editor = sp.edit();
		editor.putInt("duration", singleton.duration);
		editor.putInt("interval", singleton.interval);
		if (singleton.mContext != null) {
			editor.commit();
		}
	}
}
