package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
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
import com.hunegroup.hune.dto.DepositCashDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 11/23/17.
 */

public class NapCashTuyenDungActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private ImageView imvBack;
    private EditText edAmount;
    private TextView tvValue100000;
    private TextView tvValue200000;
    private TextView tvValue500000;
    private TextView tvValue1000000;
    private Button btnConfirm;

    private Dialog dialogProgress;
    //TODO: Declaring
    SessionUser sessionUser;
    private JSONParser jsonParser = new JSONParser();
    DepositCashDTO depositCashDTO;
    Gson gson = new Gson();
    private boolean hasFractionalPart;
    private DecimalFormat df = new DecimalFormat("#,###.##");
    private DecimalFormat dfnd = new DecimalFormat("#,###");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == 200) {
                Toast.makeText(this, R.string.deposit_cash_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("result", "success");
                setResult(200, intent);
            }
            if (resultCode == 401) {
                Toast.makeText(this, data.getStringExtra("result") + getString(R.string.toast_dacoloixayra), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_napcash_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        edAmount = (EditText) findViewById(R.id.ed_amount);
        tvValue100000 = (TextView) findViewById(R.id.tv_value_100000);
        tvValue200000 = (TextView) findViewById(R.id.tv_value_200000);
        tvValue500000 = (TextView) findViewById(R.id.tv_value_500000);
        tvValue1000000 = (TextView) findViewById(R.id.tv_value_1000000);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        edAmount.addTextChangedListener(this);

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
                edAmount.setText(getString(R.string.value_100000));
                break;
            case R.id.tv_value_200000:
                edAmount.setText(getString(R.string.value_200000));
                break;
            case R.id.tv_value_500000:
                edAmount.setText(getString(R.string.value_500000));
                break;
            case R.id.tv_value_1000000:
                edAmount.setText(getString(R.string.value_1000000));
                break;
            case R.id.btn_confirm:
                DepositServer depositServer = new DepositServer();
                depositServer.execute(sessionUser.getUserDetails().getToken(), edAmount.getText().toString().replace(".", ""));
                break;
        }
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        df.setDecimalSeparatorAlwaysShown(true);
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

    @SuppressWarnings("unused")
    @Override
    public void afterTextChanged(Editable editable) {
        edAmount.removeTextChangedListener(this);
        try {
            int inilen, endlen;
            inilen = edAmount.getText().length();

            String v = editable.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = df.parse(v);
            int cp = edAmount.getSelectionStart();
            if (hasFractionalPart) {
                edAmount.setText(df.format(n));
            } else {
                edAmount.setText(dfnd.format(n));
            }
            endlen = edAmount.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= edAmount.getText().length()) {
                edAmount.setSelection(sel);
            } else {
                // place cursor at the end?
                edAmount.setSelection(edAmount.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        edAmount.addTextChangedListener(this);
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
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_WALLET_DEPOSIT_CASH, "POST", params);

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
                Log.e("RESULT + DEPOSIT CASH", result);
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            String url = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA).getString("url_wv");
                            /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);*/
                            Intent intent = new Intent(NapCashTuyenDungActivity.this, NapCashServerActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("type", 1);
                            startActivityForResult(intent, 101);
                        }
                    } else {
                        Toast.makeText(NapCashTuyenDungActivity.this, jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }
    }

    private class PutDepositServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_WALLET_TRANSACTION_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_WALLET_HASH, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_WALLET_DEPOSIT_CASH, "PUT", params);

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
                Log.e("RESULT + DEPOSIT CASH", result);
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        DialogUtils.dialogSuccess(NapCashTuyenDungActivity.this, getString(R.string.deposit_cash_success));
                    } else {
                        Toast.makeText(NapCashTuyenDungActivity.this, jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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
