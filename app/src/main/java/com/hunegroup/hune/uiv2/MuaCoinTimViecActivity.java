package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 11/18/17.
 */

public class MuaCoinTimViecActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private ImageView imvBack;
    private TextView tvBalance;
    private TextView tvSalaryUnit;
    private EditText edBalance;
    private LinearLayout lnl100coin;
    private LinearLayout lnl200coin;
    private LinearLayout lnl500coin;
    private LinearLayout lnl1000coin;
    private Button btnConfirm;

    private Dialog dialogProgress;
    //TODO: Declaring
    SessionUser sessionUser;
    private JSONParser jsonParser = new JSONParser();
    UserDTO userDTO;
    Gson gson = new Gson();
    private boolean hasFractionalPart;
    private DecimalFormat df = new DecimalFormat("#,###.##");
    private DecimalFormat dfnd = new DecimalFormat("#,###");
    int value = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_muacoin_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvSalaryUnit = (TextView) findViewById(R.id.tv_salary_unit);
        edBalance = (EditText) findViewById(R.id.ed_balance);
        lnl100coin = (LinearLayout) findViewById(R.id.lnl_100coin);
        lnl200coin = (LinearLayout) findViewById(R.id.lnl_200coin);
        lnl500coin = (LinearLayout) findViewById(R.id.lnl_500coin);
        lnl1000coin = (LinearLayout) findViewById(R.id.lnl_1000coin);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        edBalance.addTextChangedListener(this);

        imvBack.setOnClickListener(this);
        lnl100coin.setOnClickListener(this);
        lnl200coin.setOnClickListener(this);
        lnl500coin.setOnClickListener(this);
        lnl1000coin.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
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
            case R.id.lnl_100coin:
                edBalance.setText(getString(R.string.value_100));
                break;
            case R.id.lnl_200coin:
                edBalance.setText(getString(R.string.value_200));
                break;
            case R.id.lnl_500coin:
                edBalance.setText(getString(R.string.value_500));
                break;
            case R.id.lnl_1000coin:
                edBalance.setText(getString(R.string.value_1000));
                break;
            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(edBalance.getText())) {
                    DepositServer depositServer = new DepositServer();
                    depositServer.execute(sessionUser.getUserDetails().getToken(), edBalance.getText().toString().replace(".", ""));
                } else {
                    Toast.makeText(this, R.string.coinbanmuamua, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        edBalance.removeTextChangedListener(this);
        try {
            int inilen, endlen;
            inilen = edBalance.getText().length();

            String v = editable.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = df.parse(v);
            int cp = edBalance.getSelectionStart();
            if (hasFractionalPart) {
                edBalance.setText(df.format(n));
            } else {
                edBalance.setText(dfnd.format(n));
            }
            endlen = edBalance.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= edBalance.getText().length()) {
                edBalance.setSelection(sel);
            } else {
                // place cursor at the end?
                edBalance.setSelection(edBalance.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        edBalance.addTextChangedListener(this);
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

    private class DepositServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_WALLET_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_WALLET_AMOUNT, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_WALLET_DEPOSIT_COIN, "POST", params);

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
                Log.e("RESULT + BUY COIN", result);
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        userDTO = new UserDTO();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
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
                            Intent intent = new Intent();
                            intent.putExtra("user", gson.toJson(userDTO));
                            setResult(200, intent);
                        }
                        DialogUtils.dialogSuccessTimViec(MuaCoinTimViecActivity.this, getString(R.string.buy_coin_success));
                    } else {
                        DialogUtils.dialogErrorTimViec(MuaCoinTimViecActivity.this, getString(R.string.not_enough_payment));
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
