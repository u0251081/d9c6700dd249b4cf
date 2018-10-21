package com.example.user.sensortest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity
{

    private Button connect;
    toStart tostart=new toStart();
    //ArrayList<findvalue> data= tostart.data;
    Button btnshowdata;

    public static final int DHTREQUEST =1;
    public static final int DHTRESPONSE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


         /* findvalue e1=new findvalue();
        e1.setId("日期");
        e1.setName("震度值");
        e1.setAddress("顛簸等級");
        e1.setlocation("經緯度");
        data.add(e1);
        */

        btnshowdata=(Button)findViewById(R.id.roadlist);



        //點擊按鈕後開始紀錄
      connect = (Button) findViewById( R.id.start);
        connect.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v){

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, toStart.class);
                startActivity(intent);
               MainActivity.this.finish();
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("離開坑洞終結者")
                    .setMessage("祝您行車平安~~")

                    .setPositiveButton("掰掰",
                            new DialogInterface.OnClickListener()
                            {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener()
                            {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
        }
        return true;
    }


    //切換至偵測清單畫面
    public void changeProcess(View view)
    {
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
    public void changeProcess2(View view)
    {
        Intent intent=new Intent(MainActivity.this,touser.class);
        this.startActivity(intent);
        MainActivity.this.finish();
    }

    //切換至地圖畫面
    public void changeProcess3(View view)
    {
        Intent intent=new Intent(MainActivity.this,tomap.class);
        this.startActivity(intent);
        MainActivity.this.finish();
    }

    //按鈕簡易事件程序自動委派(onClick Method結構)
   public void showDHTData(View view)
    {
        //採用雙向切換(來源Activity 可以傳遞資訊到Target /Target處理好退掉 也可以回傳資訊到這一個Activity)
        //1.建構Intent
        Intent intent=new Intent(MainActivity.this,tolist.class);
        //2.帶URL資料到目標Activity
        //2.1使用機動問出資源管理物件
        Resources resources=this.getResources();
        String hostedString=resources.getString(R.string.hosted);
        String dhtservice=resources.getString(R.string.dhtservice);
        String url=String.format("%s%s",hostedString,dhtservice);
        Log.i("網址",url);
        //帶資訊 OverLoading Method
        intent.putExtra("url",url);
        //有去有回 狀態(state) 回來要有Callback
        //如何去 帶旗標
         this.startActivityForResult(intent,MainActivity.DHTREQUEST);//第二參數當作Flag 使用Callback程序判斷
MainActivity.this.finish();


    }



    //startActivityForResult 目標機動處理完之後 進行finish(釋放) 返回進行Callback 程序
    //Overrding Activity onActivityResult Method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //要自己處理
        if(requestCode==MainActivity.DHTREQUEST && resultCode==MainActivity.DHTRESPONSE)
        {
            //透過Intent取出回傳的狀態

        }
    }

}

