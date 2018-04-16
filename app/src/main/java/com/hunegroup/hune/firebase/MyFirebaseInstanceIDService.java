package com.hunegroup.hune.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

/**
 * Created by tskil on 8/21/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyAndroidFCMIIDService";
    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
