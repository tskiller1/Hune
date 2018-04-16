package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.adapterv2.MuaCouponTuyenDungAdapter;
import com.hunegroup.hune.dto.CouponDTO;
import com.hunegroup.hune.dto.MetaDataDTO;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Utilities;

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

public class MuaCouponTuyenDungActivity extends AppCompatActivity implements View.OnClickListener, MuaCouponTuyenDungAdapter.OnItemClick {
    private ImageView imvBack;
    private TextView tvBalance;
    private RecyclerView rcvBody;

    Dialog dialogProgress;
    //TODO: Declaring
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    Gson gson = new Gson();
    MetaDataDTO metaDataDTO;
    UserDTO userDTO;
    List<CouponDTO> mList;
    MuaCouponTuyenDungAdapter mAdapter;
    boolean loading = false;
    int type;
    int visibleItemCount;
    int totalItemCount;
    int pastVisiblesItems;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 200) {
            ProfileServer profileServer = new ProfileServer();
            profileServer.execute(sessionUser.getUserDetails().getToken());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_muacoupon_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
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
        mAdapter = new MuaCouponTuyenDungAdapter(this, mList, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rcvBody.setLayoutManager(layoutManager);
        rcvBody.setAdapter(mAdapter);
        loading = true;
        ProfileServer profileServer = new ProfileServer();
        profileServer.execute(sessionUser.getUserDetails().getToken());
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

    @Override
    public void onItemClickListener(View view, int position, Object item) {
        CouponDTO couponDTO = (CouponDTO) item;
        Intent intent = new Intent(this, ChiTietCouponTuyenDungActivity.class);
        intent.putExtra("item", gson.toJson(item));
        startActivityForResult(intent,101);
    }

    @Override
    public void onItemBuySuccessListener() {
        ProfileServer profileServer = new ProfileServer();
        profileServer.execute(sessionUser.getUserDetails().getToken());
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
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_COUPON, "GET", params);

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
                                CouponDTO couponDTO = new CouponDTO();
                                JSONObject item = jsonData.getJSONObject(i);
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_ID)) {
                                    couponDTO.setId(item.getInt(Common.JsonKey.KEY_COUPON_ID));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_PARTNER_ID)) {
                                    couponDTO.setPartner_id(item.getInt(Common.JsonKey.KEY_COUPON_PARTNER_ID));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_NAME)) {
                                    couponDTO.setName(item.getString(Common.JsonKey.KEY_COUPON_NAME));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_IMAGE)) {
                                    couponDTO.setImage(item.getString(Common.JsonKey.KEY_COUPON_IMAGE));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_PRICE)) {
                                    couponDTO.setPrice(item.getDouble(Common.JsonKey.KEY_COUPON_PRICE));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_TO_DATE)) {
                                    couponDTO.setTo_date(item.getString(Common.JsonKey.KEY_COUPON_TO_DATE));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_FROM_DATE)) {
                                    couponDTO.setFrom_date(item.getString(Common.JsonKey.KEY_COUPON_FROM_DATE));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_CREATED_AT)) {
                                    couponDTO.setCreated_at(item.getString(Common.JsonKey.KEY_COUPON_CREATED_AT));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_COUPON_UPDATED_AT)) {
                                    couponDTO.setUpdated_at(item.getString(Common.JsonKey.KEY_COUPON_UPDATED_AT));
                                }
                                mList.add(couponDTO);
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

    private class ProfileServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_TOKEN, urls[0]));
//                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_USER_ID, urls[1]));
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
                Log.e("RESULT + PROFILE", result);
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        userDTO = new UserDTO();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA));
                            userDTO.setId(jsondata.getInt(Common.JsonKey.KEY_USER_ID));

                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_TOTAL_CASH)) {
                                userDTO.setTotal_cash(jsondata.getDouble(Common.JsonKey.KEY_USER_TOTAL_CASH));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_BALANCE_CASH)) {
                                userDTO.setBalance_cash(jsondata.getDouble(Common.JsonKey.KEY_USER_BALANCE_CASH));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_TOTAL_COIN)) {
                                userDTO.setTotal_coin(jsondata.getDouble(Common.JsonKey.KEY_USER_TOTAL_COIN));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_BALANCE_COIN)) {
                                userDTO.setBalance_coin(jsondata.getDouble(Common.JsonKey.KEY_USER_BALANCE_COIN));
                            }
                        }
                        tvBalance.setText(Utilities.NubmerFormatText(String.format("%.0f", userDTO.getBalance_coin())));
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

}
