package com.example.user.sensortest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.user.sensortest.findvalue;

public class viewAdapter extends BaseAdapter {
    //attribute
    private ArrayList<findvalue> data;
    //打氣筒
    private LayoutInflater inflater;
    //APP應用系統環境
    public Context context;
    //自訂建構子Injection(注入物件初始化資訊)

    //巢狀類別
    public class ViewTag
    {
        public TextView txtName;  //震度
        public TextView txtAddress; //等級
        public TextView txId; //日期
        public TextView txlocation;  //經緯度位置
    }

    public viewAdapter(Context context,ArrayList<findvalue> data)
    {
        this.context=context;
        //Inflater工廠
        this.inflater=LayoutInflater.from(context);
        this.data=data;
    }
    //核算要多少項目(View)產生
    @Override
    public int getCount() {
        return data.size();
    }

    //舞台上ListView 呼叫一個功能getItemAtPosition(index)? 對後臺檢場(傳遞順序 給代表性的物件Identifier)
    @Override
    public Object getItem(int index) {
        return data.get(index).getId(); //傳遞畫面相對順序對應的員工物件識別物件
    }

    //畫面上順序 對應背後資料的順序是否一致
    @Override
    public long getItemId(int i) {
        return i;
    }

    //配合畫面順序Recycle 產生View回傳給ListView
    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        //判斷第二個參數是否有物件(如果沒有 那就第一個 自己產生)
        ViewTag tag=null;
        if(view==null)
        {
            //使用打氣筒將配合layout file(xml)反轉一個記憶體的View物件
            view=inflater.inflate(R.layout.itemlayout,null);
            //問出View成員
            tag=new ViewTag();
            tag.txtName=view.findViewById(R.id.tvvalues);
            tag.txtAddress=view.findViewById(R.id.tvlevel);
            tag.txlocation=view.findViewById(R.id.tvlocation);
            tag.txId=view.findViewById(R.id.tvtime);
            //讓View參考這一組(畫面成員)
            view.setTag(tag);
        }else
        {
            tag=(ViewTag) view.getTag();
        }
        //派配成員內容(List集合)
        findvalue emp=data.get(index);
        tag.txtName.setText(emp.getName());
        tag.txtAddress.setText(emp.getAddress());
        tag.txlocation.setText(emp.getlocation());
        tag.txId.setText(emp.getId());

        //Log
        Log.i("項目",emp.getName()+" "+index);

        return view;
    }

}