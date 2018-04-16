package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.PostAdsNotification;
import com.hunegroup.hune.ui.BanDoActivity;
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

/**
 * Created by apple on 12/7/17.
 */

public class QuangCaoThongBaoStep2TimViecActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView imvBack;
    private RelativeLayout rlSelectAddress;
    private TextView tvSelectAddress;
    private CheckBox ckbMale;
    private CheckBox ckbFemale;
    private CheckBox ckbAll;
    private TextView tvRecruitment;
    private TextView tvFindJob;
    private Button btnContinue;

    private Dialog dialogProgress;

    //TODO:Declaring
    private PostAdsNotification request;

    List<Integer> genders;
    List<Integer> locations;
    boolean isRecruiment = false;
    boolean isFindJob = false;
    Gson gson = new Gson();
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == BanDoActivity.KEY_CODE) {
            if (data.getExtras() != null) {
                tvSelectAddress.setText(data.getStringExtra("diadiem"));
                request.setLat(data.getDoubleExtra("lat", 0));
                request.setLog(data.getDoubleExtra("log", 0));
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_quangcaothongbao_step2_timviec);
        findViews();
        initView();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        rlSelectAddress = (RelativeLayout) findViewById(R.id.rl_select_address);
        tvSelectAddress = (TextView) findViewById(R.id.tv_select_address);
        ckbMale = (CheckBox) findViewById(R.id.ckb_male);
        ckbFemale = (CheckBox) findViewById(R.id.ckb_female);
        ckbAll = (CheckBox) findViewById(R.id.ckb_all);
        tvRecruitment = (TextView) findViewById(R.id.tv_recruitment);
        tvFindJob = (TextView) findViewById(R.id.tv_find_job);
        btnContinue = (Button) findViewById(R.id.btn_continue);

        ckbMale.setOnClickListener(this);
        ckbFemale.setOnClickListener(this);
        ckbAll.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        tvSelectAddress.setOnClickListener(this);
        tvRecruitment.setOnClickListener(this);
        tvFindJob.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    private void initView() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        genders = new ArrayList<>();
        locations = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            request = gson.fromJson(getIntent().getStringExtra("postnotification"), PostAdsNotification.class);
            if (request == null) {
                finish();
            }
        } else {
            finish();
        }
//        request = new PostAdsNotification();
        ckbAll.setChecked(true);
        genders.add(1);
        genders.add(2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.ckb_male:
                genders.clear();
                genders.add(1);
                ckbMale.setChecked(true);
                ckbFemale.setChecked(false);
                ckbAll.setChecked(false);
                break;
            case R.id.ckb_female:
                genders.clear();
                genders.add(2);
                ckbMale.setChecked(false);
                ckbFemale.setChecked(true);
                ckbAll.setChecked(false);
                break;
            case R.id.ckb_all:
                genders.clear();
                genders.add(1);
                genders.add(2);
                ckbMale.setChecked(false);
                ckbFemale.setChecked(false);
                ckbAll.setChecked(true);
                break;
            case R.id.tv_recruitment:
                if (isRecruiment) {
                    locations.remove(locations.indexOf(1));
                    isRecruiment = false;
                    tvRecruitment.setBackgroundResource(R.drawable.custom_edittext_danhgia_timviec);
                    tvRecruitment.setTextColor(getResources().getColor(R.color.color_main));
                } else {
                    locations.add(1);
                    isRecruiment = true;
                    tvRecruitment.setBackgroundResource(R.drawable.custom_button_danhgia_timviec);
                    tvRecruitment.setTextColor(getResources().getColor(R.color.colorTextWhite));
                }
                break;
            case R.id.tv_find_job:
                if (isFindJob) {
                    locations.remove(locations.indexOf(2));
                    isFindJob = false;
                    tvFindJob.setBackgroundResource(R.drawable.custom_edittext_danhgia_timviec);
                    tvFindJob.setTextColor(getResources().getColor(R.color.color_main));
                } else {
                    locations.add(2);
                    isFindJob = true;
                    tvFindJob.setBackgroundResource(R.drawable.custom_button_danhgia_timviec);
                    tvFindJob.setTextColor(getResources().getColor(R.color.colorTextWhite));
                }
                break;
            case R.id.tv_select_address:
                Intent intent = new Intent(this, BanDoActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_continue:
                if (request.getLat() != 0
                        && request.getLog() != 0
                        && genders.size() != 0
                        && locations.size() != 0) {
                    request.setGender(genders);
                    request.setLocation(locations);

                    PostNotifcationAdsServer postNotifcationAdsServer = new PostNotifcationAdsServer();
                    postNotifcationAdsServer.execute(
                            sessionUser.getUserDetails().getToken(),
                            request.getTitle(),
                            request.getDescription(),
                            gson.toJson(request.getBranch()),
                            request.getStart_hour(),
                            request.getEnd_hour(),
                            Validate.StringDateFormatToUploadData(request.getStart_date()),
                            Validate.StringDateFormatToUploadData(request.getEnd_date()),
                            String.valueOf(request.getTotal_coupon()),
                            String.valueOf(request.getPrice()),
                            String.valueOf(request.getDiscount()),
                            request.getLogo(),
                            request.getCover(),
                            String.valueOf(request.getLat()),
                            String.valueOf(request.getLog()),
                            gson.toJson(request.getGender()),
                            gson.toJson(request.getLocation())
                    );
                } else {
                    Toast.makeText(this, getString(R.string.toast_vuilongnhapthongtin), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class PostNotifcationAdsServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_NAME, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_DESCRIPTION, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_BRANCH, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_START_HOUR, urls[4]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_END_HOUR, urls[5]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_START_DATE, urls[6]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_END_DATE, urls[7]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TOTAL_COUPON, urls[8]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_DISCOUNT, urls[9]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_PRICE, urls[10]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_LOGO, urls[11]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_COVER, urls[12]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_LAT, urls[13]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_LONG, urls[14]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_GENDER, urls[15]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_LOCATION, urls[16]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_ADS_PROMOTION, "POST", params);

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
                    Log.e("Create Notification Ads", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        PostAdsNotification postAdsNotification = gson.fromJson(jsonResult.getString("data"),PostAdsNotification.class);
                        Intent intent = new Intent(QuangCaoThongBaoStep2TimViecActivity.this, ThanhToanAdsTimViecActivity.class);
                        intent.putExtra("id",postAdsNotification.getId());
                        intent.putExtra("amount",postAdsNotification.getAmount());
                        startActivity(intent);
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
