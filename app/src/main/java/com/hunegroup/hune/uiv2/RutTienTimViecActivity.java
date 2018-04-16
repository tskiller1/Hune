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
 * Created by apple on 11/20/17.
 */

public class RutTienTimViecActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private ImageView imvBack;
    private TextView tvBalance;
    private TextView tvSalaryUnit;
    private EditText edBalance;
    private TextView tvValue100000;
    private TextView tvValue200000;
    private TextView tvValue500000;
    private TextView tvValue1000000;
    private Button btnConfirm;


    private Dialog dialogProgress;
    //TODO: Declaring
    SessionUser sessionUser;
    private JSONParser jsonParser = new JSONParser();
    UserDTO userDTO;
    Gson gson = new Gson();
    int value = 0;
    private boolean hasFractionalPart;
    private DecimalFormat df = new DecimalFormat("#,###.##");
    private DecimalFormat dfnd = new DecimalFormat("#,###");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_ruttien_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvSalaryUnit = (TextView) findViewById(R.id.tv_salary_unit);
        edBalance = (EditText) findViewById(R.id.ed_balance);
        tvValue100000 = (TextView) findViewById(R.id.tv_value_100000);
        tvValue200000 = (TextView) findViewById(R.id.tv_value_200000);
        tvValue500000 = (TextView) findViewById(R.id.tv_value_500000);
        tvValue1000000 = (TextView) findViewById(R.id.tv_value_1000000);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        edBalance.addTextChangedListener(this);

        imvBack.setOnClickListener(this);
        tvValue100000.setOnClickListener(this);
        tvValue200000.setOnClickListener(this);
        tvValue500000.setOnClickListener(this);
        tvValue1000000.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.tv_value_100000:
                edBalance.setText(getString(R.string.value_100000));
                break;
            case R.id.tv_value_200000:
                edBalance.setText(getString(R.string.value_200000));
                break;
            case R.id.tv_value_500000:
                edBalance.setText(getString(R.string.value_500000));
                break;
            case R.id.tv_value_1000000:
                edBalance.setText(getString(R.string.value_1000000));
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(edBalance.getText()) && Integer.parseInt(edBalance.getText().toString()) != 0) {
                    WithdrawServer withdrawServer = new WithdrawServer();
                    withdrawServer.execute(sessionUser.getUserDetails().getToken(), edBalance.getText().toString().replace(".", ""));
                }
                break;
        }
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);

        ProfileServer profileServer = new ProfileServer();
        profileServer.execute(sessionUser.getUserDetails().getToken());
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

    private class WithdrawServer extends AsyncTask<String, Void, String> {

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
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_WALLET_WITHDRAW_CASH, "POST", params);

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
                        DialogUtils.dialogSuccessTimViec(RutTienTimViecActivity.this, getString(R.string.contact_with_you));
                    } else {
                        Toast.makeText(RutTienTimViecActivity.this, jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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
