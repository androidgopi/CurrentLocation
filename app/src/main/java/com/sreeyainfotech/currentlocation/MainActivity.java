package com.sreeyainfotech.currentlocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sreeyainfotech.currentlocation.backgroundService.LocationService;
import com.sreeyainfotech.currentlocation.model.AppLocationService;
import com.sreeyainfotech.currentlocation.model.LocationAddress;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText location_edttxt;
    private ImageView Img_ShowAddress;
    private AppLocationService appLocationService;
    private Button start_background_service;
    private TextView service_text;
    private Button map_view;
    private Button stop_background_service;
    private Button two_markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for getting Location
        appLocationService = new AppLocationService(MainActivity.this);


        findViewes();

        int PERMISSION_ALL = 10;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }

    private void findViewes() {
        location_edttxt = (EditText) findViewById(R.id.location_edttxt);
        Img_ShowAddress = (ImageView) findViewById(R.id.Img_ShowAddress);
        Img_ShowAddress.setOnClickListener(this);


        start_background_service = (Button) findViewById(R.id.start_background_service);
        start_background_service.setOnClickListener(this);
        stop_background_service=(Button)findViewById(R.id.stop_background_service);
        stop_background_service.setOnClickListener(this);
        service_text = (TextView) findViewById(R.id.service_text);

        map_view=(Button)findViewById(R.id.map_view);
        map_view.setOnClickListener(this);

        two_markers=(Button)findViewById(R.id.two_markers);
        two_markers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.Img_ShowAddress:
                getCurrentLocation();
                break;

            case R.id.start_background_service:
                //--part2 get Location by using service:
                startService(new Intent(MainActivity.this, LocationService.class));
                break;

            case R.id.stop_background_service:
                stopService(new Intent(MainActivity.this,LocationService.class));
                Toast.makeText(getApplicationContext(),"Service is stopped",Toast.LENGTH_SHORT).show();
                break;

            case R.id.map_view:
                Intent i= new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
                break;

            case R.id.two_markers:
                Intent i1= new Intent(MainActivity.this, TwoMarkers.class);
                startActivity(i1);
                break;

        }
    }

    //--part1 get Location:
    private void getCurrentLocation() {
        double latitude, longitude;
        LocationAddress locationAddress;

        Location gpslocation = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);
        Location networkLocation = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);

        if (networkLocation != null) {
            String result = null;
            latitude = networkLocation.getLatitude();
            longitude = networkLocation.getLongitude();
            locationAddress = new LocationAddress();
            Geocoder gcd = new Geocoder(getBaseContext(), Locale
                    .getDefault());
            try {
                List<Address> addressList = gcd.getFromLocation(
                        latitude, longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();

                    sb.append(address.getAddressLine(0)).append(",");
                    sb.append(address.getAddressLine(1)).append(",");
                    sb.append(address.getAddressLine(2)).append(",");
                    sb.append(address.getAddressLine(3)).append(".");

                    result = sb.toString();

                    location_edttxt.setText(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (gpslocation != null) {

            try {
                latitude = gpslocation.getLatitude();
                longitude = gpslocation.getLongitude();
                locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude,
                        getApplicationContext(), new GeocoderHandler());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showSettingsAlert();
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            location_edttxt.setText(locationAddress);
        }
    }

    protected void showSettingsAlert() {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog
                .setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    //--part3 get Location details from Locatuion service class, through the Broad caste Reciver:
    //TODO get the latitude and longitude values from Location service, by using broadcaste Reciver
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Double latitude = Double.valueOf(intent.getStringExtra("latutide"));
            Double longitude = Double.valueOf(intent.getStringExtra("longitude"));

            service_text.setText(String.valueOf(latitude + " " + longitude));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }


    private boolean hasPermissions(Context context, String[] permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {


                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                    }
                }
                return false;
            }
        }
        return true;
    }


}
