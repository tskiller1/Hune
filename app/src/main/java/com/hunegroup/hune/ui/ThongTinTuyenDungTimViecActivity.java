package com.hunegroup.hune.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.adapter.ThongTinTuyenDungAdapter_TimViec;
import com.hunegroup.hune.dto.Banner;
import com.hunegroup.hune.dto.Category_ThongTinTimViecDTO;
import com.hunegroup.hune.dto.MetaDataDTO;
import com.hunegroup.hune.dto.Parent_Category_ThongTinTimViecDTO;
import com.hunegroup.hune.dto.ThongTinTimViecDTO;
import com.hunegroup.hune.dto.User_ThongTinTimViecDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionLocation;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 06/07/2017.
 */

public class ThongTinTuyenDungTimViecActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewFlipper flipperAds;
    private ImageView imgBack;
    private ImageView imgNotify;
    private ListView listThongTin;
    private FloatingActionButton btnAdd;
    private ProgressBar progressBar;
    private FrameLayout frame_progressbar;

    private CountDownTimer timer = new CountDownTimer(450000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            AdsServer adsServer = new AdsServer();
            adsServer.execute("4");
        }
    };


    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;
    boolean loading = false;
    MetaDataDTO metaDataDTO;
    public static List<ThongTinTimViecDTO> thongTinTimViecDTOs;
    private ThongTinTuyenDungAdapter_TimViec thongTinTuyenDungAdapter_timViec;
    List<Banner> adsList;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switchLangauge(this, true);
        setContentView(R.layout.activity_thongtintuyendung_timviec);
        findViews();
        AdsServer adsServer = new AdsServer();
        adsServer.execute("4");
        setListThongTin();
    }

    private void findViews() {
        flipperAds = findViewById(R.id.flipper_ads);
        frame_progressbar = (FrameLayout) findViewById(R.id.frame_progressBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgNotify = (ImageView) findViewById(R.id.imgNotify);
        listThongTin = (ListView) findViewById(R.id.listThongTin);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        imgBack.setOnClickListener(this);
        imgNotify.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        listThongTin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ThongTinTimViecDTO item = thongTinTimViecDTOs.get(i);
                Intent intent = new Intent(getBaseContext(), ChiTietCongViecTimViecActivity.class);
                intent.putExtra(Common.JsonKey.KEY_USER_ID, item.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        listThongTin.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (!loading) {
                    loading = true;
                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && totalItemCount < metaDataDTO.getTotal()) {
                        if (metaDataDTO.isHas_more_page()) {
                            String next_page = (metaDataDTO.getCurrent_page() + 1) + "";
                            getPostRelateServer getPostRelateServer = new getPostRelateServer();
                            if (SessionLocation.getInstance().isLatLng()) {
                                Log.d("VITRIHIENTAI", sessionUser.getUserDetails().getToken() + " " + SessionLocation.getInstance().getLocation().toString() + " " + SessionLocation.getInstance().getRadius() + "");
                                loading = true;
                                getPostRelateServer.execute(sessionUser.getUserDetails().getToken(), "1", SessionLocation.getInstance().getLocation().latitude + "", SessionLocation.getInstance().getLocation().longitude + "", SessionLocation.getInstance().getRadius() + "", next_page);
                            } else {
                                loading = false;
                                Toast.makeText(getBaseContext(), "Chưa có thông tin về vị trí của bạn", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else loading = false;
                }
            }
        });
    }

    private void setListThongTin() {
        thongTinTimViecDTOs = new ArrayList<>();

        thongTinTuyenDungAdapter_timViec = new ThongTinTuyenDungAdapter_TimViec(getBaseContext(), thongTinTimViecDTOs);
        listThongTin.setAdapter(thongTinTuyenDungAdapter_timViec);
        sessionUser = new SessionUser(getBaseContext());
        ThongTinTuyenDungTimViecActivity.getPostRelateServer getPostRelateServer = new ThongTinTuyenDungTimViecActivity.getPostRelateServer();

        if (SessionLocation.getInstance().isLatLng()) {
            getPostRelateServer.execute(sessionUser.getUserDetails().getToken(), "1", SessionLocation.getInstance().getLocation().latitude + "", SessionLocation.getInstance().getLocation().longitude + "", SessionLocation.getInstance().getRadius() + "", "1");
            loading = true;
        } else {
            Toast.makeText(getBaseContext(), "Chưa có thông tin về vị trí của bạn", Toast.LENGTH_SHORT).show();
            loading = false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imgBack: {
                finish();
                break;
            }
            case R.id.imgNotify: {
                startActivity(new Intent(ThongTinTuyenDungTimViecActivity.this, ThongBaoTimViecActivity.class));
                break;
            }
            case R.id.btnAdd: {
                startActivity(new Intent(ThongTinTuyenDungTimViecActivity.this, ThemViecLamTimViecActivity.class));
                break;
            }
        }
    }

    private class getPostRelateServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TYPE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LATITUDE, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LONGITUDE, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_RADIUS, urls[4]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_PAGE, urls[5]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_RELATE, "GET", params);

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
                    Log.e("result", result);
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        JSONArray jsonArray = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                        metaDataDTO = new MetaDataDTO();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ThongTinTimViecDTO thongTinTimViecDTO = new ThongTinTimViecDTO();
                            JSONObject jsondata = jsonArray.getJSONObject(i);
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_ID)) {
                                int id = jsondata.getInt(Common.JsonKey.KEY_POST_ID);
                                thongTinTimViecDTO.setId(id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_TITLE)) {
                                String title = jsondata.getString(Common.JsonKey.KEY_POST_TITLE) + "";
                                thongTinTimViecDTO.setTitle(title);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_DESCRIPTION)) {
                                String description = jsondata.getString(Common.JsonKey.KEY_POST_DESCRIPTION) + "";
                                thongTinTimViecDTO.setDecription(description);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_RATING)) {
                                String rating = jsondata.getDouble(Common.JsonKey.KEY_POST_RATING) + "";
                                thongTinTimViecDTO.setRating(Float.parseFloat(rating));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_USER_ID)) {
                                int user_id = jsondata.getInt(Common.JsonKey.KEY_POST_USER_ID);
                                thongTinTimViecDTO.setUser_id(user_id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_CATEGORY_ID)) {
                                int category_id = jsondata.getInt(Common.JsonKey.KEY_POST_CATEGORY_ID);
                                thongTinTimViecDTO.setCategory_id(category_id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID)) {
                                int category_parent_id = jsondata.getInt(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID);
                                thongTinTimViecDTO.setCategory_parent_id(category_parent_id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_DISTANCE)) {
                                String distance = jsondata.getString(Common.JsonKey.KEY_POST_DISTANCE);
                                thongTinTimViecDTO.setDistance(distance);
                            }

                            User_ThongTinTimViecDTO user_thongTinTimViecDTO = new User_ThongTinTimViecDTO();
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_USER)) {
                                JSONObject jsonObject = jsondata.getJSONObject(Common.JsonKey.KEY_POST_USER);

                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_ID)) {
                                    int id = jsonObject.getInt(Common.JsonKey.KEY_POST_ID);
                                    user_thongTinTimViecDTO.setId(id);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_PHONE)) {
                                    String phone = jsonObject.getString(Common.JsonKey.KEY_POST_PHONE);
                                    user_thongTinTimViecDTO.setPhone(phone);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_FULL_NAME)) {
                                    String full_name = jsonObject.getString(Common.JsonKey.KEY_POST_FULL_NAME);
                                    user_thongTinTimViecDTO.setFull_name(full_name);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_AVATAR)) {
                                    String avatar = jsonObject.getString(Common.JsonKey.KEY_POST_AVATAR);
                                    user_thongTinTimViecDTO.setAvatar(avatar);
                                }
                            }
                            Category_ThongTinTimViecDTO category_thongTinTimViecDTO = new Category_ThongTinTimViecDTO();
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_CATEGORY)) {
                                JSONObject jsonObject = jsondata.getJSONObject(Common.JsonKey.KEY_POST_CATEGORY);

                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_ID)) {
                                    int id = jsonObject.getInt(Common.JsonKey.KEY_POST_ID);
                                    category_thongTinTimViecDTO.setId(id);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_NAME)) {
                                    String name = jsonObject.getString(Common.JsonKey.KEY_POST_NAME);
                                    category_thongTinTimViecDTO.setName(name);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_ICON)) {
                                    String icon = jsonObject.getString(Common.JsonKey.KEY_POST_ICON);
                                    category_thongTinTimViecDTO.setUrlImage(icon);
                                }
                            }
                            Parent_Category_ThongTinTimViecDTO parent_category_thongTinTimViecDTO = new Parent_Category_ThongTinTimViecDTO();
                            if (!jsondata.isNull(Common.JsonKey.KEY_POST_PARENT_CATEGORY)) {
                                JSONObject jsonObject = jsondata.getJSONObject(Common.JsonKey.KEY_POST_PARENT_CATEGORY);

                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_ID)) {
                                    int id = jsonObject.getInt(Common.JsonKey.KEY_POST_ID);
                                    parent_category_thongTinTimViecDTO.setId(id);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_NAME)) {
                                    String name = jsonObject.getString(Common.JsonKey.KEY_POST_NAME);
                                    parent_category_thongTinTimViecDTO.setName(name);
                                }
                            }

                            thongTinTimViecDTO.setUser(user_thongTinTimViecDTO);
                            thongTinTimViecDTO.setCategory(category_thongTinTimViecDTO);
                            thongTinTimViecDTO.setParent_category(parent_category_thongTinTimViecDTO);
                            thongTinTimViecDTOs.add(thongTinTimViecDTO);
                            thongTinTuyenDungAdapter_timViec.notifyDataSetChanged();
                            if (!jsonResult.isNull(Common.JsonKey.KEY_META_DATA)) {
                                JSONObject jsonMetaData = jsonResult.getJSONObject(Common.JsonKey.KEY_META_DATA);
                                metaDataDTO.setTotal(jsonMetaData.getInt(Common.JsonKey.KEY_META_DATA_TOTAL));
                                metaDataDTO.setCurrent_page(jsonMetaData.getInt(Common.JsonKey.KEY_META_DATA_CURRENT_PAGE));
                                metaDataDTO.setHas_more_page(jsonMetaData.getBoolean(Common.JsonKey.KEY_META_DATA_HAS_MORE_PAGE));
                                metaDataDTO.setNext_link(jsonMetaData.getString(Common.JsonKey.KEY_META_DATA_NEXT_LINK));
                            }
                        }

                        int x = 0;
                        thongTinTimViecDTOs.size();
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            loading = false;
            frame_progressbar.setVisibility(View.GONE);
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
                                ImageView imvAds = new ImageView(ThongTinTuyenDungTimViecActivity.this);
                                imvAds.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                Glide.with(ThongTinTuyenDungTimViecActivity.this).load(adsList.get(i).getCover()).into(imvAds);
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
                            ViewGroup.LayoutParams layoutParams = flipperAds.getLayoutParams();
                            layoutParams.height = (int) getResources().getDimension(R.dimen.size_50);
                            flipperAds.setLayoutParams(layoutParams);
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
