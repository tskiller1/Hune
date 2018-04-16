package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.hunegroup.hune.R;
import com.hunegroup.hune.adapter.QuanLyDangTuyenAdapter;
import com.hunegroup.hune.dto.CategoryDTO;
import com.hunegroup.hune.dto.PostResultDTO;
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
 * Created by tskil on 05/07/2017.
 */

public class QuanLyDangTuyenTuyenDungActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgNotify;
    private ListView listThongTin;
    private FloatingActionButton btnAdd;
    private FrameLayout frame_progressBar;

    private List<PostResultDTO> postResultDTOList;
    private SessionUser sessionUser;
    private JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_quanlydangtuyen_tuyendung);
        sessionUser = new SessionUser(getBaseContext());
        findViews();
        PostServer postServer = new PostServer();
        postServer.execute(sessionUser.getUserDetails().getToken(),"1");
    }
    private void findViews() {
        frame_progressBar = (FrameLayout) findViewById(R.id.frame_progressBar);
        imgBack = (ImageView)findViewById( R.id.imgBack );
        imgNotify = (ImageView)findViewById( R.id.imgNotify );
        listThongTin = (ListView)findViewById( R.id.listThongTin );
        btnAdd = (FloatingActionButton)findViewById( R.id.btnAdd );

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuanLyDangTuyenTuyenDungActivity.this,DangTuyenTuyenDungActivity.class);
                startActivityForResult(intent,101);
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
                startActivity(new Intent(QuanLyDangTuyenTuyenDungActivity.this,ThongBaoTuyenDungActivity.class));
            }
        });

        listThongTin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(QuanLyDangTuyenTuyenDungActivity.this,ChinhSuaDangTuyenTuyenDungActivity.class);
                intent.putExtra(Common.JsonKey.KEY_POST_ID,postResultDTOList.get(position).getId());
                startActivityForResult(intent,99);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==99)
        {
            if(resultCode==88)
            {
                String result=data.getStringExtra("result");
                if(result.equals("thaydoi")){
                    PostServer postServer = new PostServer();
                    postServer.execute(sessionUser.getUserDetails().getToken(),"1");
                }

            }
        }
        if(requestCode==101)
        {
            PostServer postServer = new PostServer();
            postServer.execute(sessionUser.getUserDetails().getToken(),"1");
        }
    }

    private class PostServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TYPE, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_MY_POST, "GET", params);

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


                    if(code.contains(Common.JsonKey.KEY_SUCCESSFULLY)){
                        postResultDTOList = new ArrayList<>();
                        if(!jsonResult.isNull(Common.JsonKey.KEY_DATA)){
                            JSONArray jsonData = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                            for(int i = 0;i < jsonData.length();i++){
                                JSONObject jsonObject = jsonData.getJSONObject(i);
                                PostResultDTO postDTO = new PostResultDTO();
                                postDTO.setId(jsonObject.getInt(Common.JsonKey.KEY_POST_ID));
                                postDTO.setTitle(jsonObject.getString(Common.JsonKey.KEY_POST_TITLE));
                                postDTO.setStar(jsonObject.getDouble(Common.JsonKey.KEY_POST_RATING));
                                postDTO.setDescription(jsonObject.getString(Common.JsonKey.KEY_POST_DESCRIPTION));
                                postDTO.setCategory_id(jsonObject.getInt(Common.JsonKey.KEY_POST_CATEGORY_ID));
                                postDTO.setUser_id(jsonObject.getInt(Common.JsonKey.KEY_POST_USER_ID));
                                postDTO.setCategory_parent_id(jsonObject.getInt(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID));
                                postDTO.setStatus(jsonObject.getInt(Common.JsonKey.KEY_POST_STATUS));

                                JSONObject user = jsonObject.getJSONObject(Common.JsonKey.KEY_POST_USER);
                                UserDTO userDTO = new UserDTO();
                                userDTO.setId(user.getInt(Common.JsonKey.KEY_USER_ID));
                                userDTO.setPhone(user.getString(Common.JsonKey.KEY_USER_PHONE));
                                userDTO.setFull_name(user.getString(Common.JsonKey.KEY_USER_FULL_NAME));
//                                userDTO.setAvatar(user.getString(Common.JsonKey.KEY_USER_AVATAR));
                                postDTO.setUser(userDTO);

                                JSONObject category = jsonObject.getJSONObject(Common.JsonKey.KEY_POST_CATEGORY);
                                CategoryDTO categoryDTO = new CategoryDTO();
                                categoryDTO.setId(category.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                categoryDTO.setName(category.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                categoryDTO.setIcon(category.getString(Common.JsonKey.KEY_POST_ICON));
                                postDTO.setCategory(categoryDTO);

                                JSONObject categoryParent = jsonObject.getJSONObject(Common.JsonKey.KEY_POST_PARENT_CATEGORY);
                                CategoryDTO categoryParentDTO = new CategoryDTO();
                                categoryParentDTO.setId(categoryParent.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                categoryParentDTO.setName(categoryParent.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                postDTO.setCategory_parent(categoryParentDTO);
                                postResultDTOList.add(postDTO);
                            }
                        }
                        QuanLyDangTuyenAdapter quanLyDangTuyenAdapter = new QuanLyDangTuyenAdapter(QuanLyDangTuyenTuyenDungActivity.this,postResultDTOList);
                        listThongTin.setAdapter(quanLyDangTuyenAdapter);
                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            frame_progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }
}
