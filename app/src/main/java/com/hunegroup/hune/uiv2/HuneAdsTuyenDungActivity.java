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
 * Created by apple on 12/12/17.
 */

public class HuneAdsTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private ImageView imvNotification;
    private LinearLayout lnlBannerAds;
    private LinearLayout lnlLocationAds;
    private LinearLayout lnlNotificationAds;
    private LinearLayout lnlManagerAds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_huneads_tuyendung);
        findViews();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvNotification = (ImageView) findViewById(R.id.imv_notification);
        lnlBannerAds = (LinearLayout) findViewById(R.id.lnl_banner_ads);
        lnlLocationAds = (LinearLayout) findViewById(R.id.lnl_location_ads);
        lnlNotificationAds = (LinearLayout) findViewById(R.id.lnl_notification_ads);
        lnlManagerAds = (LinearLayout) findViewById(R.id.lnl_manager_ads);

        imvBack.setOnClickListener(this);
        imvNotification.setOnClickListener(this);
        lnlBannerAds.setOnClickListener(this);
        lnlLocationAds.setOnClickListener(this);
        lnlNotificationAds.setOnClickListener(this);
        lnlManagerAds.setOnClickListener(this);
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
            case R.id.lnl_banner_ads:
                startActivity(new Intent(this, QuangCaoBannerTuyenDungActivity.class));
                break;
            case R.id.lnl_location_ads:
                startActivity(new Intent(this, QuangCaoKhuVucTuyenDungActivity.class));
                break;
            case R.id.lnl_notification_ads:
                startActivity(new Intent(this, QuangCaoThongBaoTuyenDungActivity.class));
                break;
            case R.id.lnl_manager_ads:
                startActivity(new Intent(this, QuanLyHuneAdsTuyenDungActivity.class));
                break;
        }
    }
}
