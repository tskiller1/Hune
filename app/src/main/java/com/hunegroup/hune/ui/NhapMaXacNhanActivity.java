package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 02/07/2017.
 */

public class NhapMaXacNhanActivity extends AppCompatActivity {
    TextView txtSlogan, txtTitle, txtPhoneNumber, txtReSend;
    Button btnAccept;
    EditText edtCode;

    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;
    String phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_nhapmaxacnhan);
        sessionUser = new SessionUser(getBaseContext());
        Intent intent = getIntent();

        phone = intent.getStringExtra("phone");
        findViews();
        int l = phone.length();
        String phoneEncode = phone.substring(0,3);
        for (int i = 0;i<l-3;i++){
            phoneEncode +="x";
        }
        String s = getString(R.string.nhapmaxacnhanduocguidenso)+" "+phoneEncode;
        txtPhoneNumber.setText(s);
    }

    private void findViews() {
        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        txtReSend = (TextView) findViewById(R.id.txtReSend);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        edtCode = (EditText) findViewById(R.id.edtCode);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NhapMaXacNhanActivity.this,TaoLaiMatKhauActivity.class));
//                if (Validate.isEmpty(edtCode.getText().toString()) || edtCode.getText().toString().length() != 4) {
//                    Toast.makeText(NhapMaXacNhanActivity.this, getString(R.string.toast_vuilongnhapdaydumaxacnhan), Toast.LENGTH_SHORT).show();
//                } else {
//                    String code = edtCode.getText().toString();
//
//                    VerifyServer verifyServer = new VerifyServer();
//                    verifyServer.execute(phone, code);
//                }
            }
        });
    }

    private class VerifyServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            // prsbLoadingDataList.setVisibility(View.VISIBLE);
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_PHONE, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_CODE, urls[1]));

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_VERIFY, "POST", params);

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

                //prsbLoadingDataList.setVisibility(View.GONE);

                Log.e("caca", "register:" + result);

                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    Log.e("caca", "code:" + code);

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Toast.makeText(NhapMaXacNhanActivity.this, getString(R.string.toast_xacminhsdtthanhcong), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NhapMaXacNhanActivity.this, ChonChucNangActivity.class));
                    } else {
                        Toast.makeText(NhapMaXacNhanActivity.this, getString(R.string.toast_xacminhsdtthatbai), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }
}
