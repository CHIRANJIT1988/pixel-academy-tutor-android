package pixel.academy.tutor.gps;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import pixel.academy.tutor.helper.OnLocationFound;


public class GPSTracker extends Service implements LocationListener
{

	private Context mContext;

	
	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	
	Location location; // location
	//double latitude; // latitude
	//double longitude; // longitude


	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 0;

	
	// Declaring a Location Manager
	protected LocationManager locationManager;

	OnLocationFound listener;


	public GPSTracker(Context context, OnLocationFound listener)
	{

		this.mContext = context;
		this.listener = listener;
		getLocation();
	}


	public Location getLocation()
	{
		
		try
		{
			
			locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			
			if (!isGPSEnabled && !isNetworkEnabled)
			{
				// no network provider is enabled
			}
			
			else
			{

				this.canGetLocation = true;
				
				
				/*Criteria criteria = new Criteria();

		        String bestProvider = locationManager.getBestProvider(criteria, true);

		        locationManager.requestLocationUpdates(bestProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


		        if (locationManager != null)
				{

		        	location = locationManager.getLastKnownLocation(bestProvider);

		        	if (location != null)
					{
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
				}*/


		        if (isNetworkEnabled)
				{

					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");

					/*if (locationManager != null)
					{

						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

						if (location != null)
						{
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}*/
				}


				if (isGPSEnabled)
				{

					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("GPS Enabled", "GPS Enabled");

					/*if (locationManager != null)
					{

						location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}*/
				}
			}
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return location;
	}
	
	
	
	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	
	public void stopUsingGPS()
	{
		
		if(locationManager != null)
		{
			locationManager.removeUpdates(GPSTracker.this);
		}		
	}
	
	
	/**
	 * Function to get latitude
	 * */
	
	/*public double getLatitude()
	{

		if (location != null)
		{
			latitude = location.getLatitude();
		}

		return latitude;
	}*/
	
	
	/**
	 * Function to get longitude
	 * */
	
	/*public double getLongitude()
	{
		
		if(location != null)
		{
			longitude = location.getLongitude();
		}
		
		return longitude;
	}*/
	
	
	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	
	public boolean canGetLocation() 
	{
		return this.canGetLocation;
	}
	
	
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showSettingsAlert()
	{
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("GPS Settings");
 
        // Setting Dialog Message
        alertDialog.setMessage("GPS is required to find nearest stores");
 
        // Setting Cancelable
     	alertDialog.setCancelable(false);
     	
        // On pressing Settings button
        alertDialog.setPositiveButton("Turn On GPS", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});
 
        
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();
			}
		});

        // Showing Alert Message
        alertDialog.show();
	}

	
	@Override
	public void onLocationChanged(Location location)
	{

		try
		{
			listener.onLocationFound(location);
		}

		catch(Exception e)
		{

		}

		finally
		{
			stopUsingGPS();
		}
	}

	@Override
	public void onProviderDisabled(String provider)
	{
	
	}

	
	@Override
	public void onProviderEnabled(String provider)
	{
	
	}

	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
	
	}

	
	@Override
	public IBinder onBind(Intent arg0) 
	{
		return null;
	}
}