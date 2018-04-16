package com.hunegroup.hune.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hunegroup.hune.R;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.kyleduo.switchbutton.SwitchButton;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 06/07/2017.
 */

public class CaiDatTimViecActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBack;
    private ImageView imgNotify;
    private TextView txtThongTin;
    private TextView txtDanhGiaUngDung;
    private TextView txtChiaSeUngDung;
    private TextView txtNgonNgu;
    private TextView txtNhanThongBao;
    private TextView txtHuongDan;
    private TextView txtDangXuat;
    private FloatingActionButton btnAdd;
    private SwitchButton swtThongBao;

    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        setContentView(R.layout.activity_caidat_timviec);
        findViews();
        sessionUser = new SessionUser(getBaseContext());
        swtThongBao.setChecked(sessionUser.isNotifacation());
    }

    private void findViews() {
        imgBack = (ImageView)findViewById( R.id.imgBack );
        imgNotify = (ImageView)findViewById( R.id.imgNotify );
        txtThongTin = (TextView)findViewById( R.id.txtThongTin );
        txtDanhGiaUngDung = (TextView)findViewById( R.id.txtDanhGiaUngDung );
        txtChiaSeUngDung = (TextView)findViewById( R.id.txtChiaSeUngDung );
        txtNgonNgu = (TextView)findViewById( R.id.txtNgonNgu );
        txtNhanThongBao = (TextView)findViewById( R.id.txtNhanThongBao );
        txtHuongDan = (TextView)findViewById( R.id.txtHuongDan );
        txtDangXuat = (TextView)findViewById( R.id.txtDangXuat );
        btnAdd = (FloatingActionButton)findViewById( R.id.btnAdd );
        swtThongBao = (SwitchButton) findViewById(R.id.swtThongBao);
        imgBack.setOnClickListener(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaiDatTimViecActivity.this,ThongBaoTimViecActivity.class));
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaiDatTimViecActivity.this,ThemViecLamTimViecActivity.class));
            }
        });

        txtDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionUser.clearData();
                Intent intent = new Intent(CaiDatTimViecActivity.this,WelcomeActivity.class) ;
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        txtNgonNgu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaiDatTimViecActivity.this,ChonNgonNguActivity.class));
            }
        });
        txtChiaSeUngDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://hunegroup.com"))
                            .build();
                    shareDialog.show(linkContent);
                }
            }
        });

        txtThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaiDatTimViecActivity.this,ThongTinActivity.class);
                intent.putExtra(ThongTinActivity.REQUEST_CODE, Common.Type.TYPE_SEARCH_JOB);
                startActivity(intent);
            }
        });

        txtDanhGiaUngDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = CaiDatTimViecActivity.this.getPackageManager().getLaunchIntentForPackage("com.android.vending");
                ComponentName comp = new ComponentName("com.android.vending","com.google.android.finsky.activities.LaunchUrlHandlerActivity");
                launchIntent.setComponent(comp);
                launchIntent.setData(Uri.parse("market://details?id=com.hunegroup.hune"));
                startActivity(launchIntent);
            }
        });

        swtThongBao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ProfileServer profileServer = new ProfileServer();
                if(b){
                    profileServer.execute(sessionUser.getUserDetails().getToken(), FirebaseInstanceId.getInstance().getToken());
                }
                else {
                    profileServer.execute(sessionUser.getUserDetails().getToken(),"");
                }
                sessionUser.setNotification(b);
            }
        });
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.imgBack:{
                finish();
                break;
            }
            case R.id.btnAdd: {
                break;
            }
        }
    }

    private class ProfileServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FCM_TOKEN, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_PROFILE, "PUT", params);

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

                Log.e("caca", "caidat:" + result);

                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    Log.e("caca", "code:" + code);

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            /*UserDTO userDTO = new UserDTO();
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
                            // luu thong tin user
                            sessionUser.createUserSession(userDTO);*/
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }

}
