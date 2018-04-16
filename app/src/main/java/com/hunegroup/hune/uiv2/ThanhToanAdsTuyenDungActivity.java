package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.ui.TimNguoiTuyenDungActivity;
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
 * Created by apple on 12/11/17.
 */

public class ThanhToanAdsTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView imvBack;
    private RelativeLayout rlAmount;
    private TextView tvAmount;
    private TextView tvUnit;
    private LinearLayout lnlHuneCash;
    private CheckBox ckbHuneCash;
    private EditText edCode;
    private TextView tvApply;
    private RelativeLayout rlDiscount;
    private TextView tvDiscount;
    private TextView tvUnitDiscount;
    private RelativeLayout rlTotal;
    private TextView tvTotal;
    private TextView tvUnitTotal;
    private Button btnPayment;

    private Dialog dialogProgress;
    private Dialog dialogQuestion;
    private Dialog dialogMessage;
    private Dialog dialogQuestionExit;
    //TODO: Declaring
    Gson gson = new Gson();
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    int id = 0;
    double amount = 0;
    double discount = 0;
    double total = 0;
    boolean isDiscount = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_thanhtoanads_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        rlAmount = (RelativeLayout) findViewById(R.id.rl_amount);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvUnit = (TextView) findViewById(R.id.tv_unit);
        lnlHuneCash = (LinearLayout) findViewById(R.id.lnl_hune_cash);
        ckbHuneCash = (CheckBox) findViewById(R.id.ckb_hune_cash);
        edCode = (EditText) findViewById(R.id.ed_code);
        tvApply = (TextView) findViewById(R.id.tv_apply);
        rlDiscount = (RelativeLayout) findViewById(R.id.rl_discount);
        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        tvUnitDiscount = (TextView) findViewById(R.id.tv_unit_discount);
        rlTotal = (RelativeLayout) findViewById(R.id.rl_total);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        tvUnitTotal = (TextView) findViewById(R.id.tv_unit_total);
        btnPayment = (Button) findViewById(R.id.btn_payment);

        imvBack.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        ckbHuneCash.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        Log.e("token", sessionUser.getUserDetails().getToken());
        ckbHuneCash.setChecked(true);
        if (getIntent().getExtras() != null) {
            amount = getIntent().getDoubleExtra("amount", 0);
            id = getIntent().getIntExtra("id", 0);
            if (id == 0) {
                finish();
            }
        } else {
            finish();
        }
        calculate();
    }

    private void onBack() {
        dialogQuestionExit = DialogUtils.dialogQuestion(this, getString(R.string.notpayment_wantexit), new DialogUtils.DialogCallBack() {
            @Override
            public void onYesClickListener() {
                dialogQuestionExit.dismiss();
                Intent intent = new Intent(ThanhToanAdsTuyenDungActivity.this, HuneAdsTuyenDungActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelClickListener() {
                dialogQuestionExit.dismiss();
            }
        });
    }

    private void calculate() {
        total = amount - discount;
        if (total < 0) {
            total = 0;
        }
        tvAmount.setText(Utilities.NubmerFormatText(String.format("%.0f", amount)));
        tvDiscount.setText(Utilities.NubmerFormatText(String.format("%.0f", discount)));
        tvTotal.setText(Utilities.NubmerFormatText(String.format("%.0f", total)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                onBack();
                break;
            case R.id.ckb_hune_cash:
                ckbHuneCash.setChecked(true);
                break;
            case R.id.tv_apply:
                if (isDiscount) {
                    edCode.setEnabled(true);
                    isDiscount = false;
                    tvApply.setText(getString(R.string.apply));
                } else {
                    if (!TextUtils.isEmpty(edCode.getText())) {
                        DiscountServer discountServer = new DiscountServer();
                        discountServer.execute(sessionUser.getUserDetails().getToken(), edCode.getText().toString());
                    } else {
                        Toast.makeText(this, R.string.please_enter_code, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_payment:
                dialogQuestion = DialogUtils.dialogQuestion(this, getString(R.string.confirm_payment), new DialogUtils.DialogCallBack() {
                    @Override
                    public void onYesClickListener() {
                        PaymentServer paymentServer = new PaymentServer();
                        if (isDiscount) {
                            paymentServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(id), edCode.getText().toString());
                        } else {
                            paymentServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(id));
                        }
                        dialogQuestion.dismiss();
                    }

                    @Override
                    public void onCancelClickListener() {
                        dialogQuestion.dismiss();
                    }
                });
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_ID, urls[1]));
                if (urls.length == 3) {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_COUPON, urls[2]));
                }
                Log.e("pay", Common.RequestURL.API_ADS_PAY + "/" + urls[1]);
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_ADS_PAY + "/" + urls[1], "POST", params);

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
                        dialogMessage = DialogUtils.dialogSuccess(ThanhToanAdsTuyenDungActivity.this, getString(R.string.pay_success), new DialogUtils.DialogCallBack() {
                            @Override
                            public void onYesClickListener() {
                                Intent intent = new Intent(ThanhToanAdsTuyenDungActivity.this, TimNguoiTuyenDungActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                dialogMessage.dismiss();
                            }

                            @Override
                            public void onCancelClickListener() {

                            }
                        });
                    } else {
                        dialogMessage = DialogUtils.dialogError(ThanhToanAdsTuyenDungActivity.this, getString(R.string.not_enough_payment));
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

    private class DiscountServer extends AsyncTask<String, Void, String> {

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

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_COUPON + "/" + urls[1], "GET", params);

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
                        JSONObject jsonData = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA);
                        if (jsonData.getInt("for_ads") == 1) {
                            isDiscount = true;
                            tvApply.setText(R.string.edit);
                            edCode.setEnabled(false);
                            if (jsonData.getInt("type_discount") == 1) {
                                discount = jsonData.getDouble("amount");
                            } else {
                                discount = amount * jsonData.getDouble("amount") / 100;
                            }
                            calculate();
                        } else {
                            Toast.makeText(ThanhToanAdsTuyenDungActivity.this, R.string.code_is_not_found, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ThanhToanAdsTuyenDungActivity.this, R.string.code_is_not_found, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

    @Override
    public void onBackPressed() {
        onBack();
    }
}
