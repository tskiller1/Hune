package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.UserDTO;
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

public class ChonChucNangActivity extends AppCompatActivity {
    public static int APP_REQUEST_CODE = 99;

    TextView txtSlogan, txtTitle;
    Button btnRecruit, btnFindJob, btnVerify, btnLanguage;
    ProgressBar progressBar;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = loginResult.getAuthorizationCode();
                    Log.e("TOKEN", sessionUser.getUserDetails().getToken());
                    Log.e("ACK_TOKEN", toastMessage);
                    VerifyServer verifyServer = new VerifyServer();
                    verifyServer.execute(sessionUser.getUserDetails().getToken(), loginResult.getAuthorizationCode());
                }
                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
            }

            // Surface the result to your user in an appropriate way.
        }
    }

    SessionUser sessionUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_chonchucnang);
        sessionUser = new SessionUser(getBaseContext());
        init();
    }

    private void init() {
        btnLanguage = (Button) findViewById(R.id.btnChooseLanguage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnVerify = (Button) findViewById(R.id.btnVerify);
        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnFindJob = (Button) findViewById(R.id.btnFindJob);
        btnRecruit = (Button) findViewById(R.id.btnRecruit);
        btnFindJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChonChucNangActivity.this, TimViecActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        btnRecruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChonChucNangActivity.this, TimNguoiTuyenDungActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneLogin(btnVerify);
            }
        });

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ChonChucNangActivity.this, ChonNgonNguActivity.class), 101);
            }
        });
        if (sessionUser.getUserDetails().getStatus() == 2 || sessionUser.getUserDetails().getToken() == null) {
            btnVerify.setVisibility(View.GONE);
        } else {
            btnVerify.setVisibility(View.VISIBLE);
        }
    }

    public void phoneLogin(final View view) {
        final Intent intent = new Intent(ChonChucNangActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private class VerifyServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_ACK_TOKEN, urls[1]));
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
                Log.e("VERIFY RESULT ", result);
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();
                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {

                        UserDTO userDTO = new UserDTO();
                        JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA).toString());
                        if (jsondata.isNull(Common.JsonKey.KEY_USER_FULL_NAME) != true) {
                            String fullname = jsondata.getString(Common.JsonKey.KEY_USER_FULL_NAME).toString().trim();
                            String phone = jsondata.getString(Common.JsonKey.KEY_USER_PHONE);
                            int id = jsondata.getInt(Common.JsonKey.KEY_USER_ID);
                            String token = jsondata.getString(Common.JsonKey.KEY_USER_TOKEN);
                            int status = jsondata.getInt(Common.JsonKey.KEY_USER_STATUS);
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_SEX)) {
                                if (jsondata.getInt(Common.JsonKey.KEY_USER_SEX) == 1)
                                    userDTO.setSex(getString(R.string.nu));
                                else userDTO.setSex(getString(R.string.nam));
                            }
                            userDTO.setId(id);
                            userDTO.setFull_name(fullname);
                            userDTO.setPhone(phone);
                            userDTO.setToken(token);
                            userDTO.setStatus(status);
                            // luu thong tin user vao
                            sessionUser.createUserSession(userDTO);
                            sessionUser.setNotification(true);
                            btnVerify.setVisibility(View.GONE);
                            Toast.makeText(ChonChucNangActivity.this, getString(R.string.toast_xacthucthanhcong), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChonChucNangActivity.this, jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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
