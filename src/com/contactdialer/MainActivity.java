package com.contactdialer;

import java.util.Calendar;

import com.contactdialer.SettingActivity.PrefTabListener;
import com.contactdialer.SettingActivity.UserTabListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Context mContext = null;
	private Menu menu=null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		this.menu=menu;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		VarModel.getInstance().init(this);
		NumberParserModel.getInstance().init(this);
		LogsModel.getInstance().init(this);
		ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab = actionBar
				.newTab()
				.setText(R.string.nav_main_contacts)
				.setTabListener(
						new ContactsTabListener(this,
								getString(R.string.nav_main_contacts)));
		actionBar.addTab(tab);
		tab = actionBar
				.newTab()
				.setText(R.string.nav_main_log)
				.setTabListener(
						new LogsTabListener(this,
								getString(R.string.nav_main_log)));
		actionBar.addTab(tab);
	}
	public class ContactsTabListener implements ActionBar.TabListener {
	    private ContactsFragment mFragment;
	    private final String mTag;

	    /** Constructor used each time a new tab is created.
	      * @param activity  The host Activity, used to instantiate the fragment
	      * @param tag  The identifier tag for the fragment
	      * @param clz  The fragment's Class, used to instantiate the fragment
	      */
	    public ContactsTabListener(Context ctx,String tag) {
	        mTag = tag;
	    }

	    /* The following are each of the ActionBar.TabListener callbacks */

	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	        // Check if the fragment is already initialized
	        if (mFragment == null) {
	            // If not, instantiate and add it to the activity
	            mFragment = new ContactsFragment();
	            ft.add(android.R.id.content, mFragment, mTag);
	        } else {
	            // If it exists, simply attach it in order to show it
	            ft.attach(mFragment);
	        }
	        
	    }

	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	        if (mFragment != null) {
	            // Detach the fragment, because another one is being attached
	            ft.detach(mFragment);
	        }
	    }

	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	        // User selected the already selected tab. Usually do nothing.
	    }

	}
	public class LogsTabListener implements ActionBar.TabListener {
	    private LogsFragment mFragment;
	    private final String mTag;

	    /** Constructor used each time a new tab is created.
	      * @param activity  The host Activity, used to instantiate the fragment
	      * @param tag  The identifier tag for the fragment
	      * @param clz  The fragment's Class, used to instantiate the fragment
	      */
	    public LogsTabListener(Context ctx,String tag) {
	        mTag = tag;
	    }

	    /* The following are each of the ActionBar.TabListener callbacks */

	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	        // Check if the fragment is already initialized
	        if (mFragment == null) {
	            // If not, instantiate and add it to the activity
	            mFragment = new LogsFragment();
	            ft.add(android.R.id.content, mFragment, mTag);
	        } else {
	            // If it exists, simply attach it in order to show it
	            ft.attach(mFragment);
	        }
	    }

	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	        if (mFragment != null) {
	            // Detach the fragment, because another one is being attached
	            ft.detach(mFragment);
	        }
	    }

	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	        // User selected the already selected tab. Usually do nothing.
	    	 
	    }
	    

	}
	public void popDialDialog(final String numberFrom) {
		final View checkView = LayoutInflater.from(this).inflate(
				R.layout.number_check, null);
		final EditText editNumber = (EditText) checkView
				.findViewById(R.id.check_number_edit);
		UserModel uModel=new UserModel();
		editNumber.setText(new NumberConverter(numberFrom,uModel.get(VariablesControl.getInstance().getCurrentUser()),NumberParserModel.getInstance()).getConverted());
		TextView numberNoti = (TextView) checkView
				.findViewById(R.id.check_number_notification);
		numberNoti.setText(this
				.getString(R.string.notf_original_number) + numberFrom);
		new AlertDialog.Builder(this)
		.setNegativeButton(
				this.getString(R.string.word_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).setTitle("go on?")
				.setView(checkView)
				.setPositiveButton(this.getString(R.string.word_dial),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String phoneNumber = editNumber
										.getText().toString();
								// TODO Auto-generated method stub
								DiallogModel dm = new DiallogModel();
								dm.insert(dm.new DialLogItem(String
										.valueOf(Calendar.getInstance()
												.getTimeInMillis()), numberFrom));
								new Thread(new DialAgent(mContext, phoneNumber,
										numberFrom)).start();

							}
						}).show();
	}
}
