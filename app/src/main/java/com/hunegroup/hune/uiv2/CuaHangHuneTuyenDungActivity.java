package com.hunegroup.hune.uiv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hunegroup.hune.R;
import com.hunegroup.hune.ui.ThongBaoTuyenDungActivity;

/**
 * Created by apple on 11/21/17.
 */

public class CuaHangHuneTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private ImageView imvNotification;
    private LinearLayout lnlBuyCoupon;
    private LinearLayout lnlMyCoupon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_cuahanghune_tuyendung);
        findViews();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvNotification = (ImageView) findViewById(R.id.imv_notification);
        lnlBuyCoupon = (LinearLayout) findViewById(R.id.lnl_buy_coupon);
        lnlMyCoupon = (LinearLayout) findViewById(R.id.lnl_my_coupon);

        imvBack.setOnClickListener(this);
        imvNotification.setOnClickListener(this);
        lnlBuyCoupon.setOnClickListener(this);
        lnlMyCoupon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.imv_notification:
                startActivity(new Intent(this, ThongBaoTuyenDungActivity.class));
                break;
            case R.id.lnl_buy_coupon:
                startActivity(new Intent(this, MuaCouponTuyenDungActivity.class));
                break;
            case R.id.lnl_my_coupon:
                startActivity(new Intent(this, CouponCuaToiTuyenDungActivity.class));
                break;
        }
    }
}
