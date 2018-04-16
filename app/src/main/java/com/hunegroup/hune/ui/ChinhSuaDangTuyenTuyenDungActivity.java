package com.hunegroup.hune.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.hunegroup.hune.R;
import com.hunegroup.hune.app.App;
import com.hunegroup.hune.dto.CategoryDTO;
import com.hunegroup.hune.dto.Category_ChinhSuaDangTuyenDTO;
import com.hunegroup.hune.dto.Parent_Category_ChinhSuaDangTuyenDTO;
import com.hunegroup.hune.dto.Post_ChinhSuaDangTuyenDTO;
import com.hunegroup.hune.dto.SalaryPerTypeDTO;
import com.hunegroup.hune.dto.User_ChinhSuaDangTuyenDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
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

import static com.hunegroup.hune.util.Utilities.switchLangauge;

public class ChinhSuaDangTuyenTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
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
    private EditText edtSoLuong, edtTieuDe;
    private ImageView imgDate_BatDau, imgDate_KetThuc;
    private FrameLayout frame_progressBar;

    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;
    //PostDTO postDTO = new PostDTO();
    private boolean isChange = false;

    public boolean getisChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private List<CategoryDTO> categoryDTOList;
    private LoaiHinhAdapter loaiHinhAdapter;
    private List<CategoryDTO> listChild;
    private LoaiHinhAdapter loaiHinhAdapter_listChild;


    private Calendar calendar;
    /*private DatePickerDialog mDatePicker_BatDau;
    private DatePickerDialog mDatePicker_KetThuc;
    private DatePickerDialog.OnDateSetListener callbackDate_BatDau;
    private DatePickerDialog.OnDateSetListener callbackDate_KetThuc;*/
    private CalendarDatePickerDialogFragment mDatePicker_BatDau;
    private CalendarDatePickerDialogFragment mDatePicker_KetThuc;

    private Date date;
    private int start_year, start_month, start_day;
    private int end_year, end_month, end_day;
    private List<SalaryPerTypeDTO> listSalaryPerType;
    private boolean isLatLog = false;
    private boolean isFirst = false;
    Date today = new Date();

    private Post_ChinhSuaDangTuyenDTO post_chinhSuaDangTuyenDTO = new Post_ChinhSuaDangTuyenDTO();

    public Post_ChinhSuaDangTuyenDTO getPost_chinhSuaDangTuyenDTO() {
        return post_chinhSuaDangTuyenDTO;
    }

    public void setPost_chinhSuaDangTuyenDTO(Post_ChinhSuaDangTuyenDTO post_chinhSuaDangTuyenDTO) {
        this.post_chinhSuaDangTuyenDTO = post_chinhSuaDangTuyenDTO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_chinh_sua_dang_tuyen_tuyen_dung);
        sessionUser = new SessionUser(getBaseContext());

        findViews();
        frame_progressBar.setVisibility(View.VISIBLE);
        categoryDTOList = new ArrayList<>();
        loaiHinhAdapter = new LoaiHinhAdapter(getBaseContext(), categoryDTOList);
        spnLoaiHinh.setAdapter(loaiHinhAdapter);

        ChinhSuaDangTuyenTuyenDungActivity.CategoryServer categoryServer = new ChinhSuaDangTuyenTuyenDungActivity.CategoryServer();
        categoryServer.execute("0");

        //Log.e(TAB,"sessionUser:"+sessionUser.getUserDetails().getToken());
        setListSalaryPerType();
        setSpinner_LoaiLuong();


    }

    private void getData() //Lấy data từ server
    {
        Intent intent = getIntent();
        setId(intent.getIntExtra(Common.JsonKey.KEY_POST_ID, -1));
        GetPostIDServer getPostIDServer = new GetPostIDServer();
        getPostIDServer.execute(getId() + "");


    }

    private void setData() {
        calendar = Calendar.getInstance();

        Date startDate = Validate.parseStringToDate2(post_chinhSuaDangTuyenDTO.getStart_date());
        Date endDate = Validate.parseStringToDate2(post_chinhSuaDangTuyenDTO.getEnd_date());
        setStartDate(startDate);
        setEndDate(endDate);

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


        edtNhapLuong.setText(post_chinhSuaDangTuyenDTO.getSalary());
        edtSoLuong.setText(String.valueOf(post_chinhSuaDangTuyenDTO.getQuantity()));
        edtMoTa.setText(post_chinhSuaDangTuyenDTO.getDescription());

        String start_date = Validate.StringDateFormatToUploadData2(post_chinhSuaDangTuyenDTO.getStart_date());
        txtNgayBatDau.setText(start_date);
        String end_date = Validate.StringDateFormatToUploadData2(post_chinhSuaDangTuyenDTO.getEnd_date());
        txtNgayKetThuc.setText(end_date);

        edtTieuDe.setText(post_chinhSuaDangTuyenDTO.getTitle());
        rtbTieuChuan.setRating(post_chinhSuaDangTuyenDTO.getRating());
        edtDiaDiem.setText(post_chinhSuaDangTuyenDTO.getAddress());
        edtNhapLuong.setText(post_chinhSuaDangTuyenDTO.getSalary());

        for (int i = 0; i < listSalaryPerType.size(); i++)
            if (listSalaryPerType.get(i).getId() == post_chinhSuaDangTuyenDTO.getSalary_type()) {
                spnLoaiLuong.setSelection(i);
                break;
            }


        int position_LoaiHinh = 0;
        for (int i = 0; i < categoryDTOList.size(); i++)
            if (categoryDTOList.get(i).getId() == post_chinhSuaDangTuyenDTO.getParent_category().getId()) {
                spnLoaiHinh.setSelection(i);
                position_LoaiHinh = i;
                break;
            }

        listChild = new ArrayList<CategoryDTO>();
        listChild = categoryDTOList.get(position_LoaiHinh).getChild();
        loaiHinhAdapter_listChild = new LoaiHinhAdapter(getBaseContext(), listChild);
        spnLoaiCongViec.setAdapter(loaiHinhAdapter_listChild);
        if (position_LoaiHinh != 0)
            isFirst = true;
        for (int i = 0; i < listChild.size(); i++)
            if (listChild.get(i).getId() == post_chinhSuaDangTuyenDTO.getCategory().getId()) {
                spnLoaiCongViec.setSelection(i);
                break;
            }


    }

    private void setStartDate(Date date) {
        start_year = Integer.parseInt(Validate.getYear(date));
        start_month = Integer.parseInt(Validate.getMonth(date));
        start_day = Integer.parseInt(Validate.getDay(date));
    }

    private void setEndDate(Date date) {
        end_year = Integer.parseInt(Validate.getYear(date));
        end_month = Integer.parseInt(Validate.getMonth(date));
        end_day = Integer.parseInt(Validate.getDay(date));
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
                    Toast.makeText(ChinhSuaDangTuyenTuyenDungActivity.this, R.string.please_select_after_date, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ChinhSuaDangTuyenTuyenDungActivity.this, R.string.please_select_after_date, Toast.LENGTH_SHORT).show();
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
        post_chinhSuaDangTuyenDTO.setSalary_type(1);

    }

    private void findViews() {

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
        edtSoLuong = (EditText) findViewById(R.id.edtSoLuong);
        imgDate_BatDau = (ImageView) findViewById(R.id.imgDate_BatDau);
        imgDate_KetThuc = (ImageView) findViewById(R.id.imgDate_KetThuc);
        edtTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        frame_progressBar = (FrameLayout) findViewById(R.id.frame_progressBar);

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
                if (isFirst) {
                    isFirst = false;
                    return;
                }
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
                post_chinhSuaDangTuyenDTO.setLatitude(lat);
                post_chinhSuaDangTuyenDTO.setLongitude(log);
                isLatLog = true;
            }
        }
    }

    public void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                StringBuilder diadiem = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    if (i != returnedAddress.getMaxAddressLineIndex() - 1)
                        diadiem.append(returnedAddress.getAddressLine(i)).append(", ");
                    else
                        diadiem.append(returnedAddress.getAddressLine(i));
                }

                edtDiaDiem.setText(diadiem);

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

            post_chinhSuaDangTuyenDTO.setLatitude(lat);
            post_chinhSuaDangTuyenDTO.setLongitude(log);
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
                Intent intent = new Intent(ChinhSuaDangTuyenTuyenDungActivity.this, BanDoActivity.class);
                intent.putExtra("mylocation", mylocation);
                startActivityForResult(intent, 111);
                break;
            }
            case R.id.imgBack: {
                if (getisChange()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", "thaydoi");
                    setResult(88, returnIntent);
                }
                finish();
                break;
            }
            case R.id.imgNotify: {
                startActivity(new Intent(ChinhSuaDangTuyenTuyenDungActivity.this, ThongBaoTuyenDungActivity.class));
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

                frame_progressBar.setVisibility(View.VISIBLE);
                String tieude = edtTieuDe.getText().toString();
                String soluong = edtSoLuong.getText().toString();
                String diadiem = edtDiaDiem.getText().toString();
                String luong = edtNhapLuong.getText().toString();
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


                if (tieude.equals(post_chinhSuaDangTuyenDTO.getTitle()) && category_parent_id.equals(String.valueOf(post_chinhSuaDangTuyenDTO.getCategory_parent_id()))
                        && category_id.equals(String.valueOf(post_chinhSuaDangTuyenDTO.getCategory_id())) && tieuchuan == post_chinhSuaDangTuyenDTO.getRating()
                        && soluong.equals(post_chinhSuaDangTuyenDTO.getQuantity() + "") && diadiem.equals(post_chinhSuaDangTuyenDTO.getAddress())
                        && luong.equals(post_chinhSuaDangTuyenDTO.getSalary() + "") && salary_type.equals(String.valueOf(post_chinhSuaDangTuyenDTO.getSalary_type()))
                        && ngaybatdau.equals(post_chinhSuaDangTuyenDTO.getStart_date()) && ngayketthuc.equals(post_chinhSuaDangTuyenDTO.getEnd_date())
                        && mota.equals(post_chinhSuaDangTuyenDTO.getDescription())) {

                    Toast.makeText(getBaseContext(), getString(R.string.toast_khongcothongtinthaydoi), Toast.LENGTH_SHORT).show();
                    frame_progressBar.setVisibility(View.GONE);
                    return;
                }


                if (salary_type.isEmpty() || category_id.isEmpty() || category_parent_id.isEmpty() || diadiem.isEmpty() || tieude.isEmpty() || soluong.isEmpty() || luong.isEmpty()) {
                    Toast.makeText(getBaseContext(), getString(R.string.toast_suathongtin_vuilongnhapdayduthongtin), Toast.LENGTH_SHORT).show();
                    frame_progressBar.setVisibility(View.GONE);
                    return;
                }

                getLatLog(diadiem);

                if (!isLatLog) {
                    Toast.makeText(getBaseContext(), getString(R.string.toast_vuilongclick), Toast.LENGTH_SHORT).show();
                    frame_progressBar.setVisibility(View.GONE);
                    return;
                }
                if (!post_chinhSuaDangTuyenDTO.isLatLog()) {
                    Toast.makeText(getBaseContext(), getString(R.string.toast_vuilongclick), Toast.LENGTH_SHORT).show();
                    frame_progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!checkDate(Validate.parseStringToDate2(ngaybatdau), Validate.parseStringToDate2(ngayketthuc))) {
                    Toast.makeText(getBaseContext(), getString(R.string.toast_ngaybatdaukhongduoclonhonnhayketthuc), Toast.LENGTH_SHORT).show();
                    frame_progressBar.setVisibility(View.GONE);
                    return;
                }

                post_chinhSuaDangTuyenDTO.setSalary_type(Integer.parseInt(salary_type));
                post_chinhSuaDangTuyenDTO.setCategory_id(Integer.parseInt(category_id));
                post_chinhSuaDangTuyenDTO.setCategory_parent_id(Integer.parseInt(category_parent_id));
                post_chinhSuaDangTuyenDTO.setTitle(tieude);
                post_chinhSuaDangTuyenDTO.setRating(tieuchuan);
                post_chinhSuaDangTuyenDTO.setQuantity(Integer.parseInt(soluong));
                post_chinhSuaDangTuyenDTO.setAddress(diadiem);
                post_chinhSuaDangTuyenDTO.setSalary(Double.parseDouble(luong) + "");
                post_chinhSuaDangTuyenDTO.setStart_date(ngaybatdau);
                post_chinhSuaDangTuyenDTO.setEnd_date(ngayketthuc);
                post_chinhSuaDangTuyenDTO.setDescription(mota);
                post_chinhSuaDangTuyenDTO.setType(1);


                PUTPostIDServer putPostIDServer = new PUTPostIDServer();
                putPostIDServer.execute(sessionUser.getUserDetails().getToken(), getId() + "", post_chinhSuaDangTuyenDTO.getCategory_parent_id() + "", post_chinhSuaDangTuyenDTO.getCategory_id() + "", post_chinhSuaDangTuyenDTO.getTitle(), post_chinhSuaDangTuyenDTO.getRating() + "", post_chinhSuaDangTuyenDTO.getQuantity() + "", post_chinhSuaDangTuyenDTO.getAddress(), post_chinhSuaDangTuyenDTO.getLatitude() + "", post_chinhSuaDangTuyenDTO.getLongitude() + "", post_chinhSuaDangTuyenDTO.getSalary() + "", post_chinhSuaDangTuyenDTO.getSalary_type() + "", post_chinhSuaDangTuyenDTO.getStart_date(), post_chinhSuaDangTuyenDTO.getEnd_date(), post_chinhSuaDangTuyenDTO.getDescription());

                break;
            }
        }
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
                            loaiHinhAdapter.notifyDataSetChanged();
                        }
                        getData();

                    } else {
                        Toast.makeText(ChinhSuaDangTuyenTuyenDungActivity.this, getString(R.string.toast_dacoloixayra), Toast.LENGTH_SHORT).show();
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


    private class GetPostIDServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            frame_progressBar.setVisibility(View.VISIBLE);
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_ID, urls[0]));

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_GET_POST + urls[0], "GET", params);

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
                    Log.e("result = ", result.toString());
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Post_ChinhSuaDangTuyenDTO post_chinhSuaDangTuyenDTO = new Post_ChinhSuaDangTuyenDTO();
                        JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA).toString());

                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_ID)) {
                            int id = jsondata.getInt(Common.JsonKey.KEY_POST_ID);
                            post_chinhSuaDangTuyenDTO.setId(id);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_USER_ID)) {
                            int user_id = jsondata.getInt(Common.JsonKey.KEY_POST_USER_ID);
                            post_chinhSuaDangTuyenDTO.setUser_id(user_id);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID)) {
                            int category_parent_id = jsondata.getInt(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID);
                            post_chinhSuaDangTuyenDTO.setCategory_parent_id(category_parent_id);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_CATEGORY_ID)) {
                            int category_id = jsondata.getInt(Common.JsonKey.KEY_POST_CATEGORY_ID);
                            post_chinhSuaDangTuyenDTO.setCategory_id(category_id);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_TITLE)) {
                            String title = jsondata.getString(Common.JsonKey.KEY_POST_TITLE).toString().trim();
                            post_chinhSuaDangTuyenDTO.setTitle(title);
                        }
                        // - images -

                        // - end images -
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_THUMBNAIL)) {
                            String thumbnail = jsondata.getString(Common.JsonKey.KEY_POST_THUMBNAIL).toString().trim();
                            post_chinhSuaDangTuyenDTO.setThumbnail(thumbnail);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_RATING)) {
                            float rating = Float.parseFloat(jsondata.getDouble(Common.JsonKey.KEY_POST_RATING) + "");
                            post_chinhSuaDangTuyenDTO.setRating(rating);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_QUANTITY)) {
                            int quantity = jsondata.getInt(Common.JsonKey.KEY_POST_QUANTITY);
                            post_chinhSuaDangTuyenDTO.setQuantity(quantity);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_ADDRESS)) {
                            String address = jsondata.getString(Common.JsonKey.KEY_POST_ADDRESS).toString().trim();
                            post_chinhSuaDangTuyenDTO.setAddress(address);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_LATITUDE)) {
                            double latitude = jsondata.getDouble(Common.JsonKey.KEY_POST_LATITUDE);
                            post_chinhSuaDangTuyenDTO.setLatitude(latitude);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_LONGITUDE)) {
                            double longitude = jsondata.getDouble(Common.JsonKey.KEY_POST_LONGITUDE);
                            post_chinhSuaDangTuyenDTO.setLongitude(longitude);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_SALARY)) {
                            String salary = jsondata.getString(Common.JsonKey.KEY_POST_SALARY).toString().trim();
                            post_chinhSuaDangTuyenDTO.setSalary(salary);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_SALARY_TYPE)) {
                            int salary_type = jsondata.getInt(Common.JsonKey.KEY_POST_SALARY_TYPE);
                            post_chinhSuaDangTuyenDTO.setSalary_type(salary_type);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_START_DATE)) {
                            String start_date = jsondata.getString(Common.JsonKey.KEY_POST_START_DATE).toString().trim();
                            post_chinhSuaDangTuyenDTO.setStart_date(start_date);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_END_DATE)) {
                            String end_date = jsondata.getString(Common.JsonKey.KEY_POST_END_DATE).toString().trim();
                            post_chinhSuaDangTuyenDTO.setEnd_date(end_date);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_DESCRIPTION)) {
                            String description = jsondata.getString(Common.JsonKey.KEY_POST_DESCRIPTION).toString().trim();
                            post_chinhSuaDangTuyenDTO.setDescription(description);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_TYPE)) {
                            int type = jsondata.getInt(Common.JsonKey.KEY_POST_TYPE);
                            post_chinhSuaDangTuyenDTO.setType(type);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_SEX)) {
                            int sex = jsondata.getInt(Common.JsonKey.KEY_POST_TYPE);
                            post_chinhSuaDangTuyenDTO.setSex(sex);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_STATUS)) {
                            int status = jsondata.getInt(Common.JsonKey.KEY_POST_STATUS);
                            post_chinhSuaDangTuyenDTO.setStatus(status);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_CREATED_AT)) {
                            String created_at = jsondata.getString(Common.JsonKey.KEY_POST_CREATED_AT).toString().trim();
                            post_chinhSuaDangTuyenDTO.setCreated_at(created_at);
                        }
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_UPDATED_AT)) {
                            String updated_at = jsondata.getString(Common.JsonKey.KEY_POST_UPDATED_AT).toString().trim();
                            post_chinhSuaDangTuyenDTO.setUpdated_at(updated_at);
                        }
                        Category_ChinhSuaDangTuyenDTO category = new Category_ChinhSuaDangTuyenDTO();
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_CATEGORY)) {
                            JSONObject jsonObject = jsondata.getJSONObject(Common.JsonKey.KEY_POST_CATEGORY);

                            if (!jsonObject.isNull(Common.JsonKey.KEY_POST_ID)) {
                                int id = jsonObject.getInt(Common.JsonKey.KEY_POST_ID);
                                category.setId(id);
                            }
                            if (!jsonObject.isNull(Common.JsonKey.KEY_POST_NAME)) {
                                String name = jsonObject.getString(Common.JsonKey.KEY_POST_NAME).toString().trim();
                                category.setName(name);
                            }
                        }
                        Parent_Category_ChinhSuaDangTuyenDTO parent_category = new Parent_Category_ChinhSuaDangTuyenDTO();
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_PARENT_CATEGORY)) {
                            JSONObject jsonObject = jsondata.getJSONObject(Common.JsonKey.KEY_POST_PARENT_CATEGORY);

                            if (!jsonObject.isNull(Common.JsonKey.KEY_POST_ID)) {
                                int id = jsonObject.getInt(Common.JsonKey.KEY_POST_ID);
                                parent_category.setId(id);
                            }
                            if (!jsonObject.isNull(Common.JsonKey.KEY_POST_NAME)) {
                                String name = jsonObject.getString(Common.JsonKey.KEY_POST_NAME).toString().trim();
                                parent_category.setName(name);
                            }
                        }
                        User_ChinhSuaDangTuyenDTO user = new User_ChinhSuaDangTuyenDTO();
                        if (!jsondata.isNull(Common.JsonKey.KEY_POST_USER)) {
                            JSONObject jsonObject = jsondata.getJSONObject(Common.JsonKey.KEY_POST_USER);

                            if (!jsonObject.isNull(Common.JsonKey.KEY_POST_ID)) {
                                int id = jsonObject.getInt(Common.JsonKey.KEY_POST_ID);
                                user.setId(id);
                            }
                            if (!jsonObject.isNull(Common.JsonKey.KEY_POST_PHONE)) {
                                String phone = jsonObject.getString(Common.JsonKey.KEY_POST_PHONE).toString().trim();
                                user.setPhone(phone);
                            }
                            if (!jsonObject.isNull(Common.JsonKey.KEY_POST_FULL_NAME)) {
                                String full_name = jsonObject.getString(Common.JsonKey.KEY_POST_FULL_NAME).toString().trim();
                                user.setFull_name(full_name);
                            }
                            if (!jsonObject.isNull(Common.JsonKey.KEY_POST_AVATAR)) {
                                String avatar = jsonObject.getString(Common.JsonKey.KEY_POST_AVATAR).toString().trim();
                                user.setAvatar(avatar);
                            }
                        }
                        post_chinhSuaDangTuyenDTO.setCategory(category);
                        post_chinhSuaDangTuyenDTO.setParent_category(parent_category);
                        post_chinhSuaDangTuyenDTO.setUser(user);


                        setPost_chinhSuaDangTuyenDTO(post_chinhSuaDangTuyenDTO);
                        setData();
                        frame_progressBar.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(ChinhSuaDangTuyenTuyenDungActivity.this, getString(R.string.toast_dacoloixayra), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            frame_progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }


    private class PUTPostIDServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_CATEGORY_ID, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TITLE, urls[4]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_RATING, urls[5]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_QUANTITY, urls[6]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_ADDRESS, urls[7]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LATITUDE, urls[8]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LONGITUDE, urls[9]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_SALARY, urls[10]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_SALARY_TYPE, urls[11]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_START_DATE, urls[12]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_END_DATE, urls[13]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_DESCRIPTION, urls[14]));

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_PUT_POST + urls[1], "PUT", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {
            frame_progressBar.setVisibility(View.GONE);
            if (getBaseContext() == null)
                return;
            if (result != null) {
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        setChange(true);
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.toast_suadoithanhcong), Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(ChinhSuaDangTuyenTuyenDungActivity.this,QuanLyDangTuyenTuyenDungActivity.class));
                        Intent intent = new Intent();
                        intent.putExtra("result", "thaydoi");
                        setResult(88, intent);
                        finish();

                    } else {

                        Toast.makeText(ChinhSuaDangTuyenTuyenDungActivity.this, getString(R.string.toast_dacoloixayra), Toast.LENGTH_SHORT).show();
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

}
