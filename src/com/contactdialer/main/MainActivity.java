package com.contactdialer.main;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.contactdialer.R;
import com.contactdialer.common.ExtDataBase;
import com.contactdialer.common.VarProvider;
import com.contactdialer.setting.SettingActivity;

public class MainActivity extends Activity {

	public class ContactsTabListener implements ActionBar.TabListener {
		private ContactsFragment mFragment;
		private final String mTag;

		public ContactsTabListener(String tag) {
			mTag = tag;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = new ContactsFragment();
				ft.add(android.R.id.content, mFragment, mTag);
				Log.d("onCall", "create tab1");
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
				Log.d("onCall", "attach tab1");
			}
			activeFragment=mFragment;
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
			Log.d("onCall", "detach tab1");
		}

	}

	public class LogsTabListener implements ActionBar.TabListener {
		private LogsFragment mFragment;
		private final String mTag;


		public LogsTabListener(String tag) {
			mTag = tag;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.

		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = new LogsFragment();
				ft.add(android.R.id.content, mFragment, mTag);
				Log.d("onCall", "create tab2");
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
				Log.d("onCall", "attach tab2");
			}
			activeFragment=mFragment;
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
			Log.d("onCall", "detach tab2");
		}

	}

	private Context mContext = null;
	private DialFragment activeFragment=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("onCall", "Create Main");
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null){
			return;
		}
		mContext = this;
		ExtDataBase.getInstance().init(getApplicationContext());
		NumberParserModel.getInstance().init(getApplicationContext());
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab = actionBar
				.newTab()
				.setText(R.string.nav_main_contacts)
				.setTabListener(
						new ContactsTabListener(
								getString(R.string.nav_main_contacts)));
		actionBar.addTab(tab);
		tab = actionBar
				.newTab()
				.setText(R.string.nav_main_log)
				.setTabListener(
						new LogsTabListener(
								getString(R.string.nav_main_log)));
		actionBar.addTab(tab);
		Log.d("onCall", "Create Main finish");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.lastDial:
			String phoneNumber=VarProvider.getInstance().getLastNumber();
			if(phoneNumber==null){
				break;
			}
			DialAgent.dial(mContext, phoneNumber);
			Toast.makeText(mContext, phoneNumber, Toast.LENGTH_SHORT).show();
			break;
		case R.id.settings:
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.about:
			popAboutDialog();
			break;
		case R.id.exit:
			android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID，目前获取自己的也只有该API，否则从/proc中自己的枚举其他进程吧，不过要说明的是，结束其他进程不一定有权限，不然就乱套了。
			System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return false;
	}

	private void popAboutDialog() {
		View aboutDialog = LayoutInflater.from(mContext).inflate(
				R.layout.about_information, null);
		TextView text = (TextView) aboutDialog.findViewById(R.id.authorInfo);
		text.setText(this.getString(R.string.notf_author)
				+ this.getString(R.string.email));
		PackageInfo pi = null;
		try {
			pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
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
	protected void onDestroy() {
		Log.d("onCall", "Destroy Main");
		super.onDestroy();
	}

}
