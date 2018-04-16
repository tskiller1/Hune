package com.hunegroup.hune.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.hunegroup.hune.uiv2.CuaHangHuneTimViecActivity;
import com.hunegroup.hune.uiv2.HuneAdsTimViecActivity;
import com.hunegroup.hune.uiv2.HunePayTimViecActivity;
import com.hunegroup.hune.uiv2.QuanLyTimViecActivity;
import com.hunegroup.hune.uiv2.ThongTinKhuyenMaiTimViecActivity;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

import java.util.List;

import static com.hunegroup.hune.util.Utilities.deleteCache;
import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 06/07/2017.
 */

public class ProfileTimViecActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgNotify;
    private RelativeLayout rlThongtin;
    private TextView txtTenNguoiDung;
    private ImageView imgFocus;
    private LinearLayout lnDanhsachtimnguoi;
    private TextView txtSoLuongUngVien;

    private LinearLayout lnDanhsachyeuthich;
    private LinearLayout lnLuuthongtin;
    private LinearLayout lnChonlaichucnang;
    private TextView txtCaiDat;
    private FloatingActionButton btnAdd;
    private TextView txtMessenger;
    //TODO: Hune V2

    private LinearLayout lnHunePay;
    private LinearLayout lnHuneAds;
    private LinearLayout lnHuneStore;
    private LinearLayout lnDiscount;
    private LinearLayout lnQuanlytimviec;

    private UserDTO userDTO;
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    public List<ThongTinTimViecDTO> thongTinTimViecDTOs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_profile_timviec);
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
        lnQuanlytimviec = (LinearLayout) findViewById(R.id.ln_quanlytimviec);

        lnQuanlytimviec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTimViecActivity.this, QuanLyTimViecActivity.class));
            }
        });

        lnDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTimViecActivity.this, ThongTinKhuyenMaiTimViecActivity.class));
            }
        });

        lnHuneStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTimViecActivity.this, CuaHangHuneTimViecActivity.class));
            }
        });

        lnHuneAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTimViecActivity.this, HuneAdsTimViecActivity.class));
            }
        });

        lnHunePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTimViecActivity.this, HunePayTimViecActivity.class));
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileTimViecActivity.this, ThemViecLamTimViecActivity.class));
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
                startActivity(new Intent(ProfileTimViecActivity.this, ThongBaoTimViecActivity.class));
            }
        });

        rlThongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTimViecActivity.this, TrangCaNhanTimViecActivity.class));

            }
        });

        lnDanhsachyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTimViecActivity.this, DanhSachYeuThichTimViec.class));
            }
        });

        lnLuuthongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTimViecActivity.this, ThongTinDaLuuTimViecActivity.class));
            }
        });

        lnChonlaichucnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCache(getBaseContext());
                Intent intent = new Intent(ProfileTimViecActivity.this, ChonChucNangActivity.class);
                /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        txtCaiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTimViecActivity.this, CaiDatTimViecActivity.class));
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
