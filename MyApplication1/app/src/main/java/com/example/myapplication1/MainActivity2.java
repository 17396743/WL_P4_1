package com.example.myapplication1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            ArrayList<Bean.DataBean> list =(ArrayList<Bean.DataBean>)msg.obj;
            beans.addAll(list);
            recyclerViewAdapter.notifyDataSetChanged();

        }
    };
    private ArrayList<Bean.DataBean> beans;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ImageView iv;

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
        iv = headerView.findViewById(R.id.iv);
        Glide.with(MainActivity2.this).load(R.drawable.a).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_PICK);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/");

                intent.setAction(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/");
                startActivityForResult(intent,1);
            }
        });



        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,dr_main,toolbar,R.string.app_name,R.string.app_name);
        dr_main.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        dr_main.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                int right = na_main.getRight();
                cl_main.setX(right);
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

        beans = new ArrayList<>();


        recyclerViewAdapter = new RecyclerViewAdapter(beans, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(recyclerViewAdapter);


        recyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter.OnItemClick() {
            @Override
            public void OnClick(int position) {
                View inflate = LayoutInflater.from(MainActivity2.this).inflate(R.layout.layout_alertdialog, null);
                ImageView iv_image1 = inflate.findViewById(R.id.iv_image1);
                Glide.with(MainActivity2.this).load(beans.get(position).getImagePath()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv_image1);
                TextView tv_des1 = inflate.findViewById(R.id.tv_des1);
                tv_des1.setText(beans.get(position).getDesc());
                TextView tv_title1 = inflate.findViewById(R.id.tv_title1);
                tv_title1.setText(beans.get(position).getTitle());
                new AlertDialog.Builder(MainActivity2.this).setView(inflate).show();
            }
        });



        new Thread(){
            @Override
            public void run() {
                try {
                    getHttp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getHttp() throws IOException {
        URL url = new URL("https://www.wanandroid.com/banner/json");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        if (urlConnection.getResponseCode()==200){
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String len = null;
            StringBuilder sb = new StringBuilder();
            while ((len=in.readLine())!=null){
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



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"分享");
        menu.add(0,1,0,"搜索");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode == RESULT_OK){
            Uri data1 = data.getData();
            Glide.with(this).load(data1).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv);
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