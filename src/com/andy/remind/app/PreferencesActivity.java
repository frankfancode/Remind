package com.andy.remind.app;

import com.andy.remind.R;
import com.andy.remind.R.id;
import com.andy.remind.R.layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class PreferencesActivity extends FragmentActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	public static final String PREF_VIBRATE = "PREF_VIBRATE";
	public static final String PREF_VOLUME = "PREF_VOLUME";
	public static int REMIND_DISTANCE=500;

	private CheckBox vibrate;
	private SeekBar volume;
	private Button okButton;
	private Button cancelButton;

	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		volume = (SeekBar) findViewById(R.id.volume);
		vibrate = (CheckBox) findViewById(R.id.vibrate);

		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		updateUIFromPreferences();
		
		Button remindDistance=(Button)findViewById(R.id.remind_distance);
		remindDistance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				  
				
			}
		});
		
		

		okButton = (Button) findViewById(R.id.okButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);

		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				savePreferences();
				PreferencesActivity.this.setResult(RESULT_OK);
				finish();
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PreferencesActivity.this.setResult(RESULT_CANCELED);
			}
		});
	}

	protected void savePreferences() {
		int volumeIndex = volume.getProgress();
		boolean isVibrate = vibrate.isChecked();

		Editor editor = prefs.edit();
		editor.putInt(PREF_VOLUME, volumeIndex);
		editor.putBoolean(PREF_VIBRATE, isVibrate);
		editor.commit();
	}

	private void updateUIFromPreferences() {
		volume.setProgress(prefs.getInt(PREF_VOLUME, 50));
		vibrate.setChecked(prefs.getBoolean(PREF_VIBRATE, false));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}

}
