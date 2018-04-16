package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hunegroup.hune.R;
import com.hunegroup.hune.adapter.ThongTinDaLuuAdapter_TuyenDung;
import com.hunegroup.hune.dto.Category_ThongTinDaLuuDTO;
import com.hunegroup.hune.dto.MetaDataDTO;
import com.hunegroup.hune.dto.Parent_Category_ThongTinDaLuuDTO;
import com.hunegroup.hune.dto.Post_ThongTinDaLuuDTO;
import com.hunegroup.hune.dto.ThongTinDaLuuDTO;
import com.hunegroup.hune.dto.User_ThongTinDaLuuDTO;
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
 * Created by tskil on 05/07/2017.
 */

public class ThongTinDaLuuTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;
    private ImageView imgFilter;
    private ListView listThongTin;
    private FloatingActionButton btnAdd;
    private ProgressBar progressBar;
    private ThongTinDaLuuAdapter_TuyenDung thongTinDaLuuAdapter;
    public static List<ThongTinDaLuuDTO> thongTinDaLuuDTOs;

    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;
    boolean loading = false;
    MetaDataDTO metaDataDTO;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_thongtindaluu_tuyendung);
        findViews();
        setListThongTin();
    }

    private void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        imgFilter = (ImageView) findViewById(R.id.imgFilter);
        listThongTin = (ListView) findViewById(R.id.listThongTin);
        listThongTin.setVerticalScrollBarEnabled(false);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        listThongTin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ThongTinDaLuuDTO item = thongTinDaLuuDTOs.get(i);
                if(item.getPost().getStatus()== Common.Type.TYPE_STATUS_INT_ON) {
                    Intent intent = new Intent(getBaseContext(), ChiTietUngVienTuyenDungActivity.class);
                    intent.putExtra("id_post", item.getPost().getId());
                    intent.putExtra(Common.JsonKey.KEY_USER_ID, item.getPost().getUser().getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
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
                            FavouriteServer favouriteServer = new FavouriteServer();
                            favouriteServer.execute(sessionUser.getUserDetails().getToken(), Common.Type.TYPE_POST, Common.Type.TYPE_SEARCH_JOB, next_page);
                        }
                    }
                    else loading = false;
                }
            }
        });
    }

    private void setListThongTin() {
        thongTinDaLuuDTOs = new ArrayList<>();
        thongTinDaLuuAdapter = new ThongTinDaLuuAdapter_TuyenDung(getBaseContext(), thongTinDaLuuDTOs);
        listThongTin.setAdapter(thongTinDaLuuAdapter);
        FavouriteServer getFavouriteServer = new FavouriteServer();
        sessionUser = new SessionUser(getBaseContext());
        loading = true;
        getFavouriteServer.execute(sessionUser.getUserDetails().getToken(), Common.Type.TYPE_POST, Common.Type.TYPE_SEARCH_JOB,"1");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imgBack: {
                finish();
                break;
            }
            case R.id.btnAdd: {
                startActivity(new Intent(ThongTinDaLuuTuyenDungActivity.this, DangTuyenTuyenDungActivity.class));
                break;
            }
        }
    }

    private class FavouriteServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE_POST, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_PAGE,urls[3]));
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
                try {

                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        metaDataDTO = new MetaDataDTO();
                        JSONArray jsonArray = new JSONArray(jsonResult.getJSONArray(Common.JsonKey.KEY_DATA).toString());
                        ThongTinDaLuuDTO thongTinDaLuuDTO;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            thongTinDaLuuDTO = new ThongTinDaLuuDTO();
                            JSONObject jsondata = jsonArray.getJSONObject(i);
                            if (!jsondata.isNull(Common.JsonKey.KEY_FAVOURITE_ID)) {
                                String id = jsondata.getInt(Common.JsonKey.KEY_FAVOURITE_ID) + "";
                                thongTinDaLuuDTO.setId(id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_FAVOURITE_USER_ID)) {
                                String user_id = jsondata.getInt(Common.JsonKey.KEY_FAVOURITE_USER_ID) + "";
                                thongTinDaLuuDTO.setUser_id(user_id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_FAVOURITE_TYPE)) {
                                String type = jsondata.getInt(Common.JsonKey.KEY_FAVOURITE_TYPE) + "";
                                thongTinDaLuuDTO.setType(type);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID)) {
                                String source_id = jsondata.getInt(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID) + "";
                                thongTinDaLuuDTO.setSource_id(source_id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_FAVOURITE_CREATED_AT)) {
                                String created_at = jsondata.getString(Common.JsonKey.KEY_FAVOURITE_CREATED_AT).toString().trim();
                                thongTinDaLuuDTO.setCreated_at(created_at);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_FAVOURITE_UPDATED_AT)) {
                                String updated_at = jsondata.getString(Common.JsonKey.KEY_FAVOURITE_UPDATED_AT).toString().trim();
                                thongTinDaLuuDTO.setUpdated_at(updated_at);
                            }

                            //post
                            Post_ThongTinDaLuuDTO post_thongTinDaLuuDTO = new Post_ThongTinDaLuuDTO();
                            if (!jsondata.isNull(Common.JsonKey.KEY_FAVOURITE_POST)) {
                                JSONObject jsonObject = jsondata.getJSONObject(Common.JsonKey.KEY_FAVOURITE_POST);
                                if (!jsonObject.isNull(Common.JsonKey.KEY_FAVOURITE_ID)) {
                                    int id = jsonObject.getInt(Common.JsonKey.KEY_FAVOURITE_ID);
                                    post_thongTinDaLuuDTO.setId(id);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_FAVOURITE_TITLE)) {
                                    String title = jsonObject.getString(Common.JsonKey.KEY_FAVOURITE_TITLE).toString().trim();
                                    post_thongTinDaLuuDTO.setTitle(title);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_FAVOURITE_DESCRIPTION)) {
                                    String description = jsonObject.getString(Common.JsonKey.KEY_FAVOURITE_DESCRIPTION).toString().trim();
                                    post_thongTinDaLuuDTO.setDescription(description);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_FAVOURITE_CATEGORY_ID)) {
                                    String category_id = jsonObject.getInt(Common.JsonKey.KEY_FAVOURITE_CATEGORY_ID) + "";
                                    post_thongTinDaLuuDTO.setCategory_id(category_id);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_FAVOURITE_CATEGORY_PARENT_ID)) {
                                    String category_parent_id = jsonObject.getInt(Common.JsonKey.KEY_FAVOURITE_CATEGORY_PARENT_ID) + "";
                                    post_thongTinDaLuuDTO.setCategory_parent_id(category_parent_id);
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_FAVOURITE_RATING)) {
                                    String rating = jsonObject.getDouble(Common.JsonKey.KEY_FAVOURITE_RATING) + "";
                                    post_thongTinDaLuuDTO.setRating(rating);
                                }
                                if(!jsonObject.isNull(Common.JsonKey.KEY_POST_STATUS)){
                                    post_thongTinDaLuuDTO.setStatus(jsonObject.getInt(Common.JsonKey.KEY_POST_STATUS));
                                    if(post_thongTinDaLuuDTO.getStatus() == 2){
                                        post_thongTinDaLuuDTO.setTitle("(<i>"+getString(R.string.datat)+"</i>)"+post_thongTinDaLuuDTO.getTitle());
                                    }
                                }
                                //category
                                Category_ThongTinDaLuuDTO category_thongTinDaLuuDTO = new Category_ThongTinDaLuuDTO();
                                JSONObject jsonObject2 = jsonObject.getJSONObject(Common.JsonKey.KEY_FAVOURITE_CATEGORY);

                                if (!jsonObject2.isNull(Common.JsonKey.KEY_FAVOURITE_ID)) {
                                    String id = jsonObject2.getInt(Common.JsonKey.KEY_FAVOURITE_ID) + "";
                                    category_thongTinDaLuuDTO.setId(id);
                                }
                                if (!jsonObject2.isNull(Common.JsonKey.KEY_FAVOURITE_NAME)) {
                                    String name = jsonObject2.getString(Common.JsonKey.KEY_FAVOURITE_NAME).toString().trim();
                                    category_thongTinDaLuuDTO.setName(name);
                                }
                                if (!jsonObject2.isNull(Common.JsonKey.KEY_FAVOURITE_ICON)) {
                                    String icon = jsonObject2.getString(Common.JsonKey.KEY_FAVOURITE_ICON).toString().trim();
                                    category_thongTinDaLuuDTO.setUrlImage(icon);
                                }

                                //parent_category
                                Parent_Category_ThongTinDaLuuDTO parent_category_thongTinDaLuuDTO = new Parent_Category_ThongTinDaLuuDTO();
                                JSONObject jsonObject3 = jsonObject.getJSONObject(Common.JsonKey.KEY_FAVOURITE_PARENT_CATEGORY);

                                if (!jsonObject3.isNull(Common.JsonKey.KEY_FAVOURITE_ID)) {
                                    String id = jsonObject3.getInt(Common.JsonKey.KEY_FAVOURITE_ID) + "";
                                    parent_category_thongTinDaLuuDTO.setId(id);
                                }
                                if (!jsonObject3.isNull(Common.JsonKey.KEY_FAVOURITE_NAME)) {
                                    String name = jsonObject3.getString(Common.JsonKey.KEY_FAVOURITE_NAME).toString().trim();
                                    parent_category_thongTinDaLuuDTO.setName(name);
                                }

                                //user
                                User_ThongTinDaLuuDTO user=new User_ThongTinDaLuuDTO();
                                JSONObject jsonObject4 = jsonObject.getJSONObject(Common.JsonKey.KEY_FAVOURITE_USER);
                                if (!jsonObject4.isNull(Common.JsonKey.KEY_FAVOURITE_ID)) {
                                    int id = jsonObject4.getInt(Common.JsonKey.KEY_FAVOURITE_ID);
                                    user.setId(id);
                                }

                                post_thongTinDaLuuDTO.setCategory(category_thongTinDaLuuDTO);
                                post_thongTinDaLuuDTO.setParent_category(parent_category_thongTinDaLuuDTO);
                                post_thongTinDaLuuDTO.setUser(user);

                            }
                            thongTinDaLuuDTO.setPost(post_thongTinDaLuuDTO);
                            thongTinDaLuuDTOs.add(thongTinDaLuuDTO);
                            thongTinDaLuuAdapter.notifyDataSetChanged();
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
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }
}
