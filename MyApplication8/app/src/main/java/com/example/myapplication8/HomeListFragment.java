package com.example.myapplication8;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeListFragment extends Fragment {


    private RecyclerView rv;
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
        recyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter.OnItemClick() {
            @Override
            public void OnClick(int position) {
                Toast.makeText(getContext(), "你点击了一下", Toast.LENGTH_SHORT).show();
            }
        });


        getSql();
    }

    private void getSql() {
        MySqlit mySqlit = new MySqlit(getContext());
        SQLiteDatabase db = mySqlit.getReadableDatabase();
        Cursor demo = db.query("demo", null, null, null, null, null, null);
        while (demo.moveToNext()){
            String disc = demo.getString(demo.getColumnIndex("disc"));
            String title = demo.getString(demo.getColumnIndex("title"));
            String imageurl = demo.getString(demo.getColumnIndex("imageurl"));

            Bean.DataBean dataBean = new Bean.DataBean();
            dataBean.setDesc(disc);
            dataBean.setTitle(title);
            dataBean.setImagePath(imageurl);
            dataBeans.add(dataBean);

        }
        db.close();
        mySqlit.close();
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv_two);
    }

    public void request(){
        dataBeans.clear();
        getSql();
        recyclerViewAdapter.notifyDataSetChanged();
    }
}