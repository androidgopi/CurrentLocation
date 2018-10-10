package com.sreeyainfotech.currentlocation.backgroundService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sreeyainfotech.currentlocation.model.Utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by KSTL on 05-01-2018.
 */

public class LocationService extends Service {

    private static final String TAG = "com.protrans.driverapp";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 60*1000;
    private static final float LOCATION_DISTANCE = 0f;

    String City, State, Address, Zip;
    private String TimeToStr;
//    private DriverAppDBHelper dbHelper;
//    private Dao<LoadDetails, Integer> mLoadDetailsDao;
//    private List<LoadDetails> mModel;
//    private ApiInterface apiService;

   // send the latitude and longitude values to Main Activity through BroadcasteReciver
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            try {
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<android.location.Address> addressList = null;
                addressList = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (addressList != null && addressList.size() > 0) {
                    android.location.Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append(address.getAddressLine(0)).append(",");
                    sb.append(address.getAddressLine(1)).append(",");
                    sb.append(address.getAddressLine(2)).append(",");
                    sb.append(address.getAddressLine(3)).append(".");

                    String sub_address = address.getFeatureName();
                    String sub_address1 = address.getThoroughfare();
                    String sub_address2 = address.getSubLocality();

                    address.getSubAdminArea(); //city
                    address.getAdminArea();  //state
                    address.getPostalCode();  //zipcode

                    if (sub_address != null) {
                        Address = sub_address + "," + sub_address1 + "," + sub_address2;
                    }

                    if (address.getSubAdminArea() != null) {
                        City = address.getSubAdminArea();
                    }

                    if (address.getAdminArea() != null) {
                        State = address.getAdminArea();
                    }

                    if (address.getPostalCode() != null) {
                        Zip = address.getPostalCode();
                    }
                }

                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
                TimeToStr = format.format(today);

                Utilities.showToast(LocationService.this, "Lat :"+location.getLatitude()+", "+"Long :"+location.getLongitude()+" @"+String.valueOf(TimeToStr));


                //Todo  send the latitude and longitude values to Main Activity through BroadcasteReciver
                intent.putExtra("latutide",location.getLatitude()+"");
                intent.putExtra("longitude",location.getLongitude()+"");
                sendBroadcast(intent);


                //   syncLocationInBackground(location.getLatitude(), location.getLongitude());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

//    private void syncLocationInBackground(final double latitude, final double longitute) {
//
//        String data =  Utilities.getPref(LocationService.this, "access_token", "");
//
//        dbHelper = new DriverAppDBHelper(LocationService.this);
//        try {
//            mLoadDetailsDao = dbHelper.getLoadDetailsDao();
//            mModel = mLoadDetailsDao.queryForAll();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JsonObject obj = new JsonObject();
//
//        if (mModel != null && mModel.size()>0) {
//            obj.addProperty("LoadNumber", mModel.get(0).getLoadNumber());
//
//            //obj.addProperty("DriverId", Utilities.getPref(mContext, "UserId", ""));
//            obj.addProperty("Latitude", latitude);
//            obj.addProperty("Longitude", longitute);
//            obj.addProperty("Status", "InTransit");
//
//            obj.addProperty("City", City);
//            obj.addProperty("State", State);
//            obj.addProperty("Address", Address);
//            obj.addProperty("ZipCode", Zip);
//            obj.addProperty("StopId", mModel.get(0).getStopID());
//
//            JsonArray arr = new JsonArray();
//            arr.add(obj);
//
//            Call<Integer> call_Update_Loacation = apiService.updateLocationChanges(data, arr);
//            call_Update_Loacation.enqueue(new Callback<Integer>() {
//                @Override
//                public void onResponse(Call<Integer> call, Response<Integer> response) {
//                    if (response.isSuccessful()) {
//                        if (response.body() != null) {
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Integer> call, Throwable t) {
//                    syncLocationInBackground(latitude, longitute);
//                }
//            });
//        }
//    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
   //     apiService = ApiClient.getClient(this).create(ApiInterface.class);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
            //Todo  send the latitude and longitude values to Main Activity through BroadcasteReciver
            intent = new Intent(str_receiver);

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);

            //Todo  send the latitude and longitude values to Main Activity through BroadcasteReciver
            intent = new Intent(str_receiver);

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

}
