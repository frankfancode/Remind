package com.andy.remind.app;

import com.andy.remind.R;
import com.andy.remind.R.xml;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public  class UserPreferenceFragment extends PreferenceFragment {
  
	
	DistancePicker distancePicker;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.userpreferences);	
  }

}
