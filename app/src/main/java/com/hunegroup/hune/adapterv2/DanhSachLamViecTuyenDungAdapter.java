package com.hunegroup.hune.adapterv2;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.TaskDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Utilities;
import com.hunegroup.hune.util.Validate;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by apple on 11/16/17.
 */

public class DanhSachLamViecTuyenDungAdapter extends ArrayAdapter<TaskDTO> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-11-16 14:54:00 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout item_v2_danhsachlamviec_tuyendung;
        public final RelativeLayout lnlStatus;
        public final ImageView imvStatus;
        public final TextView tvStatus;
        public final RelativeLayout rlTitle;
        public final TextView tvTitle;
        public final ImageView imvDelete;
        public final RelativeLayout rlSalary;
        public final TextView tvSalary;
        public final TextView tvSalaryUnit;
        public final TextView tvStatusPayment;
        public final ImageView imvTypePost;
        public final LinearLayout lnlStart;
        public final TextView tvStart;
        public final LinearLayout lnlEnd;
        public final TextView tvEnd;
        public final RelativeLayout rlRating;
        public final TextView tvRating;
        public final MaterialRatingBar rtbRating;

        private ViewHolder(LinearLayout item_v2_danhsachlamviec_tuyendung, RelativeLayout lnlStatus, ImageView imvStatus, TextView tvStatus, RelativeLayout rlTitle, TextView tvTitle, ImageView imvDelete, RelativeLayout rlSalary, TextView tvSalary, TextView tvSalaryUnit, TextView tvStatusPayment, ImageView imvTypePost, LinearLayout lnlStart, TextView tvStart, LinearLayout lnlEnd, TextView tvEnd, RelativeLayout rlRating, TextView tvRating, MaterialRatingBar rtbRating) {
            this.item_v2_danhsachlamviec_tuyendung = item_v2_danhsachlamviec_tuyendung;
            this.lnlStatus = lnlStatus;
            this.imvStatus = imvStatus;
            this.tvStatus = tvStatus;
            this.rlTitle = rlTitle;
            this.tvTitle = tvTitle;
            this.imvDelete = imvDelete;
            this.rlSalary = rlSalary;
            this.tvSalary = tvSalary;
            this.tvSalaryUnit = tvSalaryUnit;
            this.tvStatusPayment = tvStatusPayment;
            this.imvTypePost = imvTypePost;
            this.lnlStart = lnlStart;
            this.tvStart = tvStart;
            this.lnlEnd = lnlEnd;
            this.tvEnd = tvEnd;
            this.rlRating = rlRating;
            this.tvRating = tvRating;
            this.rtbRating = rtbRating;
        }

        public static ViewHolder create(LinearLayout item_v2_danhsachlamviec_tuyendung) {
            RelativeLayout lnlStatus = (RelativeLayout) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.lnl_status);
            ImageView imvStatus = (ImageView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.imv_status);
            TextView tvStatus = (TextView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.tv_status);
            RelativeLayout rlTitle = (RelativeLayout) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.rl_title);
            TextView tvTitle = (TextView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.tv_title);
            ImageView imvDelete = (ImageView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.imv_delete);
            RelativeLayout rlSalary = (RelativeLayout) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.rl_salary);
            TextView tvSalary = (TextView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.tv_salary);
            TextView tvSalaryUnit = (TextView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.tv_salary_unit);
            TextView tvStatusPayment = (TextView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.tv_status_payment);
            ImageView imvTypePost = (ImageView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.imv_type_post);
            LinearLayout lnlStart = (LinearLayout) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.lnl_start);
            TextView tvStart = (TextView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.tv_start);
            LinearLayout lnlEnd = (LinearLayout) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.lnl_end);
            TextView tvEnd = (TextView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.tv_end);
            RelativeLayout rlRating = (RelativeLayout) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.rl_rating);
            TextView tvRating = (TextView) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.tv_rating);
            MaterialRatingBar rtbRating = (MaterialRatingBar) item_v2_danhsachlamviec_tuyendung.findViewById(R.id.rtb_rating);
            return new ViewHolder(item_v2_danhsachlamviec_tuyendung, lnlStatus, imvStatus, tvStatus, rlTitle, tvTitle, imvDelete, rlSalary, tvSalary, tvSalaryUnit, tvStatusPayment, imvTypePost, lnlStart, tvStart, lnlEnd, tvEnd, rlRating, tvRating, rtbRating);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_v2_danhsachlamviec_tuyendung, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final TaskDTO item = getItem(position);

        // TODO:Bind your data to the views here
        if (item.getStatus() == 1) {
            vh.imvStatus.setImageResource(R.mipmap.ic_waitting);
            vh.tvStatus.setTextColor(getContext().getResources().getColor(R.color.colorText));
            vh.tvStatus.setText(R.string.waiting);
        } else if (item.getStatus() == 2) {
            vh.imvStatus.setImageResource(R.mipmap.ic_working);
            vh.tvStatus.setTextColor(getContext().getResources().getColor(R.color.color_main));
            vh.tvStatus.setText(R.string.working);
        } else if (item.getStatus() == 3) {
            vh.imvStatus.setImageResource(R.mipmap.ic_finished);
            vh.tvStatus.setTextColor(getContext().getResources().getColor(R.color.green));
            vh.tvStatus.setText(R.string.finished);
        }
        if (sessionUser.getUserDetails().getId() == item.getOwner_id()) {
            vh.imvTypePost.setImageResource(R.mipmap.ic_sent);
        } else {
            vh.imvTypePost.setImageResource(R.mipmap.ic_received);
        }
        vh.tvTitle.setText(item.getName());
        String start = item.getStart_hour() + " - " + Validate.StringDateFormatToSetText(item.getStart_date());
        vh.tvStart.setText(start);
        String end = item.getEnd_hour() + " - " + Validate.StringDateFormatToSetText(item.getEnd_date());
        vh.tvEnd.setText(end);
        vh.tvSalary.setText(Utilities.NubmerFormatText(String.format("%.0f", item.getAmount())));
        if (item.getStatus_payment() == 1) {
            vh.tvStatusPayment.setVisibility(View.VISIBLE);
        } else {
            vh.tvStatusPayment.setVisibility(View.GONE);
        }
        if (item.getRate() > 0) {
            vh.tvRating.setVisibility(View.GONE);
            vh.rtbRating.setVisibility(View.VISIBLE);
            vh.rtbRating.setRating((float) item.getRate());
        } else {
            vh.tvRating.setVisibility(View.VISIBLE);
            vh.rtbRating.setVisibility(View.GONE);
        }
        vh.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogQuestion = DialogUtils.dialogQuestion(getContext(), getContext().getString(R.string.dialog_delete_text), new DialogUtils.DialogCallBack() {
                    @Override
                    public void onYesClickListener() {
                        if (item.getStatus() == 1) {
                            pos = position;
                            DeleteTaskServer deleteTaskServer = new DeleteTaskServer();
                            deleteTaskServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(item.getId()));
                            dialogQuestion.dismiss();
                        } else {
                            dialogQuestion.dismiss();
                            Toast.makeText(getContext(), R.string.rules_of_delete_task, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelClickListener() {
                        dialogQuestion.dismiss();
                    }
                });
            }
        });
        return vh.item_v2_danhsachlamviec_tuyendung;
    }


    private LayoutInflater mInflater;
    private JSONParser jsonParser = new JSONParser();
    private SessionUser sessionUser;
    private Dialog dialogQuestion;
    private List<TaskDTO> mList;
    private int pos;

    // Constructors
    public DanhSachLamViecTuyenDungAdapter(Context context, List<TaskDTO> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
        sessionUser = new SessionUser(context);
        mList = objects;
    }

    public DanhSachLamViecTuyenDungAdapter(Context context, TaskDTO[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
        sessionUser = new SessionUser(context);
    }

    private class DeleteTaskServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_ID, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_TASK + "/" + urls[1], "DELETE", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {

            if (getContext() == null)
                return;
            if (result != null) {
                try {
                    //sessionUser
                    Log.e("Delete Task", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        mList.remove(pos);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), R.string.rules_of_delete_task, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }
}
