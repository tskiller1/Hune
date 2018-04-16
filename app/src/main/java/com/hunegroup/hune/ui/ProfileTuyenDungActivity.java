package com.hunegroup.hune.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.ThongTinTimViecDTO;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.uiv2.CuaHangHuneTuyenDungActivity;
import com.hunegroup.hune.uiv2.HuneAdsTuyenDungActivity;
import com.hunegroup.hune.uiv2.HunePayTuyenDungActivity;
import com.hunegroup.hune.uiv2.QuanLyTuyenNguoiTuyenDungActivity;
import com.hunegroup.hune.uiv2.ThongTinKhuyenMaiTuyenDungActivity;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Utilities;

import java.util.List;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 05/07/2017.
 */

public class ProfileTuyenDungActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgNotify;
    private RelativeLayout rlThongtin;
    private TextView txtTenNguoiDung;
    private ImageView imgFocus;
    private LinearLayout lnDanhsachyeuthich;
    private LinearLayout lnLuuthongtin;
    private LinearLayout lnChonlaichucnang;
    private TextView txtCaiDat;
    private TextView txtMessenger;
    private FloatingActionButton btnAdd;
    //TODO: Hune V2
    private LinearLayout lnQuanLyTuyenNguoi;
    private LinearLayout lnHunePay;
    private LinearLayout lnHuneAds;
    private LinearLayout lnHuneStore;
    private LinearLayout lnDiscount;

    private UserDTO userDTO;
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    public List<ThongTinTimViecDTO> thongTinTimViecDTOs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_profile_tuyendung);
        findViews();
        setTxtTenNguoiDung();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    private void findViews() {
        txtMessenger = (TextView) findViewById(R.id.txtMessenger);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgNotify = (ImageView) findViewById(R.id.imgNotify);
        rlThongtin = (RelativeLayout) findViewById(R.id.rl_thongtin);
        txtTenNguoiDung = (TextView) findViewById(R.id.txtTenNguoiDung);
        imgFocus = (ImageView) findViewById(R.id.imgFocus);
        lnDanhsachyeuthich = (LinearLayout) findViewById(R.id.ln_danhsachyeuthich);
        lnLuuthongtin = (LinearLayout) findViewById(R.id.ln_luuthongtin);
        lnChonlaichucnang = (LinearLayout) findViewById(R.id.ln_chonlaichucnang);
        txtCaiDat = (TextView) findViewById(R.id.txtCaiDat);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        //TODO: Hune V2
        lnHunePay = (LinearLayout) findViewById(R.id.ln_hune_pay);
        lnHuneAds = (LinearLayout) findViewById(R.id.ln_hune_ads);
        lnHuneStore = (LinearLayout) findViewById(R.id.ln_hune_store);
        lnDiscount = (LinearLayout) findViewById(R.id.ln_discount);
        lnQuanLyTuyenNguoi = (LinearLayout) findViewById(R.id.ln_quanlytuyennguoi);

        lnQuanLyTuyenNguoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, QuanLyTuyenNguoiTuyenDungActivity.class));
            }
        });

        lnDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, ThongTinKhuyenMaiTuyenDungActivity.class));
            }
        });

        lnHuneStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, CuaHangHuneTuyenDungActivity.class));
            }
        });

        lnHuneAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, HuneAdsTuyenDungActivity.class));
            }
        });

        lnHunePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, HunePayTuyenDungActivity.class));
            }
        });

        //END HUNE V2

        txtMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.me/hunegroup"));
                startActivity(browserIntent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, ThongBaoTuyenDungActivity.class));
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, DangTuyenTuyenDungActivity.class));
            }
        });
        txtCaiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, CaiDatTuyenDungActivity.class));
            }
        });
        rlThongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, TrangCaNhanTuyenDungActivity.class));
            }
        });

        lnChonlaichucnang.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Utilities.deleteCache(getBaseContext());
                Intent intent = new Intent(ProfileTuyenDungActivity.this, ChonChucNangActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        lnLuuthongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, ThongTinDaLuuTuyenDungActivity.class));
            }
        });
        lnDanhsachyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTuyenDungActivity.this, DanhSachYeuThichTuyenDungActivity.class));
            }
        });
    }

    private void setTxtTenNguoiDung() {
        userDTO = new UserDTO();
        sessionUser = new SessionUser(getBaseContext());

        if (sessionUser.getUserDetails().getToken() == null)
            return;

        userDTO = sessionUser.getUserDetails();
        txtTenNguoiDung.setText("" + userDTO.getFull_name());
    }

}
