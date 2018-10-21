package com.example.user.sensortest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class mapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    LatLng currentLocation;
    double lat;
    double lng;
    Button userposition, setposition;
    LocationManager locationManager;
    String returnAddress;
    String getname;
    String getphone;
    final String KEY_ACCOUNT = "account";

    String activity_num="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        setUpMapIfNeeded();

        Intent intent= getIntent();
        activity_num=intent.getStringExtra("activity_num");
        // userposition = findViewById(R.id.userposition);
        //重新定位目前位置button放最上層
        //userposition.bringToFront();
        //按下找出目前位置的按鈕
        //userposition.setOnClickListener(listener1);

        final String name = constant.n;

        //取得使用者電話
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://163.18.62.49:80/Android_login_register/user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //如果不是error
                if (!response.contains("Error")) {
                    String[] array = response.split(",");
                    getphone = array[2];
                } else {
                    //Toast.makeText(touser.this,response, Toast.LENGTH_LONG).show();
                    //tx.setText(response);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(touser.this,"失敗:\n"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put(KEY_USERNAME,name);
                params.put(KEY_ACCOUNT, name);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //選擇用gps 或 網路來獲得現在位置
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            try {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                lat = location.getLatitude();
                lng = location.getLongitude();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }




        //點擊按鈕後開始手動輸入畫面
        setposition = (Button) findViewById(R.id.setposition);
        setposition.bringToFront();
        setposition.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                constant.n = name;
                constant.p = getphone;
                intent.putExtra("activity_num",activity_num);
                intent.setClass(mapsActivity.this, mapsActivity2.class);
                startActivity(intent);
                mapsActivity.this.finish();

            }
        });

    }

    //按下找出目前位置的按鈕
   /* private Button.OnClickListener listener1 = new View.OnClickListener()
    {
        @Override

        public void onClick(View v)
        {

            showMylocation();
        }
    };
*/

    //如果目前位置改變
    @Override
    public void onLocationChanged(Location location) {

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

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    //關閉Activity後解除註冊感測器監聽
    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

    }


    //找出目前位置
   /*public void showMylocation()
    {
        LatLng currentLocation = new LatLng(lat, lng);
        Geocoder geocoder=new Geocoder(this);
        try
        {
            geocoder.getFromLocation(lat,lng,1);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //顯示目前地址
        Geocoder gc = new Geocoder(getApplicationContext(),Locale.getDefault());
        List<Address> lstAddress = null;
        try {
            lstAddress = gc.getFromLocation(lat, lng ,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
       String returnAddress=lstAddress.get(0).getAddressLine(0);


        //目前位置標記在地圖
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocation);
        markerOptions.title("目前位置");
        markerOptions.snippet("緯度:"+Double.toString(lat)+"\n經度:"+Double.toString(lng)+"\n地址:"+returnAddress);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        //鋪成標記點小框框
        //Set Custom InfoWindow Adapter
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(mapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));
    }*/

    //設定map
    private void setUpMapIfNeeded() {
       if (mMap == null) {
           //SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
           MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
           mapFrag.getMapAsync(this);


           if (mMap != null) {
               setUpMap();
           }

       }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
        //showMylocation();

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //預設道路施工01
        Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> lstAddress = null;
        try {
            lstAddress = gc.getFromLocation(22.742622, 120.326795, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnAddress = lstAddress.get(0).getAddressLine(0);
        MarkerOptions markerOptions = new MarkerOptions();
        //寫進map 座標內小框框
        markerOptions.position(new LatLng(22.742622, 120.326795));
        markerOptions.title("道路施工");
        markerOptions.snippet("地址: " + returnAddress);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));
        //鋪成畫面
        //Set Custom InfoWindow Adapter
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(mapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.742622, 120.326795), 17.0f));

//預設道路施工02
        gc = new Geocoder(getApplicationContext(), Locale.getDefault());
        lstAddress = null;
        try {
            lstAddress = gc.getFromLocation(22.741989, 120.328683, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnAddress = lstAddress.get(0).getAddressLine(0);
        markerOptions = new MarkerOptions();
        //寫進map 座標內小框框
        markerOptions.position(new LatLng(22.741989, 120.328683));
        markerOptions.title("道路施工");
        markerOptions.snippet("地址: " + returnAddress);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));
        //鋪成畫面
        //Set Custom InfoWindow Adapter
        adapter = new CustomInfoWindowAdapter(mapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.741989, 120.328683), 17.0f));
//預設道路施工03
        gc = new Geocoder(getApplicationContext(), Locale.getDefault());
        lstAddress = null;
        try {
            lstAddress = gc.getFromLocation(22.741484, 120.338339, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnAddress = lstAddress.get(0).getAddressLine(0);
        markerOptions = new MarkerOptions();
        //寫進map 座標內小框框
        markerOptions.position(new LatLng(22.741484, 120.338339));
        markerOptions.title("道路施工");
        markerOptions.snippet("地址: " + returnAddress);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));
        //鋪成畫面
        //Set Custom InfoWindow Adapter
        adapter = new CustomInfoWindowAdapter(mapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.741484, 120.338339), 17.0f));
        //預設道路施工04
        gc = new Geocoder(getApplicationContext(), Locale.getDefault());
        lstAddress = null;
        try {
            lstAddress = gc.getFromLocation(22.732801, 120.331623, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnAddress = lstAddress.get(0).getAddressLine(0);
        markerOptions = new MarkerOptions();
        //寫進map 座標內小框框
        markerOptions.position(new LatLng(22.732801, 120.331623));
        markerOptions.title("道路施工");
        markerOptions.snippet("地址: " + returnAddress);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));
        //鋪成畫面
        //Set Custom InfoWindow Adapter
        adapter = new CustomInfoWindowAdapter(mapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.732801, 120.331623), 17.0f));
        //預設道路施工05
        gc = new Geocoder(getApplicationContext(), Locale.getDefault());
        lstAddress = null;
        try {
            lstAddress = gc.getFromLocation(22.729813, 120.322793, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnAddress = lstAddress.get(0).getAddressLine(0);
        markerOptions = new MarkerOptions();
        //寫進map 座標內小框框
        markerOptions.position(new LatLng(22.729813, 120.322793));
        markerOptions.title("道路施工");
        markerOptions.snippet("地址: " + returnAddress);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));
        //鋪成畫面
        //Set Custom InfoWindow Adapter
        adapter = new CustomInfoWindowAdapter(mapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.729813, 120.322793), 17.0f));
        //預設道路施工06
        gc = new Geocoder(getApplicationContext(), Locale.getDefault());
        lstAddress = null;
        try {
            lstAddress = gc.getFromLocation(22.671400, 120.317638, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnAddress = lstAddress.get(0).getAddressLine(0);
        markerOptions = new MarkerOptions();
        //寫進map 座標內小框框
        markerOptions.position(new LatLng(22.671400, 120.317638));
        markerOptions.title("道路施工");
        markerOptions.snippet("地址: " + returnAddress);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));
        //鋪成畫面
        //Set Custom InfoWindow Adapter
        adapter = new CustomInfoWindowAdapter(mapsActivity.this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.671400, 120.317638), 17.0f));

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String ioturl = "https://iot.cht.com.tw/iot/v1/device/10482012315/sensor/road_test19/rawdata?start=2017-08-04T10%3A24%3A50.956Z";

        //下載平台上的資料標記在google map
        JsonArrayRequest jsonarrayRequest = new JsonArrayRequest
                (ioturl,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    String a = "";
                                    String b = "";

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject road = response.getJSONObject(i);
                                        JSONArray values = road.getJSONArray("value");
                                        String lat2 = road.getString("lat");//緯度
                                        String lng2 = road.getString("lon");//經度
                                        String item0 = values.getString(0);//震度
                                        String item1 = values.getString(1);//等級
                                        String item2 = values.getString(2);//地址
                                        String item3 = values.getString(3);//上傳時間
                                        String item4 = values.getString(4);//使用者名稱
                                        String item5 = values.getString(5);//電話

                                        String fixname = "一條龍道路維修公司";
                                        String fixphone = "02-24214965";

                                        if(item0.equals("00") && item1.equals("00")) {
                                            MarkerOptions markerOptions = new MarkerOptions();
                                            //寫進map 座標內小框框
                                            markerOptions.position(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)));
                                            markerOptions.title("坑洞位置");
                                            markerOptions.snippet("上傳方式: 地圖標記"+"\n緯度: " + Double.parseDouble(lat2) + "\n經度: " + Double.parseDouble(lng2) + "\n地址: " + item2 + "\n上傳時間: " + item3 + "\n使用者名稱: " + item4 + "     電話: " + item5 + "\n維修單位: " + fixname + "\n電話: " + fixphone);
                                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                                            //鋪成畫面
                                            //Set Custom InfoWindow Adapter
                                            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(mapsActivity.this);
                                            mMap.setInfoWindowAdapter(adapter);
                                            mMap.addMarker(markerOptions).showInfoWindow();

                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)), 17.0f));

                                            /*MarkerOptions markerOptions = new MarkerOptions();
                                            //寫進map 座標內小框框
                                            markerOptions.position(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)));
                                            markerOptions.title("坑洞位置");
                                            markerOptions.snippet("震度: " + item0 + "     等級: " + item1 + "\n緯度: " + Double.parseDouble(lat2) + "\n經度: " + Double.parseDouble(lng2) + "\n地址: " + item2 + "\n上傳時間: " + item3 + "\n使用者名稱: " + item4 + "     電話: " + item5 + "\n維修單位: " + fixname + "\n電話: " + fixphone);
                                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                            //鋪成畫面
                                            //Set Custom InfoWindow Adapter
                                            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(tomap.this);
                                            map.setInfoWindowAdapter(adapter);
                                            map.addMarker(markerOptions).showInfoWindow();

                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)), 17.0f));

                                            MarkerOptions markerOptions = new MarkerOptions();
                                            //寫進map 座標內小框框
                                            markerOptions.position(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)));
                                            markerOptions.title("坑洞位置");
                                            markerOptions.snippet("震度: " + item0 + "     等級: " + item1 + "\n緯度: " + Double.parseDouble(lat2) + "\n經度: " + Double.parseDouble(lng2) + "\n地址: " + item2 + "\n上傳時間: " + item3 + "\n使用者名稱: " + item4 + "     電話: " + item5);
                                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                            //鋪成畫面
                                            //Set Custom InfoWindow Adapter
                                            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(tomap.this);
                                            map.setInfoWindowAdapter(adapter);
                                            map.addMarker(markerOptions).showInfoWindow();

                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)), 17.0f));
                                            */
                                        }
                                        else
                                        {
                                            MarkerOptions markerOptions = new MarkerOptions();
                                            //寫進map 座標內小框框
                                            markerOptions.position(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)));
                                            markerOptions.title("坑洞位置");
                                            markerOptions.snippet(
                                                    "上傳方式: 手機偵測\n"+"震度: " + item0 + "     等級: " + item1 + "\n緯度: " + Double.parseDouble(lat2) + "\n經度: " + Double.parseDouble(lng2) + "\n地址: " + item2 + "\n上傳時間: " + item3 + "\n使用者名稱: " + item4 + "     電話: " + item5 + "\n維修單位: " + fixname + "\n電話: " + fixphone);
                                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                                            //鋪成畫面
                                            //Set Custom InfoWindow Adapter
                                            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(mapsActivity.this);
                                            mMap.setInfoWindowAdapter(adapter);
                                            mMap.addMarker(markerOptions).showInfoWindow();

                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)), 17.0f));

                                            /*MarkerOptions markerOptions = new MarkerOptions();
                                            //寫進map 座標內小框框
                                            markerOptions.position(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)));
                                            markerOptions.title("坑洞位置");
                                            markerOptions.snippet("震度: " + item0 + "     等級: " + item1 + "\n緯度: " + Double.parseDouble(lat2) + "\n經度: " + Double.parseDouble(lng2) + "\n地址: " + item2 + "\n上傳時間: " + item3 + "\n使用者名稱: " + item4 + "     電話: " + item5 + "\n維修單位: " + fixname + "\n電話: " + fixphone);
                                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                            //鋪成畫面
                                            //Set Custom InfoWindow Adapter
                                            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(tomap.this);
                                            map.setInfoWindowAdapter(adapter);
                                            map.addMarker(markerOptions).showInfoWindow();

                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)), 17.0f));

                                            MarkerOptions markerOptions = new MarkerOptions();
                                            //寫進map 座標內小框框
                                            markerOptions.position(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)));
                                            markerOptions.title("坑洞位置");
                                            markerOptions.snippet("震度: " + item0 + "     等級: " + item1 + "\n緯度: " + Double.parseDouble(lat2) + "\n經度: " + Double.parseDouble(lng2) + "\n地址: " + item2 + "\n上傳時間: " + item3 + "\n使用者名稱: " + item4 + "     電話: " + item5);
                                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                            //鋪成畫面
                                            //Set Custom InfoWindow Adapter
                                            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(tomap.this);
                                            map.setInfoWindowAdapter(adapter);
                                            map.addMarker(markerOptions).showInfoWindow();

                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat2), Double.parseDouble(lng2)), 17.0f));
                                            */
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast t = Toast.makeText(mapsActivity.this, e.toString(), Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast t = Toast.makeText(mapsActivity.this, error.toString(), Toast.LENGTH_LONG);
                                t.show();
                            }
                        }
                )

        {
            //Overriding 覆寫父親既定的方法
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //操作Headers加入金鑰
                //建構Map Collection
                Map<String, String> headers = new HashMap<>();
                //加入Key與value
                //String key=AppUtility.getResourceKeyValue("CK");
                //Log.i("Key",key);
                headers.put("CK", "PKQO1V8D10R44L63U7");
                return headers;
            }

        };

        requestQueue.add(jsonarrayRequest);
    }

    //設定map
    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    //回主畫面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回

            Intent intent=new Intent();
            if(activity_num.equals("1")||activity_num=="1")
            {
                intent.setClass(mapsActivity.this, MainActivity.class);
                this.startActivity(intent);
                mapsActivity.this.finish();
            }
            else
            {

                intent.setClass(mapsActivity.this, toStart.class);
                this.startActivity(intent);
                mapsActivity.this.finish();
            }
        }

        return true;
    }
}
