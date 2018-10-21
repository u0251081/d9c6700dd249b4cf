package com.example.user.sensortest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.sensortest.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.google.android.gms.maps.MapView;

import static android.content.ContentValues.TAG;

public class tomap extends Activity implements OnMapReadyCallback, LocationListener

{
    private GoogleMap map;
    LocationManager locationManager;
    LocationListener mLocationListener;
    double lat;
    double lng;
    String returnAddress;
    String latLng;
    String accountname;
    Button userposition, setposition;
    boolean click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);


        setposition = findViewById(R.id.setposition);
        //手動輸入坑洞位置button放最上層
        setposition.bringToFront();

    }

    private Button.OnClickListener listener1 = new View.OnClickListener() {
        @Override

        public void onClick(View v) {

                Intent intent = getIntent();
                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                // map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                mapFragment.getMapAsync(tomap.this);
                if (ActivityCompat.checkSelfPermission(tomap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(tomap.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                String s = LocationManager.NETWORK_PROVIDER;
                Location location = locationManager.getLastKnownLocation(s);
                //每5秒更新一次地圖(包含目前位置和坑洞標記點)
                locationManager.requestLocationUpdates(s, 0, 0, tomap.this);

                click=true;

        }
    };

    //顯示地圖
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

       // Toast t=Toast.makeText(tomap.this,"每5秒更新一次目前位置及坑洞資料",Toast.LENGTH_LONG);
        //t.show();

    }

    //如果目前位置改變
@Override
    public void onLocationChanged(Location location) {

        map.clear();

        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());


        //顯示目前地址
        Geocoder gc = new Geocoder(getApplicationContext(),Locale.getDefault());
        List<Address> lstAddress = null;
        try {
            lstAddress = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnAddress=lstAddress.get(0).getAddressLine(0);


        //目前位置標記在地圖
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocation);
        markerOptions.title("目前位置");
        markerOptions.snippet("緯度:"+Double.toString(location.getLatitude())+"\n經度:"+Double.toString(location.getLongitude())+"\n地址:"+returnAddress);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        //鋪成畫面
        //Set Custom InfoWindow Adapter
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(tomap.this);
        map.setInfoWindowAdapter(adapter);
        map.addMarker(markerOptions).showInfoWindow();


        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));

       // Toast t=Toast.makeText(tomap.this,"資料更新中",Toast.LENGTH_LONG);
        //t.show();
    }

@Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

@Override
    public void onProviderEnabled(String provider) {

    }

@Override
    public void onProviderDisabled(String provider) {

    }

    //回主畫面
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK)
        { // 攔截返回鍵

            Intent intent = new Intent();
            intent.setClass(tomap.this, MainActivity.class);
            startActivity(intent);
            tomap.this.finish();
        }

        return true;
    }

    //註冊感測器監聽
    @Override
    protected void onResume() {
        super.onResume();

    }

    //關閉Activity後解除註冊感測器監聽
    @Override
    protected void onPause()
    {
        super.onPause();
        if (locationManager != null)
        {
            locationManager.removeUpdates(tomap.this);
        }
    }

   /* public void changeProcess(View view)
    {

        connect.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, tolist.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }*/

}

