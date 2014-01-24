package com.contactdialer.setting;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.contactdialer.R;
import com.contactdialer.common.UserModel;

public class UserEditActivity extends Activity {
	private String originalName;
	private int mode;
	public static final int MODE_EDIT = 0;
	public static final int MODE_CREATE = 1;

	private String getValue(View view) {
		EditText et = (EditText) view.findViewById(R.id.value);
		return et.getText().toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle extras = getIntent().getExtras();
		mode = extras.getInt("mode");
		if (mode == MODE_EDIT) {
			originalName = extras.getString("name");
		} else if (mode == MODE_CREATE) {
			originalName = null;
		}
		UserModel um = new UserModel();
		UserModel.User user = um.get(originalName);
		View view = LayoutInflater.from(this).inflate(R.layout.user_edit, null);
		View item = view.findViewById(R.id.name);
		setKeyAndValue(item, getString(R.string.word_name), user.getName());
		((EditText) item.findViewById(R.id.value))
				.setInputType(InputType.TYPE_CLASS_TEXT);
		item = view.findViewById(R.id.district);
		setKeyAndValue(item, getString(R.string.word_district),
				user.getDistrict());
		item = view.findViewById(R.id.group);
		setKeyAndValue(item, getString(R.string.word_group), user.getGroup());
		item = view.findViewById(R.id.in_prefix);
		setKeyAndValue(item, getString(R.string.word_in_prefix),
				user.getInGroup());
		item = view.findViewById(R.id.out_prefix);
		setKeyAndValue(item, getString(R.string.word_out_prefix),
				user.getOutGroup());
		setContentView(view);
		Log.d("getValue", getValue(findViewById(R.id.name)));
		super.onCreate(savedInstanceState);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {
		case R.id.menu_setting_save:
			updateUser();
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setKeyAndValue(View view, String key, String value) {
		TextView tv = (TextView) view.findViewById(R.id.key);
		EditText et = (EditText) view.findViewById(R.id.value);
		tv.setText(key);
		et.setText(value);
	}

	private void updateUser() {
		UserModel um = new UserModel();
		UserModel.User user = um.new User();

		String name = getValue(findViewById(R.id.name));
		String country = UserModel.User.CHINA;
		String district = getValue(findViewById(R.id.district));
		String group = getValue(findViewById(R.id.group));
		String inGroup = getValue(findViewById(R.id.in_prefix));
		String outGroup = getValue(findViewById(R.id.out_prefix));
		String mobilePrefix = UserModel.User.MOBILEPREFIX;

		user.set(name, country, district, group, inGroup, outGroup,
				mobilePrefix);
		if (mode == MODE_EDIT) {
			um.update(user, originalName);
		} else {
			um.add(user);
		}

	}

}
