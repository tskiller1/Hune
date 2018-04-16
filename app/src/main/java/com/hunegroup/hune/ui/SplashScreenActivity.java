package com.hunegroup.hune.ui;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hunegroup.hune.R;
import com.hunegroup.hune.gps.GPSTracker;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

public class SplashScreenActivity extends AppCompatActivity {
    public static Context context;
    JSONParser jsonParser = new JSONParser();
    GPSTracker gpsTracker;
    SessionUser sessionUser;
    private final Handler handler = new Handler();
    private final Runnable waitTask = new Runnable() {
        public void run() {

            if (Utilities.isConnectingToInternet(getBaseContext())) {

                if (sessionUser.getUserDetails().getToken() != null) {
                    // vao trang home
                    if (gpsTracker.canGetLocation()) {
                        UpdateLocationServer updateLocationServer = new UpdateLocationServer();
                        updateLocationServer.execute(sessionUser.getUserDetails().getToken(), gpsTracker.getLatitude() + "", gpsTracker.getLongitude() + "");
                    } else {
                        showSettingsAlert();
                    }
                } else {
                    // vao trang dang nhap
                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(getBaseContext(), "Please check your network connection!.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    };

    public void showSettingsAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_open_location);
        dialog.setTitle("");
        dialog.setCanceledOnTouchOutside(false);
        final Button btnCo = (Button) dialog.findViewById(R.id.btnCo);
        Button btnKhong = (Button) dialog.findViewById(R.id.btnKhong);
        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 101);
            }
        });
        btnKhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                    System.exit(0);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                    System.exit(0);
                } else {
                    finish();
                    System.exit(0);
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Intent intent = new Intent(SplashScreenActivity.this, SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_splash_screen);
        switchLangauge(this, true);//cal  lai trong activity khac la ok nhen cho nao no khong doi thi em zem file string co no chua nhen
        sessionUser = new SessionUser(getBaseContext());
        gpsTracker = new GPSTracker(getBaseContext());
        context = getBaseContext();
        if (getIntent().getExtras() != null) {
            Intent getintent = getIntent();
            try {
                if (getintent.getStringExtra("type") != null
                        && getintent.getStringExtra("post_id") != null
                        && getintent.getStringExtra("owner_post") != null) {
//                    String title = getintent.getStringExtra("title");
//                    String body = getintent.getStringExtra("body");
                    int type = Integer.parseInt(getintent.getStringExtra("type"));
                    int owner_post = Integer.parseInt(getintent.getStringExtra("owner_post"));
                    int post_id = Integer.parseInt(getintent.getStringExtra("post_id"));

                    Intent intent = null;
                    if (sessionUser.getUserDetails().getToken() == null) {
                        intent = new Intent(this, SplashScreenActivity.class);
                    } else {
                        if (post_id != 0) {
                            if (type == Integer.parseInt(Common.Type.TYPE_ENROLLMENT)) {
                                Log.e("type postid owner id", "true");
                                intent = new Intent(this, ChiTietCongViecTimViecActivity.class);
                                intent.putExtra(Common.JsonKey.KEY_USER_ID, post_id);
                                intent.putExtra("type", 3);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            } else if (type == Integer.parseInt(Common.Type.TYPE_SEARCH_JOB)) {
                                Log.e("type postid owner id", "false");
                                intent = new Intent(this, ChiTietUngVienTuyenDungActivity.class);
                                intent.putExtra(Common.JsonKey.KEY_USER_ID, owner_post);
                                intent.putExtra("id_post", post_id);
                                intent.putExtra("type", 3);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            } else intent = new Intent(this, SplashScreenActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
            } catch (NumberFormatException ex) {

            }
        }
        // check android 6.0
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            insertDummyContactWrapper();
        } else {
            handler.postDelayed(waitTask, 1500);
        }
        // de sau nay neu ma user da dang nhap thi cho vao trang home

       // generateHashkey();
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(waitTask);
        super.onBackPressed();
    }



 /* todo : begind check pemission */

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void insertDummyContactWrapper() {

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        //CAMERA
        /*if (!addPermission(permissionsList, android.Manifest.permission.CAMERA)) {
            permissionsNeeded.add("Read camera");
        }
        //WRITE_EXTERNAL_STORAGE
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Write Contacts storage");
        }
        //READ_EXTERNAL_STORAGE
        if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Read Contacts storage");
        }*/
        //ACCESS_FINE_LOCATION
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionsNeeded.add("Read CACCESS FINE LOCATION");
        }
        //ACCESS_COARSE_LOCATION
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissionsNeeded.add("Read ACCESS COARSE LOCATION");
        }


        //READ_CONTACTS
        /*if (!addPermission(permissionsList, android.Manifest.permission.READ_CONTACTS)) {
            permissionsNeeded.add("Read READ CONTACTS");
        }*/
        //GET_ACCOUNTS
        /*if (!addPermission(permissionsList, android.Manifest.permission.GET_ACCOUNTS)) {
            permissionsNeeded.add("Read GET ACCOUNTS");
        }*/
        //READ_PHONE_STATE
        /*if (!addPermission(permissionsList, android.Manifest.permission.READ_PHONE_STATE)) {
            permissionsNeeded.add("Read READ PHONE STATE");
        }*/
        //CALL_PHONE
        /*if (!addPermission(permissionsList, android.Manifest.permission.CALL_PHONE)) {
            permissionsNeeded.add("Read CALL PHONE");
        }*/
        /*//WRITE_SETTINGS
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_SETTINGS)) {
            permissionsNeeded.add("Read WRITE SETTINGS");
        }
        */

        if (permissionsList.size() > 0) {

            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                if (Build.VERSION.SDK_INT >= 23) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }

            return;
        }

        insertDummyContact();
    }

    private void insertDummyContact() {
        handler.postDelayed(waitTask, 1500);
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    /* todo : end check pemision */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
//                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
//                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
//                perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
//                perms.put(android.Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
//                perms.put(android.Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);
                // perms.put(android.Manifest.permission.WRITE_SETTINGS, PackageManager.PERMISSION_GRANTED);
//                perms.put(android.Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
//                if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(android.Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                    // && perms.get(android.Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED

                        ) {
                    // All Permissions Granted
                    insertDummyContact();
                } else {
                    // Permission Denied
                    insertDummyContact();
                    Toast.makeText(SplashScreenActivity.this, getResources().getString(R.string.capquyen), Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    public static final String PACKAGE = "com.hunegroup.hune";

    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(PACKAGE, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("package_name:", "" + info.packageName);
                Log.e("hash_key:", "" + Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                printHashKey();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("capt", e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("capt", e.getMessage(), e);
        }
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(PACKAGE, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("printHashKey:", "" + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            // Log.e("printHashKey()", e);
        } catch (Exception e) {
            //Log.e( "printHashKey()", e);
        }
    }

    /* todo : end ======================================== end */
    private class UpdateLocationServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LATITUDE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LONGITUDE, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_UPDATE_LOCATION, "PUT", params);

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

                Log.e("caca", "gps:" + result);

                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        ProfileServer profileServer = new ProfileServer();
                        Log.e("messaging_token", FirebaseInstanceId.getInstance().getToken());
                        profileServer.execute(sessionUser.getUserDetails().getToken(), FirebaseInstanceId.getInstance().getToken());
                    } else {
                        Log.e("MSG", jsonResult.getString(Common.JsonKey.KEY_MESSAGE));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }

    private class ProfileServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_FCM_TOKEN, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_PROFILE, "PUT", params);

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

                    Log.e("caca", "firebase_change_token:" + result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE);
                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Intent intent = new Intent(SplashScreenActivity.this, ChonChucNangActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }

}
