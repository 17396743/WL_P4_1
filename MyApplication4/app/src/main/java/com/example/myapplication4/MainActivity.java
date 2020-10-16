package com.example.myapplication4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;

    private NavigationView nav_main;
    private DrawerLayout dra_main;
    private Toolbar toolbar;
    private ConstraintLayout cl_main;
    private ImageView iv_icon;
    private ArrayList<Bean.DataBean> dataBeans;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            ArrayList<Bean.DataBean> beans = (ArrayList<Bean.DataBean>) msg.obj;
            dataBeans.addAll(beans);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    };
    private RecyclerViewAdapter recyclerViewAdapter;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        initToolbar();
        initMain();
        initRecyclerView();

    }


    private void initRecyclerView() {
        dataBeans = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter(dataBeans, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                pos = position;
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                intent.putExtra("imageUrl", dataBeans.get(position).getImagePath());
                intent.putExtra("dec", dataBeans.get(position).getDesc());
                intent.putExtra("title", dataBeans.get(position).getTitle());
                startActivityForResult(intent, 5);

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

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(0, 0, 0, "分享");
//        menu.add(0, 1, 0, "搜索");

        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case 0:
//            case 1:
//            case 2: {
//                Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
//                break;
//            }
//        }
        switch (item.getItemId()) {
            case R.id.item11: {
                Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.item12: {
                Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMain() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, dra_main, toolbar, R.string.app_name, R.string.app_name);
        nav_main.setItemIconTintList(null);
        nav_main.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1: {
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.item2: {
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.item3: {
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });
        View headerView = nav_main.getHeaderView(0);
        iv_icon = headerView.findViewById(R.id.iv_icon);
        Glide.with(this).load(R.drawable.a).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv_icon);
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });

        dra_main.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        dra_main.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//                int right = nav_main.getRight();
//                cl_main.setX(right);
                cl_main.setX(drawerView.getRight());
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
    }


    private void initToolbar() {
        toolbar.setTitle("主标题");
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri data1 = data.getData();
            Glide.with(this).load(data1).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv_icon);
        }

        if (requestCode == 5) {
            String etdec = data.getStringExtra("etdec");
            String ettitle = data.getStringExtra("ettitle");
            Bean.DataBean dataBean = new Bean.DataBean();
            dataBean.setDesc(etdec);
            dataBean.setImagePath(dataBeans.get(pos).getImagePath());
            dataBean.setTitle(ettitle);
            dataBeans.set(pos, dataBean);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        rv = (RecyclerView) findViewById(R.id.rv);
        nav_main = (NavigationView) findViewById(R.id.nav_main);
        dra_main = (DrawerLayout) findViewById(R.id.dra_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cl_main = (ConstraintLayout) findViewById(R.id.cl_main);
    }
}