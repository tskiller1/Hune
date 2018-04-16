package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.CouponDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Utilities;
import com.hunegroup.hune.util.Validate;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 12/13/17.
 */

public class ChiTietCouponTimViecActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private ImageView imvLogo;
    private TextView tvTitle;
    private TextView tvPrice;
    private ImageView imvCoin;
    private TextView tvTime;
    private TextView tvDayDown;
    private TextView tvCountDown;
    private TextView tvAddress;
    private TextView tvNumCoupon;
    private TextView tvDescription;
    private Button btnBuy;

    private Dialog dialogMessage;
    Dialog dialogProgress;
    //TODO: Declaring
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    Gson gson = new Gson();
    CouponDTO couponDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_chitietcoupon_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvLogo = (ImageView) findViewById(R.id.imv_logo);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        imvCoin = (ImageView) findViewById(R.id.imv_coin);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDayDown = (TextView) findViewById(R.id.tv_day_down);
        tvCountDown = (TextView) findViewById(R.id.tv_count_down);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvNumCoupon = (TextView) findViewById(R.id.tv_num_coupon);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        btnBuy = (Button) findViewById(R.id.btn_buy);

        btnBuy.setOnClickListener(this);
        imvBack.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        if (getIntent().getExtras() != null) {
            couponDTO = gson.fromJson(getIntent().getStringExtra("item"), CouponDTO.class);
            if (couponDTO != null && couponDTO.getId() != 0) {
                GetCouponServer getCouponServer = new GetCouponServer();
                getCouponServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(couponDTO.getId()));
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void updateView() {
        Glide.with(this).load(couponDTO.getImage()).into(imvLogo);
        tvTitle.setText(couponDTO.getName());
        tvPrice.setText(Utilities.NubmerFormatText(String.format("%.0f", couponDTO.getPrice())));
        String time = Validate.StringDateFormatToSetText(couponDTO.getFrom_date()) + " " + getString(R.string.to) + " " + Validate.StringDateFormatToSetText(couponDTO.getTo_date());
        tvTime.setText(time);
        String numdown = getString(R.string.still) + " " + Utilities.getDifferenceFromTodayToDays(Validate.parseStringToDate(couponDTO.getTo_date())) + " " + getString(R.string.date);
        tvDayDown.setText(numdown);
        String countdown = getString(R.string.still) + " " + couponDTO.getCoupon_available_count() + " " + getString(R.string.coupon_code);
        tvCountDown.setText(countdown);
        String numcoupon = couponDTO.getCoupon_count() + " " + getString(R.string.coupon);
        tvNumCoupon.setText(numcoupon);
        tvDescription.setText(Html.fromHtml(couponDTO.getDescription_html()));
        tvAddress.setText(Html.fromHtml(couponDTO.getBranch_html()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.btn_buy:
                BuyCouponServer couponServer = new BuyCouponServer();
                couponServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(couponDTO.getId()));
                break;
        }
    }

    private class GetCouponServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            dialogProgress.show();
        }

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    Thread.sleep(1000);
                }
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_TOKEN, urls[0]));

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_COUPON_GROUP + urls[1], "GET", params);

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
                            couponDTO = gson.fromJson(jsonData.toString(), CouponDTO.class);
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

    private class BuyCouponServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    Thread.sleep(1000);
                }
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_COUPON_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_COUPON_ID, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_COUPON, "POST", params);

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
                    Log.e("Buy coupon ", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Intent intentResult = new Intent();
                        intentResult.putExtra("result", "result");
                        setResult(200, intentResult);
                        dialogMessage = DialogUtils.dialogSuccess(ChiTietCouponTimViecActivity.this, getString(R.string.buy_coupon_success), new DialogUtils.DialogCallBack() {
                            @Override
                            public void onYesClickListener() {
                                dialogMessage.dismiss();
                                GetCouponServer getCouponServer = new GetCouponServer();
                                getCouponServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(couponDTO.getId()));
                            }

                            @Override
                            public void onCancelClickListener() {

                            }
                        });
                    } else {
                        if (!jsonResult.isNull(Common.JsonKey.KEY_MESSAGE)) {
                            Toast.makeText(ChiTietCouponTimViecActivity.this, jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

}
