package com.hunegroup.hune.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.PostResultDTO;
import com.hunegroup.hune.ui.QuanLyTinDangTimViecActivity;
import com.hunegroup.hune.ui.SuaViecLamTimViecActivity;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 10/08/2017.
 */

public class QuanLyDangTimViecAdapter extends ArrayAdapter<PostResultDTO> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-08-10 10:43:30 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout linear_Main;
        public final TextView txtName;
        public final CheckBox ckbItem;
        public final TextView txtSua;
        public final TextView txtXem;

        private ViewHolder(LinearLayout linear_Main, TextView txtName, CheckBox ckbItem, TextView txtSua, TextView txtXem) {
            this.linear_Main = linear_Main;
            this.txtName = txtName;
            this.ckbItem = ckbItem;
            this.txtSua = txtSua;
            this.txtXem = txtXem;
        }

        public static ViewHolder create(LinearLayout linear_Main) {
            TextView txtName = (TextView)linear_Main.findViewById( R.id.txtName );
            CheckBox ckbItem = (CheckBox)linear_Main.findViewById( R.id.ckbItem );
            TextView txtSua = (TextView)linear_Main.findViewById( R.id.txt_Sua );
            TextView txtXem = (TextView)linear_Main.findViewById( R.id.txt_Xem );
            return new ViewHolder( linear_Main, txtName, ckbItem, txtSua, txtXem );
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if ( convertView == null ) {
            View view = mInflater.inflate( R.layout.item_list_quanlydangtimviec, parent, false );
            vh = ViewHolder.create( (LinearLayout)view );
            view.setTag( vh );
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        final PostResultDTO item = getItem( position );

        // TODOBind your data to the views here
        vh.txtName.setText(item.getCategory().getName());
        if(item.getStatus()==1)
            vh.ckbItem.setChecked(true);
        else
            vh.ckbItem.setChecked(false);


        vh.ckbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                PostPutServer postPutServer=new PostPutServer();
                String status = Common.Type.TYPE_STATUS_OFF;
                String msg=item.getCategory().getName()+"  OFF";

                if(isChecked) {
                    status = Common.Type.TYPE_STATUS_ON;
                    msg=item.getCategory().getName()+"  ON";
                }
                QuanLyTinDangTimViecActivity.itemListThongTin.get(position).setStatus(Integer.parseInt(status));
                notifyDataSetChanged();
                postPutServer.execute(sessionUser.getUserDetails().getToken(),item.getId()+"",status,msg);
            }
        });
        vh.txtSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SuaViecLamTimViecActivity.class);
                intent.putExtra(Common.JsonKey.KEY_POST_ID,item.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        vh.txtXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return vh.linear_Main;
    }

    private LayoutInflater mInflater;

    // Constructors
    public QuanLyDangTimViecAdapter(Context context, List<PostResultDTO> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from( context );
        this.mContext=context;
        sessionUser=new SessionUser(mContext);
    }
    public QuanLyDangTimViecAdapter(Context context, PostResultDTO[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from( context );
        this.mContext=context;
        sessionUser=new SessionUser(mContext);
    }
    private Context mContext;
    private SessionUser sessionUser;
    private JSONParser jsonParser = new JSONParser();
    private class PostPutServer extends AsyncTask<String, Void, String> {
        private String msg="";
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

                msg=urls[3]; // message
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_STATUS, urls[2]));

                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_PUT_POST+urls[1], "PUT", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {

            if (mContext == null)
                return;
            if (result != null) {

                //prsbLoadingDataList.setVisibility(View.GONE);


                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();


                    if(code.contains(Common.JsonKey.KEY_SUCCESSFULLY)){
                        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
                    }else{
//                        Toast.makeText(mContext,mContext.getResources().getString(R.string.toast_dacoloixayra),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }
}
