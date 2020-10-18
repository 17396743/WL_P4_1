package com.example.myapplication7;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
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

    private Toolbar toolbar;
    private RecyclerView rv;
    private ConstraintLayout cl_main;
    private NavigationView na_main;
    private DrawerLayout dr_main;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            ArrayList<Bean.DataBean> beans = (ArrayList<Bean.DataBean>) msg.obj;
            dataBeans.addAll(beans);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    };
    private ArrayList<Bean.DataBean> dataBeans;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        initData();
    }

    private void initData() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        View headerView = na_main.getHeaderView(0);
        imageView = headerView.findViewById(R.id.iv_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this, "头部", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,1);
            }
        });

        na_main.setItemIconTintList(null);
        na_main.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                    case R.id.item2:
                    case R.id.item3: {
                        Toast.makeText(MainActivity2.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, dr_main, toolbar, R.string.app_name, R.string.app_name);
        dr_main.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        dr_main.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                cl_main.setX(na_main.getRight());
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        dataBeans = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(dataBeans, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity2.this, dataBeans.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        new Thread() {
            @Override
            public void run() {
                getHttp();
            }
        }.start();
    }

    private void getHttp() {
        try {
            URL url = new URL("https://www.wanandroid.com/banner/json");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String len = null;
                StringBuilder sb = new StringBuilder();
                while ((len = in.readLine()) != null) {
                    sb.append(len.trim());
                }

                String json = sb.toString();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK){
            Uri data1 = data.getData();
            Glide.with(this).load(data1).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv = (RecyclerView) findViewById(R.id.rv);
        cl_main = (ConstraintLayout) findViewById(R.id.cl_main);
        na_main = (NavigationView) findViewById(R.id.na_main);
        dr_main = (DrawerLayout) findViewById(R.id.dr_main);

    }
}