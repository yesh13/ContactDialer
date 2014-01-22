package com.contactdialer;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ContactsActivity extends Activity {

	private Context mContext = null;
	private ContactsBook mContactsBook = new ContactsBook();
	private Cursor originalCursor=null;
	private Bitmap contactPhoto;

	private ListView mListView = null;
	private MyListAdapter myAdapter = null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menulist, menu);
		return true;
	}

	private void popAboutDialog() {
		View aboutDialog = LayoutInflater.from(mContext).inflate(
				R.layout.about_information, null);
		TextView text = (TextView) aboutDialog.findViewById(R.id.authorInfo);
		text.setText(this.getString(R.string.notf_author)
				+ this.getString(R.string.email));
		PackageInfo pi = null;
		try {
			pi = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		text = (TextView) aboutDialog.findViewById(R.id.versionInfo);
		text.setText(this.getString(R.string.notf_version) + pi.versionName);
		new AlertDialog.Builder(mContext)
				.setTitle(this.getString(R.string.word_about))
				.setView(aboutDialog).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_about:
			popAboutDialog();
			return true;
		case R.id.menu_exit:
			android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID，目前获取自己的也只有该API，否则从/proc中自己的枚举其他进程吧，不过要说明的是，结束其他进程不一定有权限，不然就乱套了。
			System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addSearch() {
		EditText editSearch = (EditText) this.findViewById(R.id.editSearch);
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				mContactsBook.filter(originalCursor,s.toString());
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = this;
		NumberParserModel.getInstance().init(mContext);
		PrefModel.getInstance().init(mContext);
		this.setContentView(R.layout.main);
		mListView = (ListView) this.findViewById(R.id.contactsView);
		/** 得到手机通讯录联系人信息 **/
		ContentResolver resolver = mContext.getContentResolver();
		originalCursor = resolver.query(Phone.CONTENT_URI,
				ContactsUnit.PHONES_PROJECTION, null, null,
				Phone.SORT_KEY_PRIMARY + " asc");
		mContactsBook.filter(originalCursor,"");
		contactPhoto = BitmapFactory.decodeResource(getResources(),
				R.drawable.contact_photo);
		myAdapter = new MyListAdapter();
		addSearch();
		mListView.setAdapter(myAdapter);

		mListView.setOnItemClickListener(new ListItemClickListener());

		super.onCreate(savedInstanceState);
	}

	class ListItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, final View view,
				int position, long id) {
			// pop a dialog to notify the user the app is dialing another phone
			// number right now
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
					.setNegativeButton(
							mContext.getString(R.string.word_cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
			if (Playing.get()) {
				builder.setTitle(
						mContext.getString(R.string.notf_dialing))
						.show();
				return;
			}
			// phone number TextView of an item
			TextView itemNumber = (TextView) view.findViewById(R.id.color_text);

			String numberFrom = itemNumber.getText().toString();
			final View checkView = LayoutInflater.from(mContext).inflate(
					R.layout.number_check, null);
			final EditText editNumber = (EditText) checkView
					.findViewById(R.id.check_number_edit);
			UserModel uModel=new UserModel();
			editNumber.setText(new NumberConverter(numberFrom,uModel.new User(),NumberParserModel.getInstance()).getConverted());
			TextView numberNoti = (TextView) checkView
					.findViewById(R.id.check_number_notification);
			numberNoti.setText(mContext
					.getString(R.string.notf_original_number) + numberFrom);
			builder.setTitle("go on?")
					.setView(checkView)
					.setPositiveButton(mContext.getString(R.string.word_dial),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// TODO Auto-generated method stub
									new Thread(new DialAgent(mContext,
											editNumber)).start();

								}
							}).show();

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
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.colorlist, null);

			}
			ImageView iamge = (ImageView) convertView
					.findViewById(R.id.color_image);
			TextView title = (TextView) convertView
					.findViewById(R.id.color_title);
			TextView text = (TextView) convertView
					.findViewById(R.id.color_text);
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