package com.blstream.urbangame.parcelableData.test;

import android.content.Intent;
import android.location.Location;
import android.test.AndroidTestCase;

import com.blstream.urbangame.listeners.GpsLocationListener;

public class GpsLocationListenerTest extends AndroidTestCase {
	private GpsLocationListener gpsLocationListener;
	private GpsLocationListener gpsLocationListenerAfterPassing;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		gpsLocationListener = new GpsLocationListener();
		Location l = new Location("test");
		l.setLatitude(50);
		l.setLongitude(34);
		
		gpsLocationListener.setLastLocation(l);
		gpsLocationListener.setTimeFromLastUpdate(1234);
		Intent i = new Intent();
		i.putExtra("gpsLocationListener", gpsLocationListener);
		gpsLocationListenerAfterPassing = i.getParcelableExtra("gpsLocationListener");
	}
	
	public void testGpsLocationListenerParcelable() {
		
		assertEquals(gpsLocationListener.getTimeFromLastUpdate(),
			gpsLocationListenerAfterPassing.getTimeFromLastUpdate());
		assertEquals(gpsLocationListener.getLastLocation().getLatitude(), gpsLocationListenerAfterPassing
			.getLastLocation().getLatitude());
		assertEquals(gpsLocationListener.getLastLocation().getLatitude(), gpsLocationListenerAfterPassing
			.getLastLocation().getLatitude());
		
	}
	
}
