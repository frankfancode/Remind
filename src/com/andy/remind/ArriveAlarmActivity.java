package com.andy.remind;

import com.andy.remind.util.ArriveAlarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class ArriveAlarmActivity extends Activity {
	private Button ISeeButton;
	private PowerManager.WakeLock m_wakeLockObj = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		
		setContentView(R.layout.activity_arrive_alarm);
		AcquireWakeLock(10000);
		//stopService(new Intent(this,LocationService.class));
		ISeeButton=(Button)findViewById(R.id.ISeeId);
		//ArriveAlarm.startAlarm(getApplicationContext());
		ISeeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				System.exit(0);
				
			}
		});
		
	}
	public void AcquireWakeLock(long milltime) {
		if (m_wakeLockObj == null) {
		PowerManager pm = (PowerManager) getSystemService(getApplicationContext().POWER_SERVICE);
		m_wakeLockObj = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
		| PowerManager.ACQUIRE_CAUSES_WAKEUP
		| PowerManager.ON_AFTER_RELEASE, "TAG");

		m_wakeLockObj.acquire(milltime);
		}
		}


}
