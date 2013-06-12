/* I think we should call
 * locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
 * GpsLocationManager instance); at the start of the application because we need
 * gps to get hidden games or something like this, thats why I made one listener
 * and pass it through intent */

package com.blstream.urbangame.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

//Location;

public class GpsLocationListener implements LocationListener, Parcelable {
	
	public static final String GPS_LISTENER_KEY = "gpsLocationListener";
	
	private Location lastLocation;
	private long lastUpdateTime;
	private boolean isEnabled; //checks if listener wasn't removed
	private static final long MAX_ACCEPTABLE_TIME = 5000; //if there were no updates during this time, we assume gps connection is lost
	
	public GpsLocationListener() {
		//if getTimeFromLastUpdate is used before any updates it returns max possible time
		lastUpdateTime = Long.MAX_VALUE;
		isEnabled = false;
	}
	
	public GpsLocationListener(Parcel in) {
		lastUpdateTime = in.readLong();
		lastLocation = Location.CREATOR.createFromParcel(in);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		lastLocation = location;
		lastUpdateTime = SystemClock.elapsedRealtime();
		
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	@Override
	public void onProviderEnabled(String provider) {
		isEnabled = true;
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		isEnabled = false;
	}
	
	public Location getLastLocation() {
		return lastLocation;
	}
	
	//helps checking if gps data is not out of date
	public long getTimeFromLastUpdate() {
		return SystemClock.elapsedRealtime() - lastUpdateTime;
	}
	
	//setters only for tests
	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(lastUpdateTime);
		if (lastLocation != null) {
			lastLocation.writeToParcel(out, flags);
		}
		
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public static final Parcelable.Creator<GpsLocationListener> CREATOR = new Parcelable.Creator<GpsLocationListener>() {
		@Override
		public GpsLocationListener createFromParcel(Parcel in) {
			return new GpsLocationListener(in);
		}
		
		@Override
		public GpsLocationListener[] newArray(int size) {
			return new GpsLocationListener[size];
		}
	};
	
	public boolean isGPSFix() {
		Log.i("tm[", getTimeFromLastUpdate() + "");
		return getTimeFromLastUpdate() < MAX_ACCEPTABLE_TIME;
	}
}
