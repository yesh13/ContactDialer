package com.contactdialer;

import android.R.menu;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.app.Fragment;

public class SettingActivity extends Activity {
	public static final int ADD_ID=0;
	private boolean mSave=false;
	private Menu menu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);

		Tab tab = actionBar
				.newTab()
				.setText(R.string.setting_nav_pref)
				.setTabListener(
						new PrefTabListener(this,
								getString(R.string.setting_nav_pref)));
		actionBar.addTab(tab);
		tab = actionBar
				.newTab()
				.setText(R.string.setting_nav_user)
				.setTabListener(
						new UserTabListener(this,
								getString(R.string.setting_nav_user)));
		actionBar.addTab(tab);
	}

	public void saveDuration(View view) {
		TextView tv = (TextView) this.findViewById(R.id.editDuration);
		String str = tv.getText().toString();
		VariablesControl.getInstance().setDuration(Integer.parseInt(str));
		tv = (TextView) this.findViewById(R.id.editInterval);
		str = tv.getText().toString();
		VariablesControl.getInstance().setInterval(Integer.parseInt(str));
		SeekBar sb = (SeekBar) this.findViewById(R.id.seekVolume);
		VariablesControl.getInstance().setVolume(sb.getProgress());
		VariablesControl.getInstance().updateAll();
		finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.menu=menu;
		 MenuItem mi=menu.add(0, 0, 0, R.string.word_add);
	        mi.setIcon(R.drawable.ic_action_save);
	        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	        mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()) {
					case 0:
						mSave=true;
						finish();
						break;

					default:
						break;
					}
					return false;
				}
			});
		return true;
	}
	

	@Override
	protected void onDestroy() {
		if(mSave){
			VariablesControl.getInstance().updateAll();
		} else {
			VariablesControl.getInstance().queryAll();
		}
		super.onDestroy();
	}
	public class TabListener<T extends Fragment> implements ActionBar.TabListener {
	    private Fragment mFragment;
	    private final Activity mActivity;
	    private final String mTag;
	    private final Class<T> mClass;

	    /** Constructor used each time a new tab is created.
	      * @param activity  The host Activity, used to instantiate the fragment
	      * @param tag  The identifier tag for the fragment
	      * @param clz  The fragment's Class, used to instantiate the fragment
	      */
	    public TabListener(Activity activity, String tag, Class<T> clz) {
	        mActivity = activity;
	        mTag = tag;
	        mClass = clz;
	    }

	    /* The following are each of the ActionBar.TabListener callbacks */

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

	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	        // User selected the already selected tab. Usually do nothing.
	    }

	}
	public class UserTabListener implements ActionBar.TabListener {
	    private UserFragment mFragment;
	    private final String mTag;
	    private final Context mContext;

	    /** Constructor used each time a new tab is created.
	      * @param activity  The host Activity, used to instantiate the fragment
	      * @param tag  The identifier tag for the fragment
	      * @param clz  The fragment's Class, used to instantiate the fragment
	      */
	    public UserTabListener(Context ctx,String tag) {
	    	mContext=ctx;
	        mTag = tag;
	    }

	    /* The following are each of the ActionBar.TabListener callbacks */

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
	        MenuItem mi=menu.add(0, 0, 0, R.string.word_add);
	        mi.setIcon(R.drawable.ic_action_new);
	        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	        mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()) {
					case 0:
						mFragment.addUser();
						break;

					default:
						break;
					}
					return false;
				}
			});
	    }

	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	        if (mFragment != null) {
	            // Detach the fragment, because another one is being attached
	            ft.detach(mFragment);
	        }
	        menu.clear();
	    }

	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	        // User selected the already selected tab. Usually do nothing.
	    }

	}
	public class PrefTabListener implements ActionBar.TabListener {
	    private PrefFragment mFragment;
	    private final String mTag;
	    private final Context mContext;

	    /** Constructor used each time a new tab is created.
	      * @param activity  The host Activity, used to instantiate the fragment
	      * @param tag  The identifier tag for the fragment
	      * @param clz  The fragment's Class, used to instantiate the fragment
	      */
	    public PrefTabListener(Context ctx,String tag) {
	    	mContext=ctx;
	        mTag = tag;
	    }

	    /* The following are each of the ActionBar.TabListener callbacks */

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
			if (menu != null) {
				MenuItem mi = menu.add(0, 0, 0, R.string.word_add);
				mi.setIcon(R.drawable.ic_action_save);
				mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						case 0:
							mSave = true;
							finish();
							break;

						default:
							break;
						}
						return false;
					}
				});

			}
	       
	    }

	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	        if (mFragment != null) {
	            // Detach the fragment, because another one is being attached
	            ft.detach(mFragment);
	        }
	        menu.clear();
	    }

	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	        // User selected the already selected tab. Usually do nothing.
	    	 
	    }
	    

	}
	
}



