package com.example.myapplication6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_image_one;
    private EditText et_title;
    /**
     * 修改
     */
    private Button btn_set;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        image = intent.getStringExtra("image");

        Glide.with(this).load(image).into(iv_image_one);
        et_title.setText(title);
    }

    private void initView() {
        iv_image_one = (ImageView) findViewById(R.id.iv_image_one);
        et_title = (EditText) findViewById(R.id.et_title);
        btn_set = (Button) findViewById(R.id.btn_set);
        btn_set.setOnClickListener(this);

        iv_image_one.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_set:
                String ettitle = et_title.getText().toString();
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                intent.putExtra("ettitle", ettitle);
                intent.putExtra("ivimageone", image);
                setResult(5, intent);
                finish();
                break;
            case R.id.iv_image_one:
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_PICK);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent1,2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode == RESULT_OK) {
            Uri data1 = data.getData();
            Glide.with(this).load(data1).into(iv_image_one);
        }
    }
}