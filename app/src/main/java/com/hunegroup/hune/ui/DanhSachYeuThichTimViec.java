package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.adapter.DanhSachYeuThichAdapter_TimViec;
import com.hunegroup.hune.dto.FavouriteDTO;
import com.hunegroup.hune.dto.MetaDataDTO;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
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
 * Created by tskil on 7/31/2017.
 */

public class DanhSachYeuThichTimViec extends AppCompatActivity {

    public static List<FavouriteDTO> favouriteDTOList;
    private DanhSachYeuThichAdapter_TimViec danhSachYeuThichAdapter;

    private ImageView imgBack;
    private ImageView imgNotify;
    private ListView listThongTin;
    private FloatingActionButton btnAdd;
    private ProgressBar frame_progressBar;
    private JSONParser jsonParser = new JSONParser();
    private SessionUser sessionUser;
    private MetaDataDTO metaDataDTO;
    private boolean loading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_danhsachyeuthich_timviec);
        findViews();

        sessionUser = new SessionUser(getBaseContext());
        favouriteDTOList = new ArrayList<>();
        danhSachYeuThichAdapter = new DanhSachYeuThichAdapter_TimViec(DanhSachYeuThichTimViec.this, favouriteDTOList);
        listThongTin.setAdapter(danhSachYeuThichAdapter);

        FavouriteServer favouriteServer = new FavouriteServer();
        loading = true;
        favouriteServer.execute(sessionUser.getUserDetails().getToken(), Common.Type.TYPE_USER, "1");
    }

    private void findViews() {
        frame_progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgNotify = (ImageView) findViewById(R.id.imgNotify);
        listThongTin = (ListView) findViewById(R.id.listThongTin);
        listThongTin.setVerticalScrollBarEnabled(false);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DanhSachYeuThichTimViec.this, ThemViecLamTimViecActivity.class));
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DanhSachYeuThichTimViec.this, ThongBaoTimViecActivity.class));

            }
        });

        listThongTin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(DanhSachYeuThichTimViec.this, l + "", Toast.LENGTH_SHORT).show();
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
                        if (metaDataDTO.isHas_more_page() == true) {
                            String next_page = (metaDataDTO.getCurrent_page() + 1) + "";
                            FavouriteServer favouriteServer = new FavouriteServer();
                            favouriteServer.execute(sessionUser.getUserDetails().getToken(), Common.Type.TYPE_USER, next_page);
                        }
                    }
                    else loading = false;
                }
            }
        });
    }

    private class FavouriteServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_PAGE, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_FAVOURITE, "GET", params);

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
                Log.e("FAVOURITE ", result);

                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();


                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        metaDataDTO = new MetaDataDTO();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONArray jsonData = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                            for (int i = 0; i < jsonData.length(); i++) {
                                JSONObject jsonObject = jsonData.getJSONObject(i);
                                FavouriteDTO favouriteDTO = new FavouriteDTO();
                                favouriteDTO.setId(jsonObject.getInt(Common.JsonKey.KEY_FAVOURITE_ID));
                                favouriteDTO.setUser_id(jsonObject.getInt(Common.JsonKey.KEY_FAVOURITE_USER_ID));
                                favouriteDTO.setType(jsonObject.getInt(Common.JsonKey.KEY_FAVOURITE_TYPE));
                                favouriteDTO.setSource_id(jsonObject.getInt(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID));
                                JSONObject user = jsonObject.getJSONObject(Common.JsonKey.KEY_FAVOURITE_USER);
                                UserDTO userDTO = new UserDTO();
                                userDTO.setId(user.getInt(Common.JsonKey.KEY_USER_ID));
                                userDTO.setFull_name(user.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                                userDTO.setFavourite_count(user.getInt(Common.JsonKey.KEY_USER_FAVOURITE_COUNT));
                                userDTO.setRating(user.getDouble(Common.JsonKey.KEY_USER_RATING));
                                if (!user.isNull(Common.JsonKey.KEY_USER_PHONE))
                                    userDTO.setPhone(user.getString(Common.JsonKey.KEY_USER_PHONE));
                                if (!user.isNull(Common.JsonKey.KEY_USER_AVATAR))
                                    userDTO.setAvatar(user.getString(Common.JsonKey.KEY_USER_AVATAR));
                                favouriteDTO.setUserDTO(userDTO);
                                favouriteDTOList.add(favouriteDTO);
                            }
                            danhSachYeuThichAdapter.notifyDataSetChanged();
                        }
                        if (!jsonResult.isNull(Common.JsonKey.KEY_META_DATA)) {
                            JSONObject jsonMetaData = jsonResult.getJSONObject(Common.JsonKey.KEY_META_DATA);
                            metaDataDTO.setTotal(jsonMetaData.getInt(Common.JsonKey.KEY_META_DATA_TOTAL));
                            metaDataDTO.setCurrent_page(jsonMetaData.getInt(Common.JsonKey.KEY_META_DATA_CURRENT_PAGE));
                            metaDataDTO.setHas_more_page(jsonMetaData.getBoolean(Common.JsonKey.KEY_META_DATA_HAS_MORE_PAGE));
                            metaDataDTO.setNext_link(jsonMetaData.getString(Common.JsonKey.KEY_META_DATA_NEXT_LINK));
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            loading = false;
            frame_progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }
}
