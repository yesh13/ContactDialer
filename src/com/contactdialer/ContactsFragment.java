package com.contactdialer;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ContactsFragment extends Fragment {

	private ContactsBook mContactsBook = new ContactsBook();
	private Cursor originalCursor=null;
	private Bitmap contactPhoto;

	private ListView mListView = null;
	private MyListAdapter myAdapter = null;
	private void addSearch(View view) {
		EditText editSearch = (EditText) view.findViewById(R.id.editSearch);
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				mContactsBook.filter(originalCursor,s.toString());
				mListView.scrollTo(0, 0);
				((MyListAdapter) mListView.getAdapter()).notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

		});
	}
	
	class ListItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, final View view,
				int position, long id) {
			// pop a dialog to notify the user the app is dialing another phone
			// number right now
			
			if (Playing.get()) {
				Toast.makeText(getActivity(), R.string.notf_dialing, Toast.LENGTH_SHORT).show();
				return;
			}
			// phone number TextView of an item
			TextView itemNumber = (TextView) view.findViewById(R.id.number);

			String numberFrom = itemNumber.getText().toString();
			((MainActivity) getActivity()).popDialDialog(numberFrom);

		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.contacts, null);
		mListView = (ListView) view.findViewById(R.id.contactsView);
		/** 得到手机通讯录联系人信息 **/
		ContentResolver resolver = getActivity().getContentResolver();
		originalCursor = resolver.query(Phone.CONTENT_URI,
				ContactsUnit.PHONES_PROJECTION, null, null,
				Phone.SORT_KEY_PRIMARY + " asc");
		mContactsBook.filter(originalCursor,"");
		contactPhoto = BitmapFactory.decodeResource(getResources(),
				R.drawable.contact_photo);
		myAdapter = new MyListAdapter();
		mListView.setAdapter(myAdapter);
		mListView.setOnItemClickListener(new ListItemClickListener());
		
		addSearch(view);
		return view;
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		menu.removeItem(R.id.edit);
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if (originalCursor!=null) {
			originalCursor.close();
		}
	}

	class MyListAdapter extends BaseAdapter {

		public int getCount() {
			return mContactsBook.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.colorlist, null);

			}
			ImageView iamge = (ImageView) convertView
					.findViewById(R.id.color_image);
			TextView title = (TextView) convertView
					.findViewById(R.id.color_title);
			TextView text = (TextView) convertView
					.findViewById(R.id.number);
			ContactsUnit person = mContactsBook.get(position);
			// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
			// if (photoid > 0) {
			// Uri uri = ContentUris.withAppendedId(
			// ContactsContract.Contacts.CONTENT_URI, contactid);
			// InputStream input = ContactsContract.Contacts
			// .openContactPhotoInputStream(resolver, uri);
			// contactPhoto = BitmapFactory.decodeStream(input);
			// } else {

			// }

			// 绘制联系人名称
			title.setText(person.getDisplayName());
			// 绘制联系人号码
			text.setText(person.getNumber());
			// 绘制联系人头像
			iamge.setImageBitmap(contactPhoto);
			// clear the displayname text if the name is the same as the last
			// name
			if (position > 0) {
				ContactsUnit lastPerson = mContactsBook
						.get(position - 1);
				if (lastPerson.getSortKey().equals(person.getSortKey())) {
					title.setText("");
					iamge.setImageBitmap(null);
				}
			}

			return convertView;
		}

	}
	

	

}
