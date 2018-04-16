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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;

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

public class TaoLaiMatKhauActivity extends AppCompatActivity {
    TextView txtSlogan,txtTitle;
    EditText edtNewPassword,edtReNewPassword;
    Button btnComplete;
    ProgressBar progressBar;
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_taolaimatkhau);
        init();
    }
    private void init(){
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtReNewPassword = (EditText) findViewById(R.id.edtReNewPassword);
        btnComplete = (Button) findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String ack_token = intent.getStringExtra("ack_token");
                String newpass = edtNewPassword.getText().toString();
                String repass = edtReNewPassword.getText().toString();
                if(newpass.equals(repass)){
                    ForgotPasswordServer forgotPasswordServer = new ForgotPasswordServer();
                    forgotPasswordServer.execute(ack_token,newpass,repass);
                }
                else {
                    Toast.makeText(TaoLaiMatKhauActivity.this,getString(R.string.toast_matkhaukhongtrungkhop), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class ForgotPasswordServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_ACK_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_NEW_PASS,urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_RE_PASS,urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_FORGOT_PASSWORD, "POST", params);

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
                Log.e("VERIFY RESULT ", result);
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();
                    if(code.contains(Common.JsonKey.KEY_SUCCESSFULLY)){
                        Toast.makeText(TaoLaiMatKhauActivity.this, getString(R.string.toast_doimatkhauthanhcong), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(TaoLaiMatKhauActivity.this,jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }

}
