package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.adapterv2.DanhSachLamViecTuyenDungAdapter;
import com.hunegroup.hune.dto.CategoryDTO;
import com.hunegroup.hune.dto.MetaDataDTO;
import com.hunegroup.hune.dto.PostResultDTO;
import com.hunegroup.hune.dto.TaskDTO;
import com.hunegroup.hune.dto.UserDTO;
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
 * Created by apple on 11/15/17.
 */

public class DanhSachViecLamTuyenDungActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ImageView imvBack;
    private ImageView imvFilter;
    private ListView lsvBody;
    private SwipeRefreshLayout srlRefresh;

    private Dialog dialogProgress;

    //TODO: Declaring
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    Gson gson = new Gson();
    MetaDataDTO metaDataDTO;
    List<TaskDTO> mList;
    boolean loading = false;
    DanhSachLamViecTuyenDungAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_danhsachvieclam_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvFilter = (ImageView) findViewById(R.id.imv_filter);
        lsvBody = (ListView) findViewById(R.id.lsv_body);
        srlRefresh = findViewById(R.id.srl_refresh);

        srlRefresh.setOnRefreshListener(this);

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
                            GetTaskServer getTaskServer = new GetTaskServer();
                            getTaskServer.execute(sessionUser.getUserDetails().getToken(), next_page);
                        }
                    } else loading = false;
                }
            }
        });

        lsvBody.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskDTO item = mList.get(i);
                Intent intent = new Intent(DanhSachViecLamTuyenDungActivity.this, ThongTinViecLamTuyenDungActivity.class);
                intent.putExtra(Common.IntentKey.KEY_TASK, gson.toJson(item));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
        srlRefresh.setColorSchemeColors(getResources().getColor(R.color.color_main));
        mList = new ArrayList<>();
        mAdapter = new DanhSachLamViecTuyenDungAdapter(this, mList);
        lsvBody.setAdapter(mAdapter);
        loading = true;
        GetTaskServer getTaskServer = new GetTaskServer();
        getTaskServer.execute(sessionUser.getUserDetails().getToken(), "1");
    }

    @Override
    public void onRefresh() {
        loading = true;
        GetTaskServer getTaskServer = new GetTaskServer();
        getTaskServer.execute(sessionUser.getUserDetails().getToken(), "1");
    }

    private class GetTaskServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_COUPON_PAGE, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_TASK, "GET", params);

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
                    Log.e("GET TASK", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {

                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONArray jsonData = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                            for (int i = 0; i < jsonData.length(); i++) {
                                JSONObject item = jsonData.getJSONObject(i);
                                TaskDTO taskDTO = new TaskDTO();
                                if (!item.isNull(Common.JsonKey.KEY_TASK_ID)) {
                                    taskDTO.setId(item.getInt(Common.JsonKey.KEY_TASK_ID));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_OWNER)) {
                                    taskDTO.setOwner_id(item.getInt(Common.JsonKey.KEY_TASK_OWNER));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_USER_ID)) {
                                    taskDTO.setUser_id(item.getInt(Common.JsonKey.KEY_TASK_USER_ID));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_POST_ID)) {
                                    taskDTO.setPost_id(item.getInt(Common.JsonKey.KEY_TASK_POST_ID));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_NAME)) {
                                    taskDTO.setName(item.getString(Common.JsonKey.KEY_TASK_NAME));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_AMOUNT)) {
                                    taskDTO.setAmount(item.getDouble(Common.JsonKey.KEY_TASK_AMOUNT));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_START_DATE)) {
                                    taskDTO.setStart_date(item.getString(Common.JsonKey.KEY_TASK_START_DATE));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_END_DATE)) {
                                    taskDTO.setEnd_date(item.getString(Common.JsonKey.KEY_TASK_END_DATE));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_START_HOUR)) {
                                    taskDTO.setStart_hour(item.getString(Common.JsonKey.KEY_TASK_START_HOUR));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_END_HOUR)) {
                                    taskDTO.setEnd_hour(item.getString(Common.JsonKey.KEY_TASK_END_HOUR));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_DESCRIPTION)) {
                                    taskDTO.setDescription(item.getString(Common.JsonKey.KEY_TASK_DESCRIPTION));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_DELETE_AT)) {
                                    taskDTO.setDelete_at(item.getString(Common.JsonKey.KEY_TASK_DELETE_AT));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_STATUS_PAYMENT)) {
                                    taskDTO.setStatus_payment(item.getInt(Common.JsonKey.KEY_TASK_STATUS_PAYMENT));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_STATUS)) {
                                    taskDTO.setStatus(item.getInt(Common.JsonKey.KEY_TASK_STATUS));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_CREATED_AT)) {
                                    taskDTO.setCreated_at(item.getString(Common.JsonKey.KEY_TASK_CREATED_AT));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_UPDATE_AT)) {
                                    taskDTO.setUpdated_at(item.getString(Common.JsonKey.KEY_TASK_UPDATE_AT));
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_RATE)) {
                                    taskDTO.setRate(item.getDouble(Common.JsonKey.KEY_TASK_RATE));
                                }

                                if (!item.isNull(Common.JsonKey.KEY_TASK_USER)) {
                                    JSONObject user = item.getJSONObject(Common.JsonKey.KEY_TASK_USER);
                                    UserDTO userDTO = new UserDTO();
                                    if (!user.isNull(Common.JsonKey.KEY_USER_ID)) {
                                        userDTO.setId(user.getInt(Common.JsonKey.KEY_USER_ID));
                                    }
                                    if (!user.isNull(Common.JsonKey.KEY_USER_PHONE)) {
                                        userDTO.setPhone(user.getString(Common.JsonKey.KEY_USER_PHONE));
                                    }
                                    if (!user.isNull(Common.JsonKey.KEY_USER_AVATAR)) {
                                        userDTO.setAvatar(user.getString(Common.JsonKey.KEY_USER_AVATAR));
                                    }
                                    if (!user.isNull(Common.JsonKey.KEY_USER_FULL_NAME)) {
                                        userDTO.setFull_name(user.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                                    }
                                    taskDTO.setUser(userDTO);
                                }
                                if (!item.isNull(Common.JsonKey.KEY_TASK_POST)) {
                                    JSONObject post = item.getJSONObject(Common.JsonKey.KEY_TASK_POST);
                                    PostResultDTO postResultDTO = new PostResultDTO();
                                    if (!post.isNull(Common.JsonKey.KEY_POST_ID)) {
                                        postResultDTO.setId(post.getInt(Common.JsonKey.KEY_POST_ID));
                                    }
                                    if (!post.isNull(Common.JsonKey.KEY_POST_USER_ID)) {
                                        postResultDTO.setUser_id(post.getInt(Common.JsonKey.KEY_POST_USER_ID));
                                    }
                                    if (!post.isNull(Common.JsonKey.KEY_POST_CATEGORY_ID)) {
                                        postResultDTO.setCategory_id(post.getInt(Common.JsonKey.KEY_POST_CATEGORY_ID));
                                    }
                                    if (!post.isNull(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID)) {
                                        postResultDTO.setCategory_parent_id(post.getInt(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID));
                                    }
                                    if (!post.isNull(Common.JsonKey.KEY_POST_ADDRESS)) {
                                        postResultDTO.setAddress(post.getString(Common.JsonKey.KEY_POST_ADDRESS));
                                    }
                                    if (!post.isNull(Common.JsonKey.KEY_POST_LATITUDE)) {
                                        postResultDTO.setLatitude((float) post.getDouble(Common.JsonKey.KEY_POST_LATITUDE));
                                    }
                                    if (!post.isNull(Common.JsonKey.KEY_POST_LONGITUDE)) {
                                        postResultDTO.setLongitude((float) post.getDouble(Common.JsonKey.KEY_POST_LONGITUDE));
                                    }
                                    if (!post.isNull(Common.JsonKey.KEY_POST_CATEGORY)) {
                                        JSONObject category = post.getJSONObject(Common.JsonKey.KEY_POST_CATEGORY);
                                        CategoryDTO categoryDTO = new CategoryDTO();
                                        if (!category.isNull(Common.JsonKey.KEY_CATEGORY_ID)) {
                                            categoryDTO.setId(category.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                        }
                                        if (!category.isNull(Common.JsonKey.KEY_CATEGORY_NAME)) {
                                            categoryDTO.setName(category.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                        }
                                        if (!category.isNull(Common.JsonKey.KEY_CATEGORY_ICON)) {
                                            categoryDTO.setIcon(category.getString(Common.JsonKey.KEY_CATEGORY_ICON));
                                        }
                                        postResultDTO.setCategory(categoryDTO);
                                    }
                                    if (!post.isNull(Common.JsonKey.KEY_POST_CATEGORY)) {
                                        JSONObject category_parent = post.getJSONObject(Common.JsonKey.KEY_POST_CATEGORY);
                                        CategoryDTO category_parentDTO = new CategoryDTO();
                                        if (!category_parent.isNull(Common.JsonKey.KEY_CATEGORY_ID)) {
                                            category_parentDTO.setId(category_parent.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                        }
                                        if (!category_parent.isNull(Common.JsonKey.KEY_CATEGORY_NAME)) {
                                            category_parentDTO.setName(category_parent.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                        }
                                        if (!category_parent.isNull(Common.JsonKey.KEY_CATEGORY_ICON)) {
                                            category_parentDTO.setIcon(category_parent.getString(Common.JsonKey.KEY_CATEGORY_ICON));
                                        }
                                        postResultDTO.setCategory_parent(category_parentDTO);
                                    }
                                    taskDTO.setPost(postResultDTO);
                                    mList.add(taskDTO);
                                }
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
            srlRefresh.setRefreshing(false);
//            progressBar.setVisibility(View.GONE);
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

}
