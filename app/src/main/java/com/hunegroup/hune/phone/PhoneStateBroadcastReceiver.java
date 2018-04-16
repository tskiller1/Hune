package com.hunegroup.hune.phone;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hunegroup.hune.uiv2.NhapThongTinTimViecActivity;
import com.hunegroup.hune.uiv2.NhapThongTinTuyenDungActivity;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionType;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tskil on 8/8/2017.
 */

public class PhoneStateBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneStateBroadcastReceiver";
    Context mContext;
    String incoming_nr;
    private int prev_state;
    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //TelephonyManager object
        CustomPhoneStateListener customPhoneListener = new CustomPhoneStateListener();
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE); //Register our listener with TelephonyManager

        Bundle bundle = intent.getExtras();
        String phoneNr = bundle.getString("incoming_number");
        mContext = context;
        sessionUser = new SessionUser(mContext);
    }

    /* Custom PhoneStateListener */
    public class CustomPhoneStateListener extends PhoneStateListener

    {
        private static final String TAG = "CustomPhoneStateListener";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (incomingNumber != null && incomingNumber.length() > 0) incoming_nr = incomingNumber;

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    prev_state = state;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    prev_state = state;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if ((prev_state == TelephonyManager.CALL_STATE_OFFHOOK)) {
                        prev_state = state;
                        //Answered Call which is ended
                        if (SessionType.getInstance().getType() == 1) {
                            CallServer callServer = new CallServer();
                            callServer.execute(sessionUser.getUserDetails().getToken(), SessionType.getInstance().getPost_id() + "");
                            /*Intent intent = new Intent(mContext, DanhGiaTuyenDungActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);*/

                            Intent intent = new Intent(mContext, NhapThongTinTuyenDungActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                            SessionType.getInstance().setType(3);
                        }
                        if (SessionType.getInstance().getType() == 2) {
                            CallServer callServer = new CallServer();
                            callServer.execute(sessionUser.getUserDetails().getToken(), SessionType.getInstance().getPost_id() + "");
                            /*Intent intent = new Intent(mContext, DanhGiaTimViecActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);*/

                            Intent intent = new Intent(mContext, NhapThongTinTimViecActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                            SessionType.getInstance().setType(3);
                        }
                    }
                    if ((prev_state == TelephonyManager.CALL_STATE_RINGING)) {
                        prev_state = state;
                        //Rejected or Missed call\
                        if (SessionType.getInstance().getType() == 1) {
                            CallServer callServer = new CallServer();
                            callServer.execute(sessionUser.getUserDetails().getToken(), SessionType.getInstance().getPost_id() + "");
                           /* Intent intent = new Intent(mContext, DanhGiaTuyenDungActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);*/

                            Intent intent = new Intent(mContext, NhapThongTinTuyenDungActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                            SessionType.getInstance().setType(3);
                        }
                        if (SessionType.getInstance().getType() == 2) {
                            //Auto decrease
                            CallServer callServer = new CallServer();
                            callServer.execute(sessionUser.getUserDetails().getToken(), SessionType.getInstance().getPost_id() + "");
                            /*Intent intent = new Intent(mContext, DanhGiaTimViecActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);*/

                            Intent intent = new Intent(mContext, NhapThongTinTimViecActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                            SessionType.getInstance().setType(3);
                        }
                    }
                    break;

            }
        }
    }

    private class CallServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_ID, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_ID_POST + urls[1] + Common.RequestURL.API_POST_CONTACT, "PUT", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {
            Log.e("RESULT CALL", result);
            if (result != null) {
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Log.d("ThanhCong", "TRUE");
                    } else {
                        Log.d("ThanhCong", jsonResult.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }
}
