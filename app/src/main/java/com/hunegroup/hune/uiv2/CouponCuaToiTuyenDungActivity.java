package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.adapterv2.CouponCuaToiTuyenDungAdapter;
import com.hunegroup.hune.dto.CouponDTO;
import com.hunegroup.hune.dto.MetaDataDTO;
import com.hunegroup.hune.dto.MyCoupon;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 11/22/17.
 */

public class CouponCuaToiTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private RecyclerView rcvBody;

    Dialog dialogProgress;
    //TODO: Declaring
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    MetaDataDTO metaDataDTO;
    List<MyCoupon> mList;
    CouponCuaToiTuyenDungAdapter mAdapter;
    Gson gson = new Gson();
    boolean loading = false;
    int visibleItemCount;
    int totalItemCount;
    int pastVisiblesItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_couponcuatoi_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        rcvBody = (RecyclerView) findViewById(R.id.rcv_body);

        rcvBody.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    GridLayoutManager layoutManager = (GridLayoutManager) rcvBody.getLayoutManager();
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (!loading && metaDataDTO.isHas_more_page()) {
                            loading = true;
                            //bottom of recyclerview
                            String next_page = (metaDataDTO.getCurrent_page() + 1) + "";
                            GetCouponServer getCouponServer = new GetCouponServer();
                            getCouponServer.execute(sessionUser.getUserDetails().getToken(), next_page);
                        }
                    }
                } catch (Exception ignored) {

                }
            }
        });


        imvBack.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        mList = new ArrayList<>();
        mAdapter = new CouponCuaToiTuyenDungAdapter(this, mList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rcvBody.setLayoutManager(layoutManager);
        rcvBody.setAdapter(mAdapter);
        loading = true;
        GetCouponServer getCouponServer = new GetCouponServer();
        getCouponServer.execute(sessionUser.getUserDetails().getToken(), "1");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
        }
    }

    private class GetCouponServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_PAGE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_COUPON_LIMIT, "16"));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_MY_COUPON, "GET", params);

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
                    Log.e("GET COUPON", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {

                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONArray jsonData = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                            for (int i = 0; i < jsonData.length(); i++) {
                                MyCoupon coupon = new MyCoupon();
                                JSONObject item = jsonData.getJSONObject(i);
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_ID)) {
                                    coupon.setId(item.getInt(Common.JsonKey.KEY_COUPON_ID));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_CODE)) {
                                    coupon.setCode(item.getString(Common.JsonKey.KEY_COUPON_CODE));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_GROUP_ID)) {
                                    coupon.setGroup_id(item.getInt(Common.JsonKey.KEY_COUPON_GROUP_ID));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_USER_ID)) {
                                    coupon.setUser_id(item.getInt(Common.JsonKey.KEY_COUPON_USER_ID));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_COUPON_GROUP)) {
                                    JSONObject coupon_group = item.getJSONObject(Common.JsonKey.KEY_COUPON_COUPON_GROUP);
                                    CouponDTO group = new CouponDTO();
                                    if (!coupon_group.isNull(Common.JsonKey.KEY_COUPON_ID)) {
                                        group.setId(coupon_group.getInt(Common.JsonKey.KEY_COUPON_ID));
                                    }
                                    if (!coupon_group.isNull(Common.JsonKey.KEY_COUPON_NAME)) {
                                        group.setName(coupon_group.getString(Common.JsonKey.KEY_COUPON_NAME));
                                    }
                                    if (!coupon_group.isNull(Common.JsonKey.KEY_COUPON_IMAGE)) {
                                        group.setImage(coupon_group.getString(Common.JsonKey.KEY_COUPON_IMAGE));
                                    }
                                    if (!coupon_group.isNull(Common.JsonKey.KEY_COUPON_PRICE)) {
                                        group.setPrice(coupon_group.getDouble(Common.JsonKey.KEY_COUPON_PRICE));
                                    }
                                    coupon.setCoupon_DTO_group(group);
                                }
                                mList.add(coupon);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_META_DATA)) {
                            metaDataDTO = new MetaDataDTO();
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
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

}
