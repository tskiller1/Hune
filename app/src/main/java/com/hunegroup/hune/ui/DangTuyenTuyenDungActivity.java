package com.hunegroup.hune.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.app.App;
import com.hunegroup.hune.dto.Banner;
import com.hunegroup.hune.dto.CategoryDTO;
import com.hunegroup.hune.dto.PostDTO;
import com.hunegroup.hune.dto.SalaryPerTypeDTO;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionLocation;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Validate;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 04/07/2017.
 */

public class DangTuyenTuyenDungActivity extends AppCompatActivity implements View.OnClickListener, OnLocationUpdatedListener {
    public static int APP_REQUEST_CODE = 99;
    static public String TAB = "caca";

    private ViewFlipper flipperAds;
    private ImageView imgBack;
    private ImageView imgNotify;
    private Spinner spnLoaiCongViec;
    private Spinner spnLoaiHinh;
    private RatingBar rtbTieuChuan;

    private EditText edtDiaDiem;
    private TextView txtChonBanDo;
    private TextView textView2;
    private EditText edtNhapLuong;
    private Spinner spnLoaiLuong;
    private TextView txtNgayBatDau;
    private TextView txtNgayKetThuc;
    private EditText edtMoTa;
    private Button btnHoanTat;
    private TextView txtCancel;
    private EditText edtSoLuong, edtTieuDe;
    private ImageView imgDate_BatDau, imgDate_KetThuc;
    private ProgressBar progressBar;
    private FrameLayout frame_progressbar;

    private CountDownTimer timer = new CountDownTimer(450000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            AdsServer adsServer = new AdsServer();
            adsServer.execute("1");
        }
    };


    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;
    SessionLocation sessionLocation = SessionLocation.getInstance();
    private PostDTO postDTO = new PostDTO();

    private List<CategoryDTO> categoryDTOList;
    private List<CategoryDTO> listChild = new ArrayList<>();
    ;
    private LoaiHinhAdapter loaiHinhAdapter_listChild;
    private LocationGooglePlayServicesProvider provider;

    private Calendar calendar;
    private CalendarDatePickerDialogFragment mDatePicker_BatDau;
    private CalendarDatePickerDialogFragment mDatePicker_KetThuc;
    //    private DatePickerDialog.OnDateSetListener callbackDate_BatDau;
//    private DatePickerDialog.OnDateSetListener callbackDate_KetThuc;
    private Date date;

    private List<SalaryPerTypeDTO> listSalaryPerType;
    private boolean isLatLog = false;

    List<Banner> adsList;
    Gson gson = new Gson();
    Date today = new Date();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_dangtuyen_tuyendung);
        sessionUser = new SessionUser(getBaseContext());

        findViews();
        AdsServer adsServer = new AdsServer();
        adsServer.execute("1");
        //get data Loại hình & Công việc

        CategoryServer categoryServer = new CategoryServer();
        categoryServer.execute("0");

        // Set adapter cho spinner - Loại lương
        setListSalaryPerType();
        setSpinner_LoaiLuong();

        // get location hiện tại của user
        if (sessionLocation.isLatLng()) {
            LatLng latLng_Current = sessionLocation.getLocation();
            getCompleteAddressString(latLng_Current.latitude, latLng_Current.longitude);
        }

        // set date current cho edt Bắt đầu & edt Kết thúc
        getDateCurrent();
        showDateCurrent(txtNgayBatDau);
        showDateCurrent(txtNgayKetThuc);


        mDatePicker_BatDau = new CalendarDatePickerDialogFragment()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDoneText(getString(R.string.ok))
                .setCancelText(getString(R.string.cancel));
        CallbackDate_BatDau();

        mDatePicker_KetThuc = new CalendarDatePickerDialogFragment()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDoneText(getString(R.string.ok))
                .setCancelText(getString(R.string.cancel));
        CallbackDate_KetThuc();
        /* todo : Ca get address curent */
        startLocation();

    }

    private void getDateCurrent() {
        calendar = Calendar.getInstance();
        date = calendar.getTime();
    }


    private void showDateCurrent(TextView txt) {
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        showDate(date, txt);
    }

    private void CallbackDate_BatDau() {
        mDatePicker_BatDau.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                date = calendar.getTime();
                if (!date.before(today)) {
                    showDate(date, txtNgayBatDau);
                } else {
                    Toast.makeText(DangTuyenTuyenDungActivity.this, R.string.please_select_after_date, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CallbackDate_KetThuc() {
        mDatePicker_KetThuc.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                date = calendar.getTime();
                if (!date.before(today)) {
                    showDate(date, txtNgayKetThuc);
                } else {
                    Toast.makeText(DangTuyenTuyenDungActivity.this, R.string.please_select_after_date, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setListSalaryPerType() {
        listSalaryPerType = new ArrayList<>();
        listSalaryPerType.add(new SalaryPerTypeDTO(1, App.getSalaryPerTypeInstance().getSalaryTypeHour()));
        listSalaryPerType.add(new SalaryPerTypeDTO(2, App.getSalaryPerTypeInstance().getSalaryTypeDay()));
        listSalaryPerType.add(new SalaryPerTypeDTO(3, App.getSalaryPerTypeInstance().getSalaryTypeWeek()));
        listSalaryPerType.add(new SalaryPerTypeDTO(4, App.getSalaryPerTypeInstance().getSalaryTypeMonth()));
        listSalaryPerType.add(new SalaryPerTypeDTO(5, App.getSalaryPerTypeInstance().getSalaryTypeTime()));
        listSalaryPerType.add(new SalaryPerTypeDTO(6, App.getSalaryPerTypeInstance().getSalaryTypeAgreement()));
    }

    private void setSpinner_LoaiLuong() {
        SalaryPerTypeAdapter salaryPerTypeAdapter = new SalaryPerTypeAdapter(getBaseContext(), listSalaryPerType);
        spnLoaiLuong.setAdapter(salaryPerTypeAdapter);

        spnLoaiLuong.setSelection(0);
        postDTO.setSalary_type(1);


    }

    private void findViews() {
        frame_progressbar = (FrameLayout) findViewById(R.id.frame_progressBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        flipperAds = findViewById(R.id.flipper_ads);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgNotify = (ImageView) findViewById(R.id.imgNotify);
        spnLoaiCongViec = (Spinner) findViewById(R.id.spnLoaiCongViec);
        spnLoaiHinh = (Spinner) findViewById(R.id.spnLoaiHinh);
        rtbTieuChuan = (RatingBar) findViewById(R.id.rtbTieuChuan);

        edtDiaDiem = (EditText) findViewById(R.id.edtDiaDiem);
        txtChonBanDo = (TextView) findViewById(R.id.txtChonBanDo);
        textView2 = (TextView) findViewById(R.id.textView2);
        edtNhapLuong = (EditText) findViewById(R.id.edtNhapLuong);
        spnLoaiLuong = (Spinner) findViewById(R.id.spnLoaiLuong);
        txtNgayBatDau = (TextView) findViewById(R.id.txtNgayBatDau);
        txtNgayKetThuc = (TextView) findViewById(R.id.txtNgayKetThuc);
        edtMoTa = (EditText) findViewById(R.id.edtMoTa);
        btnHoanTat = (Button) findViewById(R.id.btnHoanTat);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        edtSoLuong = (EditText) findViewById(R.id.edtSoLuong);
        imgDate_BatDau = (ImageView) findViewById(R.id.imgDate_BatDau);
        imgDate_KetThuc = (ImageView) findViewById(R.id.imgDate_KetThuc);
        edtTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        imgBack.setOnClickListener(this);
        btnHoanTat.setOnClickListener(this);
        imgDate_BatDau.setOnClickListener(this);
        imgDate_KetThuc.setOnClickListener(this);
        txtChonBanDo.setOnClickListener(this);
        imgNotify.setOnClickListener(this);

        // xu ly lay id khi chon loai hinh
        spnLoaiHinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoryDTOList.size() != 0) {

                    //set adapter cho spinner Công việc
                    listChild = new ArrayList<CategoryDTO>();
                    listChild = categoryDTOList.get(position).getChild();
                    loaiHinhAdapter_listChild = new LoaiHinhAdapter(getBaseContext(), listChild);
                    spnLoaiCongViec.setAdapter(loaiHinhAdapter_listChild);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnLoaiLuong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 5) {
                    edtNhapLuong.setEnabled(false);
                } else edtNhapLuong.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == BanDoActivity.KEY_CODE) {
                //LatLng latLng=SessionLocation.getInstance().getLocation();
                double lat = data.getDoubleExtra("lat", -999);
                double log = data.getDoubleExtra("log", -999);
                String diadiem = data.getStringExtra("diadiem");
                edtDiaDiem.setText(diadiem);
                if (lat == -999 && log == -999) {
                    isLatLog = false;
                    return;
                }
                postDTO.setLatitude(Float.parseFloat(lat + ""));
                postDTO.setLongtitude(Float.parseFloat(log + ""));
                isLatLog = true;
            }
        }
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

    public void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                /*StringBuilder strReturnedAddress = new StringBuilder("");
                StringBuilder diadiem = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    if (i != returnedAddress.getMaxAddressLineIndex() - 1)
                        diadiem.append(returnedAddress.getAddressLine(i)).append(", ");
                    else
                        diadiem.append(returnedAddress.getAddressLine(i));
                }*/

                edtDiaDiem.setText(returnedAddress.getAddressLine(0));

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }

    }

    private void getLatLog(String mylocation) {

        Geocoder gc = new Geocoder(this);
        try {
            List<Address> list = gc.getFromLocationName(mylocation, 1);
            if (list.isEmpty()) {
                isLatLog = false;
                return;
            }
            Address address = list.get(0);

            double lat = address.getLatitude();
            double log = address.getLongitude();

            postDTO.setLatitude(Float.parseFloat(lat + ""));
            postDTO.setLongtitude(Float.parseFloat(log + ""));
            isLatLog = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.txtChonBanDo: {
                String mylocation = edtDiaDiem.getText().toString();
                Intent intent = new Intent(DangTuyenTuyenDungActivity.this, BanDoActivity.class);
                intent.putExtra("mylocation", mylocation);
                startActivityForResult(intent, 111);
                break;
            }
            case R.id.imgBack: {
                finish();
                break;
            }
            case R.id.imgNotify: {
                startActivity(new Intent(DangTuyenTuyenDungActivity.this, ThongBaoTuyenDungActivity.class));
                break;
            }
            case R.id.imgDate_KetThuc: {
                mDatePicker_KetThuc.show(getSupportFragmentManager(), String.valueOf(RecurrencePickerDialogFragment.STYLE_NORMAL));
                break;
            }
            case R.id.imgDate_BatDau: {
                mDatePicker_BatDau.show(getSupportFragmentManager(), String.valueOf(RecurrencePickerDialogFragment.STYLE_NORMAL));
                break;
            }


            case R.id.btnHoanTat: {
                if (sessionUser.getUserDetails().getStatus() == 2) {
                    frame_progressbar.setVisibility(View.VISIBLE);
                    String tieude = edtTieuDe.getText().toString();
                    String soluong = edtSoLuong.getText().toString();
                    String diadiem = edtDiaDiem.getText().toString();
                    String luong;
                    if (spnLoaiLuong.getSelectedItemPosition() == 5) {
                        luong = "0";
                    } else luong = edtNhapLuong.getText().toString();
                    String mota = edtMoTa.getText().toString();


                    String category_parent_id = "";
                    String category_id = "";
                    String salary_type = "";

                    if (!categoryDTOList.isEmpty())
                        category_parent_id = categoryDTOList.get(Integer.parseInt(spnLoaiHinh.getSelectedItemId() + "")).getId() + "";
                    if (!listChild.isEmpty())
                        category_id = listChild.get(Integer.parseInt(spnLoaiCongViec.getSelectedItemId() + "")).getId() + "";
                    if (!listSalaryPerType.isEmpty())
                        salary_type = listSalaryPerType.get(Integer.parseInt(spnLoaiLuong.getSelectedItemId() + "")).getId() + "";

                    float tieuchuan = rtbTieuChuan.getRating();
                    String ngaybatdau = Validate.StringDateFormatToUploadData(txtNgayBatDau.getText().toString());
                    String ngayketthuc = Validate.StringDateFormatToUploadData(txtNgayKetThuc.getText().toString());

                    if (salary_type.isEmpty() || category_id.isEmpty() || category_parent_id.isEmpty() || diadiem.isEmpty() || tieude.isEmpty() || soluong.isEmpty() || luong.isEmpty()) {
                        Toast.makeText(getBaseContext(), getString(R.string.toast_suathongtin_vuilongnhapdayduthongtin), Toast.LENGTH_SHORT).show();
                        frame_progressbar.setVisibility(View.GONE);
                        return;
                    }

                    getLatLog(diadiem);

                    if (!isLatLog) {
                        Toast.makeText(getBaseContext(), getString(R.string.toast_vuilongclick), Toast.LENGTH_SHORT).show();
                        frame_progressbar.setVisibility(View.GONE);
                        return;
                    }
                    if (!postDTO.isLatLog()) {
                        Toast.makeText(getBaseContext(), getString(R.string.toast_vuilongclick), Toast.LENGTH_SHORT).show();
                        frame_progressbar.setVisibility(View.GONE);
                        return;
                    }
                    if (!checkDate(Validate.parseStringToDate2(ngaybatdau), Validate.parseStringToDate2(ngayketthuc))) {
                        Toast.makeText(getBaseContext(), getString(R.string.toast_ngaybatdaukhongduoclonhonnhayketthuc), Toast.LENGTH_SHORT).show();
                        frame_progressbar.setVisibility(View.GONE);
                        return;
                    }
                    postDTO.setSalary_type(Integer.parseInt(salary_type));
                    postDTO.setCategory_id(Integer.parseInt(category_id));
                    postDTO.setCategory_parent_id(Integer.parseInt(category_parent_id));
                    postDTO.setToken(sessionUser.getUserDetails().getToken());
                    postDTO.setTitle(tieude);
                    postDTO.setStar(tieuchuan);
                    postDTO.setQuantity(Integer.parseInt(soluong));
                    postDTO.setAddress(diadiem);
                    postDTO.setSalary(Double.parseDouble(luong));
                    postDTO.setStart_date(ngaybatdau);
                    postDTO.setEnd_date(ngayketthuc);
                    postDTO.setDescription(mota);
                    postDTO.setType(1);

                    PostServer postServer = new PostServer();
                    postServer.execute(postDTO.getToken() + "", postDTO.getCategory_parent_id() + "", postDTO.getCategory_id() + "", postDTO.getTitle(), postDTO.getStar() + "", postDTO.getQuantity() + "", postDTO.getAddress(), postDTO.getLatitude() + "", postDTO.getLongtitude() + "", postDTO.getSalary() + "", postDTO.getSalary_type() + "", postDTO.getStart_date(), postDTO.getEnd_date(), postDTO.getDescription(), postDTO.getType() + "");
                    break;
                } else {
                    showDialog();
                }
            }
        }
    }

    public void phoneLogin(final View view) {
        final Intent intent = new Intent(DangTuyenTuyenDungActivity.this, AccountKitActivity.class);
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

    private boolean checkDate(Date startDate, Date endDate) {
        if (startDate.compareTo(endDate) == 1) {
            return false;
        }
        return true;
    }

    private void showDate(Date date, TextView txt) {
        String mDate = Validate.DateToStringSetText(date);
        txt.setText(mDate);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(DangTuyenTuyenDungActivity.this);
        dialog.setContentView(R.layout.dialog_verify_tuyendung);
        dialog.setTitle("");
        dialog.setCanceledOnTouchOutside(false);
        final Button btnCo = (Button) dialog.findViewById(R.id.btnCo);
        Button btnKhong = (Button) dialog.findViewById(R.id.btnKhong);
        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneLogin(btnHoanTat);
                dialog.dismiss();
            }
        });
        btnKhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class CategoryServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_CATEGORY_PARENT_ID, urls[0]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_CATEGORY, "GET", params);

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


                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();


                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        categoryDTOList = new ArrayList<>();
                        JSONArray jsonArray = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CategoryDTO categoryDTO = new CategoryDTO();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            categoryDTO.setId(jsonObject.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                            categoryDTO.setName(jsonObject.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                            categoryDTO.setIcon(jsonObject.getString(Common.JsonKey.KEY_CATEGORY_ICON));
                            categoryDTO.setParent_id(jsonObject.getInt(Common.JsonKey.KEY_CATEGORY_PARENT_ID));
                            List<CategoryDTO> listChild = new ArrayList<>();
                            if (!jsonObject.isNull(Common.JsonKey.KEY_CATEGORY_CHILD)) {
                                JSONArray jsonArrayChild = jsonObject.getJSONArray(Common.JsonKey.KEY_CATEGORY_CHILD);
                                for (int j = 0; j < jsonArrayChild.length(); j++) {
                                    CategoryDTO categoryChild = new CategoryDTO();
                                    JSONObject jsonObjectChild = jsonArrayChild.getJSONObject(j);
                                    categoryChild.setId(jsonObjectChild.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                    categoryChild.setName(jsonObjectChild.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                    categoryChild.setIcon(jsonObjectChild.getString(Common.JsonKey.KEY_CATEGORY_ICON));
                                    categoryChild.setParent_id(jsonObjectChild.getInt(Common.JsonKey.KEY_CATEGORY_PARENT_ID));
                                    listChild.add(categoryChild);
                                }
                            }
                            categoryDTO.setChild(listChild);
                            categoryDTOList.add(categoryDTO);
                        }
                        spnLoaiHinh.setAdapter(new LoaiHinhAdapter(getBaseContext(), categoryDTOList));

                    } else {

                        Toast.makeText(DangTuyenTuyenDungActivity.this, "Hiện tại chưa có danh sách loại hình", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }

    /* todo :  Adapter Loai Hinh */
    class LoaiHinhAdapter extends ArrayAdapter<CategoryDTO> {

        public LoaiHinhAdapter(Context context, List<CategoryDTO> objects) {
            super(context, R.layout.item_loaihinh, objects);
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

            final CategoryDTO item = getItem(position);
            TextView tvNameLoaihinh = (TextView) convertView.findViewById(R.id.tv_name_loaihinh);
            tvNameLoaihinh.setText(item.getName());

            return convertView;
        }
    }

    class SalaryPerTypeAdapter extends ArrayAdapter<SalaryPerTypeDTO> {

        public SalaryPerTypeAdapter(Context context, List<SalaryPerTypeDTO> objects) {
            super(context, R.layout.item_loaihinh, objects);
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
                convertView = View.inflate(getContext(), R.layout.item_salary_per_type, null);
            }

            final SalaryPerTypeDTO item = getItem(position);
            TextView tvNameLoaihinh = (TextView) convertView.findViewById(R.id.txtName);
            tvNameLoaihinh.setText(item.getName());

            return convertView;
        }
    }


    private class PostServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            frame_progressbar.setVisibility(View.VISIBLE);
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_CATEGORY_ID, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TITLE, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_RATING, urls[4]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_QUANTITY, urls[5]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_ADDRESS, urls[6]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LATITUDE, urls[7]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LONGITUDE, urls[8]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_SALARY, urls[9]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_SALARY_TYPE, urls[10]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_START_DATE, urls[11]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_END_DATE, urls[12]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_DESCRIPTION, urls[13]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE, urls[14]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST, "POST", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {
            frame_progressbar.setVisibility(View.GONE);
            if (getBaseContext() == null)
                return;
            if (result != null) {
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Toast.makeText(DangTuyenTuyenDungActivity.this, getString(R.string.toast_thanhcong), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DangTuyenTuyenDungActivity.this, ThongTinTimViecTuyenDungActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        if (!jsonResult.isNull(Common.JsonKey.KEY_POST_MSG)) {
                            String mess = jsonResult.getString(Common.JsonKey.KEY_POST_MSG).toString().trim();
                            Toast.makeText(DangTuyenTuyenDungActivity.this, mess, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(DangTuyenTuyenDungActivity.this, getString(R.string.toast_dacoloixayra), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            super.onPostExecute(result);
        }

    }

    /* todo : End Adapter Loai Hinh */

    /* todo : 7/8/17 : Ca get address curent */
    String tempAddress1 = null;

    @Override
    public void onLocationUpdated(Location location) {

        SmartLocation.with(getBaseContext()).geocoding().reverse(location, new OnReverseGeocodingListener() {
            @Override
            public void onAddressResolved(Location original, List<Address> results) {
                if (results.size() > 0) {
                    Address result = results.get(0);

                    List<String> addressElements = new ArrayList<>();
                    for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                        addressElements.add(result.getAddressLine(i));
                    }
                    tempAddress1 = "" + TextUtils.join(", ", addressElements);
                    //Log.e("caca", " location.getLongitude():" + tempAddress1);
                    edtDiaDiem.setText("" + tempAddress1);
                }
            }
        });


    }

    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);
        SmartLocation smartLocation = new SmartLocation.Builder(getBaseContext()).logging(true).build();
        smartLocation.location(provider).start(this);
    }
    /* todo : 7/8/17 : Ca get address curent */

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
                        JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA));
                        if (!jsondata.isNull(Common.JsonKey.KEY_USER_FULL_NAME)) {
                            String fullname = jsondata.getString(Common.JsonKey.KEY_USER_FULL_NAME).trim();
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

                            Toast.makeText(DangTuyenTuyenDungActivity.this, getString(R.string.toast_xacthucthanhcong), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DangTuyenTuyenDungActivity.this, jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }

    private class AdsServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POSITION, urls[0]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_ADS_BANNER, "GET", params);

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


                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();


                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        JSONArray jsonData = jsonResult.getJSONArray("data");
                        adsList = new ArrayList<>();
                        for (int i = 0; i < jsonData.length(); i++) {
                            Banner banner = gson.fromJson(jsonData.getJSONObject(i).toString(), Banner.class);
                            adsList.add(banner);
                        }
                        if (adsList.size() > 0) {
                            for (int i = 0; i < adsList.size(); i++) {
                                ImageView imvAds = new ImageView(DangTuyenTuyenDungActivity.this);
                                imvAds.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                Glide.with(DangTuyenTuyenDungActivity.this).load(adsList.get(i).getCover()).into(imvAds);
                                final String url = adsList.get(i).getUrl();
                                imvAds.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            startActivity(browserIntent);
                                        } catch (Exception e) {

                                        }
                                    }
                                });
                                flipperAds.addView(imvAds);
                            }
                            flipperAds.setVisibility(View.VISIBLE);
                            timer.start();
                        }
                    } else {
                        Toast.makeText(DangTuyenTuyenDungActivity.this, "Hiện tại chưa có danh sách loại hình", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }

}
