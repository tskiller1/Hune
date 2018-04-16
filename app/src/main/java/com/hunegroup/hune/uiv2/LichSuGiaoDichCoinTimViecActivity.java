package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.adapterv2.LichSuGiaoDichCoinTimViecAdapter;
import com.hunegroup.hune.dto.MetaDataDTO;
import com.hunegroup.hune.dto.TransactionDTO;
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
 * Created by apple on 11/20/17.
 */

public class LichSuGiaoDichCoinTimViecActivity extends AppCompatActivity {
    private ImageView imvBack;
    private ListView lsvBody;

    private Dialog dialogProgress;
    //TODO: Declaring
    SessionUser sessionUser;
    private JSONParser jsonParser = new JSONParser();
    Gson gson = new Gson();
    List<TransactionDTO> mList;
    LichSuGiaoDichCoinTimViecAdapter mAdapter;
    MetaDataDTO metaDataDTO;
    boolean loading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_lichsugiaodichcoin_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        lsvBody = (ListView) findViewById(R.id.lsv_body);

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
                            TransactionServer transactionServer = new TransactionServer();
                            transactionServer.execute(sessionUser.getUserDetails().getToken(), Common.Type.TYPE_MONEY_COIN, next_page);
                        }
                    } else loading = false;
                }
            }
        });


        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        mList = new ArrayList<>();
        mAdapter = new LichSuGiaoDichCoinTimViecAdapter(this, mList);
        lsvBody.setAdapter(mAdapter);
        loading = true;
        TransactionServer transactionServer = new TransactionServer();
        transactionServer.execute(sessionUser.getUserDetails().getToken(), Common.Type.TYPE_MONEY_COIN, "1");
    }

    private class TransactionServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_WALLET_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_WALLET_TYPE_MONEY, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_PAGE, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_WALLET_TRANSACTIONS, "GET", params);

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
                Log.e("RESULT + HISTORY", result);
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONArray jsonData = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                            for (int i = 0; i < jsonData.length(); i++) {
                                JSONObject jsonObject = jsonData.getJSONObject(i);
                                TransactionDTO transactionDTO = new TransactionDTO();
                                if (!jsonObject.isNull(Common.JsonKey.KEY_TRANSACTION_NAME)) {
                                    transactionDTO.setName(jsonObject.getString(Common.JsonKey.KEY_TRANSACTION_NAME));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_TRANSACTION_AMOUNT)) {
                                    transactionDTO.setAmount(jsonObject.getDouble(Common.JsonKey.KEY_TRANSACTION_AMOUNT));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_TRANSACTION_TYPE_MONEY)) {
                                    transactionDTO.setType_money(jsonObject.getInt(Common.JsonKey.KEY_TRANSACTION_TYPE_MONEY));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_TRANSACTION_DESCRIPTION)) {
                                    transactionDTO.setDescription(jsonObject.getString(Common.JsonKey.KEY_TRANSACTION_DESCRIPTION));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_TRANSACTION_TXID)) {
                                    transactionDTO.setTxid(jsonObject.getString(Common.JsonKey.KEY_TRANSACTION_TXID));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_TRANSACTION_TYPE)) {
                                    transactionDTO.setType(jsonObject.getInt(Common.JsonKey.KEY_TRANSACTION_TYPE));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_TRANSACTION_DEFINE_TRANSACTION)) {
                                    transactionDTO.setDefine_transaction(jsonObject.getInt(Common.JsonKey.KEY_TRANSACTION_DEFINE_TRANSACTION));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_TRANSACTION_CREATED_AT)) {
                                    transactionDTO.setCreated_at(jsonObject.getString(Common.JsonKey.KEY_TRANSACTION_CREATED_AT));
                                }
                                mList.add(transactionDTO);
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
