package com.example.myapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        ArrayList<View> views = new ArrayList<>();
        views.add(LayoutInflater.from(this).inflate(R.layout.layout_item1,null));
        views.add(LayoutInflater.from(this).inflate(R.layout.layout_item2,null));
        views.add(LayoutInflater.from(this).inflate(R.layout.layout_item3,null));
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_item4, null);
        Button btn_set = inflate.findViewById(R.id.btn_set);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences iv = getSharedPreferences("ma", MODE_PRIVATE);
                SharedPreferences.Editor edit = iv.edit();
                edit.putBoolean("flay",true);
                edit.commit();
                startActivity(new Intent(MainActivity.this,MainActivity2.class));
                finish();
            }
        });
        views.add(inflate);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(views, this);
        vp.setAdapter(viewPagerAdapter);
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.vp);
        SharedPreferences ma = getSharedPreferences("ma", MODE_PRIVATE);
        boolean flay = ma.getBoolean("flay", false);
        if (flay){
            startActivity(new Intent(MainActivity.this,MainActivity2.class));
            finish();
        }
    }
}