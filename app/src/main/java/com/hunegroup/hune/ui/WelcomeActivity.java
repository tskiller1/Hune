package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Validate;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.hunegroup.hune.util.Utilities.switchLangauge;
import static java.util.Arrays.asList;

public class WelcomeActivity extends AppCompatActivity {

    /* todo : layout*/
    private ImageView imgLogo;
    private TextView txtSlogan;
    private EditText edtPhoneNumber;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtForgotPassword;
    private Button btnRegister;
    private Button btnFacebook;
    private TextView txtTest;
    private ProgressBar progressBar;
    JSONParser jsonParser = new JSONParser();
    private UserDTO userDTO;
    SessionUser sessionUser;
    CallbackManager callbackManager;
    AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder;
    public static int APP_REQUEST_CODE = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getBaseContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("Facebook Login", loginResult.getAccessToken().getToken());
                        LoginFacebookServer loginFacebookServer = new LoginFacebookServer();
                        loginFacebookServer.execute(FirebaseInstanceId.getInstance().getToken(),loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("Facebook Login", exception.toString());
                    }
                });
        switchLangauge(this,true);
        setContentView(R.layout.activity_welcome);

        sessionUser = new SessionUser(getBaseContext());
        findViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    Intent intent = new Intent(WelcomeActivity.this,TaoLaiMatKhauActivity.class);
                    intent.putExtra("ack_token",loginResult.getAuthorizationCode());
                    startActivity(intent);
                }
                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
            }

            // Surface the result to your user in an appropriate way.
        }
        else callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        txtTest = (TextView) findViewById(R.id.txtTest);
        txtTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* todo : Ca */
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ChonChucNangActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PhoneNumber = edtPhoneNumber.getText().toString();
                String Password = edtPassword.getText().toString();

                if (CheckLogin(PhoneNumber, Password)) {
                    LoginServer loginServer = new LoginServer();
                    loginServer.execute(FirebaseInstanceId.getInstance().getToken(),PhoneNumber, Password);
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(WelcomeActivity.this, asList("user_friends", "public_profile"));
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneLogin(txtForgotPassword);
            }
        });
    }

    public void phoneLogin(final View view) {
        final Intent intent = new Intent(WelcomeActivity.this, AccountKitActivity.class);
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

    private boolean CheckLogin(String PhoneNumber, String Password) {
        if (PhoneNumber.isEmpty() && Password.isEmpty()) {
            MakeText(getString(R.string.toast_vuilongnhapthongtintruockhidangnhap));
            return false;
        } else {
            if (PhoneNumber.isEmpty()) {
                MakeText(getString(R.string.toast_vuilongnhapsodienthoaitruockhidangnhap));
                return false;
            } else if (Password.isEmpty()) {
                MakeText(getString(R.string.toast_vuilongnhapmatkhautruockhidangnhap));
                return false;
            } else
                return true;
        }

    }

    private void MakeText(String text) {
        Toast.makeText(getBaseContext(), "" + text, Toast.LENGTH_SHORT).show();
    }

    private class LoginServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FCM_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_PHONE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_PASS, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_LOGIN, "POST", params);

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
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        userDTO = new UserDTO();
                        JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA).toString());

                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_PHONE)) {
                            String phone = jsondata.getString(Common.JsonKey.KEY_USER_PHONE).toString().trim();
                            userDTO.setPhone("" + phone);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_FULL_NAME)) {
                            String fullname = jsondata.getString(Common.JsonKey.KEY_USER_FULL_NAME).toString().trim();
                            userDTO.setFull_name("" + fullname);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_TOKEN)) {
                            String token = jsondata.getString(Common.JsonKey.KEY_USER_TOKEN).toString().trim();
                            userDTO.setToken("" + token);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_ID)) {
                            String id = jsondata.getString(Common.JsonKey.KEY_USER_ID).toString().trim();
                            userDTO.setId(Integer.parseInt(id));
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_SEX)) {
                            int sex = jsondata.getInt(Common.JsonKey.KEY_USER_SEX);
                            if (sex == 1) {
                                userDTO.setSex(getString(R.string.nu));
                            } else
                                userDTO.setSex(getString(R.string.nam));
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_BIRTHDAY)) {
                            String birthday = jsondata.getString(Common.JsonKey.KEY_USER_BIRTHDAY).toString().trim();
                            userDTO.setBirthday(Validate.StringDateFormatToSetText(birthday));
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_STATUS)) {
                            int status = jsondata.getInt(Common.JsonKey.KEY_USER_STATUS);
                            userDTO.setStatus(status);
                        }
                        // luu thong tin user
                        sessionUser.createUserSession(userDTO);
                        sessionUser.setNotification(true);
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), ChonChucNangActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        MakeText(getString(R.string.toast_dangnhapthatbai));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }

    private class LoginFacebookServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FCM_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FACEBOOK_TOKEN, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_LOGIN_FACBOOK, "POST", params);

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
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        userDTO = new UserDTO();
                        JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA).toString());

                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_PHONE)) {
                            String phone = jsondata.getString(Common.JsonKey.KEY_USER_PHONE).toString().trim();
                            userDTO.setPhone("" + phone);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_FULL_NAME)) {
                            String fullname = jsondata.getString(Common.JsonKey.KEY_USER_FULL_NAME).toString().trim();
                            userDTO.setFull_name("" + fullname);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_TOKEN)) {
                            String token = jsondata.getString(Common.JsonKey.KEY_USER_TOKEN).toString().trim();
                            userDTO.setToken("" + token);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_ID)) {
                            String id = jsondata.getString(Common.JsonKey.KEY_USER_ID).toString().trim();
                            userDTO.setId(Integer.parseInt(id));
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_SEX)) {
                            int sex = jsondata.getInt(Common.JsonKey.KEY_USER_SEX);
                            if (sex == 1) {
                                userDTO.setSex(getString(R.string.nu));
                            } else
                                userDTO.setSex(getString(R.string.nam));
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_BIRTHDAY)) {
                            String birthday = jsondata.getString(Common.JsonKey.KEY_USER_BIRTHDAY).toString().trim();
                            userDTO.setBirthday(Validate.StringDateFormatToSetText(birthday));
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_STATUS)) {
                            int status = jsondata.getInt(Common.JsonKey.KEY_USER_STATUS);
                            userDTO.setStatus(status);
                        }
                        // luu thong tin user
                        sessionUser.createUserSession(userDTO);
                        sessionUser.setNotification(true);
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), ChonChucNangActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        MakeText(getString(R.string.toast_dangnhapthatbai));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_ACK_TOKEN,urls[1]));
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
                    if(code.contains(Common.JsonKey.KEY_SUCCESSFULLY)){

                        UserDTO userDTO = new UserDTO();
                        JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA).toString());
                        if(jsondata.isNull(Common.JsonKey.KEY_USER_FULL_NAME)!=true){
                            String fullname = jsondata.getString(Common.JsonKey.KEY_USER_FULL_NAME).toString().trim();
                            String phone = jsondata.getString(Common.JsonKey.KEY_USER_PHONE);
                            int id = jsondata.getInt(Common.JsonKey.KEY_USER_ID);
                            String token = jsondata.getString(Common.JsonKey.KEY_USER_TOKEN);
                            int status = jsondata.getInt(Common.JsonKey.KEY_USER_STATUS);
                            if(!jsondata.isNull(Common.JsonKey.KEY_USER_SEX)){
                                if(jsondata.getInt(Common.JsonKey.KEY_USER_SEX)==1)
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
                            Toast.makeText(WelcomeActivity.this,getString(R.string.toast_xacthucthanhcong), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(WelcomeActivity.this,jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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
