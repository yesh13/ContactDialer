package com.contactdialer;

import android.media.AudioManager;
import android.media.ToneGenerator;
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
import android.provider.ContactsContract.CommonDataKinds.Photo;
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
	private ContactsBook mContactsBook=new ContactsBook();
	private ContactsBook mFilteredContactsBook=null;
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
		View aboutDialog = LayoutInflater.from(mContext)
				.inflate(R.layout.about_information, null);
		TextView text = (TextView) aboutDialog.findViewById(R.id.authorInfo);
		text.setText(this.getString(R.string.label_author)+this.getString(R.string.email));
		PackageInfo pi=null;
		try {
			pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		text = (TextView) aboutDialog.findViewById(R.id.versionInfo);
		text.setText(this.getString(R.string.label_version)+pi.versionName);
		new AlertDialog.Builder(mContext)
		.setTitle(this.getString(R.string.label_about))
		.setView(aboutDialog)
		.show();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, Setting.class);
			startActivity(intent);
			return true;
		case R.id.menu_about:
			popAboutDialog();
			return true;
		case R.id.menu_exit:
			 android.os.Process.killProcess(android.os.Process.myPid());    //获取PID，目前获取自己的也只有该API，否则从/proc中自己的枚举其他进程吧，不过要说明的是，结束其他进程不一定有权限，不然就乱套了。
			  System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addSearch() {
		EditText editSearch = (EditText) this.findViewById(R.id.editSearch);
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				mFilteredContactsBook=mContactsBook.filter(s.toString());
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
		Duration.init(this);
		Volume.init(this);
		this.setContentView(R.layout.main);
		mListView = (ListView) this.findViewById(R.id.contactsView);
		/** 得到手机通讯录联系人信息 **/
		ContentResolver resolver = mContext.getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				ContactsUnit.PHONES_PROJECTION, null, 
				null, Phone.SORT_KEY_PRIMARY+" asc");
		mContactsBook.read(phoneCursor);
		mFilteredContactsBook=mContactsBook;
		contactPhoto = BitmapFactory.decodeResource(getResources(),
				R.drawable.contact_photo);
		myAdapter = new MyListAdapter();
		addSearch();
		mListView.setAdapter(myAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView,
					final View view, int position, long id) {
				
				if (Playing.get()) {
					AlertDialog hintDialog = new AlertDialog.Builder(mContext)
					.setNegativeButton(mContext.getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).setTitle(mContext.getString(R.string.label_dialnotification)).create();
					hintDialog.show();
					return;
				}
				//phone number TextView of an item
				TextView itemNumber = (TextView) view
						.findViewById(R.id.color_text);
				
				String numberFrom = itemNumber.getText().toString();
				final EditText editNumber = (EditText) LayoutInflater.from(mContext)
						.inflate(R.layout.number_check, null);
				editNumber.setText(numberFrom);
				new AlertDialog.Builder(mContext)
				.setTitle("go on?")
				.setView(editNumber)
				.setPositiveButton(mContext.getString(R.string.label_dial), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						// TODO Auto-generated method stub
						new Thread(new Runnable() {
							public void run() {
								Playing.set(true);
								AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
								int musicMax = am.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
								int musicCurrent = am.getStreamVolume( AudioManager.STREAM_MUSIC );
								am.setStreamVolume(AudioManager.STREAM_MUSIC, 
										(int) (Volume.getVolume()/((double) Volume.max)*musicMax), 0);
								ToneGenerator tg = new ToneGenerator(
										AudioManager.STREAM_MUSIC, 100);
								String phoneNumber = editNumber.getText().toString();
								int textLength = phoneNumber.length();
								for (int itr = 0; itr < textLength; itr = itr + 1) {
									int tone = (int) phoneNumber.charAt(itr) - 0x30;
									if (tone < 10 && tone >= 0)
										tg.startTone(tone, Duration.getDuration());
									else
										continue;

									try {
										Thread.sleep(Duration.getDuration()
												+ Duration.getInterval());
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								Playing.set(false);
								am.setStreamVolume(AudioManager.STREAM_MUSIC,
										musicCurrent,0);
							}
						}).start();
						
					}
				})
				.setNegativeButton(mContext.getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();

			}
		});

		super.onCreate(savedInstanceState);
	}

	class MyListAdapter extends BaseAdapter {

		public int getCount() {
			return mFilteredContactsBook.size();
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
			ImageView iamge = (ImageView) convertView.findViewById(R.id.color_image);
			TextView title = (TextView) convertView.findViewById(R.id.color_title);
			TextView text = (TextView) convertView.findViewById(R.id.color_text);
			ContactsUnit person= mFilteredContactsBook.get(position);
				// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
//				if (photoid > 0) {
//					Uri uri = ContentUris.withAppendedId(
//							ContactsContract.Contacts.CONTENT_URI, contactid);
//					InputStream input = ContactsContract.Contacts
//							.openContactPhotoInputStream(resolver, uri);
				//	contactPhoto = BitmapFactory.decodeStream(input);
				//} else {

			//}

			// 绘制联系人名称
			title.setText(person.getDisplayName());
			// 绘制联系人号码
			text.setText(person.getNumber());
			// 绘制联系人头像
			iamge.setImageBitmap(contactPhoto);
			// clear the displayname text if the name is the same as the last
			// name
			if (position > 0) {
				ContactsUnit lastPerson = mFilteredContactsBook
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