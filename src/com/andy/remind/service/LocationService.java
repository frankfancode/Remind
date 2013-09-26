package com.andy.remind.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.andy.remind.ArriveAlarmActivity;
import com.andy.remind.Declare;
import com.andy.remind.MainActivity;
import com.andy.remind.util.AMapUtil;
import com.andy.remind.util.ArriveAlarm;

public class LocationService extends Service implements LocationSource,
		AMapLocationListener {
	AMapLocation currentLocation;

	LatLng currentLatLng = Declare.getCurrentLatlng();
	LatLng targetLatlng = Declare.getTargetLatlng();
	String TAG = Declare.getTag();
	int distance = -1;
	private LocationManagerProxy mAMapLocationManager;
	private OnLocationChangedListener mListener;
	private boolean isArrived=false;
	
	// private IBinder binder=new LocationSource.LocalBinder();
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {

		// 这里可以启动媒体播放器
		// if(mediaPlayer==null)
		// mediaPlayer=MediaPlayer.create(this, uri);
		super.onCreate();
		Log.i(TAG, "LocationService.onCreate");
	
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		Log.i(TAG, "LocationService.deactive");
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
		}
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
		 */
		// Location API定位采用GPS和网络混合定位方式，时间最短是5000毫秒
		mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		Log.i(TAG, "onStartCommand");
		activate(mListener);
		return Service.START_NOT_STICKY;
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		Log.i(TAG, "LocationService.onLocationChanged");
		currentLocation = aLocation;
		currentLatLng = new LatLng(aLocation.getLatitude(),
				aLocation.getLongitude());

		Log.i(TAG, currentLatLng.toString());
		if (Declare.getTargetLatlng() != null) {
			targetLatlng = Declare.getTargetLatlng();
			Log.i(TAG, "targetLatlng " + targetLatlng.toString());
		}
		distance = (int) AMapUtil.getDistance(
				new LatLng(currentLocation.getLatitude(), currentLocation
						.getLongitude()), targetLatlng);
		// ToastUtil.show(MainActivity.this, "当前位置距目的地" + (int) distance + "米");
		Log.i(TAG, "距离：" + distance + " ");
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(LocationService.this);
		int prefsDistance = Integer.parseInt(mPrefs.getString("PREF_DISTANCE",
				"0"));
		Log.i(TAG, prefsDistance + "");
		prefsDistance = 500;
		if (distance > 0 && distance < prefsDistance) {
			//ArriveAlarm.startAlarm(getApplicationContext());
			Intent arriveAlarmIntent=new Intent(this,ArriveAlarmActivity.class);
			arriveAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			if(false==isArrived){
			startActivity(arriveAlarmIntent);
			isArrived=true;
			}
			
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;

	}
	
	
}
