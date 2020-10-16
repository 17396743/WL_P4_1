package com.example.myapplication3;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView rv;
    private ArrayList<Bean.DataBean> Beans;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Integer> integers;
    private ArrayList<Bean_one> bean_ones;

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

    private void initData() {
        Beans = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(Beans, getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(recyclerViewAdapter);

        OnItemClick();


        getSql();
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
                et_desc.setText(Beans.get(position).getDesc());
                btn_set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String etdesc = et_desc.getText().toString();

                        setSql_one(position, etdesc);


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

    private void setSql_one(int position, String etdesc) {
        MySqlit mySqlit = new MySqlit(getContext());
        SQLiteDatabase db = mySqlit.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("disc", etdesc);
        contentValues.put("title", bean_ones.get(position).getTitle());
        contentValues.put("image", bean_ones.get(position).getImage());

        db.update("demo", contentValues, "id=?", new String[]{bean_ones.get(position).getId().toString()});

        db.close();
        mySqlit.close();

        request();
    }


    private void setSql(int position) {
        MySqlit mySqlit = new MySqlit(getContext());
        SQLiteDatabase db = mySqlit.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("disc", Beans.get(position).getDesc());
        contentValues.put("title", Beans.get(position).getTitle());
        contentValues.put("image", Beans.get(position).getImagePath());
        db.insert("demo", null, contentValues);
        db.close();
        mySqlit.close();

        request();
    }

    private void getSql() {
        MySqlit mySqlit = new MySqlit(getContext());
        SQLiteDatabase db = mySqlit.getReadableDatabase();
        bean_ones = new ArrayList<>();
        Cursor demo = db.query("demo", null, null, null, null, null, null);
        while (demo.moveToNext()) {
            int id = demo.getInt(demo.getColumnIndex("id"));
            String disc = demo.getString(demo.getColumnIndex("disc"));
            String title = demo.getString(demo.getColumnIndex("title"));
            String image = demo.getString(demo.getColumnIndex("image"));


            bean_ones.add(new Bean_one(id, disc, title, image));


            Bean.DataBean dataBean = new Bean.DataBean();
            dataBean.setDesc(disc);
            dataBean.setTitle(title);
            dataBean.setImagePath(image);
            Beans.add(dataBean);
        }
        recyclerViewAdapter.notifyDataSetChanged();
        db.close();
        mySqlit.close();

    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv_two);

    }

    public void request() {
        Beans.clear();
        getSql();
//        Beans.addAll(sqlList);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}