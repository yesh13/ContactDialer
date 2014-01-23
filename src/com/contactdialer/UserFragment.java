package com.contactdialer;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class UserFragment extends Fragment {
	private String longClicked = null;
	private ListView mListView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_list, null);
		
		mListView = (ListView) view.findViewById(R.id.list1);
		mListView.setAdapter(new MyListAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView,
					final View view, int position, long id) {
				TextView tv = (TextView) view
						.findViewById(R.id.user_list_item_name);
				VariablesControl.getInstance().setCurrentUser(
						tv.getText().toString());
				((MyListAdapter) mListView.getAdapter()).notifyDataSetChanged();
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, long id) {
				TextView tv = (TextView) view
						.findViewById(R.id.user_list_item_name);
				popUpMenu(view);
				longClicked = tv.getText().toString();

				return false;
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		((MyListAdapter) mListView.getAdapter()).notifyDataSetChanged();
		super.onResume();
	}

	private void popUpMenu(View anchor) {
		PopupMenu popup = new PopupMenu(getActivity(), anchor);
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.user_item_selected_edit:
					Intent intent = new Intent(getActivity(),
							UserEditActivity.class);
					intent.putExtra("name", longClicked);
					intent.putExtra("mode", UserEditActivity.MODE_EDIT);
					startActivity(intent);
					break;
				case R.id.user_item_selected_delete:
					UserModel um = new UserModel();
					if (VariablesControl.getInstance().getCurrentUser()
							.equals(longClicked)) {
						Toast.makeText(getActivity(),
								R.string.equal_name_notification,
								Toast.LENGTH_SHORT).show();
						break;
					}
					um.delete(longClicked);
					((MyListAdapter) mListView.getAdapter())
							.notifyDataSetChanged();
					break;

				default:
					break;
				}
				return false;
			}
		});
		popup.inflate(R.menu.user_item_selected);
		popup.show();
	}

	class MyListAdapter extends BaseAdapter {

		public int getCount() {
			return new UserModel().list().size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.user_list_item, null);

			}

			
			TextView text = (TextView) convertView
					.findViewById(R.id.user_list_item_name);
			TextView prefix = (TextView) convertView.findViewById(R.id.prefix);
			String name = new UserModel().list().get(position);
			text.setText(name);
			if (name.equals(VariablesControl.getInstance().getCurrentUser())) {
				prefix.setText("��");

			} else {
				prefix.setText(" ");
			}
			// clear the displayname text if the name is the same as the last
			// name
			// if (name.equals(VariablesControl.getInstance().getCurrentUser()))
			// {
			// Bitmap checkIcon = BitmapFactory.decodeResource(getResources(),
			// R.drawable.ic_check);
			// iamge.setImageBitmap(checkIcon);
			// } else{
			// iamge.setImageBitmap(null);
			// }
			return convertView;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

	}

	public void addUser() {
		Intent intent = new Intent(getActivity(), UserEditActivity.class);
		intent.putExtra("mode", UserEditActivity.MODE_CREATE);
		startActivity(intent);
	}
}
