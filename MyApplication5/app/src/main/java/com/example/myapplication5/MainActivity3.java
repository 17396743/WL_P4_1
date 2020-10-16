package com.example.myapplication5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_image_one;
    private EditText et_desc;
    /**
     * 修改
     */
    private Button btn_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();

        String image = intent.getStringExtra("image");
        String desc = intent.getStringExtra("desc");

        Glide.with(this).load(image).into(iv_image_one);
        et_desc.setText(desc);


    }

    private void initView() {
        iv_image_one = (ImageView) findViewById(R.id.iv_image_one);
        et_desc = (EditText) findViewById(R.id.et_desc);
        btn_set = (Button) findViewById(R.id.btn_set);
        btn_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_set:
                String etdesc = et_desc.getText().toString();
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                intent.putExtra("etdesc", etdesc);
                setResult(5, intent);
                finish();
                break;
        }
    }
}