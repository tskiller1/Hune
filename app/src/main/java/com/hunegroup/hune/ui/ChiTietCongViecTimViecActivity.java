package com.hunegroup.hune.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.adapter.DanhGiaAdapter;
import com.hunegroup.hune.app.App;
import com.hunegroup.hune.dto.Banner;
import com.hunegroup.hune.dto.CategoryDTO;
import com.hunegroup.hune.dto.DanhGiaDTO;
import com.hunegroup.hune.dto.PostResultDTO;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.lib.ExpandableHeightListView;
import com.hunegroup.hune.uiv2.NhapThongTinTimViecActivity;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionType;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Validate;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hunegroup.hune.util.Common.JsonKey.KEY_POST_CATEGORY_ID;
import static com.hunegroup.hune.util.Common.JsonKey.KEY_POST_SALARY_TYPE;
import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 06/07/2017.
 */

public class ChiTietCongViecTimViecActivity extends AppCompatActivity {

    private ViewFlipper flipperAds;
    private Toolbar toolbar;
    private ImageView imgBack;
    private ImageView imgSave;
    private TextView txtTenNguoiTuyenDung;
    private ImageView imgTheoDoi;
    private ImageView imgChiaSe;
    private ImageButton btnLienLac;
    private ImageView imgThemTask;
    private TextView txtLoaiHinh;
    private TextView txtTieuDe;
    private TextView txtMota;
    private TextView txtSoLuong;
    private TextView txtDiaDiem;
    private TextView txtLuong;
    private TextView txtNgayBatDau;
    private TextView txtNgayKetThuc;
    private TextView txtXemBanDo;
    private ExpandableHeightListView listDanhGia;
    private FloatingActionButton btnAdd;
    private ProgressBar progressBar;
    private FrameLayout frame_progressbar;
    private TextView txtThem;

    private CountDownTimer timer = new CountDownTimer(450000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            AdsServer adsServer = new AdsServer();
            adsServer.execute("5");
        }
    };


    int id_post;
    int id_user_post;
    String phone_post;
    double latitude;
    double longitude;
    int category_id;
    int category_parent_id;
    UserDTO userDTO;
    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;
    PostResultDTO postResultDTO;
    int type;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    List<Banner> adsList;
    Gson gson = new Gson();

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
            perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_post));
                    SessionType.getInstance().setType(2);
                    SessionType.getInstance().setSessionType(1);
                    SessionType.getInstance().setPost_id(id_post);
                    SessionType.getInstance().setId(id_user_post);
                    SessionType.getInstance().setPhone(userDTO.getPhone());
                    SessionType.getInstance().setCategory(postResultDTO.getCategory());
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                    finish();
                } else {
                    return;
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(ChiTietCongViecTimViecActivity.this, getString(R.string.toast_chiasethanhcong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        setContentView(R.layout.activity_chitietcongviec_timviec);
        findViews();
        Intent intent = getIntent();
        int id = intent.getIntExtra(Common.JsonKey.KEY_USER_ID, 0); // id post
        type = intent.getIntExtra("type", 0);
        sessionUser = new SessionUser(getBaseContext());
        AdsServer adsServer = new AdsServer();
        adsServer.execute("5");
        GetPostServer getPostServer = new GetPostServer();
        getPostServer.execute(id + "");
    }

    private void findViews() {
        flipperAds = findViewById(R.id.flipper_ads);
        txtThem = (TextView) findViewById(R.id.txtThem);
        frame_progressbar = (FrameLayout) findViewById(R.id.frame_progressBar);
        txtXemBanDo = (TextView) findViewById(R.id.txtXemBanDo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgSave = (ImageView) findViewById(R.id.imgSave);
        txtTenNguoiTuyenDung = (TextView) findViewById(R.id.txtTenNguoiTuyenDung);
        imgTheoDoi = (ImageView) findViewById(R.id.imgTheoDoi);
        imgChiaSe = (ImageView) findViewById(R.id.imgChiaSe);
        imgThemTask = (ImageView) findViewById(R.id.imgThemTask);
        btnLienLac = (ImageButton) findViewById(R.id.btnLienLac);
        txtLoaiHinh = (TextView) findViewById(R.id.txtLoaiHinh);
        txtTieuDe = (TextView) findViewById(R.id.txtTieuDe);
        txtDiaDiem = (TextView) findViewById(R.id.txtDiaDiem);
        txtMota = (TextView) findViewById(R.id.txtMota);
        txtSoLuong = (TextView) findViewById(R.id.txtSoLuong);
        txtLuong = (TextView) findViewById(R.id.txtLuong);
        txtNgayBatDau = (TextView) findViewById(R.id.txtNgayBatDau);
        txtNgayKetThuc = (TextView) findViewById(R.id.txtNgayKetThuc);
        listDanhGia = (ExpandableHeightListView) findViewById(R.id.listDanhGia);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        imgTheoDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteServer deleteFavouriteServer = new FavouriteServer();
                Log.d("favourite", sessionUser.getUserDetails().getToken() + " " + userDTO.isFavourite() + " " + userDTO.getId());
                if (userDTO.isFavourite()) {
                    deleteFavouriteServer.execute(sessionUser.getUserDetails().getToken(), userDTO.getId() + "", "1", "DELETE");
                } else {
                    deleteFavouriteServer.execute(sessionUser.getUserDetails().getToken(), userDTO.getId() + "", "1", "POST");
                }
            }
        });

        btnLienLac.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ChiTietCongViecTimViecActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.CALL_PHONE);
                    permissions.add(Manifest.permission.READ_PHONE_STATE);
                    requestPermissions(permissions.toArray(new String[permissions.size()]), 100);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_post));
                    SessionType.getInstance().setType(2);
                    SessionType.getInstance().setSessionType(1);
                    SessionType.getInstance().setPost_id(id_post);
                    SessionType.getInstance().setId(id_user_post);
                    SessionType.getInstance().setPhone(userDTO.getPhone());
                    SessionType.getInstance().setCategory(postResultDTO.getCategory());
                    startActivity(intent);
                    finish();
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChiTietCongViecTimViecActivity.this, ThemViecLamTimViecActivity.class));
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 3) {
                    Intent intent = new Intent(ChiTietCongViecTimViecActivity.this, SplashScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else finish();
            }
        });

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostFavouriteServer postFavouriteServer = new PostFavouriteServer();
                postFavouriteServer.execute(sessionUser.getUserDetails().getToken(), "2", id_post + "");
            }
        });

        txtXemBanDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("category_id", category_id);
                    intent.putExtra("category_parent_id", category_parent_id);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    setResult(TimViecActivity.CODE_RESPONSE, intent);
                    finish();
                } else {
                    Intent intent = new Intent(ChiTietCongViecTimViecActivity.this, TimViecActivity.class);
                    intent.putExtra("category_id", category_id);
                    intent.putExtra("category_parent_id", category_parent_id);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        imgChiaSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetShareLink getShareLink = new GetShareLink();
                getShareLink.execute(sessionUser.getUserDetails().getToken(), Common.Type.TYPE_SHARE_POST, id_post + "");
            }
        });

        imgThemTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietCongViecTimViecActivity.this, NhapThongTinTimViecActivity.class);
                SessionType.getInstance().setType(3);
                SessionType.getInstance().setPost_id(id_post);
                SessionType.getInstance().setId(id_user_post);
                SessionType.getInstance().setPhone(userDTO.getPhone());
                SessionType.getInstance().setCategory(postResultDTO.getCategory());
                startActivity(intent);
                finish();
            }
        });

        txtThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionType.getInstance().setType(2);
                SessionType.getInstance().setPost_id(id_post);
                SessionType.getInstance().setId(id_user_post);
                startActivity(new Intent(ChiTietCongViecTimViecActivity.this, DanhGiaTimViecActivity.class));
                finish();
            }
        });
    }

    private class GetPostServer extends AsyncTask<String, Void, String> {

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
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST + "/" + urls[0], "GET", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (getBaseContext() == null)
                return;
            if (result != null) {
                Log.e("RESULT", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        postResultDTO = new PostResultDTO();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONObject jsonData = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA);
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_ID))
                                postResultDTO.setId(jsonData.getInt(Common.JsonKey.KEY_POST_ID));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_USER_ID))
                                postResultDTO.setUser_id(jsonData.getInt(Common.JsonKey.KEY_POST_USER_ID));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID))
                                postResultDTO.setCategory_parent_id(jsonData.getInt(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID));
                            if (!jsonData.isNull(KEY_POST_CATEGORY_ID))
                                postResultDTO.setCategory_id(jsonData.getInt(KEY_POST_CATEGORY_ID));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_TITLE))
                                postResultDTO.setTitle(jsonData.getString(Common.JsonKey.KEY_POST_TITLE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_RATING))
                                postResultDTO.setStar(jsonData.getDouble(Common.JsonKey.KEY_POST_RATING));
                            else postResultDTO.setStar(0);
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_QUANTITY))
                                postResultDTO.setQuantity(jsonData.getInt(Common.JsonKey.KEY_POST_QUANTITY));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_ADDRESS))
                                postResultDTO.setAddress(jsonData.getString(Common.JsonKey.KEY_POST_ADDRESS));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_LATITUDE))
                                postResultDTO.setLatitude((float) jsonData.getDouble(Common.JsonKey.KEY_POST_LATITUDE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_LONGITUDE))
                                postResultDTO.setLongitude((float) jsonData.getDouble(Common.JsonKey.KEY_POST_LONGITUDE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_SALARY))
                                postResultDTO.setSalary(Double.parseDouble(jsonData.getString(Common.JsonKey.KEY_POST_SALARY)));
                            if (!jsonData.isNull(KEY_POST_SALARY_TYPE))
                                postResultDTO.setSalary_type(jsonData.getInt(KEY_POST_SALARY_TYPE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_START_DATE))
                                postResultDTO.setDate_start(Validate.StringDateFormatToSetText(jsonData.getString(Common.JsonKey.KEY_POST_START_DATE)));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_END_DATE))
                                postResultDTO.setDate_end(Validate.StringDateFormatToSetText(jsonData.getString(Common.JsonKey.KEY_POST_END_DATE)));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_DESCRIPTION))
                                postResultDTO.setDescription(jsonData.getString(Common.JsonKey.KEY_POST_DESCRIPTION));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_TYPE))
                                postResultDTO.setType(jsonData.getInt(Common.JsonKey.KEY_POST_TYPE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_SEX))
                                if (jsonData.getInt(Common.JsonKey.KEY_POST_SEX) == 1)
                                    postResultDTO.setSex(getString(R.string.nu));
                                else postResultDTO.setSex(getString(R.string.nam));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_USER)) {
                                UserDTO userDTO = new UserDTO();
                                JSONObject jsonUser = jsonData.getJSONObject(Common.JsonKey.KEY_POST_USER);
                                userDTO.setId(jsonUser.getInt(Common.JsonKey.KEY_USER_ID));
                                userDTO.setPhone(jsonUser.getString(Common.JsonKey.KEY_USER_PHONE));
                                userDTO.setFull_name(jsonUser.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                                postResultDTO.setUser(userDTO);
                            }
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_CATEGORY)) {
                                CategoryDTO categoryDTO = new CategoryDTO();
                                JSONObject jsonCate = jsonData.getJSONObject(Common.JsonKey.KEY_POST_CATEGORY);
                                categoryDTO.setId(jsonCate.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                categoryDTO.setName(jsonCate.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                postResultDTO.setCategory(categoryDTO);
                            }
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_PARENT_CATEGORY)) {
                                CategoryDTO categoryDTO = new CategoryDTO();
                                JSONObject jsonCateParent = jsonData.getJSONObject(Common.JsonKey.KEY_POST_PARENT_CATEGORY);
                                categoryDTO.setId(jsonCateParent.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                categoryDTO.setName(jsonCateParent.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                postResultDTO.setCategory_parent(categoryDTO);
                            }
                        }
                        Log.e("DATE", postResultDTO.getDate_start() + " " + postResultDTO.getDate_end());
                        txtTenNguoiTuyenDung.setText(postResultDTO.getUser().getFull_name());
                        txtLoaiHinh.setText(postResultDTO.getCategory().getName());
                        txtTieuDe.setText(postResultDTO.getTitle());
                        txtSoLuong.setText(postResultDTO.getQuantity() + "");
                        txtDiaDiem.setText(postResultDTO.getAddress());
//                        int salary = (int) postResultDTO.getSalary();
                        DecimalFormat myFormatter = new DecimalFormat("###,###,###.###");
                        String salary = myFormatter.format(postResultDTO.getSalary());
                        if (postResultDTO.getSalary_type() == 1) {
                            txtLuong.setText(salary + App.getSalaryPerTypeInstance().getSalaryTypeHour());
                        } else if (postResultDTO.getSalary_type() == 2) {
                            txtLuong.setText(salary + App.getSalaryPerTypeInstance().getSalaryTypeDay());
                        } else if (postResultDTO.getSalary_type() == 3) {
                            txtLuong.setText(salary + App.getSalaryPerTypeInstance().getSalaryTypeWeek());
                        } else if (postResultDTO.getSalary_type() == 4) {
                            txtLuong.setText(salary + App.getSalaryPerTypeInstance().getSalaryTypeMonth());
                        } else if (postResultDTO.getSalary_type() == 5) {
                            txtLuong.setText(salary + App.getSalaryPerTypeInstance().getSalaryTypeTime());
                        } else if (postResultDTO.getSalary_type() == 6) {
                            txtLuong.setText(App.getSalaryPerTypeInstance().getSalaryTypeAgreement());
                        }
                        txtMota.setText(Html.fromHtml("<strong>" + getString(R.string.chitietcongviec_tv_mota) + " </strong>" + postResultDTO.getDescription().replace("\n", "<br>")));
                        txtNgayBatDau.setText(postResultDTO.getDate_start());
                        txtNgayKetThuc.setText(postResultDTO.getDate_end());
                        id_post = postResultDTO.getId();
                        latitude = postResultDTO.getLatitude();
                        longitude = postResultDTO.getLongitude();
                        category_id = postResultDTO.getCategory_id();
                        category_parent_id = postResultDTO.getCategory_parent_id();
                        id_user_post = postResultDTO.getUser_id();

                        CommendServer commendServer = new CommendServer();
                        commendServer.execute(sessionUser.getUserDetails().getToken(), postResultDTO.getId() + "", Common.Type.TYPE_POST);
                        ProfileServer profileServer = new ProfileServer();
                        profileServer.execute(sessionUser.getUserDetails().getToken(), postResultDTO.getUser_id() + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
            }
            super.onPostExecute(result);
        }

    }

    private class CommendServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_SOURCE_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_TYPE, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_VOTE, "GET", params);

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
                Log.e("RESULT + Commend", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        List<DanhGiaDTO> danhGiaDTOList = new ArrayList<>();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONArray jsondata = new JSONArray(jsonResult.getString(Common.JsonKey.KEY_DATA).toString());
                            DanhGiaDTO danhGiaDTO = new DanhGiaDTO();
                            for (int i = 0; i < jsondata.length(); i++) {
                                JSONObject jsonObject = jsondata.getJSONObject(i);
                                danhGiaDTO.setId(jsonObject.getInt(Common.JsonKey.KEY_VOTE_ID));
                                danhGiaDTO.setUser_id(jsonObject.getInt(Common.JsonKey.KEY_VOTE_SOURCE_ID));
                                danhGiaDTO.setRate(jsonObject.getDouble(Common.JsonKey.KEY_VOTE_RATE));
                                danhGiaDTO.setComment(jsonObject.getString(Common.JsonKey.KEY_VOTE_COMMENT));
                                danhGiaDTO.setType(jsonObject.getInt(Common.JsonKey.KEY_VOTE_TYPE));
                                JSONObject userJson = jsonObject.getJSONObject(Common.JsonKey.KEY_VOTE_OWNER);
                                UserDTO owner = new UserDTO();
                                JSONObject ownerJson = jsonObject.getJSONObject(Common.JsonKey.KEY_VOTE_OWNER);
                                owner.setFull_name(ownerJson.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                                owner.setId(ownerJson.getInt(Common.JsonKey.KEY_USER_ID));
                                danhGiaDTO.setOwner(owner);
                                danhGiaDTOList.add(danhGiaDTO);
                            }
                        }
                        DanhGiaAdapter danhGiaAdapter = new DanhGiaAdapter(ChiTietCongViecTimViecActivity.this, danhGiaDTOList);
                        listDanhGia.setAdapter(danhGiaAdapter);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            frame_progressbar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }

    private class FavouriteServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TOKEN, urls[0]));
                if (urls[3] == "DELETE") {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID, urls[1]));
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE, urls[2]));
                } else {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID, urls[1]));
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE, urls[2]));
                }
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_FAVOURITE, urls[3], params);

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
                Log.e("RESULT FAVOURITE", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();
                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Intent intent = new Intent(ChiTietCongViecTimViecActivity.this, ChiTietCongViecTimViecActivity.class);
                        intent.putExtra("id", id_post);
                        startActivity(intent);
                        finish();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

    private class ProfileServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_USER_ID, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_PROFILE, "GET", params);

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
                Log.e("RESULT", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        userDTO = new UserDTO();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA).toString());
                            userDTO.setId(jsondata.getInt(Common.JsonKey.KEY_USER_ID));

                            userDTO.setPhone(jsondata.getString(Common.JsonKey.KEY_USER_PHONE));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_SEX)) {
                                if (jsondata.getInt(Common.JsonKey.KEY_USER_SEX) == 1)
                                    userDTO.setSex(getString(R.string.nu));
                                else userDTO.setSex(getString(R.string.nam));
                            }
                            userDTO.setFull_name(jsondata.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_BIRTHDAY)) {
                                userDTO.setBirthday(Validate.StringDateFormatToSetText(jsondata.getString(Common.JsonKey.KEY_USER_BIRTHDAY)));
                            }
//                            userDTO.setAvatar();
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_DESCRIPTION))
                                userDTO.setBirthday(Validate.StringDateFormatToSetText(jsondata.getString(Common.JsonKey.KEY_USER_DESCRIPTION)));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_ADDRESS))
                                userDTO.setAddress(jsondata.getString(Common.JsonKey.KEY_USER_ADDRESS));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_RATING))
                                userDTO.setRating(jsondata.getDouble(Common.JsonKey.KEY_USER_RATING));
                            else userDTO.setRating(0);
                            userDTO.setFavourite_count(jsondata.getInt(Common.JsonKey.KEY_USER_FAVOURITE_COUNT));
                            userDTO.setFavourite(jsondata.getBoolean(Common.JsonKey.KEY_USER_IS_FAVOURITE));
                        }
                        if (userDTO.isFavourite()) {
                            imgTheoDoi.setImageResource(R.mipmap.ic_heart_fill);
                        } else imgTheoDoi.setImageResource(R.mipmap.ic_heart_red);
                        phone_post = userDTO.getPhone();
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

    private class PostFavouriteServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_FAVOURITE, "POST", params);

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
                Log.e("RESULT FAVOURITE", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();
                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Toast.makeText(ChiTietCongViecTimViecActivity.this, getString(R.string.toast_luuthongtinthanhcong), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChiTietCongViecTimViecActivity.this, getString(R.string.toast_bandaluuthongtinroi), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

    private class GetShareLink extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_SHARE_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_SHARE_TYPE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_SHARE_POST_ID, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_SHARE_LINK + "/" + urls[1], "GET", params);

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
                Log.e("RESULT SHARE", result);
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();
                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {

                            JSONObject jsonData = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA);
                            String url = jsonData.getString(Common.JsonKey.KEY_SHARE_URL);

                            if (ShareDialog.canShow(ShareLinkContent.class)) {
                                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                        .setContentUrl(Uri.parse(url))
                                        .build();
                                shareDialog.show(linkContent);
                            }
                        }
                    } else {
                        Toast.makeText(ChiTietCongViecTimViecActivity.this, getString(R.string.toast_bandaluuthongtinroi), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
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
                                ImageView imvAds = new ImageView(ChiTietCongViecTimViecActivity.this);
                                imvAds.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                Glide.with(ChiTietCongViecTimViecActivity.this).load(adsList.get(i).getCover()).into(imvAds);
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }

}
