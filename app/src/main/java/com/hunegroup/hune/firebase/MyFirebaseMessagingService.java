package com.hunegroup.hune.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.Extra;
import com.hunegroup.hune.ui.ChiTietCongViecTimViecActivity;
import com.hunegroup.hune.ui.ChiTietUngVienTuyenDungActivity;
import com.hunegroup.hune.ui.SplashScreenActivity;
import com.hunegroup.hune.uiv2.ChiTietKhuyenMaiTuyenDungActivity;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.SessionUser;

/**
 * Created by tskil on 8/21/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message: " + remoteMessage.getData().get("title"));
        //create notification
        if (remoteMessage.getData().size() > 0) {
            createNotification(remoteMessage.getNotification().getTitle()
                    , remoteMessage.getNotification().getBody()
                    , Integer.parseInt(remoteMessage.getData().get("type"))
                    , Integer.parseInt(remoteMessage.getData().get("owner_post"))
                    , Integer.parseInt(remoteMessage.getData().get("post_id")),
                    remoteMessage.getData().get("extra")
            );
        }
    }

    private void createNotification(String messageTitle, String messageBody, int type, int owner_post, int post_id, String extra) {
        SessionUser sessionUser = new SessionUser(getApplicationContext());
        Intent intent = null;
        if (sessionUser.getUserDetails().getToken() == null) {
            intent = new Intent(this, SplashScreenActivity.class);
        } else {
            if (type == 4) {
                intent = new Intent(this, ChiTietKhuyenMaiTuyenDungActivity.class);
                Extra extraDTO = new Gson().fromJson(extra, Extra.class);
                intent.putExtra("ads_id", extraDTO.getAds_id());
                intent.putExtra("type", 2);
            } else if (post_id != 0) {
                if (type == Integer.parseInt(Common.Type.TYPE_ENROLLMENT)) {
                    intent = new Intent(this, ChiTietCongViecTimViecActivity.class);
                    intent.putExtra(Common.JsonKey.KEY_USER_ID, post_id);
                    intent.putExtra("type", 3);
                } else if (type == Integer.parseInt(Common.Type.TYPE_SEARCH_JOB)) {
                    intent = new Intent(this, ChiTietUngVienTuyenDungActivity.class);
                    intent.putExtra(Common.JsonKey.KEY_USER_ID, owner_post);
                    intent.putExtra("id_post", post_id);
                    intent.putExtra("type", 3);
                } else intent = new Intent(this, SplashScreenActivity.class);
            } else intent = new Intent(this, SplashScreenActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_push_notification)
                .setColor(getResources().getColor(R.color.color_main))
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
