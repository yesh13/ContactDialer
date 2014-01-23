package com.contactdialer;

import java.util.Calendar;

import com.contactdialer.LogsModel.LogItem;
import com.contactdialer.LogsModel.LogsList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("DefaultLocale")
public class LogsFragment extends Fragment {
	View dialogView = null;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.label_edit_filter);
			dialogView = buildDialogView();
			builder.setView(dialogView);
			builder.setPositiveButton(R.string.word_save,
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							boolean[] ft = new boolean[LogsFilter.NUM_TYPE];
							ft[LogsFilter.TYPE_DIAL] = ((CheckBox) dialogView
									.findViewById(R.id.dial)).isChecked();
							ft[LogsFilter.TYPE_SMS_IN] = ((CheckBox) dialogView
									.findViewById(R.id.sms_in)).isChecked();
							ft[LogsFilter.TYPE_SMS_OUT] = ((CheckBox) dialogView
									.findViewById(R.id.sms_out)).isChecked();
							ft[LogsFilter.TYPE_OUT_GOING] = ((CheckBox) dialogView
									.findViewById(R.id.out_going)).isChecked();
							ft[LogsFilter.TYPE_IN_COMING] = ((CheckBox) dialogView
									.findViewById(R.id.in_coming)).isChecked();
							ft[LogsFilter.TYPE_MISSED] = ((CheckBox) dialogView
									.findViewById(R.id.missed)).isChecked();
							VariablesControl.getInstance().setFilter(
									new LogsFilter(ft));
							updateCallLogList();
							((MyListAdapter) mListView.getAdapter())
									.notifyDataSetChanged();
						}
					});
			builder.setNegativeButton(R.string.word_cancel,
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
			builder.show();
			break;
		default:
			return true;
		}
		return false;
	}
	
	

	private View buildDialogView() {
		boolean[] ft = VariablesControl.getInstance().getLogFilter()
				.getFilterTag();
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.edit_filter, null);
		((CheckBox) view.findViewById(R.id.dial))
				.setChecked(ft[LogsFilter.TYPE_DIAL]);
		((CheckBox) view.findViewById(R.id.sms_in))
				.setChecked(ft[LogsFilter.TYPE_SMS_IN]);
		((CheckBox) view.findViewById(R.id.sms_out))
				.setChecked(ft[LogsFilter.TYPE_SMS_OUT]);
		((CheckBox) view.findViewById(R.id.out_going))
				.setChecked(ft[LogsFilter.TYPE_OUT_GOING]);
		((CheckBox) view.findViewById(R.id.in_coming))
				.setChecked(ft[LogsFilter.TYPE_IN_COMING]);
		((CheckBox) view.findViewById(R.id.missed))
				.setChecked(ft[LogsFilter.TYPE_MISSED]);
		return view;
	}

	private ListView mListView = null;
	private MyListAdapter myAdapter = null;
	private LogsList mCallLogList;
	private Bitmap photoCall;
	private Bitmap photoSMS;
	private Bitmap photoDial;
	private Bitmap iconOut;
	private Bitmap iconIn;
	private Bitmap iconMissed;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.calllogs, null);
		mListView = (ListView) view.findViewById(R.id.listView);
		updateCallLogList();
		myAdapter = new MyListAdapter();
		mListView.setAdapter(myAdapter);
		mListView.setOnItemClickListener(new ListItemClickListener());
		photoCall = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_action_call);
		photoSMS = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_action_email);
		photoDial = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_action_dial);
		iconOut = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_type2_outgoing);
		iconIn = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_type2_incoming);
		iconMissed = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_type2_missed);
		return view;
	}



	private void updateCallLogList() {
		mCallLogList = LogsModel.getInstance().getLogsList();
	}

	class ListItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, final View view,
				int position, long id) {
			// pop a dialog to notify the user the app is dialing another phone
			// number right now

			if (Playing.get()) {
				Toast.makeText(getActivity(), R.string.notf_dialing,
						Toast.LENGTH_SHORT).show();
				return;
			}
			// phone number TextView of an item
			TextView itemNumber = (TextView) view.findViewById(R.id.number);

			String numberFrom = itemNumber.getText().toString();
			((MainActivity) getActivity()).popDialDialog(numberFrom);

		}
	}

	@SuppressLint("DefaultLocale")
	class MyListAdapter extends BaseAdapter {

		public int getCount() {
			return mCallLogList.getCount();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("DefaultLocale")
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.calllogs_list_item, null);
			}
			updateView(position, convertView);
			return convertView;
		}

		private void updateView(int position, View convertView) {
			LogItem item = mCallLogList.get(position);
			((TextView) convertView.findViewById(R.id.name)).setText(item
					.getName());
			((TextView) convertView.findViewById(R.id.number)).setText(item
					.getNumber());
			Calendar now = Calendar.getInstance();
			Calendar callDate = Calendar.getInstance();
			callDate.setTimeInMillis(item.getDate());
			String dateString = null;
			if (now.get(Calendar.YEAR) == callDate.get(Calendar.YEAR)) {
				if (now.get(Calendar.DAY_OF_YEAR) == callDate
						.get(Calendar.DAY_OF_YEAR)) {
					dateString = String.format("%d:%d:%d",
							callDate.get(Calendar.HOUR),
							callDate.get(Calendar.MINUTE),
							callDate.get(Calendar.SECOND));
				} else {
					dateString = String.format("%d/%d",
							callDate.get(Calendar.MONTH) + 1,
							callDate.get(Calendar.DAY_OF_MONTH));
				}
			} else {
				dateString = String.format("%d/%d/%d",
						1 + callDate.get(Calendar.MONTH),
						callDate.get(Calendar.DAY_OF_MONTH),
						callDate.get(Calendar.YEAR));
			}
			((TextView) convertView.findViewById(R.id.date))
					.setText(dateString);
			switch (item.getType()) {
			case LogsFilter.TYPE_OUT_GOING:
			case LogsFilter.TYPE_IN_COMING:
			case LogsFilter.TYPE_MISSED:
				((ImageView) convertView.findViewById(R.id.type1))
						.setImageBitmap(photoCall);
				break;
			case LogsFilter.TYPE_SMS_IN:
			case LogsFilter.TYPE_SMS_OUT:
				((ImageView) convertView.findViewById(R.id.type1))
						.setImageBitmap(photoSMS);
				break;
			case LogsFilter.TYPE_DIAL:
				((ImageView) convertView.findViewById(R.id.type1))
						.setImageBitmap(photoDial);
				break;
			default:
				break;
			}
			switch (item.getType()) {
			case LogsFilter.TYPE_IN_COMING:
			case LogsFilter.TYPE_SMS_IN:
				((ImageView) convertView.findViewById(R.id.type2))
						.setImageBitmap(iconIn);
				break;
			case LogsFilter.TYPE_SMS_OUT:
			case LogsFilter.TYPE_DIAL:
			case LogsFilter.TYPE_OUT_GOING:
				((ImageView) convertView.findViewById(R.id.type2))
						.setImageBitmap(iconOut);
				break;
			case LogsFilter.TYPE_MISSED:
				((ImageView) convertView.findViewById(R.id.type2))
						.setImageBitmap(iconMissed);
				break;
			default:
				break;
			}
		}
	}

}
