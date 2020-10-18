package com.example.myapplication8;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


public class HomeFragment extends Fragment {


    private RecyclerView rv;
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

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
        initData();
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv_one);
    }

    private void initData() {
        dataBeans = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(dataBeans, getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter.OnItemClick() {
            @Override
            public void OnClick(final int position) {
               new AlertDialog.Builder(getContext()).setTitle("是否添加收藏").setPositiveButton("是", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       setSql(position);
                       Toast.makeText(getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                   }
               }).setNegativeButton("否",null).show();
            }
        });

        new Thread() {
            @Override
            public void run() {
                getHttp();
            }
        }.start();
    }

    private void setSql(int position) {
        MySqlit mySqlit = new MySqlit(getContext());
        SQLiteDatabase db = mySqlit.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("disc", dataBeans.get(position).getDesc());
        contentValues.put("title", dataBeans.get(position).getTitle());
        contentValues.put("imageurl", dataBeans.get(position).getImagePath());

        db.insert("demo", null, contentValues);

        db.close();
        mySqlit.close();
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
}