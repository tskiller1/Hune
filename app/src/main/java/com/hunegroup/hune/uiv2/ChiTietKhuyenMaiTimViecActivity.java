package com.hunegroup.hune.uiv2;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.Branch;
import com.hunegroup.hune.dto.PostAdsNotification;
import com.hunegroup.hune.dto.Promotion;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Validate;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.hunegroup.hune.util.Common.RequestCode.CAMERA;

/**
 * Created by apple on 12/14/17.
 */

public class ChiTietKhuyenMaiTimViecActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private ImageView imvLogo;
    private TextView tvTitle;
    private TextView tvTime;
    private LinearLayout lnlBranch;
    private TextView tvNumCoupon;
    private TextView tvDescription;
    private Button btnUse;
    private LinearLayout lnlUsed;
    private TextView tvUsed;

    private Dialog dialogProgress;
    private Dialog dialogMessage;

    //TODO: Declaring
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    Gson gson = new Gson();
    Promotion promotion;
    PostAdsNotification postAdsNotification;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    /* todo : xu ly khi ket qua khi scand qrcode thanh cong */
                    UsePromtionServer usePromtionServer = new UsePromtionServer();
                    usePromtionServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(promotion.getId()), result.getContents());
                }
            } else {

                super.onActivityResult(requestCode, resultCode, data);
            }


        } catch (Exception ignored) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == CAMERA) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                scanBarcode();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_chitietkhuyenmai_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvLogo = (ImageView) findViewById(R.id.imv_logo);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTime = (TextView) findViewById(R.id.tv_time);
        lnlBranch = (LinearLayout) findViewById(R.id.lnl_branch);
        tvNumCoupon = (TextView) findViewById(R.id.tv_num_coupon);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        btnUse = (Button) findViewById(R.id.btn_use);
        lnlUsed = (LinearLayout) findViewById(R.id.lnl_used);
        tvUsed = (TextView) findViewById(R.id.tv_used);

        imvBack.setOnClickListener(this);
        btnUse.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        if (getIntent().getExtras() != null) {
            int id = getIntent().getIntExtra("ads_id", 0);
            if (id != 0) {
                GetPromotionServer getPromotionServer = new GetPromotionServer();
                getPromotionServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(id));
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void updateView() {
        if (promotion != null && promotion.getId() != 0) {
            postAdsNotification = promotion.getAds();
            if (promotion.getUsed_at() != null) {
                btnUse.setVisibility(View.GONE);
                lnlUsed.setVisibility(View.VISIBLE);
                String used_at = getString(R.string.used_at) + "\n" + Validate.StringDateFormatToSetText3(promotion.getUsed_at());
                tvUsed.setText(used_at);
            } else {
                btnUse.setVisibility(View.VISIBLE);
                lnlUsed.setVisibility(View.GONE);
            }
            Glide.with(this).load(postAdsNotification.getLogo()).into(imvLogo);
            tvTitle.setText(postAdsNotification.getTitle());
            String time = Validate.StringDateFormatToSetText(postAdsNotification.getStart_date()) + " " + getString(R.string.to) + " " + Validate.StringDateFormatToSetText(postAdsNotification.getEnd_date());
            tvTime.setText(time);
            tvDescription.setText(postAdsNotification.getDescription());
            String numdiscount = postAdsNotification.getTotal_coupon() + " " + getString(R.string.promotion_code);
            tvNumCoupon.setText(numdiscount);
            if (postAdsNotification.getAds_branch() != null && postAdsNotification.getAds_branch().size() > 0) {
                for (int i = 0; i < postAdsNotification.getAds_branch().size(); i++) {
                    View view = LayoutInflater.from(this).inflate(R.layout.item_v2_branch, null);
                    TextView tvName = (TextView) view.findViewById(R.id.tv_branch_name);
                    tvName.setText(postAdsNotification.getAds_branch().get(i).getName());
                    lnlBranch.addView(view);
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.btn_use:
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    List<String> permissions = new ArrayList<String>();
                    permissions.add(Manifest.permission.CAMERA);
                    requestPermissions(permissions.toArray(new String[permissions.size()]), CAMERA);
                    return;
                }
                scanBarcode();
                break;
        }
    }

    /* todo : Qrcode */
    public void scanBarcode() {
        try {
            new IntentIntegrator((Activity) this).initiateScan();
        } catch (Exception e) {

        }

    }


    private class GetPromotionServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            dialogProgress.show();
        }

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                if (Build.VERSION.SDK_INT < 11) {
                    Thread.sleep(1000);
                }
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_TOKEN, urls[0]));

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_ADS_PROMOTION + urls[1], "GET", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {

            if (getBaseContext() == null)
                return;
            if (result != null) {
                try {
                    //sessionUser
                    Log.e("GET COUPON", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {

                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONObject jsonData = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA);
                            promotion = gson.fromJson(jsonData.toString(), Promotion.class);
                            updateView();
                        }
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }
    }

    private class UsePromtionServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            dialogProgress.show();
        }

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                if (Build.VERSION.SDK_INT < 11) {
                    Thread.sleep(1000);
                }
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_COUPON_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_BRANCH_KEY, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_ADS_PROMOTION + urls[1], "POST", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    //sessionUser
                    Log.e("use promotion ", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        JSONObject jsonData = jsonResult.getJSONObject("data");
                        promotion.setUsed_at(jsonData.getString("used_at"));
                        promotion.setBranch(gson.fromJson(jsonData.getJSONObject("branch").toString(), Branch.class));
                        updateView();
                        dialogMessage = DialogUtils.dialogSuccessTimViec(ChiTietKhuyenMaiTimViecActivity.this, getString(R.string.use_coupon_success));
                        if (!jsonResult.isNull(Common.JsonKey.KEY_MESSAGE)) {
                            Toast.makeText(ChiTietKhuyenMaiTimViecActivity.this, jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }


}
