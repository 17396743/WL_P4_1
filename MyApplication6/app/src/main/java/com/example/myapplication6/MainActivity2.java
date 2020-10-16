package com.example.myapplication6;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView rv;
    private ArrayList<Bean.DataBean> dataBeans;
    private RecyclerViewAdapter recyclerViewAdapter;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            ArrayList<Bean.DataBean> beans = (ArrayList<Bean.DataBean>) msg.obj;
            dataBeans.addAll(beans);

            recyclerViewAdapter.notifyDataSetChanged();
        }
    };
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        initData();
    }

    private void initData() {
        dataBeans = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter(dataBeans, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                pos = position;
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                intent.putExtra("title",dataBeans.get(position).getTitle());
                intent.putExtra("image",dataBeans.get(position).getImagePath());
                startActivityForResult(intent,5);

            }
        });

        new Thread(){

            @Override
            public void run() {
                getHttp();
            }
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5){
            String ettitle = data.getStringExtra("ettitle");
            String ivimageone = data.getStringExtra("ivimageone");
            Bean.DataBean dataBean = new Bean.DataBean();
            dataBean.setTitle(ettitle);
            dataBean.setImagePath(ivimageone);
            dataBeans.set(pos,dataBean);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void getHttp() {
        try {
            URL url = new URL("https://www.wanandroid.com/banner/json");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String len = null;
                StringBuilder sb = new StringBuilder();
                while ((len=in.readLine())!=null){
                    sb.append(len.trim());
                }
                String json = sb.toString();
                Log.i("tag","==》"+json);
                Gson gson = new Gson();
                Bean bean = gson.fromJson(json, Bean.class);
                List<Bean.DataBean> data = bean.getData();

                Message message = new Message();
                message.obj = data;
                handler.sendMessage(message);


            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        rv = (RecyclerView) findViewById(R.id.rv);
    }
}