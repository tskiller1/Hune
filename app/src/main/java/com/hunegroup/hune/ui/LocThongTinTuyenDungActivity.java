package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.CategoryDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 05/07/2017.
 */

public class LocThongTinTuyenDungActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgNotify;
    private ImageView imgLeft;
    private ImageView imgRight;
    private HorizontalScrollView horizoncalScrollView;
    private LinearLayout llListFilter;
    private ImageView imgLeftChild;
    private ImageView imgRightChild;
    private HorizontalScrollView horizoncalScrollChild;
    private LinearLayout llListChild;
    private CheckBox ckbNam;
    private CheckBox ckbNu;
    private Button btnChapNhan;
    private FloatingActionButton btnAdd;

    String parent_category_id;
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    List<CategoryDTO> categoryDTOList;
    Boolean isFirst = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_locthongtin_tuyendung);
        findViews();
        CategoryServer categoryServer = new CategoryServer();
        categoryServer.execute("0");
    }

    private void findViews() {
        imgBack = (ImageView)findViewById( R.id.imgBack );
        imgNotify = (ImageView)findViewById( R.id.imgNotify );
        imgLeft = (ImageView)findViewById( R.id.imgLeft );
        imgRight = (ImageView)findViewById( R.id.imgRight );
        horizoncalScrollView = (HorizontalScrollView)findViewById( R.id.horizoncalScrollView );
        llListFilter = (LinearLayout)findViewById( R.id.ll_list_filter );
        imgLeftChild = (ImageView)findViewById( R.id.imgLeftChild );
        imgRightChild = (ImageView)findViewById( R.id.imgRightChild );
        horizoncalScrollChild = (HorizontalScrollView)findViewById( R.id.horizoncalScrollChild );
        llListChild = (LinearLayout)findViewById( R.id.ll_list_child );
        ckbNam = (CheckBox)findViewById( R.id.ckbNam );
        ckbNu = (CheckBox)findViewById( R.id.ckbNu );
        btnChapNhan = (Button)findViewById( R.id.btnChapNhan );
        btnAdd = (FloatingActionButton)findViewById( R.id.btnAdd );

        btnChapNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocThongTinTuyenDungActivity.this,DangTuyenTuyenDungActivity.class));
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
                startActivity(new Intent(LocThongTinTuyenDungActivity.this,ThongBaoTuyenDungActivity.class));
            }
        });
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

                Log.e("caca", "category:" + result);

                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        categoryDTOList = new ArrayList<>();
                        JSONArray jsonArray = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final CategoryDTO categoryDTO = new CategoryDTO();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            categoryDTO.setId(jsonObject.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                            categoryDTO.setName(jsonObject.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                            categoryDTO.setIcon(jsonObject.getString(Common.JsonKey.KEY_CATEGORY_ICON));
                            categoryDTO.setParent_id(jsonObject.getInt(Common.JsonKey.KEY_CATEGORY_PARENT_ID));
                            List<CategoryDTO> listChild = new ArrayList<>();
                            if (!jsonObject.isNull(Common.JsonKey.KEY_CATEGORY_CHILD)) {
                                JSONArray jsonArrayChild = jsonObject.getJSONArray(Common.JsonKey.KEY_CATEGORY_CHILD);
                                for (int j = 0; j < jsonArrayChild.length(); j++) {
                                    CategoryDTO categoryChild = new CategoryDTO();
                                    JSONObject jsonObjectChild = jsonArrayChild.getJSONObject(j);
                                    categoryChild.setId(jsonObjectChild.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                    categoryChild.setName(jsonObjectChild.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                    categoryChild.setIcon(jsonObjectChild.getString(Common.JsonKey.KEY_CATEGORY_ICON));
                                    categoryChild.setParent_id(jsonObjectChild.getInt(Common.JsonKey.KEY_CATEGORY_PARENT_ID));
                                    listChild.add(categoryChild);
                                }
                            }
                            categoryDTO.setChild(listChild);
                            categoryDTOList.add(categoryDTO);
                            View subview = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_loaihinh_locthongtin, null);
                            TextView tv_name_loaihinh = (TextView) subview.findViewById(R.id.txtName);
                            LinearLayout llParent = (LinearLayout) subview.findViewById(R.id.llParent);
                            tv_name_loaihinh.setText(categoryDTO.getName());
                            final CheckBox ckbFilter = (CheckBox) subview.findViewById(R.id.ckbItem);
                            ckbFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                }
                            });
                            llParent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    llListChild.removeAllViews();
                                    for(int j = 0;j<categoryDTO.getChild().size();j++){
                                        View subChild = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_loaihinh_locthongtin, null);
                                        TextView tv_name_Child = (TextView) subChild.findViewById(R.id.txtName);
                                        tv_name_Child.setText(categoryDTO.getChild().get(j).getName());
                                        CheckBox ckbChild = (CheckBox) subChild.findViewById(R.id.ckbItem);
                                        if(ckbFilter.isChecked())
                                            ckbChild.setChecked(true);
                                        else
                                            ckbChild.setChecked(false);
                                        llListChild.addView(subChild);
                                    }
                                }
                            });
                            llListFilter.addView(subview);
                            if(isFirst && i==0){
                                isFirst = false;
                                for(int j = 0;j<categoryDTO.getChild().size();j++){
                                    View subChild = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_loaihinh_locthongtin, null);
                                    TextView tv_name_Child = (TextView) subChild.findViewById(R.id.txtName);
                                    tv_name_Child.setText(categoryDTO.getChild().get(j).getName());
                                    CheckBox ckbChild = (CheckBox) subChild.findViewById(R.id.ckbItem);
                                    if(ckbFilter.isChecked())
                                        ckbChild.setChecked(true);
                                    else
                                        ckbChild.setChecked(false);
                                    llListChild.addView(subChild);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(LocThongTinTuyenDungActivity.this, "Hiện tại chưa có danh sách loại hình", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }

}
