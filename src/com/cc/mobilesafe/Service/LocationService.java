package com.cc.mobilesafe.Service;

import com.cc.mobilesafe.utils.ConstantValue;
import com.cc.mobilesafe.utils.SpUtils;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class LocationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自动生成的方法存根
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(bestProvider, 0, 0, new MyLocationListener());
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO 自动生成的方法存根
			double altitude = location.getAltitude();
			double longitude = location.getLongitude();
			String text = "altitude = " + altitude + "," + "longitude = " + longitude;
			String safe_contact_num = SpUtils.getString(getApplicationContext(),ConstantValue.SAFE_CONTACT_NUM, "");
			if (!TextUtils.isEmpty(safe_contact_num)) {
				SmsManager.getDefault().sendTextMessage(safe_contact_num, null, text, null, null);
			}
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO 自动生成的方法存根

		}

	}

}
