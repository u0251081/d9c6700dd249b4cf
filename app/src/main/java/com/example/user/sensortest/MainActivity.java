package com.example.user.sensortest;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {

    private Button connect;
    //ArrayList<findvalue> data= tostart.data;
    Button btnshowdata, logoutuser;
    TextView welcomeusertext;
    public static final int DHTREQUEST = 1;
    public static final int DHTRESPONSE = 2;
    String mName;
    sessionManager sessionManager;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigation_view;
private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    String one="1";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sessionManager = new sessionManager(this);
        sessionManager.checkLogin();

         /* findvalue e1=new findvalue();
        e1.setId("日期");
        e1.setName("震度值");
        e1.setAddress("顛簸等級");
        e1.setlocation("經緯度");
        data.add(e1);
        */



drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
//drawerLayout.bringToFront();
toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


              navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // 點選時收起選單
                drawerLayout.closeDrawer(GravityCompat.START);

                // 取得選項id
                int id = item.getItemId();

                // 前往使用者資料畫面
                if (id == R.id.nav_account) {
                    //Toast.makeText(MainActivity.this, "使用者資料", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, touser.class);
                    constant.n=mName;
                    intent.putExtra("activity_num",one);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                    return true;
                }
                // 前往已上傳資料清單
                else if (id == R.id.nav_list) {
                    // 按下「使用說明」要做的事
                    //Toast.makeText(MainActivity.this, "已上傳資料清單", Toast.LENGTH_SHORT).show();
                    //採用雙向切換(來源Activity 可以傳遞資訊到Target /Target處理好退掉 也可以回傳資訊到這一個Activity)
                    //1.建構Intent
                    Intent intent = new Intent(MainActivity.this, tolist.class);
                    //2.帶URL資料到目標Activity
                    //2.1使用機動問出資源管理物件
                    Resources resources = MainActivity.this.getResources();
                    String hostedString = resources.getString(R.string.hosted);
                    String dhtservice = resources.getString(R.string.dhtservice);
                    String url = String.format("%s%s", hostedString, dhtservice);
                    Log.i("網址", url);
                    //帶資訊 OverLoading Method
                    intent.putExtra("url", url);
                    constant.n=mName;
                    intent.putExtra("activity_num",one);
                    //有去有回 狀態(state) 回來要有Callback
                    //如何去 帶旗標
                    MainActivity.this.startActivityForResult(intent, MainActivity.DHTREQUEST);//第二參數當作Flag 使用Callback程序判斷
                    MainActivity.this.finish();

                    return true;
                }
                //前往地圖標記
                else if (id == R.id.nav_map) {
                    // 按下「使用說明」要做的事
                    //Toast.makeText(MainActivity.this, "地圖標記", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, mapsActivity.class);
                    constant.n = mName;
                    intent.putExtra("activity_num",one);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                    return true;
                }
                //關於SkyTrain訊息框跳出
                else if (id == R.id.nav_about) {
                    // 按下「使用說明」要做的事
                    //Toast.makeText(MainActivity.this, "關於SkyTrain", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("關於功能")
                            .setMessage("此APP藉由偵測手機震動來判斷路面坑洞\n如果震動數值幅度大於14.5則紀錄為坑洞\n我們把震動幅度分為五個等級\n" +
                                    "讓施工單位或使用者能了解各地區域的路面坑洞嚴重程度\n此APP利用手機內建的三軸感測器進行測量\n***偵測時請盡量直立手機以確保其精準度")

                            .setPositiveButton("了解",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                        }
                                    })
                            .setNegativeButton("",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).show();
                    return true;
                }
                //坑洞震度等級說明訊息框跳出
               /* else if (id == R.id.nav_level) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(MainActivity.this, "坑洞震度等級說明", Toast.LENGTH_SHORT).show();
                    return true;
                }*/


                return false;
            }
        });




        btnshowdata = (Button) findViewById(R.id.roadlist);
        welcomeusertext = (TextView) findViewById(R.id.welcomeusertext);
        logoutuser = findViewById(R.id.btnlogout);

        //獲得使用者名稱
        HashMap<String, String> user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);
        // String mEmail=user.get(sessionManager.EMAIL);


        //判斷目前是早上還晚上
        GregorianCalendar ca = new GregorianCalendar();
        int sun = ca.get(GregorianCalendar.AM_PM);


        if (sun == 0) {
            welcomeusertext.setText(mName + " 早安阿!\n快快樂樂出門  平平安安回家");
        } else {
            welcomeusertext.setText(mName + " 午/晚安阿!\n快快樂樂出門  平平安安回家");
        }

        //點擊按鈕後開始紀錄
        connect = (Button) findViewById(R.id.start);
        connect.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                constant.n=mName;
                intent.setClass(MainActivity.this, toStart.class);
                startActivity(intent);
                MainActivity.this.finish();

            }
        });

        //點擊按鈕後登出
        logoutuser.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                sessionManager.logout();
            }
        });



    }


    private static Boolean isExit = false;

    private static Boolean hasTask = false;


    //判斷是否要退出 app
    Timer timerExit = new Timer();

    TimerTask task = new TimerTask() {

        @Override

        public void run() {

            isExit = false;

            hasTask = true;

        }

    };


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        // 判斷是否按下Back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否要退出
            if (isExit == false) {

                isExit = true; //記錄下一次要退出

                Toast.makeText(this, "再按一次離開 SkyTrain"

                        , Toast.LENGTH_SHORT).show();

                // 如果超過兩秒則恢復預設值
                if (!hasTask) {

                    timerExit.schedule(task, 2000);

                }

            } else {

                finish(); // 離開程式

                System.exit(0);

            }

        }
        return false;
    }


    //切換至偵測清單畫面
    public void changeProcess(View view) {
      /*  //Toast.makeText(this,"變更畫面!!",Toast.LENGTH_LONG).show();
        //1.建構Intent 指定來源 設定目標的Meta data
        Intent intent=new Intent(MainActivity.this,tolist.class);
        //帶資料(序列化)
        intent.putExtra("data",data);
        //帶入android環境 進行切換
        this.startActivity(intent);
        MainActivity.this.finish();
        */


     /*Intent intent=new Intent(MainActivity.this,tolist.class);
        this.startActivity(intent);
        MainActivity.this.finish();
*/

        showDHTData(btnshowdata);
    }

    //切換至使用者基本資料畫面
    public void changeProcess2(View view) {
        Intent intent = new Intent(MainActivity.this, touser.class);
        constant.n=mName;
        intent.putExtra("activity_num",one);
        this.startActivity(intent);
        MainActivity.this.finish();
    }

    //切換至地圖畫面
    public void changeProcess3(View view) {
        Intent intent = new Intent(MainActivity.this, mapsActivity.class);
        constant.n = mName;
        intent.putExtra("activity_num",one);
        this.startActivity(intent);
        //MainActivity.this.finish();
    }

    //按鈕簡易事件程序自動委派(onClick Method結構)
    public void showDHTData(View view) {
        //採用雙向切換(來源Activity 可以傳遞資訊到Target /Target處理好退掉 也可以回傳資訊到這一個Activity)
        //1.建構Intent
        Intent intent = new Intent(MainActivity.this, tolist.class);
        //2.帶URL資料到目標Activity
        //2.1使用機動問出資源管理物件
        Resources resources = this.getResources();
        String hostedString = resources.getString(R.string.hosted);
        String dhtservice = resources.getString(R.string.dhtservice);
        String url = String.format("%s%s", hostedString, dhtservice);
        Log.i("網址", url);
        //帶資訊 OverLoading Method
        intent.putExtra("url", url);
        constant.n=mName;
        intent.putExtra("activity_num",one);
        //有去有回 狀態(state) 回來要有Callback
        //如何去 帶旗標
        MainActivity.this.startActivityForResult(intent, MainActivity.DHTREQUEST);//第二參數當作Flag 使用Callback程序判斷
        MainActivity.this.finish();

    }


    //startActivityForResult 目標機動處理完之後 進行finish(釋放) 返回進行Callback 程序
    //Overrding Activity onActivityResult Method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //要自己處理
        if (requestCode == MainActivity.DHTREQUEST && resultCode == MainActivity.DHTRESPONSE) {
            //透過Intent取出回傳的狀態

        }
    }

@Override
    public boolean onOptionsItemSelected(MenuItem item)
{
    if(toggle.onOptionsItemSelected(item))
    {
        return true;
    }
    return super.onOptionsItemSelected(item);
}


}

