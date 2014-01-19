package com.contactdialer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class Setting extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // TODO Auto-generated method stub
	    setContentView(R.layout.setting);
	    
	    TextView tv=(TextView) this.findViewById(R.id.editDuration);
	    tv.setText(String.valueOf(Duration.getDuration()));
	    tv=(TextView) this.findViewById(R.id.editInterval);
	    tv.setText(String.valueOf(Duration.getInterval()));
	    SeekBar sb=(SeekBar) this.findViewById(R.id.seekVolume);
	    sb.setMax(Volume.max);
	    sb.setProgress(Volume.getVolume());
	}
	
	public void saveDuration(View view) {
		TextView tv=(TextView) this.findViewById(R.id.editDuration);
		String str=tv.getText().toString();
	    Duration.setDuration(Integer.parseInt(str));
	    tv=(TextView) this.findViewById(R.id.editInterval);
	    str=tv.getText().toString();
	    Duration.setInterval(Integer.parseInt(str));
	    Duration.commit(this);
	    SeekBar sb=(SeekBar) this.findViewById(R.id.seekVolume);
	    Volume.setVolume(sb.getProgress());
	    Volume.commit(this);
		finish();
	}

}
