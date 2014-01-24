package com.contactdialer.setting;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.contactdialer.R;
import com.contactdialer.common.VarProvider;

@SuppressLint("ValidFragment")
class PrefFragment extends Fragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.pref, null);
		TextView tv = (TextView) view.findViewById(R.id.editDuration);
		tv.setText(String.valueOf(VarProvider.getInstance().getDuration()));
		tv = (TextView) view.findViewById(R.id.editInterval);
		tv.setText(String.valueOf(VarProvider.getInstance().getInterval()));
		SeekBar sb = (SeekBar) view.findViewById(R.id.seekVolume);
		sb.setMax(VarProvider.MAX);
		sb.setProgress(VarProvider.getInstance().getVolume());
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem mi = menu.add(0, 0, 0, R.string.word_add);
		mi.setIcon(R.drawable.ic_action_save);
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case 0:
					((SettingActivity) getActivity()).setmSave(true);
					getActivity().finish();
					break;

				default:
					break;
				}
				return false;
			}
		});
	}



	@Override
	public void onDestroyView() {
		TextView tv = (TextView) getView().findViewById(R.id.editDuration);
		String str = tv.getText().toString();
		VarProvider.getInstance().setDuration(Integer.parseInt(str));
		tv = (TextView) getView().findViewById(R.id.editInterval);
		str = tv.getText().toString();
		VarProvider.getInstance().setInterval(Integer.parseInt(str));
		SeekBar sb = (SeekBar) getView().findViewById(R.id.seekVolume);
		VarProvider.getInstance().setVolume(sb.getProgress());
		super.onDestroyView();
	}

}
