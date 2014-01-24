package com.contactdialer.setting;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.contactdialer.R;
import com.contactdialer.common.VarProvider;

public class SettingActivity extends Activity {
	public class PrefTabListener implements ActionBar.TabListener {
		private PrefFragment mFragment;
		private final String mTag;


		public PrefTabListener(Context ctx, String tag) {
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
				mFragment = new PrefFragment();
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

	}

	public class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;


		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
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

	}

	public class UserTabListener implements ActionBar.TabListener {
		private UserFragment mFragment;
		private final String mTag;


		public UserTabListener(Context ctx, String tag) {
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
				mFragment = new UserFragment();
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

	}

	public static final int ADD_ID = 0;

	private boolean mSave = false;
	private Menu menu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab tab = actionBar
				.newTab()
				.setText(R.string.setting_nav_user)
				.setTabListener(
						new UserTabListener(this,
								getString(R.string.setting_nav_user)));
		actionBar.addTab(tab);
		tab = actionBar
				.newTab()
				.setText(R.string.setting_nav_pref)
				.setTabListener(
						new PrefTabListener(this,
								getString(R.string.setting_nav_pref)));
		actionBar.addTab(tab);

	}

	

	@Override
	protected void onDestroy() {
		if (mSave) {
			VarProvider.getInstance().updateAll();
		} else {
			VarProvider.getInstance().queryAll();
		}
		super.onDestroy();
	}

	public void saveDuration(View view) {
		TextView tv = (TextView) this.findViewById(R.id.editDuration);
		String str = tv.getText().toString();
		VarProvider.getInstance().setDuration(Integer.parseInt(str));
		tv = (TextView) this.findViewById(R.id.editInterval);
		str = tv.getText().toString();
		VarProvider.getInstance().setInterval(Integer.parseInt(str));
		SeekBar sb = (SeekBar) this.findViewById(R.id.seekVolume);
		VarProvider.getInstance().setVolume(sb.getProgress());
		VarProvider.getInstance().updateAll();
		finish();
	}

	public void setmSave(boolean mSave) {
		this.mSave = mSave;
	}

}
