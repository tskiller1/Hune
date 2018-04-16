package com.hunegroup.hune.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunegroup.hune.R;
import com.hunegroup.hune.util.Common;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 8/17/2017.
 */

public class ThongTinActivity extends AppCompatActivity {
    public static final String REQUEST_CODE = "1";

    ImageView imgBack;
    Toolbar toolbar;
    TextView txtAbout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_thongtin);
        findViews();
        Intent intent = getIntent();
        String code = intent.getStringExtra(REQUEST_CODE);
        if(code.equals(Common.Type.TYPE_SEARCH_JOB)){
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.setStatusBarColor(Color.parseColor("#cc3333"));
            }
            toolbar.setBackgroundColor(Color.parseColor("#cc3333"));

        }
        else {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.setStatusBarColor(Color.parseColor("#33ccff"));
            }
            toolbar.setBackgroundColor(Color.parseColor("#33ccff"));
        }
        txtAbout.setText(Html.fromHtml(getString(R.string.about_info)));
    }

    private void findViews(){
        imgBack = (ImageView) findViewById(R.id.imgBack);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtAbout = (TextView) findViewById(R.id.txtAbout);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
