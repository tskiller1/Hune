package com.hunegroup.hune.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.adapter.QuanLyDangTimViecAdapter;
import com.hunegroup.hune.dto.CategoryDTO;
import com.hunegroup.hune.dto.PostResultDTO;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.squareup.picasso.Picasso;

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

public class QuanLyTinDangTimViecActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgBack;
    private ImageView imgNotify;
    private TextView txtTheoLoaiHinh;
    private RelativeLayout rlLoaiCongViec;
    private ImageView imgLeft;
    private ImageView imgRight;
    private ListView listThongTin;
    private FloatingActionButton btnAdd;
    private LinearLayout linear_LoaiHinh;
    private QuanLyDangTimViecAdapter quanLyDangTimViecAdapter;
    private FrameLayout frame_progressBar;

    private List<PostResultDTO> postResultDTOList;


    private List<CategoryDTO> categoryDTOListFull;
    public static List<PostResultDTO> itemListThongTin;
    private SessionUser sessionUser;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_quanlytindang_timviec);
        findViews();

        frame_progressBar.setVisibility(View.VISIBLE);
        sessionUser=new SessionUser(getBaseContext());
        CategoryServer categoryServer=new CategoryServer();
        categoryServer.execute("0");



    }

    private void findViews() {
        imgBack = (ImageView)findViewById( R.id.imgBack );
        imgNotify = (ImageView)findViewById( R.id.imgNotify );
        txtTheoLoaiHinh = (TextView)findViewById( R.id.txtTheoLoaiHinh );
        rlLoaiCongViec = (RelativeLayout)findViewById( R.id.rl_loaiCongViec );
        imgLeft = (ImageView)findViewById( R.id.imgLeft );
        imgRight = (ImageView)findViewById( R.id.imgRight );
        listThongTin = (ListView)findViewById( R.id.listThongTin );
        btnAdd = (FloatingActionButton)findViewById( R.id.btnAdd );
        linear_LoaiHinh= (LinearLayout) findViewById(R.id.linear_LoaiHinh);
        frame_progressBar= (FrameLayout) findViewById(R.id.frame_progressBar);


        imgBack.setOnClickListener(this);
        imgNotify.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    private void setListHorizontal_LoaiHinh()
    {
        LayoutInflater inflater= (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i=0;i<categoryDTOListFull.size();i++)
        {
            View v=inflater.inflate(R.layout.item_quanlytindang_loaihinh,null);
            ImageView img= (ImageView) v.findViewById(R.id.imgImage);
            TextView txt= (TextView) v.findViewById(R.id.txtName);
            CheckBox checkBox= (CheckBox) v.findViewById(R.id.ckbItem);
            txt.setText(categoryDTOListFull.get(i).getName());
            Picasso.with(getBaseContext()).load(categoryDTOListFull.get(i).getIcon()).into(img);
            final int id=i;
            if(isChecked(categoryDTOListFull.get(i).getId()))
            {
                //checkBox.setSelected(true);
                checkBox.setChecked(true);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int id_parent=categoryDTOListFull.get(id).getId();
                    changeListThongTin(id_parent,isChecked);


                }
            });
            linear_LoaiHinh.addView(v);
        }
    }
    private void changeListThongTin(int id_parent,boolean isChecked)
    {
        if(isChecked) {
            for (PostResultDTO item:postResultDTOList
                 ) {
                if(item.getCategory_parent().getId()==id_parent){
                    itemListThongTin.add(item);
                }
            }
            //quanLyDangTimViecAdapter.notifyDataSetChanged();

        }
        else {
            int[] index=new int[itemListThongTin.size()];
            int n=0;
            for(int i=0;i<itemListThongTin.size();i++)
            {
                if(itemListThongTin.get(i).getCategory_parent().getId()==id_parent){
                    index[n++]=i;
                }
            }
            for(int i=n-1;i>=0;i--)
            {
                itemListThongTin.remove(index[i]);
            }
            //quanLyDangTimViecAdapter.notifyDataSetChanged();
        }
        quanLyDangTimViecAdapter=new QuanLyDangTimViecAdapter(getBaseContext(),itemListThongTin);
        listThongTin.setAdapter(quanLyDangTimViecAdapter);

    }
    private void setListThongTin()
    {
        itemListThongTin=new ArrayList<>();
        for (PostResultDTO item:postResultDTOList
             ) {
            itemListThongTin.add(item);
        }
        quanLyDangTimViecAdapter=new QuanLyDangTimViecAdapter(getBaseContext(),itemListThongTin);
        listThongTin.setAdapter(quanLyDangTimViecAdapter);
    }
    private boolean isChecked(int id)
    {
        for (int i=0;i<postResultDTOList.size();i++) {
            if(id==postResultDTOList.get(i).getCategory_parent().getId())
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.imgBack:
            {
                finish();
                break;
            }
            case R.id.imgNotify:
            {
                startActivity(new Intent(QuanLyTinDangTimViecActivity.this,ThongBaoTimViecActivity.class));
                break;
            }
            case R.id.btnAdd:
            {
                startActivity(new Intent(QuanLyTinDangTimViecActivity.this,ThemViecLamTimViecActivity.class));
                break;
            }
        }
    }

    private class PostServer extends AsyncTask<String, Void, String> {

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
                    frame_progressBar.setVisibility(View.GONE);
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

                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_ID)) {
                                    postDTO.setId(jsonObject.getInt(Common.JsonKey.KEY_POST_ID));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_TITLE)) {
                                    postDTO.setTitle(jsonObject.getString(Common.JsonKey.KEY_POST_TITLE));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_RATING)) {
                                    postDTO.setStar(jsonObject.getDouble(Common.JsonKey.KEY_POST_RATING));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_DESCRIPTION)) {
                                    postDTO.setDescription(jsonObject.getString(Common.JsonKey.KEY_POST_DESCRIPTION));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_CATEGORY_ID)) {
                                    postDTO.setCategory_id(jsonObject.getInt(Common.JsonKey.KEY_POST_CATEGORY_ID));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_USER_ID)) {
                                    postDTO.setUser_id(jsonObject.getInt(Common.JsonKey.KEY_POST_USER_ID));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID)) {
                                    postDTO.setCategory_parent_id(jsonObject.getInt(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_STATUS)) {
                                    postDTO.setStatus(jsonObject.getInt(Common.JsonKey.KEY_POST_STATUS));
                                }

                                UserDTO userDTO = new UserDTO();
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_USER)) {

                                    JSONObject user = jsonObject.getJSONObject(Common.JsonKey.KEY_POST_USER);

                                    if (!user.isNull(Common.JsonKey.KEY_USER_ID)) {
                                        userDTO.setId(user.getInt(Common.JsonKey.KEY_USER_ID));
                                    }
                                    if (!user.isNull(Common.JsonKey.KEY_USER_PHONE)) {
                                        userDTO.setPhone(user.getString(Common.JsonKey.KEY_USER_PHONE));
                                    }
                                    if (!user.isNull(Common.JsonKey.KEY_USER_FULL_NAME)) {
                                        userDTO.setFull_name(user.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                                    }
                                    if (!user.isNull(Common.JsonKey.KEY_USER_AVATAR)) {
                                        userDTO.setAvatar(user.getString(Common.JsonKey.KEY_USER_AVATAR));
                                    }
                                }
                                postDTO.setUser(userDTO);


                                CategoryDTO categoryDTO = new CategoryDTO();
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_CATEGORY)) {

                                    JSONObject category = jsonObject.getJSONObject(Common.JsonKey.KEY_POST_CATEGORY);
                                    if (!category.isNull(Common.JsonKey.KEY_CATEGORY_ID)) {
                                        categoryDTO.setId(category.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                    }
                                    if (!category.isNull(Common.JsonKey.KEY_CATEGORY_NAME)) {
                                        categoryDTO.setName(category.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                    }
                                }

                                postDTO.setCategory(categoryDTO);

                                CategoryDTO categoryParentDTO = new CategoryDTO();
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_PARENT_CATEGORY)) {
                                    JSONObject categoryParent = jsonObject.getJSONObject(Common.JsonKey.KEY_POST_PARENT_CATEGORY);
                                    if (!categoryParent.isNull(Common.JsonKey.KEY_CATEGORY_ID)) {
                                        categoryParentDTO.setId(categoryParent.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                    }
                                    if (!categoryParent.isNull(Common.JsonKey.KEY_CATEGORY_NAME)) {
                                        categoryParentDTO.setName(categoryParent.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                    }
                                }

                                postDTO.setCategory_parent(categoryParentDTO);

                                postResultDTOList.add(postDTO);
                            }
                            setListHorizontal_LoaiHinh();
                            setListThongTin();
                            frame_progressBar.setVisibility(View.GONE);
                        }

                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }

    private class CategoryServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_CATEGORY_PARENT_ID, urls[0]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_CATEGORY, "GET", params);

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

                        categoryDTOListFull = new ArrayList<>();
                        if(!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONArray jsonArray = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                CategoryDTO categoryDTO = new CategoryDTO();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (!jsonObject.isNull(Common.JsonKey.KEY_CATEGORY_ID)) {
                                    categoryDTO.setId(jsonObject.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_CATEGORY_NAME)) {
                                    categoryDTO.setName(jsonObject.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_CATEGORY_ICON)) {
                                    categoryDTO.setIcon(jsonObject.getString(Common.JsonKey.KEY_CATEGORY_ICON));
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_CATEGORY_PARENT_ID)) {
                                    categoryDTO.setParent_id(jsonObject.getInt(Common.JsonKey.KEY_CATEGORY_PARENT_ID));
                                }
                                List<CategoryDTO> listChild = new ArrayList<>();
                                if (!jsonObject.isNull(Common.JsonKey.KEY_CATEGORY_CHILD)) {
                                    JSONArray jsonArrayChild = jsonObject.getJSONArray(Common.JsonKey.KEY_CATEGORY_CHILD);
                                    for (int j = 0; j < jsonArrayChild.length(); j++) {
                                        CategoryDTO categoryChild = new CategoryDTO();
                                        JSONObject jsonObjectChild = jsonArrayChild.getJSONObject(j);
                                        if (!jsonObjectChild.isNull(Common.JsonKey.KEY_CATEGORY_PARENT_ID)) {
                                            categoryChild.setId(jsonObjectChild.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                        }
                                        if (!jsonObjectChild.isNull(Common.JsonKey.KEY_CATEGORY_NAME)) {
                                            categoryChild.setName(jsonObjectChild.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                        }
                                        if (!jsonObjectChild.isNull(Common.JsonKey.KEY_CATEGORY_ICON)) {
                                            categoryChild.setIcon(jsonObjectChild.getString(Common.JsonKey.KEY_CATEGORY_ICON));
                                        }
                                        if (!jsonObjectChild.isNull(Common.JsonKey.KEY_CATEGORY_PARENT_ID)) {
                                            categoryChild.setParent_id(jsonObjectChild.getInt(Common.JsonKey.KEY_CATEGORY_PARENT_ID));
                                        }
                                        listChild.add(categoryChild);
                                    }
                                }
                                categoryDTO.setChild(listChild);
                                categoryDTOListFull.add(categoryDTO);
                            }

                            PostServer postServer = new PostServer();
                            postServer.execute(sessionUser.getUserDetails().getToken(), "2");
                        }

                    } else {

                        Toast.makeText(getBaseContext(), "Hiện tại chưa có danh sách loại hình", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }
}
