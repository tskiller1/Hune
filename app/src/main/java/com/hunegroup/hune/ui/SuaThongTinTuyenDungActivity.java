package com.hunegroup.hune.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
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
import java.util.Calendar;
import java.util.List;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 05/07/2017.
 */

public class SuaThongTinTuyenDungActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgNotify;
    private EditText edtTenNguoiDung;
    private Spinner spnGioiTinh;
    private TextView txtNgaySinh;
    private EditText txtSoDienThoai;
    private RelativeLayout rlNamSinh;
    private Button btnHoanTat;
    private FrameLayout frame_progressbar;
    JSONParser jsonParser = new JSONParser();
    Calendar myCalendar;
    SessionUser sessionUser;
    String birthday;
    String sex;
    CalendarDatePickerDialogFragment calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_suathongtin_tuyendung);
        sessionUser = new SessionUser(getBaseContext());
        findViews();
        createDialog();
        UserDTO userDTO = sessionUser.getUserDetails();
        edtTenNguoiDung.setText(userDTO.getFull_name());
        addListSex();
        if (userDTO.getSex() == getString(R.string.nu))
            spnGioiTinh.setSelection(0);
        else spnGioiTinh.setSelection(1);
        if (userDTO.getBirthday() != null)
            txtNgaySinh.setText(userDTO.getBirthday());
        else txtNgaySinh.setText(getString(R.string.chuacothongtin));
        txtSoDienThoai.setText(userDTO.getPhone());
        if (!Validate.isEmpty(txtSoDienThoai.getText().toString())) {
            txtSoDienThoai.setClickable(false);
            txtSoDienThoai.setFocusable(false);
        } else {
            txtSoDienThoai.setClickable(true);
            txtSoDienThoai.setFocusable(true);
        }
    }

    private void findViews() {
        frame_progressbar = (FrameLayout) findViewById(R.id.frame_progressBar);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgNotify = (ImageView) findViewById(R.id.imgNotify);
        edtTenNguoiDung = (EditText) findViewById(R.id.edtTenNguoiDung);
        spnGioiTinh = (Spinner) findViewById(R.id.spnGioiTinh);
        txtNgaySinh = (TextView) findViewById(R.id.txtNgaySinh);
        txtSoDienThoai = (EditText) findViewById(R.id.edtSoDienThoai);
        rlNamSinh = (RelativeLayout) findViewById(R.id.rlNamSinh);
        btnHoanTat = (Button) findViewById(R.id.btnHoanTat);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuaThongTinTuyenDungActivity.this, TrangCaNhanTuyenDungActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuaThongTinTuyenDungActivity.this, ThongBaoTuyenDungActivity.class));

            }
        });

        btnHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileServer profileServer = new ProfileServer();
                String fullname = edtTenNguoiDung.getText().toString();
                String token = sessionUser.getUserDetails().getToken();
                if (Validate.isDate(txtNgaySinh.getText().toString())) {
                    birthday = Validate.StringDateFormatToUploadData(txtNgaySinh.getText().toString());
                    if (!Validate.isEmpty(txtSoDienThoai.getText().toString())) {
                        String phone = txtSoDienThoai.getText().toString();
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_TOKEN, sessionUser.getUserDetails().getToken()));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FULL_NAME, fullname));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_SEX, sex));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_BIRTHDAY, birthday));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_PHONE, phone));
                        profileServer.execute(params);
                    } else {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_TOKEN, sessionUser.getUserDetails().getToken()));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FULL_NAME, fullname));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_SEX, sex));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_BIRTHDAY, birthday));
                        profileServer.execute(params);
                    }
                } else {
                    if (!Validate.isEmpty(txtSoDienThoai.getText().toString())) {
                        String phone = txtSoDienThoai.getText().toString();
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_TOKEN, sessionUser.getUserDetails().getToken()));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FULL_NAME, fullname));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_SEX, sex));
                        params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_PHONE, phone));
                        profileServer.execute(params);
                    }
                }
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

        rlNamSinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.show(getSupportFragmentManager(), String.valueOf(RecurrencePickerDialogFragment.STYLE_NORMAL));
            }
        });
    }

    private void addListSex() {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.nu));
        list.add(getString(R.string.nam));
        spnGioiTinh.setAdapter(new GioiTinhAdapter(this, list));
    }

    private void createDialog() {
        myCalendar = Calendar.getInstance();
        calendar = new CalendarDatePickerDialogFragment()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDoneText(getString(R.string.ok))
                .setCancelText(getString(R.string.cancel));
        calendar.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                txtNgaySinh.setText(Validate.DateToStringSetText(myCalendar.getTime()));
            }
        });
    }

    class GioiTinhAdapter extends ArrayAdapter<String> {

        public GioiTinhAdapter(Context context, List<String> objects) {
            super(context, R.layout.item_gioitinh, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return initView(position, convertView);
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
            tvNameLoaihinh.setText(item);

            return convertView;
        }
    }

    private class ProfileServer extends AsyncTask<List<NameValuePair>, Void, String> {

        protected void onPreExecute() {
            frame_progressbar.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected String doInBackground(List<NameValuePair>... urls) {
            String result = null;
            try {
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    Thread.sleep(1000);
                }
                // Building Parameters
                /*List<NameValuePair> params = new ArrayList<NameValuePair>();
                *//* params *//*
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FULL_NAME, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_SEX, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_BIRTHDAY, urls[3]));
                if (urls[4] != null) {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_PHONE, urls[4]));
                }*/
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_PROFILE, "PUT", urls[0]);

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
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            UserDTO userDTO = new UserDTO();
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
                                userDTO.setStatus(jsondata.getInt(Common.JsonKey.KEY_USER_STATUS));
                            }
                            // luu thong tin user
                            sessionUser.createUserSession(userDTO);
                        }
                        Toast.makeText(SuaThongTinTuyenDungActivity.this, getString(R.string.toast_suathongtin_thanhcong), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SuaThongTinTuyenDungActivity.this, getString(R.string.toast_suathongtin_datontai), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            frame_progressbar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }
}
