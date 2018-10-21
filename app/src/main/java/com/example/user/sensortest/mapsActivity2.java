package com.example.user.sensortest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class mapsActivity2 extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    LatLng currentLocation;
    double lat;
    double lng;
    TextView t;
    Button position_ok, searchok, userposition;
    LocationManager locationManager;
    String returnAddress;
    String getname, getphone;
    String requestUrl = "https://iot.cht.com.tw/iot/v1/device/10482012315/rawdata";  //上傳網址
    String requestMethod = "POST";
    String num_to_string = "";
    double getlat, getlng;
    String activity_num;
    MarkerOptions m;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map2);
        setUpMapIfNeeded();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        t = findViewById(R.id.address_search);

        Intent intent = getIntent();
        getname = constant.n;
        getphone = constant.p;
        activity_num=intent.getStringExtra("activity_num");


        //按下後查詢地點
        searchok = (Button) findViewById(R.id.searchok);
        searchok.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mMap.clear();
                EditText searchposition = (EditText) findViewById(R.id.searchposition);
                String loc = searchposition.getText().toString().trim();
                List<Address> addressList = null;


                //如果 Edit tiext 為空
                if (loc == null || loc.equals(""))
                {
                    Toast re = Toast.makeText(mapsActivity2.this, "請輸入想查詢的地址或名稱", Toast.LENGTH_LONG);
                    re.show();

                } else
                    {
                    Geocoder geocoder = new Geocoder(mapsActivity2.this);
                    try {
                        addressList = geocoder.getFromLocationName(loc, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    getlat = latLng.latitude;
                    getlng = latLng.longitude;

                    MarkerOptions marker = new MarkerOptions()
                            .position(latLng)
                            .title("您查詢的地點")
                            .snippet("可拖曳此標記點至您想放置的位置")
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    //鋪成畫面
                    //Set Custom InfoWindow Adapter
                    CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(mapsActivity2.this);
                    mMap.setInfoWindowAdapter(adapter);
                    mMap.addMarker(marker).showInfoWindow();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));


                            String yy = "00";
                            String address2;

                            // 獲取當前時間
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH點mm");
                            Date curDate = new Date(System.currentTimeMillis());
                            String currenttime = formatter.format(curDate);


                            double a = latLng.latitude;
                            double b = latLng.longitude;

                            //顯示目前地址
                            Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> lstAddress = null;
                            try {
                                lstAddress = gc.getFromLocation(a, b, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            address2 = lstAddress.get(0).getAddressLine(0);

                            num_to_string =
                                    "[ {\"id\":\"road_test19\",\"lat\":" + Double.toString(a) + ",\"lon\":" + Double.toString(b) + ",\"save\":true,\"value\":[\"" + yy + "\",\"00\",\"" + address2 + "\",\"" + currenttime + "\",\"" + getname + "\",\"" + getphone + "\"] } ]";

                }
            }
        });

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

        //按下後確定上傳資料
        position_ok = (Button) findViewById(R.id.position_ok);
        position_ok.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                //如果搜尋欄是空值或沒有拖曳過標記點
                if (num_to_string.equals("") || num_to_string == null) {
                    String yy = "00";
                    String address;

                    // 獲取當前時間
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH點mm");
                    Date curDate = new Date(System.currentTimeMillis());
                    String currenttime = formatter.format(curDate);

                    //LatLng latLng=arg0.getPosition();
                    double a = getlat;
                    double b = getlng;

                    //顯示目前地址
                    Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> lstAddress = null;
                    try {
                        lstAddress = gc.getFromLocation(a, b, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    address = lstAddress.get(0).getAddressLine(0);

                    num_to_string =
                            "[ {\"id\":\"road_test19\",\"lat\":" + Double.toString(a) + ",\"lon\":" + Double.toString(b) + ",\"save\":true,\"value\":[\"" + yy + "\",\"00\",\"" + address + "\",\"" + currenttime + "\",\"" + getname + "\",\"" + getphone + "\"] } ]";

                }
                httpRequest(requestUrl, requestMethod, num_to_string);
            }
        });

        /*進入此activity時EditText不先跳出鍵盤*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    //按下找出目前位置的按鈕
   /* private Button.OnClickListener listener1 = new View.OnClickListener()
    {
        @Override

        public void onClick(View v)
        {

            showMylocation();
        }
    };*/

    //找出目前位置
   /* public void showMylocation()
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
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(mapsActivity2.this);
        mMap.setInfoWindowAdapter(adapter);
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));
    }*/
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
    //------------------------------------------------------------------------------------------------------------------

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

    //設定map
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment mapFrag = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2));
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

        mMap.clear();


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                MarkerOptions marker=new MarkerOptions()
                        .position(point)
                        .title("拖曳標記點")
                        .snippet("可拖曳此標記點至您想放置的位置")
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(mapsActivity2.this);
                mMap.setInfoWindowAdapter(adapter);
                mMap.addMarker(marker).showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17.0f));

                String yy = "00";
                String address;

                // 獲取當前時間
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH點mm");
                Date curDate = new Date(System.currentTimeMillis());
                String currenttime = formatter.format(curDate);


                LatLng latLng = point;
                double a = latLng.latitude;
                double b = latLng.longitude;

                getname = constant.n;
                getphone = constant.p;


                //顯示目前地址
                Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> lstAddress = null;
                try {
                    lstAddress = gc.getFromLocation(a, b, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address = lstAddress.get(0).getAddressLine(0);

                num_to_string =
                        "[ {\"id\":\"road_test19\",\"lat\":" + Double.toString(a) + ",\"lon\":" + Double.toString(b) + ",\"save\":true,\"value\":[\"" + yy + "\",\"00\",\"" + address + "\",\"" + currenttime + "\",\"" + getname + "\",\"" + getphone + "\"] } ]";
            }
        });


        getname = constant.n;
        getphone = constant.p;


        //按下後確定上傳資料
        position_ok = (Button) findViewById(R.id.position_ok);
        position_ok.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                //如果搜尋欄是空值或沒有拖曳過標記點
                if (num_to_string.equals("") || num_to_string == null) {
                    String yy = "00";
                    String address;

                    // 獲取當前時間
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH點mm");
                    Date curDate = new Date(System.currentTimeMillis());
                    String currenttime = formatter.format(curDate);

                    //LatLng latLng=arg0.getPosition();
                    LatLng latLng = m.getPosition();
                    double a = latLng.latitude;
                    double b = latLng.longitude;
                    //double a = getlat;
                    //double b = getlng;

                    //顯示目前地址
                    Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> lstAddress = null;
                    try {
                        lstAddress=  gc.getFromLocation(a, b, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }}
                httpRequest(requestUrl, requestMethod, num_to_string);
            }
        });

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

    //回 mapsActivity
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵

            Intent intent = new Intent();
            intent.setClass(mapsActivity2.this, mapsActivity.class);
            intent.putExtra("activity_num",activity_num);
            startActivity(intent);
            mapsActivity2.this.finish();
        }

        return true;
    }

    //上傳感測資料至ioT平台
    public void httpRequest(String requestUrl, String requestMethod, String outputStr) {
        String result = null;
        StringBuffer buffer = new StringBuffer();
        String ou = outputStr;
        HttpsURLConnection httpUrlConn = null;
        try {

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        /*TrustManager[] tm = {new MyX509TrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();*/

            URL ioturl = new URL(requestUrl);
            httpUrlConn = (HttpsURLConnection) ioturl.openConnection();
            //httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setConnectTimeout(50000);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            httpUrlConn.setRequestProperty("CK", "PKEJNLQMANM2M7KWNP");//设置请求头
            httpUrlConn.setRequestProperty("Content-Type", "application/json");//设置参数类型是json格式


            //httpUrlConn.setRequestProperty("Content-Length", "86");

            httpUrlConn.connect();

            // 当有数据需要提交时

            OutputStream outputStream = new BufferedOutputStream(httpUrlConn.getOutputStream());
            // 注意编码格式，防止中文乱码
            outputStream.write(ou.getBytes("utf-8"));
            outputStream.flush();
            outputStream.close();


            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            // inputStream = null;

            result = buffer.toString();
            Toast re = Toast.makeText(mapsActivity2.this, "上傳成功 " + result, Toast.LENGTH_LONG);
            re.show();
            httpUrlConn.disconnect();
            // jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            ce.printStackTrace();
            Toast re = Toast.makeText(mapsActivity2.this, "上傳失敗: " + ce.toString(), Toast.LENGTH_LONG);
            re.show();
            // log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            e.printStackTrace();
            Toast re = Toast.makeText(mapsActivity2.this, "上傳失敗: " + e.toString(), Toast.LENGTH_LONG);
            re.show();
            // log.error("https request error:{}", e);
        }
    }

}
