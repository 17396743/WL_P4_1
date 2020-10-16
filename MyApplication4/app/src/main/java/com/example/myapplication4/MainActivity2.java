package com.example.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_imageicon;
    private EditText et_dec;
    private EditText et_title;
    /**
     * 确定
     */
    private Button btn_set;
    private Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {
        iv_imageicon = (ImageView) findViewById(R.id.iv_imageicon);
        et_dec = (EditText) findViewById(R.id.et_dec);
        et_title = (EditText) findViewById(R.id.et_title);
        btn_set = (Button) findViewById(R.id.btn_set);
        btn_set.setOnClickListener(this);

        Intent intent = getIntent();

        String imageUrl = intent.getStringExtra("imageUrl");
        String dec = intent.getStringExtra("dec");
        String title = intent.getStringExtra("title");

        Glide.with(this).load(imageUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv_imageicon);
        et_dec.setText(dec);
        et_title.setText(title);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_set:
                String etdec = et_dec.getText().toString();
                String ettitle = et_title.getText().toString();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("etdec", etdec);
                intent.putExtra("ettitle", ettitle);
                setResult(5, intent);
                finish();

                break;
        }
    }
}