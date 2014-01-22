package com.contactdialer;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.widget.EditText;

class DialAgent implements Runnable {
	/**
	 * 
	 */
	private final Context mContext;
	private final EditText editNumber;

	DialAgent(Context context, EditText editNumber) {
		mContext=context;
		this.editNumber = editNumber;
	}

	public void run() {
		Playing.set(true);
		AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		int musicMax = am
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int musicCurrent = am
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		am.setStreamVolume(
				AudioManager.STREAM_MUSIC,
				(int) (VariablesControl.getInstance().getVolume()
						/ ((double) VariablesControl.MAX) * musicMax),
				0);
		ToneGenerator tg = new ToneGenerator(
				AudioManager.STREAM_MUSIC,
				100);
		String phoneNumber = editNumber
				.getText().toString();
		int textLength = phoneNumber
				.length();
		for (int itr = 0; itr < textLength; itr = itr + 1) {
			int tone = (int) phoneNumber
					.charAt(itr) - 0x30;
			if (tone < 10 && tone >= 0)
				tg.startTone(tone, VariablesControl.getInstance()
						.getDuration());
			else
				continue;

			try {
				Thread.sleep(VariablesControl.getInstance()
						.getDuration()
						+ VariablesControl.getInstance()
								.getInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch
				// block
				e.printStackTrace();
			}
		}
		Playing.set(false);
		am.setStreamVolume(
				AudioManager.STREAM_MUSIC,
				musicCurrent, 0);
	}
}