package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 1/17/18.
 */

public class DoiCoinTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private EditText edCode;
    private Button btnConfirm;

    private Dialog dialogProgress;

    //TODO: Declaring
    SessionUser sessionUser;
    private JSONParser jsonParser = new JSONParser();
    UserDTO userDTO;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_doicoin_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        edCode = (EditText) findViewById(R.id.ed_code);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        imvBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    private void initData() {
        sessionUser = new SessionUser(this);
        dialogProgress = DialogUtils.dialogProgress(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(edCode.getText())) {
                    DepositServer depositServer = new DepositServer();
                    depositServer.execute(sessionUser.getUserDetails().getToken(), edCode.getText().toString());
                }
                break;
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_CODE, urls[1]));
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
                            Intent intent = new Intent();
                            intent.putExtra("user", gson.toJson(userDTO));
                            setResult(200, intent);
                        }
                        DialogUtils.dialogSuccess(DoiCoinTuyenDungActivity.this, getString(R.string.exchange_coin_success));
                    } else {
                        DialogUtils.dialogError(DoiCoinTuyenDungActivity.this, jsonResult.getString(Common.JsonKey.KEY_MESSAGE));
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
