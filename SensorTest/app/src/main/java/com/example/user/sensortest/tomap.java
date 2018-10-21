package com.example.user.sensortest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.user.sensortest.R;

public class tomap extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);


    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(tomap.this)
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

