package com.contactdialer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class PrefFragment extends Fragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.pref, null);
		TextView tv = (TextView) view.findViewById(R.id.editDuration);
		tv.setText(String.valueOf(VariablesControl.getInstance().getDuration()));
		tv = (TextView) view.findViewById(R.id.editInterval);
		tv.setText(String.valueOf(VariablesControl.getInstance().getInterval()));
		SeekBar sb = (SeekBar) view.findViewById(R.id.seekVolume);
		sb.setMax(VariablesControl.MAX);
		sb.setProgress(VariablesControl.getInstance().getVolume());
		return view;
	}

	@Override
	public void onDestroyView() {
		TextView tv = (TextView) getView().findViewById(R.id.editDuration);
		String str = tv.getText().toString();
		VariablesControl.getInstance().setDuration(Integer.parseInt(str));
		tv = (TextView) getView().findViewById(R.id.editInterval);
		str = tv.getText().toString();
		VariablesControl.getInstance().setInterval(Integer.parseInt(str));
		SeekBar sb = (SeekBar) getView().findViewById(R.id.seekVolume);
		VariablesControl.getInstance().setVolume(sb.getProgress());
		super.onDestroyView();
	}
	
}
