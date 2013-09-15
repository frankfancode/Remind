package com.andy.remind;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class FragmentPreferences extends PreferenceActivity {

  @Override
  public void onBuildHeaders(List<Header> target) {
   // loadHeadersFromResource(R.xml.preference_headers, target);
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      getFragmentManager().beginTransaction().replace(android.R.id.content, new UserPreferenceFragment())
              .commit();
  }
}