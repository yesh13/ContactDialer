package com.contactdialer.main;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.contactdialer.R;
import com.contactdialer.common.UserModel;
import com.contactdialer.common.VarProvider;

class DialAgent {
	static class DialThread implements Runnable {
		/**
		 * 
		 */
		private final Context mContext;
		private String phoneNumber;

		DialThread(Context ctx, String phoneNumber) {
			mContext = ctx;
			this.phoneNumber = phoneNumber;
		}

		public void run() {
			playing = true;
			AudioManager am = (AudioManager) mContext
					.getSystemService(Context.AUDIO_SERVICE);
			int musicMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int musicCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			am.setStreamVolume(AudioManager.STREAM_MUSIC,
					(int) (VarProvider.getInstance().getVolume()
							/ ((double) VarProvider.MAX) * musicMax), 0);
			ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
			int textLength = phoneNumber.length();
			for (int itr = 0; itr < textLength; itr = itr + 1) {
				int tone = (int) phoneNumber.charAt(itr) - 0x30;
				if (tone < 10 && tone >= 0)
					tg.startTone(tone, VarProvider.getInstance().getDuration());
				else
					continue;

				try {
					Thread.sleep(VarProvider.getInstance().getDuration()
							+ VarProvider.getInstance().getInterval());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch
					// block
					e.printStackTrace();
				}
			}
			playing = false;
			am.setStreamVolume(AudioManager.STREAM_MUSIC, musicCurrent, 0);
		}
	}

	private static boolean playing = false;

	public static void popDialDialog(final Context ctx, final String numberFrom,final DialFragment df,boolean check) {
		// pop a dialog to notify the user the app is dialing another phone
		// number right now
		if (playing) {
			Toast.makeText(ctx, R.string.notf_dialing, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// if there is no dialing now, then popup a dialog to check number
		final View checkView = LayoutInflater.from(ctx).inflate(
				R.layout.number_check, null);
		final EditText editNumber = (EditText) checkView
				.findViewById(R.id.check_number_edit);
		UserModel uModel = new UserModel();
		final String phoneNumber=new NumberConverter(numberFrom, uModel
				.get(VarProvider.getInstance().getCurrentUser()),
				NumberParserModel.getInstance()).getConverted();
		editNumber.setText(phoneNumber);
		TextView numberNoti = (TextView) checkView
				.findViewById(R.id.check_number_notification);
		numberNoti.setText(ctx.getString(R.string.notf_original_number)
				+ numberFrom);
		new AlertDialog.Builder(ctx)
				.setNegativeButton(ctx.getString(R.string.word_cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						})
				.setTitle("go on?")
				.setView(checkView)
				.setPositiveButton(ctx.getString(R.string.word_dial),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String finalPhoneNumber=editNumber.getText().toString();
								dialAndInsertLog(ctx, numberFrom, finalPhoneNumber,df);
							}

							
						}).show();
	}
	private static void dialAndInsertLog(final Context ctx,
			final String numberFrom,
			final String phoneNumber,
			final DialFragment df) {
		DiallogModel dm = new DiallogModel();
		dm.insert(dm.new DialLogItem(String
				.valueOf(Calendar.getInstance()
						.getTimeInMillis()), numberFrom));
		if (df!=null) {
			df.onDial();
		}
		VarProvider.getInstance().setLastNumber(phoneNumber);
		new Thread(new DialThread(ctx, phoneNumber))
				.start();
	}
	public static void popDialDialog(final Context ctx, final String numberFrom){
		popDialDialog(ctx, numberFrom, null,true);
	}
	public static void dial(final Context ctx, final String phoneNumber) {
		// pop a dialog to notify the user the app is dialing another phone
		// number right now
		if (playing) {
			Toast.makeText(ctx, R.string.notf_dialing, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		new Thread(new DialThread(ctx, phoneNumber))
		.start();
	}
}
