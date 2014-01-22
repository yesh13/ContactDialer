package com.contactdialer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		setContentView(R.layout.setting);

		TextView tv = (TextView) this.findViewById(R.id.editDuration);
		tv.setText(String.valueOf(VariablesControl.getInstance().getDuration()));
		tv = (TextView) this.findViewById(R.id.editInterval);
		tv.setText(String.valueOf(VariablesControl.getInstance().getInterval()));
		SeekBar sb = (SeekBar) this.findViewById(R.id.seekVolume);
		sb.setMax(VariablesControl.MAX);
		sb.setProgress(VariablesControl.getInstance().getVolume());
	}

	public void saveDuration(View view) {
		TextView tv = (TextView) this.findViewById(R.id.editDuration);
		String str = tv.getText().toString();
		VariablesControl.getInstance().setDuration(Integer.parseInt(str));
		tv = (TextView) this.findViewById(R.id.editInterval);
		str = tv.getText().toString();
		VariablesControl.getInstance().setInterval(Integer.parseInt(str));
		SeekBar sb = (SeekBar) this.findViewById(R.id.seekVolume);
		VariablesControl.getInstance().setVolume(sb.getProgress());
		VariablesControl.getInstance().commit();
		finish();
	}

}
