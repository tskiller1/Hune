package com.hunegroup.hune.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hunegroup.hune.R;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 06/07/2017.
 */

public class LocThongTinTimViecActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgNotify;
    private TextView txtTheoLoaiHinh;
    private RelativeLayout rlLoaiCongViec;
    private ImageView imgLeft;
    private ImageView imgRight;
    private LinearLayout listThongTin;
    private CheckBox ckbNam;
    private CheckBox ckbNu;
    private Spinner spnMucLuong;
    private Button btnChapNhan;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_locthongtin_timviec);
    }

    private void findViews() {
        imgBack = (ImageView)findViewById( R.id.imgBack );
        imgNotify = (ImageView)findViewById( R.id.imgNotify );
        txtTheoLoaiHinh = (TextView)findViewById( R.id.txtTheoLoaiHinh );
        rlLoaiCongViec = (RelativeLayout)findViewById( R.id.rl_loaiCongViec );
        imgLeft = (ImageView)findViewById( R.id.imgLeft );
        imgRight = (ImageView)findViewById( R.id.imgRight );
        listThongTin = (LinearLayout)findViewById( R.id.listThongTin );
        ckbNam = (CheckBox)findViewById( R.id.ckbNam );
        ckbNu = (CheckBox)findViewById( R.id.ckbNu );
        spnMucLuong = (Spinner)findViewById( R.id.spnMucLuong );
        btnChapNhan = (Button)findViewById( R.id.btnChapNhan );
        btnAdd = (FloatingActionButton)findViewById( R.id.btnAdd );

        btnChapNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
