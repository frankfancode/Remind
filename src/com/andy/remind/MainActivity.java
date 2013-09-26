package com.andy.remind;

import java.util.List;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.search.core.AMapException;
import com.amap.api.search.geocoder.Geocoder;
import com.andy.remind.app.FragmentPreferences;
import com.andy.remind.app.PreferencesActivity;
import com.andy.remind.service.LocationService;
import com.andy.remind.util.AMapUtil;

public class MainActivity extends FragmentActivity implements LocationSource,
		AMapLocationListener, OnMapClickListener, OnMarkerClickListener,
		OnInfoWindowClickListener, InfoWindowAdapter, OnMapLoadedListener {
	public static String TAG = Declare.getTag();
	private static final int OPTIONS_MENU_ID_LOGOUT = 1;
	private static final int OPTIONS_MENU_ID_PREFERENCES = 2;
	protected static final int OPTIONS_MENU_ID_EXIT = 13;
	public static final String START_ALARM = "com.andy.remind.START_ALARM";
	protected static boolean FIRST_LOCATION = true;
	private boolean isonMapLoaded = false;
	private Button destinationButton = null;
	private AMap aMap;
	private OnLocationChangedListener mListener;
	int distance = 0;
	public AMapLocation currentLocation = null;
	private LatLng tmpTargetLatLng=null;
	
	
	private LocationManagerProxy mAMapLocationManager;
	private Geocoder geocoder;
	private String targetName;
	private Marker targetMarker = null;
	private float radius = 500;
	PendingIntent startAlarmPendingInetent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CharSequence titleLable = "REMINDER";
		setTitle(titleLable);
		init();

		geocoder = new Geocoder(this);
		destinationButton = (Button) findViewById(R.id.selectDestination);
		destinationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showToast("在地图上点击您的目的地");
				registerListener();
			}
		});

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			android.support.v4.app.FragmentManager myFM = getSupportFragmentManager();
			aMap = ((SupportMapFragment) myFM.findFragmentById(R.id.map))
					.getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {

		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));
		myLocationStyle.strokeColor(Color.BLUE);
		myLocationStyle.strokeWidth(1);
		aMap.setMyLocationStyle(myLocationStyle);
		mAMapLocationManager = LocationManagerProxy
				.getInstance(MainActivity.this);
		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);// 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false
		aMap.getUiSettings().setZoomControlsEnabled(true);// 设置系统默认缩放按钮可见
		aMap.getUiSettings().setScaleControlsEnabled(true);
		// aMap.getUiSettings().set
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {

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
		if (true == FIRST_LOCATION) {
			Toast.makeText(MainActivity.this, "正在定位...", Toast.LENGTH_LONG)
					.show();
		}

	}

	/**
	 * 取消定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(Location aLocation) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		Log.i(TAG, "MainActivityonLocationChanged");
		currentLocation = aLocation;
		Declare.setCurrentLatlng(new LatLng(aLocation.getLatitude(),
				aLocation.getLongitude()));
		
		if (mListener != null) {
			mListener.onLocationChanged(aLocation);
		}
//		distance = (int) AMapUtil.getDistance(
//				new LatLng(currentLocation.getLatitude(), currentLocation
//						.getLongitude()), targetLatLng);
//		// ToastUtil.show(MainActivity.this, "当前位置距目的地" + (int) distance + "米");
//		Log.i(TAG, "距离：" + distance + " " + isonMapLoaded);
//		SharedPreferences mPrefs = PreferenceManager
//				.getDefaultSharedPreferences(MainActivity.this);
//		int prefsDistance = Integer.parseInt(mPrefs.getString("PREF_DISTANCE",
//				"0"));
//		Log.i(TAG, prefsDistance + "");
//		if (distance > 0 && distance < prefsDistance) {
//			new AlertDialog.Builder(this).setTitle("请输入")
//					.setIcon(android.R.drawable.ic_dialog_info)
//					.setView(new EditText(this)).setPositiveButton("确定", null)
//					.setNegativeButton("取消", null).show();
//		}
		if (true == FIRST_LOCATION) {
			Log.i(TAG, "FIRST_LOCATION"+FIRST_LOCATION);
			CameraPosition currentPosition = new CameraPosition.Builder()
					.target(Declare.getCurrentLatlng()).zoom(14).bearing(0).tilt(0).build();
			aMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(currentPosition));
			FIRST_LOCATION = false;
			deactivate();
			startLocationService();
		}

	}

	@Override
	public void onMapClick(LatLng targetPoint) {
		tmpTargetLatLng = targetPoint;
		Log.i(TAG, "onMapClick");
		if (targetMarker != null) {
			targetMarker.remove();
		}
		registerListener();
		targetMarker = aMap
				.addMarker(new MarkerOptions()
						.anchor(0.5f, 1)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_geo))
						.position(targetPoint).title("点击选择为目的地"));
		targetMarker.showInfoWindow();

	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub

		if (targetMarker.equals(marker)) {
			
			Declare.setTargetLatlng(tmpTargetLatLng);
			Log.i(TAG, "onInfoWindowClick "+tmpTargetLatLng.toString());
			getAddress(tmpTargetLatLng.latitude, tmpTargetLatLng.longitude);
			distance = (int) AMapUtil.getDistance(
					new LatLng(currentLocation.getLatitude(), currentLocation
							.getLongitude()), tmpTargetLatLng);
			if (null == targetName) {
				targetName = "";
			}
			Toast.makeText(
					MainActivity.this,
					"距离目的地 " + targetName + " " + distance / 1000 + "公里"
							+ distance % 1000 + "米", Toast.LENGTH_LONG).show();

		}
		releaseListener();
		targetMarker.hideInfoWindow();

	}

	private void registerListener() {
		aMap.setOnMapClickListener(MainActivity.this);
		aMap.setOnMarkerClickListener(MainActivity.this);
		aMap.setOnInfoWindowClickListener(MainActivity.this);
		aMap.setInfoWindowAdapter(MainActivity.this);
	}

	private void releaseListener() {
		aMap.setOnMapClickListener(null);
		aMap.setOnMarkerClickListener(null);
		aMap.setOnInfoWindowClickListener(null);
		aMap.setInfoWindowAdapter(null);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {

		return false;
	}

	// 逆地理编码
	public String getAddress(final double mlat, final double mLon) {
		Log.i(TAG, mlat + " " + mLon);

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {

					List<Address> address = geocoder.getFromLocation(mlat,
							mLon, 1);
					if (address != null && address.size() > 0) {
						Address addres = address.get(address.size() - 1);
						targetName = addres.getAdminArea()
								+ addres.getSubLocality()
								+ addres.getFeatureName() + "附近";

					}
				} catch (AMapException e) {

				}

			}
		});

		t.start();
		return targetName;
	}

	public void showToast(String showString) {
		Toast.makeText(getApplicationContext(), showString, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		deactivate();
	}

	/**
	 * 1像素代表多少米
	 */
	public void getScale(View v) {
		float scale = aMap.getScalePerPixel();
		Toast.makeText(MainActivity.this, "每像素代表" + scale + "米",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		MenuItem item;
		item = menu.add(0, OPTIONS_MENU_ID_LOGOUT, 0, R.string.omenu_signout);
		item.setIcon(android.R.drawable.ic_menu_revert);

		item = menu.add(0, OPTIONS_MENU_ID_PREFERENCES, 0,
				R.string.omenu_settings);
		item.setIcon(android.R.drawable.ic_menu_preferences);
		item = menu.add(0, OPTIONS_MENU_ID_EXIT, 0, R.string.omenu_exit);
		item.setIcon(android.R.drawable.ic_menu_close_clear_cancel);

		return true;
	}

	protected static final int REQUEST_CODE_PREFERENCES = 1;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case OPTIONS_MENU_ID_LOGOUT:
			// logout();
			return true;
		case OPTIONS_MENU_ID_PREFERENCES:

			Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ? PreferencesActivity.class
					: FragmentPreferences.class;
			Intent launchPreferencesIntent = new Intent(this, c);

			startActivityForResult(launchPreferencesIntent,
					REQUEST_CODE_PREFERENCES);
			return true;
		case OPTIONS_MENU_ID_EXIT:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapLoaded() {
		isonMapLoaded = true;
	}

	private void startLocationService() {
		Log.d(TAG, "start alarm");
		//Intent i = new Intent("com.andy.remind.START_TRACK_SERVICE");
        //startService(i);
        
//		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		Intent collectIntent = new Intent(this, LocationService.class);
//		PendingIntent collectSender = PendingIntent.getService(this, 0,
//				collectIntent, 0);
//		am.cancel(collectSender);
//		am.setRepeating(AlarmManager.ELAPSED_REALTIME,
//				SystemClock.elapsedRealtime(), 10 * 1000, collectSender);
		Intent intent=new Intent(this,LocationService.class);
        startService(intent);
        
	}

}
