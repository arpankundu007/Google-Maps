package com.example.googlemaps;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

	
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	GoogleMap mMap;
	MapView mv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(servicesok())
		{
			setContentView(R.layout.activity_map);
			final EditText et = (EditText) findViewById(R.id.editText);
			Button Submit = (Button) findViewById(R.id.getloc); 
			//Button Curloc = (Button) findViewById(R.id.curloc);
	        //mv = (MapView) findViewById(R.id.map);
			//mv.onCreate(savedInstanceState);
			if(initMap())
			{
			/*	Curloc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						LatLng position = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
						
					}
				});*/
				mMap.setMyLocationEnabled(true);
				Submit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						String loc = et.getText().toString();
						Geocoder gc = new Geocoder(MainActivity.this);
						List<Address> list = null;
						try {
							list= gc.getFromLocationName(loc, 1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						Address add = list.get(0);
						String locality = add.getLocality();
						Toast.makeText(getBaseContext(), locality, Toast.LENGTH_LONG).show();
						mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(add.getLatitude(), add.getLongitude()),15));
						mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
						mMap.addMarker(new MarkerOptions().title(locality).position(new LatLng(add.getLatitude(),add.getLongitude())));
					}
				});
			}
			else
			{
				Toast.makeText(getBaseContext(), "Cannot Map", Toast.LENGTH_SHORT).show();
			}
		}
		else
		setContentView(R.layout.activity_main);
		
		
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.normal) {
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		}
		else if(id==R.id.terrain)
			mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		else if(id==R.id.satellite)
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		else if(id==R.id.hybrid)
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		else if(id == R.id.none)
			mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
		else if(id==R.id.reset)
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 1));
		
		return super.onOptionsItemSelected(item);
	}
	public boolean servicesok()
	{
		int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(isAvailable == ConnectionResult.SUCCESS)
			return true;
		else if(GooglePlayServicesUtil.isUserRecoverableError(isAvailable))
		{
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		}
		else
		{
			Toast.makeText(getBaseContext(), "Can't connect!", Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	private boolean initMap()
	{
		if(mMap==null)
		{
			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
			mMap = mapFrag.getMap();
			
		}
		return (mMap!=null);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MapStateManager mgr = new MapStateManager(this);
		mgr.saveMapState(mMap);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MapStateManager mgr = new MapStateManager(this);
		CameraPosition position = mgr.getSavedCameraPosition(mMap);
		if(position!=null)
		{
			CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
			mMap.moveCamera(update);
		}
	}
/*	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mv.onDestroy();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		mv.onLowMemory();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mv.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mv.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mv.onSaveInstanceState(outState);
	}*/

	protected void goToCurrentLocation()
	{
		
	}
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
