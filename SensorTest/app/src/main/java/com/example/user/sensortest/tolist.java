package com.example.user.sensortest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.sensortest.DHT;  //從類別DHT取方法
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.example.user.sensortest.DHT;
import static android.content.ContentValues.TAG;

public class tolist extends Activity
{
    //private Context mContext;
    //private Activity mActivity;
    private CoordinatorLayout mCLayout;

    //Attribute 請求佇列區 物件Constructor完之後引發
    private RequestQueue requestQueue;  //this->Context
    private TextView t;

Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview2);
        t=(TextView) findViewById(R.id.listData);
        t.setText("");
       /* Intent intent=this.getIntent();
        //取出序列化物件
        ArrayList<findvalue> data=
                (ArrayList<findvalue>)intent.getSerializableExtra("data");

        //Toast.makeText(this,data.size()+"",Toast.LENGTH_LONG).show();
        //TODO 如何將Data轉換成ListView裡面View進行呈現
        //1.建構自訂配接器(Context,List資料)
        viewAdapter adapter=new viewAdapter(this,data);
        //配上去
        listData.setAdapter(adapter); //ListAdapter介面 進行介面多型化
*/

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //取出Intent
        Intent intent=this.getIntent();
        //取出DHT服務位址
        String ioturl=intent.getStringExtra("url");

        JsonArrayRequest jsonarrayRequest= new JsonArrayRequest
                ( Request.Method.GET,ioturl,  (String) null,
                        new Response.Listener<JSONArray>()
                        {
                            @Override
                            public void onResponse(JSONArray response)
                            {
                                try
                                {

               for(int i=0;i<response.length();i++)
               {
                   JSONObject road=response.getJSONObject(i);
                   JSONArray values=road.getJSONArray("value");
                   String item=values.getString(0);
                   Log.i("結果",item);

                   //將字串符合JSON Object格式 Parser成JSONObject 反序列化
                   //使用Gson jar
                   Gson gson=new Gson();
                   //反序列化 String convert to Custom Class Instance
                   DHT valueObject=gson.fromJson(item, DHT.class);
                   t.append(valueObject.getjump()+" "+valueObject.getlevel()+" "+valueObject.getlongitude()+" "+valueObject.getlatitude());
                   t.append("\n");
                   t.append(valueObject.getuploadtime());
                   t.append("\n\n");
                   //鋪成畫面
               }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {

                            }
                        }
                )

        {
            //Overriding 覆寫父親既定的方法
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                //操作Headers加入金鑰
                //建構Map Collection
                Map<String,String> headers=new HashMap<>();
                //加入Key與value
                //String key=AppUtility.getResourceKeyValue("CK");
                //Log.i("Key",key);
                headers.put("CK","PKVMV5FUVNZMZEOOD1");
                return headers;
            }

        };

        requestQueue.add(jsonarrayRequest);

        /*進入此activity時EditText不先跳出鍵盤*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    //回偵測畫面
    public void changeProcess4(View view)
    {
                Intent intent = new Intent();
                intent.setClass(tolist.this, MainActivity.class);
                startActivity(intent);
this.finish();

    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(tolist.this)
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

/*    public static String String2Json(String s)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.toCharArray()[i];
            switch (c)
            {
                case '.':
                    sb.append("."); break;
                case '-':
                    sb.append("-"); break;
                case '/':
                    sb.append("\\/"); break;
                case '\b':
                    sb.append("\\b"); break;
                case '\f':
                    sb.append("\\f"); break;
                case '\n':
                    sb.append("\\n"); break;
                case '\r':
                    sb.append("\\r"); break;
                case '\t':
                    sb.append("\\t"); break;
                default:
                    if ((c >= 0 && c <= 31)||c ==127)//在ASCⅡ码中，第0～31号及第127号(共33个)是控制字符或通讯专用字符
                    {

                    }
                    else
                    {
                        sb.append(c);
                    }
                    break;
            }
        }
        return sb.toString();
    }
*/


}



