package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.ui.ThongBaoTuyenDungActivity;
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

/**
 * Created by apple on 11/18/17.
 */

public class HunePayTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private ImageView imvNotification;
    private LinearLayout lnlBalance;
    private TextView tvBalance;
    private LinearLayout lnlActionCash;
    private TextView tvSalaryUnit;
    private ImageView imvRight;
    private LinearLayout lnlRecharge;
    private LinearLayout lnlWithdraw;
    private LinearLayout lnlHistory;
    private LinearLayout lnlHuneCoin;

    private Dialog dialogProgress;
    //TODO: Declaring
    SessionUser sessionUser;
    private JSONParser jsonParser = new JSONParser();
    UserDTO userDTO;
    Gson gson = new Gson();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 200) {
            if (data.getExtras() != null) {
                userDTO = gson.fromJson(data.getStringExtra("user"), UserDTO.class);
                tvBalance.setText(Utilities.NubmerFormatText(String.format("%.0f", userDTO.getBalance_cash())));
            }
        }
        if (requestCode == 102 && resultCode == 200) {
            ProfileServer profileServer = new ProfileServer();
            profileServer.execute(sessionUser.getUserDetails().getToken());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_hunepay_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvNotification = (ImageView) findViewById(R.id.imv_notification);
        lnlBalance = (LinearLayout) findViewById(R.id.lnl_balance);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        lnlActionCash = (LinearLayout) findViewById(R.id.lnl_action_cash);
        tvSalaryUnit = (TextView) findViewById(R.id.tv_salary_unit);
        imvRight = (ImageView) findViewById(R.id.imv_right);
        lnlRecharge = (LinearLayout) findViewById(R.id.lnl_recharge);
        lnlWithdraw = (LinearLayout) findViewById(R.id.lnl_withdraw);
        lnlHistory = (LinearLayout) findViewById(R.id.lnl_history);
        lnlHuneCoin = (LinearLayout) findViewById(R.id.lnl_hune_coin);

        imvBack.setOnClickListener(this);
        imvNotification.setOnClickListener(this);
        lnlRecharge.setOnClickListener(this);
        lnlBalance.setOnClickListener(this);
        lnlHistory.setOnClickListener(this);
        lnlHuneCoin.setOnClickListener(this);
        lnlWithdraw.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);

        ProfileServer profileServer = new ProfileServer();
        profileServer.execute(sessionUser.getUserDetails().getToken());
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
            case R.id.lnl_recharge:
                startActivityForResult(new Intent(this, NapCashTuyenDungActivity.class), 102);
                break;
            case R.id.lnl_hune_coin:
                startActivityForResult(new Intent(this, HuneCoinTuyenDungActivity.class), 101);
                break;
            case R.id.lnl_history:
                startActivity(new Intent(this, LichSuGiaoDichCashTuyenDungActivity.class));
                break;
            case R.id.lnl_withdraw:
                startActivityForResult(new Intent(this, RutTienTuyenDungActivity.class), 101);
                break;
            case R.id.lnl_balance:
                if (lnlActionCash.getVisibility() == View.GONE) {
                    imvRight.setImageResource(R.drawable.ic_arrow_down);
                    lnlActionCash.setVisibility(View.VISIBLE);
                } else {
                    lnlActionCash.setVisibility(View.GONE);
                    imvRight.setImageResource(R.drawable.ic_arrow_up);
                }
                break;
        }
    }

    private class ProfileServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_TOKEN, urls[0]));
//                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_USER_ID, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_PROFILE, "GET", params);

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
                Log.e("RESULT + PROFILE", result);
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        userDTO = new UserDTO();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA));
                            userDTO.setId(jsondata.getInt(Common.JsonKey.KEY_USER_ID));

                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_TOTAL_CASH)) {
                                userDTO.setTotal_cash(jsondata.getDouble(Common.JsonKey.KEY_USER_TOTAL_CASH));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_BALANCE_CASH)) {
                                userDTO.setBalance_cash(jsondata.getDouble(Common.JsonKey.KEY_USER_BALANCE_CASH));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_TOTAL_COIN)) {
                                userDTO.setTotal_coin(jsondata.getDouble(Common.JsonKey.KEY_USER_TOTAL_COIN));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_BALANCE_COIN)) {
                                userDTO.setBalance_coin(jsondata.getDouble(Common.JsonKey.KEY_USER_BALANCE_COIN));
                            }
                        }
                        tvBalance.setText(Utilities.NubmerFormatText(String.format("%.0f", userDTO.getBalance_cash())));
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

}
