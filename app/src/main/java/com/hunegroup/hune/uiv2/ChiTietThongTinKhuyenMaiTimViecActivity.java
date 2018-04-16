package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.ThongTinKhuyenMai;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

/**
 * Created by apple on 12/13/17.
 */

public class ChiTietThongTinKhuyenMaiTimViecActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private ImageView imvLogo;
    private TextView tvTitle;
    private LinearLayout lnlBranch;
    private TextView tvDescription;

    private Dialog dialogProgress;

    //TODO: Declaring
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    Gson gson = new Gson();

    ThongTinKhuyenMai item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_chitiet_thongtinkhuyenmai_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvLogo = (ImageView) findViewById(R.id.imv_logo);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        lnlBranch = (LinearLayout) findViewById(R.id.lnl_branch);
        tvDescription = (TextView) findViewById(R.id.tv_description);

        imvBack.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        if (getIntent().getExtras() != null) {
            item = gson.fromJson(getIntent().getStringExtra("item"), ThongTinKhuyenMai.class);
            if (item != null && item.getId() != 0) {
                Glide.with(this).load(item.getLogo()).into(imvLogo);
                tvTitle.setText(item.getName());
                tvDescription.setText(item.getDescription());
                for (int i = 0; i < item.getAds_branch().size(); i++) {
                    View view = LayoutInflater.from(this).inflate(R.layout.item_v2_branch, null);
                    TextView tvName = (TextView) view.findViewById(R.id.tv_branch_name);
                    tvName.setText(item.getAds_branch().get(i).getName());
                    lnlBranch.addView(view);
                }
            } else {
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
        }
    }

}
