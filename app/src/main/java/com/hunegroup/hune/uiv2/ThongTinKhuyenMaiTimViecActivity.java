package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hunegroup.hune.R;
import com.hunegroup.hune.adapterv2.ThongTinKhuyenMaiAdapter;
import com.hunegroup.hune.dto.MetaDataDTO;
import com.hunegroup.hune.dto.ThongTinKhuyenMai;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 12/13/17.
 */

public class ThongTinKhuyenMaiTimViecActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView imvBack;
    private ListView lsvBody;

    private Dialog dialogProgress;

    //TODO: Declaring
    Gson gson = new Gson();
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();

    ThongTinKhuyenMaiAdapter mAdapter;
    List<ThongTinKhuyenMai> mList;
    boolean loading = false;
    MetaDataDTO metaDataDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_thongtinkhuyenmai_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        lsvBody = (ListView) findViewById(R.id.lsv_body);

        lsvBody.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ThongTinKhuyenMaiTimViecActivity.this, ChiTietThongTinKhuyenMaiTimViecActivity.class);
                intent.putExtra("item", gson.toJson(mList.get(i)));
                startActivity(intent);
            }
        });

        lsvBody.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                            LocationAdsServer locationAdsServer = new LocationAdsServer();
                            locationAdsServer.execute(sessionUser.getUserDetails().getToken(), next_page);
                        }
                    } else loading = false;
                }
            }
        });


        imvBack.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        mList = new ArrayList<>();
        mAdapter = new ThongTinKhuyenMaiAdapter(this, mList);
        lsvBody.setAdapter(mAdapter);
        Log.e("token", sessionUser.getUserDetails().getToken());
        LocationAdsServer locationAdsServer = new LocationAdsServer();
        locationAdsServer.execute(sessionUser.getUserDetails().getToken(), "1");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
        }
    }

    private class LocationAdsServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            dialogProgress.show();
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_PAGE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_LIMIT, "10"));

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_ADS_LOCATION, "GET", params);

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
                Log.e("Discount Server", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        JSONArray jsonData = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                        Type listType = new TypeToken<ArrayList<ThongTinKhuyenMai>>() {
                        }.getType();
                        List<ThongTinKhuyenMai> thongTinKhuyenMais = new ArrayList<>();
                        thongTinKhuyenMais = gson.fromJson(jsonData.toString(), listType);
                        mList.addAll(thongTinKhuyenMais);
                        mAdapter.notifyDataSetChanged();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_META_DATA)) {
                            metaDataDTO = gson.fromJson(jsonResult.getJSONObject(Common.JsonKey.KEY_META_DATA).toString(), MetaDataDTO.class);
                            metaDataDTO.setHas_more_page(jsonResult.getJSONObject(Common.JsonKey.KEY_META_DATA).getBoolean(Common.JsonKey.KEY_META_DATA_HAS_MORE_PAGE));
                        }
                    } else {
//                        Toast.makeText(ThongTinKhuyenMaiTuyenDungActivity.this, "Mã giảm giá không hợp lệ!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            loading = false;
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

}
