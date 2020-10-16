package com.example.myapplication3;

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
import android.widget.Button;
import android.widget.EditText;

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


public class HomeListFragment extends Fragment {


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

    public HomeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
        initData();
    }

    private void initData() {
        dataBeans = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(dataBeans, getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(recyclerViewAdapter);

        OnItemClick();

        new Thread() {
            @Override
            public void run() {
                getHttp();
            }
        }.start();
    }

    private void OnItemClick() {
        recyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter.OnItemClick() {

            private AlertDialog alertDialog;
            private EditText et_desc;

            @Override
            public void OnClick(final int position) {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_homelistfragmentalertdialog, null);
                et_desc = inflate.findViewById(R.id.et_desc);
                Button btn_set = inflate.findViewById(R.id.btn_set);
                Button btn_request = inflate.findViewById(R.id.btn_request);
                et_desc.setText(dataBeans.get(position).getDesc());
                btn_set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String etdesc = et_desc.getText().toString();
                        Bean.DataBean dataBean = new Bean.DataBean();
                        dataBean.setImagePath(dataBeans.get(position).getImagePath());
                        dataBean.setTitle(dataBeans.get(position).getTitle());
                        dataBean.setDesc(etdesc);
                        recyclerViewAdapter.notifyDataSetChanged();
                        dataBeans.set(position, dataBean);
                        alertDialog.cancel();
                    }
                });
                btn_request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                alertDialog = new AlertDialog.Builder(getContext()).setView(inflate).create();

                alertDialog.show();
            }
        });

        recyclerViewAdapter.setOnLongClick(new RecyclerViewAdapter.OnLongItemClick() {
            @Override
            public void OnLongClick(final int position) {
                new AlertDialog.Builder(getContext()).setTitle("是否添加").setNegativeButton("否", null)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setSql(position);
                            }
                        }).show();
            }
        });
    }

    private void setSql(int position) {
        MySqlit mySqlit = new MySqlit(getContext());
        SQLiteDatabase db = mySqlit.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("disc", dataBeans.get(position).getDesc());
        contentValues.put("title", dataBeans.get(position).getTitle());
        contentValues.put("image", dataBeans.get(position).getImagePath());

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

    private void initView(View view) {
        rv = view.findViewById(R.id.rv_one);
    }
}