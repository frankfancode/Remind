package com.andy.remind;

import com.amap.api.maps.model.LatLng;

import android.R.integer;
import android.app.Application;
import android.media.RingtoneManager;
import android.net.Uri;

public class Declare extends Application {
	public static String TAG = "com.andy.remind";
	private static  int distance = -1;
	private static LatLng currentLatlng;
	private static LatLng targetLatlng;
	private static Uri alarmUri=null; 
	/**
	 * @return the uriAlarm
	 */
	public static Uri getUriAlarm() {
		return alarmUri;
	}
	/**
	 * @param uriAlarm the uriAlarm to set
	 */
	public static void setUriAlarm(Uri uriAlarm) {
		Declare.alarmUri = uriAlarm;
	}
	/**
	 * @return the tag
	 */
	public static String getTag() {
		return TAG;
	}
	/**
	 * @return the distance
	 */
	public static int getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public static void setDistance(int distance) {
		distance = distance;
	}
	/**
	 * @return the currentLatlng
	 */
	public static LatLng getCurrentLatlng() {
		return currentLatlng;
	}
	/**
	 * @param currentLatlng the currentLatlng to set
	 */
	public static  void setCurrentLatlng(LatLng Latlng) {
		currentLatlng = Latlng;
	}
	/**
	 * @return the targetLatlng
	 */
	public static LatLng getTargetLatlng() {
		return targetLatlng;
	}
	/**
	 * @param targetLatlng the targetLatlng to set
	 */
	public static  void setTargetLatlng(LatLng Latlng) {
		targetLatlng = Latlng;
	}
}
