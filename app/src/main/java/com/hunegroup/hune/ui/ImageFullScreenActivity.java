package com.hunegroup.hune.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import com.hunegroup.hune.R;

public class ImageFullScreenActivity extends AppCompatActivity {
    private ImageView imgBack,imgFullScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        initView();

        Intent intentResult=getIntent();
        if(intentResult!=null)
        {
            String urlImage=intentResult.getStringExtra(ChiTietUngVienTuyenDungActivity.TAG_URLIMAGE);
            Picasso.with(getBaseContext()).load(urlImage).into(imgFullScreen);
        }
    }

    private void initView() {
        imgBack= (ImageView) findViewById(R.id.imgBack);
        imgFullScreen= (ImageView) findViewById(R.id.imgFullScreen);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
