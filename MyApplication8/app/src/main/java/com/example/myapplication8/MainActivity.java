package com.example.myapplication8;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private ViewPager vp;
    private TabLayout tab;
    private ConstraintLayout cl_main;
    private NavigationView na_main;
    private DrawerLayout dr_main;
    private HomeListFragment homeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,dr_main,toolbar,R.string.app_name,R.string.app_name);
        dr_main.addDrawerListener(actionBarDrawerToggle);
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
        actionBarDrawerToggle.syncState();
        na_main.setItemIconTintList(null);
        na_main.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item1:
                    case R.id.item2:
                    case R.id.item3:{
                        Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        View headerView = na_main.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.iv_icon);
        Glide.with(this).load(R.drawable.a).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        homeListFragment = new HomeListFragment();
        fragments.add(homeListFragment);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("首页");
        strings.add("收藏");
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, strings);
        vp.setAdapter(fragmentAdapter);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position==1){
                    homeListFragment.request();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tab.setupWithViewPager(vp);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        vp = (ViewPager) findViewById(R.id.vp);
        tab = (TabLayout) findViewById(R.id.tab);
        cl_main = (ConstraintLayout) findViewById(R.id.cl_main);
        na_main = (NavigationView) findViewById(R.id.na_main);
        dr_main = (DrawerLayout) findViewById(R.id.dr_main);
    }
}