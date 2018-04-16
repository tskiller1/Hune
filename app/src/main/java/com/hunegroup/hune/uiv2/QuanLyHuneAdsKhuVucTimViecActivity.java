package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.Ads;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by apple on 12/21/17.
 */

public class QuanLyHuneAdsKhuVucTimViecActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imvBack;
    private ImageView imvLogo;
    private TextView tvTitle;
    private LinearLayout lnlBranch;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvSex;
    private TextView tvDateAds;
    private TextView tvPrice;
    private TextView tvUnit;
    private ImageView imvStatus;
    private TextView tvStatus;

    private Dialog dialogProgress;

    //TODO: Declaring
    Gson gson = new Gson();
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();

    Ads item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_quanlyhuneads_khuvuc_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvLogo = (ImageView) findViewById(R.id.imv_logo);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        lnlBranch = (LinearLayout) findViewById(R.id.lnl_branch);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvDateAds = (TextView) findViewById(R.id.tv_date_ads);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvUnit = (TextView) findViewById(R.id.tv_unit);
        imvStatus = (ImageView) findViewById(R.id.imv_status);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        imvBack.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        if (getIntent().getExtras() != null) {
            int id = getIntent().getIntExtra("id_ads", 0);
            if (id != 0) {
                AdsServer server = new AdsServer();
                server.execute(sessionUser.getUserDetails().getToken(), String.valueOf(id));
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    public void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                tvLocation.setText(returnedAddress.getAddressLine(0));
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }

    }

    private void updateView() {
        getCompleteAddressString(item.getLat(), item.getLog());
        Glide.with(this).load(item.getLogo()).into(imvLogo);
        tvTitle.setText(item.getName());
        tvDescription.setText(item.getDescription());
        for (int i = 0; i < item.getAds_branch().size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_v2_branch, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_branch_name);
            tvName.setText(item.getAds_branch().get(i).getName());
            lnlBranch.addView(view);
        }
        if (item.getGender().size() == 2) {
            tvSex.setText(getString(R.string.all));
        } else {
            if (item.getGender().get(0) == 1) {
                tvSex.setText(getString(R.string.female));
            } else {
                tvSex.setText(getString(R.string.male));
            }
        }
        if (item.getDates() != null && item.getDates().size() > 0) {
            String dates = item.getDates().get(0);
            for (int i = 1; i < item.getDates().size(); i++) {
                dates += ",\n" + item.getDates().get(i);
            }
            tvDateAds.setText(dates);
        }
        if (item.getStatus() == 1) {
            imvStatus.setImageResource(R.mipmap.ic_waitting);
            tvStatus.setTextColor(getResources().getColor(R.color.colorText));
            tvStatus.setText(R.string.not_payment);
        } else if (item.getStatus() == 2) {
            imvStatus.setImageResource(R.mipmap.ic_finished);
            tvStatus.setTextColor(getResources().getColor(R.color.green));
            tvStatus.setText(R.string.paid);
        } else if (item.getStatus() == 3) {
            imvStatus.setImageResource(R.mipmap.ic_remove);
            tvStatus.setTextColor(getResources().getColor(R.color.colorRed));
            tvStatus.setText(R.string.reject);
        }
        tvPrice.setText(Utilities.NubmerFormatText(String.format("%.0f", item.getAmount())));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
        }
    }

    private class AdsServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TOKEN, urls[0]));

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_ADS + urls[1], "GET", params);

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
                Log.e("Discount Server", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
//                        JSONObject jsonData = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA);
                        item = gson.fromJson(jsonResult.getJSONObject(Common.JsonKey.KEY_DATA).toString(), Ads.class);
                        updateView();
                    } else {
                        finish();
//                        Toast.makeText(ThongTinKhuyenMaiTuyenDungActivity.this, "Mã giảm giá không hợp lệ!", Toast.LENGTH_SHORT).show();
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
