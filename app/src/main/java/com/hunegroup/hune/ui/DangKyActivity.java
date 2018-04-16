package com.hunegroup.hune.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class DangKyActivity extends AppCompatActivity {

    /* todo : layout */
    private TextView txtTitle;
    private EditText edtUsername;
    private EditText edtPhoneNumber;
    private EditText edtPassword;
    private EditText edtRePassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private Spinner spnGioiTinh;
    /* todo : implement connect server */
    JSONParser jsonParser = new JSONParser();
    UserDTO userDTO;
    SessionUser sessionUser;
    String sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,false);
        setContentView(R.layout.activity_dang_ky);

        sessionUser = new SessionUser(getBaseContext());
        findViews();
        addListSex();

    }
    private void findViews() {
        spnGioiTinh = (Spinner) findViewById(R.id.spnGioiTinh);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtTitle = (TextView)findViewById( R.id.txtTitle );
        edtUsername = (EditText)findViewById( R.id.edtUsername );
        edtPhoneNumber = (EditText)findViewById( R.id.edtPhoneNumber );
        edtPassword = (EditText)findViewById( R.id.edtPassword );
        edtRePassword = (EditText)findViewById( R.id.edtRePassword );
        btnRegister = (Button)findViewById( R.id.btnRegister );

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check validate truoc khi dang ky
                if(!Validate.isEmpty(edtUsername.getText().toString())
                        && !Validate.isEmpty(edtPhoneNumber.getText().toString())
                        && !Validate.isEmpty(edtPassword.toString())
                        && !Validate.isEmpty(edtRePassword.toString())){
                    String tempFullName = edtUsername.getText().toString();
                    String tempPhone =edtPhoneNumber.getText().toString();
                    String tempPass =edtPassword.getText().toString();
                    String tempRePass =edtRePassword.getText().toString();
                    if(!Validate.isNumber(tempPhone)){
                        Toast.makeText(DangKyActivity.this,getString(R.string.toast_sodienthoaichiduocsudung), Toast.LENGTH_SHORT).show();
                    }
                    else if(tempPhone.length()<10 || tempPhone.length()>12){
                        Toast.makeText(DangKyActivity.this,getString(R.string.toast_sodienthoaikhonghople), Toast.LENGTH_SHORT).show();
                    }
                    else if(!tempPass.equals(tempRePass)){
                        Toast.makeText(DangKyActivity.this,getString(R.string.toast_matkhaukhongtrungkhop), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        RegisterServer registerServer = new RegisterServer();
                        registerServer.execute(FirebaseInstanceId.getInstance().getToken(),tempFullName,tempPhone,tempPass,tempRePass,sex);
                    }
                }
                else
                    Toast.makeText(DangKyActivity.this,getString(R.string.toast_vuilongnhapdayduthongtin), Toast.LENGTH_SHORT).show();
            }
        });

        spnGioiTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    sex = "1";
                else sex = "2";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addListSex(){
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.nu));
        list.add(getString(R.string.nam));
        spnGioiTinh.setAdapter(new GioiTinhAdapter(this,list));
    }
    private class RegisterServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FCM_TOKEN,urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FULL_NAME, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_PHONE, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_PASS, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_RE_PASS, urls[4]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_SEX, urls[5]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_REGISTER, "POST", params);

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

                Log.e("caca", "register:"+result);

                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    Log.e("caca", "code:"+code);

                    if(code.contains(Common.JsonKey.KEY_SUCCESSFULLY)){
                        userDTO = new UserDTO();
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
                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(DangKyActivity.this,getString(R.string.toast_dangkythanhcong), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DangKyActivity.this,ChonChucNangActivity.class));
                            finish();
                        }

                    }else{
                        String msg = jsonResult.getString(Common.JsonKey.KEY_MESSAGE).toString().trim();
                        Toast.makeText(DangKyActivity.this,msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }

    class GioiTinhAdapter extends ArrayAdapter<String> {

        public GioiTinhAdapter(Context context, List<String> objects) {
            super(context, R.layout.item_gioitinh, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_gioitinh_dangky, null);
            }

            final String item = getItem(position);
            TextView tvNameLoaihinh = (TextView) convertView.findViewById(R.id.tv_name_loaihinh);
            tvNameLoaihinh.setGravity(Gravity.CENTER);
            tvNameLoaihinh.setText(item);

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return initView(position, convertView);
        }

        private View initView(int position, View convertView) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_loaihinh, null);
            }
            final String item = getItem(position);
            TextView tvNameLoaihinh = (TextView) convertView.findViewById(R.id.tv_name_loaihinh);
            tvNameLoaihinh.setGravity(Gravity.CENTER);
            tvNameLoaihinh.setText(item);

            return convertView;
        }
    }

}
