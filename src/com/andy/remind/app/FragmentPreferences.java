package com.andy.remind.app;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.andy.remind.R;

public class FragmentPreferences extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	public static final String PREFS_NAME = "RemindPrefsFile";
	public static final String PREF_VIBRATE = "PREF_VIBRATE";

	public SharedPreferences mPrefs;
	SharedPreferences.Editor editor;

	@Override
	public void onBuildHeaders(List<Header> target) {
		// loadHeadersFromResource(R.xml.preference_headers, target);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// FragmentManager fragmentManager= ;

		UserPreferenceFragment upf = new UserPreferenceFragment();

		Context context = getApplicationContext();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		mPrefs.getBoolean(PREF_VIBRATE, true);
		addPreferencesFromResource(R.xml.userpreferences);

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(PREF_VIBRATE)) {

			Preference connectionPref = (Preference) findPreference(key);
			// Set summary to be the user-description for the selected value
			connectionPref.setSummary(sharedPreferences.getBoolean(
					PREF_VIBRATE, true) + "a");
			Toast.makeText(getApplicationContext(),
					findPreference(PREF_VIBRATE).getSummary() + "1",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mPrefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPrefs.unregisterOnSharedPreferenceChangeListener(this);
	}

}