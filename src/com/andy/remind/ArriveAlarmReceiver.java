package com.andy.remind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class ArriveAlarmReceiver  extends BroadcastReceiver {
	public static String TAG = "com.andy.remind";
	
	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i(TAG, "OnRecive");
        // 获取是否进入指定区域  
        boolean isEnter = intent.getBooleanExtra(  
                LocationManager.KEY_PROXIMITY_ENTERING, false);  
        if (isEnter) {  
            // 给出提示信息  
            Toast.makeText(context, "您已经进入成都市成华区", Toast.LENGTH_LONG).show();  
        } else {  
            // 给出提示信息  
            Toast.makeText(context, "您已经离开成都市成华区", Toast.LENGTH_LONG).show();  
        }  		
	}

}
