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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by apple on 12/13/17.
 */

public class QuangCaoKhuVucStep2TuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView imvBack;
    private TextView tvSelectAddress;
    private CheckBox ckbMale;
    private CheckBox ckbFemale;
    private CheckBox ckbAll;
    private LinearLayout lnlNumDay;
    private TextView tvSelectNumDay;
    private Button btnContinue;

    private Dialog dialogProgress;

    //TODO: Declaring
    Gson gson = new Gson();
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();

    List<Date> selectDateList;
    List<String> dateList;
    List<Integer> genders;
    private PostAdsNotification request;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 200) {
            if (data.getExtras() != null) {
                Type listType = new TypeToken<ArrayList<Date>>() {
                }.getType();
                selectDateList = gson.fromJson(data.getStringExtra("days"), listType);
                if (selectDateList != null && selectDateList.size() != 0) {
                    tvSelectNumDay.setText("Đã chọn" + " " + selectDateList.size() + " " + "ngày");
                    dateList = new ArrayList<>();
                    for (Date date : selectDateList) {
                        dateList.add(Validate.DateToStringUpload(date));
                    }
                }
            }
        }
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
        setContentView(R.layout.activity_v2_quangcaokhuvuc_step2_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tvSelectAddress = (TextView) findViewById(R.id.tv_select_address);
        ckbMale = (CheckBox) findViewById(R.id.ckb_male);
        ckbFemale = (CheckBox) findViewById(R.id.ckb_female);
        ckbAll = (CheckBox) findViewById(R.id.ckb_all);
        lnlNumDay = (LinearLayout) findViewById(R.id.lnl_num_day);
        tvSelectNumDay = (TextView) findViewById(R.id.tv_select_num_day);
        btnContinue = (Button) findViewById(R.id.btn_continue);

        ckbMale.setOnClickListener(this);
        ckbFemale.setOnClickListener(this);
        ckbAll.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        tvSelectAddress.setOnClickListener(this);
        tvSelectNumDay.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        genders = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            request = gson.fromJson(getIntent().getStringExtra("postnotification"), PostAdsNotification.class);
            if (request == null) {
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
            case R.id.tv_select_address:
                Intent intent = new Intent(this, BanDoActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_select_num_day:
                startActivityForResult(new Intent(this, ChonNgayTuyenDungActivity.class), 101);
                break;
            case R.id.btn_continue:
                if (request.getLat() != 0
                        && request.getLog() != 0
                        && genders.size() != 0
                        && (dateList != null && dateList.size() != 0)) {
                    request.setGender(genders);
                    request.setDates(dateList);
                    PostAdsServer postAdsServer = new PostAdsServer();
                    postAdsServer.execute(sessionUser.getUserDetails().getToken(),
                            request.getTitle(), request.getDescription(),
                            gson.toJson(request.getBranch()),
                            request.getLogo(),
                            String.valueOf(request.getLat()),
                            String.valueOf(request.getLog()),
                            gson.toJson(request.getGender()),
                            gson.toJson(request.getDates()
                            ));
                } else {
                    Toast.makeText(this, getString(R.string.toast_vuilongnhapthongtin), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class PostAdsServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_LOGO, urls[4]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_LAT, urls[5]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_LONG, urls[6]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_GENDER, urls[7]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_DATES, urls[8]));

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_ADS_LOCATION, "POST", params);

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
                        PostAdsNotification postAdsNotification = gson.fromJson(jsonResult.getString("data"), PostAdsNotification.class);
                        Intent intent = new Intent(QuangCaoKhuVucStep2TuyenDungActivity.this, ThanhToanAdsTuyenDungActivity.class);
                        intent.putExtra("id", postAdsNotification.getId());
                        intent.putExtra("amount", postAdsNotification.getAmount());
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
