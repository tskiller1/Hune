package com.hunegroup.hune.adapterv2;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.CouponDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 11/22/17.
 */

public class MuaCouponTimViecAdapter extends RecyclerView.Adapter<MuaCouponTimViecAdapter.CouponHolder> {

    private Context mContext;
    private List<CouponDTO> mList;
    private JSONParser jsonParser = new JSONParser();
    private SessionUser sessionUser;
    private OnItemClick mListener;

    private Dialog dialogMessage;

    public MuaCouponTimViecAdapter(Context mContext, List<CouponDTO> mList, OnItemClick listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.sessionUser = new SessionUser(mContext);
        this.mListener = listener;
    }

    @Override
    public CouponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponHolder(LayoutInflater.from(mContext).inflate(R.layout.item_v2_coupon_timviec, parent, false));
    }

    @Override
    public void onBindViewHolder(final CouponHolder holder, int position) {
        final CouponDTO item = mList.get(position);
        Glide.with(mContext).load(item.getImage()).into(holder.imvCoupon);
        String time = Utilities.dateStringFormatToString(item.getFrom_date(), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy")
                + " " + "đến" + " " +
                Utilities.dateStringFormatToString(item.getTo_date(), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");
        holder.tvTime.setText(time);
        holder.tvBalance.setText(Utilities.NubmerFormatText(String.format("%.0f", item.getPrice())));
        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyCouponServer buyCouponServer = new BuyCouponServer();
                buyCouponServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(item.getId()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClickListener(holder.itemView, holder.getAdapterPosition(), item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class BuyCouponServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_COUPON_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_COUPON_ID, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_COUPON, "POST", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    //sessionUser
                    Log.e("Buy coupon ", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        dialogMessage = DialogUtils.dialogSuccessTimViec(mContext, mContext.getString(R.string.buy_coupon_success), new DialogUtils.DialogCallBack() {
                            @Override
                            public void onYesClickListener() {
                                mListener.onItemBuySuccessListener();
                                dialogMessage.dismiss();
                            }

                            @Override
                            public void onCancelClickListener() {

                            }
                        });
                    } else {
                        if (!jsonResult.isNull(Common.JsonKey.KEY_MESSAGE)) {
                            Toast.makeText(mContext, jsonResult.getString(Common.JsonKey.KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

    public interface OnItemClick {
        void onItemClickListener(View view, int position, Object item);

        void onItemBuySuccessListener();
    }

    public class CouponHolder extends RecyclerView.ViewHolder {
        private ImageView imvCoupon;
        private TextView tvTime;
        private TextView tvBalance;
        private ImageView imvCoin;
        private Button btnBuy;

        public CouponHolder(View itemView) {
            super(itemView);
            imvCoupon = (ImageView) itemView.findViewById(R.id.imv_coupon);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvBalance = (TextView) itemView.findViewById(R.id.tv_balance);
            imvCoin = (ImageView) itemView.findViewById(R.id.imv_coin);
            btnBuy = (Button) itemView.findViewById(R.id.btn_buy);
        }
    }
}

