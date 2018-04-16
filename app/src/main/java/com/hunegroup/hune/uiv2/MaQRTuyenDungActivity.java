package com.hunegroup.hune.uiv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.Branch;
import com.hunegroup.hune.util.CapturePhotoUtils;

import net.glxn.qrgen.android.QRCode;

/**
 * Created by apple on 1/9/18.
 */

public class MaQRTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private TextView tvName;
    private ImageView imvQrCode;
    private ImageView imvDownload;

    //TODO: Declaring
    Gson gson = new Gson();
    Branch branch;
    Bitmap bitmapQR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_viewqrcode_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tvName = (TextView) findViewById(R.id.tv_name);
        imvQrCode = (ImageView) findViewById(R.id.imv_qr_code);
        imvDownload = (ImageView) findViewById(R.id.imv_download);

        imvDownload.setOnClickListener(this);
        imvBack.setOnClickListener(this);
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            branch = gson.fromJson(getIntent().getStringExtra("branch"), Branch.class);
            if (branch.getExtra() != null) {
                tvName.setText(branch.getName());
                bitmapQR = QRCode.from(branch.getExtra()).withSize((int) getResources().getDimension(R.dimen.size_200), (int) getResources().getDimension(R.dimen.size_200)).bitmap();
                imvQrCode.setImageBitmap(bitmapQR);

            }
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.imv_download:
                if (bitmapQR != null) {
                    String path = CapturePhotoUtils.insertImage(getContentResolver(), bitmapQR, branch.getName(), "QR CODE");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(path))); /** replace with your own uri */
                }
                break;
        }
    }
}
