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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.TaskDTO;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.ui.DanhGiaTuyenDungActivity;
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
 * Created by apple on 11/27/17.
 */

public class ThanhToanTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView imvBack;
    private EditText edBalance;
    private CheckBox ckbHuneCash;
    private CheckBox ckbMoney;
    private Button btnPayment;

    private Dialog dialogProgress;

    //TODO: Declaring
    Gson gson = new Gson();
    TaskDTO taskDTO;
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    UserDTO userDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_thanhtoan_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        edBalance = (EditText) findViewById(R.id.ed_balance);
        ckbHuneCash = (CheckBox) findViewById(R.id.ckb_hune_cash);
        ckbMoney = (CheckBox) findViewById(R.id.ckb_money);
        btnPayment = (Button) findViewById(R.id.btn_payment);

        ckbHuneCash.setOnClickListener(this);
        ckbMoney.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        if (getIntent().getExtras() != null) {
            taskDTO = gson.fromJson(getIntent().getStringExtra(Common.IntentKey.KEY_TASK), TaskDTO.class);
            if (taskDTO != null && taskDTO.getId() != 0) {
                edBalance.setText(String.format("%.0f", taskDTO.getAmount()));
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.ckb_hune_cash:
                ckbHuneCash.setChecked(true);
                ckbMoney.setChecked(false);
                break;
            case R.id.ckb_money:
                ckbMoney.setChecked(true);
                ckbHuneCash.setChecked(false);
                break;
            case R.id.btn_payment:
                int type = 0;
                if (ckbHuneCash.isChecked()) {
                    type = 1;
                } else if (ckbMoney.isChecked()) {
                    type = 2;
                } else {
                    Toast.makeText(this, "Vui lòng chọn phương thức thanh toán !", Toast.LENGTH_SHORT).show();
                    break;
                }
                PaymentServer paymentServer = new PaymentServer();
                paymentServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(taskDTO.getId()), String.valueOf(type));
                break;
        }
    }

    private class PaymentServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_PAYMENT_TYPE, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_TASK + "/" + urls[1] + "/pay", "POST", params);

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
                Log.e("PUT TASK", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        JSONObject jsonData = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA);
                        if (!jsonData.isNull(Common.JsonKey.KEY_TASK_STATUS)) {
                            taskDTO.setStatus(jsonData.getInt(Common.JsonKey.KEY_TASK_STATUS));
                        }
                        if (!jsonData.isNull(Common.JsonKey.KEY_TASK_STATUS_PAYMENT)) {
                            taskDTO.setStatus_payment(jsonData.getInt(Common.JsonKey.KEY_TASK_STATUS_PAYMENT));
                        }
                        Toast.makeText(ThanhToanTuyenDungActivity.this, R.string.payment_success, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ThanhToanTuyenDungActivity.this, DanhGiaTuyenDungActivity.class);
                        startActivity(intent);
                        Intent iResult = new Intent();
                        iResult.putExtra("task", gson.toJson(taskDTO));
                        setResult(200, iResult);
                        finish();
                    } else {
                        DialogUtils.dialogError(ThanhToanTuyenDungActivity.this, getString(R.string.not_enough_payment));
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
