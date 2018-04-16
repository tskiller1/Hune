package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunegroup.hune.R;
import com.hunegroup.hune.util.SessionUser;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 06/07/2017.
 */

public class TrangCaNhanTimViecActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgNotify;
    private TextView txtTenNguoiDung;
    private ImageView imgSua;
    private TextView txtGioiTinh;
    private TextView txtNgaySinh;
    private TextView txtCaiDat;
    private FloatingActionButton btnAdd;

    SessionUser sessionUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_trangcanhan_timviec);
        findViews();
        sessionUser = new SessionUser(getBaseContext());
        txtTenNguoiDung.setText(sessionUser.getUserDetails().getFull_name());
        if ((sessionUser.getUserDetails().getBirthday()!=null))
            txtNgaySinh.setText(sessionUser.getUserDetails().getBirthday());
        else txtNgaySinh.setText(getString(R.string.chuacothongtin));
        if ((sessionUser.getUserDetails().getSex()!=null))
            txtGioiTinh.setText(sessionUser.getUserDetails().getSex());
        else txtGioiTinh.setText(getString(R.string.chuacothongtin));
    }

    private void findViews() {
        imgBack = (ImageView)findViewById( R.id.imgBack );
        imgNotify = (ImageView)findViewById( R.id.imgNotify );
        txtTenNguoiDung = (TextView)findViewById( R.id.txtTenNguoiDung );
        imgSua = (ImageView)findViewById( R.id.imgSua );
        txtGioiTinh = (TextView)findViewById( R.id.txtGioiTinh );
        txtNgaySinh = (TextView)findViewById( R.id.txtNgaySinh );
        txtCaiDat = (TextView)findViewById( R.id.txtCaiDat );
        btnAdd = (FloatingActionButton)findViewById( R.id.btnAdd );

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrangCaNhanTimViecActivity.this,ThongBaoTimViecActivity.class));
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrangCaNhanTimViecActivity.this,ThemViecLamTimViecActivity.class));
            }
        });

        imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrangCaNhanTimViecActivity.this,SuaThongTinTimViecActivity.class));
                finish();
            }
        });

        txtCaiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrangCaNhanTimViecActivity.this,CaiDatTimViecActivity.class));
            }
        });
    }
}
