package com.hunegroup.hune.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.CategoryDTO;
import com.hunegroup.hune.dto.PostResultDTO;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.gps.GPSTracker;
import com.hunegroup.hune.itf.CallbackBitmap;
import com.hunegroup.hune.uiv2.CuaHangHuneTimViecActivity;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionLocation;
import com.hunegroup.hune.util.SessionUser;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hunegroup.hune.util.Common.ADS_BANNER;
import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 06/07/2017.
 */

public class TimViecActivity extends AppCompatActivity implements OnMapReadyCallback
        , GoogleMap.OnInfoWindowClickListener
        , GoogleMap.OnMarkerClickListener
        , GoogleMap.OnCameraIdleListener {
    public static int CODE_REQUEST = 200;
    public static int CODE_RESPONSE = 100;
    private static String LIMIT_OF_LOAD_DATA_TO_MAP = "100";
    private static float ZOOM_DEFAULT = 17f;
    private static float MIN_ZOOM = 12f;

    private ImageView imgBack;
    private ImageView imgNotify;
    private RelativeLayout lnFooter;
    private RelativeLayout lnFooterChild;
    private RelativeLayout rlListChildBack;
    private LinearLayout lllist_filter;
    private ImageView imgLeft;
    private ImageView imgRight;
    private ImageView imgBackChild;
    private SupportMapFragment fragmentMap;
    private LinearLayout lllist_child;
    private Button btnDangTimNguoi;
    private ProgressBar progressBar;
    private RelativeLayout rela_temp;
    private TextView tvNumNotification;
    private RelativeLayout rlStore;

    private GoogleMap gMap;
    private LatLng latLngPrevous;
    private LatLng centerScreen;
    private boolean isFirst = false;
    private JSONParser jsonParser = new JSONParser();
    private SessionUser sessionUser;
    String category_parent_id;
    String category_id;
    String token;
    GPSTracker gpsTracker;
    List<CategoryDTO> categoryDTOList;
    List<PostResultDTO> postResultDTOList;
    private boolean isLoadFirst = false;
    boolean isFirstMarker = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == 200) {
            tvNumNotification.setText("0");
        } else if (requestCode == CODE_REQUEST) {
            if (resultCode == CODE_RESPONSE) {
                LatLng latLng = new LatLng(data.getDoubleExtra("latitude", 0), data.getDoubleExtra("longitude", 0));
                category_parent_id = String.valueOf(data.getIntExtra("category_parent_id", 0));
                category_id = String.valueOf(data.getIntExtra("category_id", 0));
                loadDataToMaps(latLng, getRadiusOfCircle() + "", category_parent_id, category_id);
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    gMap.setMyLocationEnabled(true);
                    gMap.setOnInfoWindowClickListener(this);
                    gMap.setOnMarkerClickListener(this);
                    gMap.setOnCameraIdleListener(this);
                    gMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    gMap.setMinZoomPreference(MIN_ZOOM);
                    if (!isLoadFirst) {
                        isLoadFirst = true;
                        if (gpsTracker.canGetLocation()) {
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), ZOOM_DEFAULT));
                        } else
                            Toast.makeText(TimViecActivity.this, getString(R.string.toast_vuilongbatvitri), Toast.LENGTH_SHORT).show();
                    }
                    if (getIntent().getExtras() != null) {
                        Intent data = getIntent();
                        LatLng latLng = new LatLng(data.getDoubleExtra("latitude", 0), data.getDoubleExtra("longitude", 0));
                        category_parent_id = String.valueOf(data.getIntExtra("category_parent_id", 0));
                        category_id = String.valueOf(data.getIntExtra("category_id", 0));
                        loadDataToMaps(latLng, getRadiusOfCircle() + "", category_parent_id, category_id);
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT));
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_timviec);
        findViews();
        sessionUser = new SessionUser(getBaseContext());
        getNotificationServer_Counter getNotificationServer_counter = new getNotificationServer_Counter();
        getNotificationServer_counter.execute(sessionUser.getUserDetails().getToken());
        CategoryServer categoryServer = new CategoryServer();
        categoryServer.execute("0");
        token = sessionUser.getUserDetails().getToken();
        gpsTracker = new GPSTracker(getBaseContext());
        // --todo : ads google banner ---
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLayout);
        AdView adView = new AdView(getBaseContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(ADS_BANNER);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
        // --todo : ads google banner ---
    }

    private void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        lllist_filter = (LinearLayout) findViewById(R.id.ll_list_filter);
        lllist_child = (LinearLayout) findViewById(R.id.ll_list_child);
        rlListChildBack = (RelativeLayout) findViewById(R.id.rlListChildBack);
        imgBack = (ImageView) findViewById(R.id.imgInfomation);
        imgNotify = (ImageView) findViewById(R.id.imgNotify);
        lnFooter = (RelativeLayout) findViewById(R.id.ln_footer);
        lnFooterChild = (RelativeLayout) findViewById(R.id.ln_footerChild);
        imgBackChild = (ImageView) findViewById(R.id.imgBackChild);
        imgLeft = (ImageView) findViewById(R.id.imgLeft);
        imgRight = (ImageView) findViewById(R.id.imgRight);
        btnDangTimNguoi = (Button) findViewById(R.id.btnDangTimNguoi);
        tvNumNotification = (TextView) findViewById(R.id.tv_num_notification);
        rlStore = (RelativeLayout) findViewById(R.id.rl_store);

        fragmentMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        fragmentMap.getMapAsync(this);

        rlStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TimViecActivity.this, CuaHangHuneTimViecActivity.class));
            }
        });

        rlListChildBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnFooter.setVisibility(View.VISIBLE);
                lnFooterChild.setVisibility(View.GONE);
                lllist_child.removeAllViews();
                category_id = null;
                loadDataToMaps(gMap.getCameraPosition().target, String.valueOf(((double) getRadiusOfCircle() / 1000)), category_parent_id, category_id);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionUser.getUserDetails().getToken() != null) {
                    startActivity(new Intent(TimViecActivity.this, ProfileTimViecActivity.class));
                } else {
                    showDialog();
                }
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionUser.getUserDetails().getToken() != null) {
                    startActivityForResult(new Intent(TimViecActivity.this, ThongBaoTimViecActivity.class), 101);
                } else {
                    showDialog();
                }

            }
        });

        btnDangTimNguoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionUser.getUserDetails().getToken() != null) {
                    startActivity(new Intent(TimViecActivity.this, ThemViecLamTimViecActivity.class));
                } else {
                    showDialog();
                }

            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(TimViecActivity.this);
        dialog.setContentView(R.layout.dialog_dangnhap_timviec);
        dialog.setTitle("");
        dialog.setCanceledOnTouchOutside(false);
        Button btnCo = (Button) dialog.findViewById(R.id.btnCo);
        Button btnKhong = (Button) dialog.findViewById(R.id.btnKhong);
        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TimViecActivity.this, WelcomeActivity.class));
                finish();
                dialog.dismiss();
            }
        });
        btnKhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Google Maps API Function
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            List<String> permissions = new ArrayList<>();
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            requestPermissions(permissions.toArray(new String[permissions.size()]), 100);
            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.setOnInfoWindowClickListener(this);
        gMap.setOnMarkerClickListener(this);
        gMap.setOnCameraIdleListener(this);
        gMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setMinZoomPreference(MIN_ZOOM);

        if (getIntent().getExtras() != null) {
            Intent data = getIntent();
            LatLng latLng = new LatLng(data.getDoubleExtra("latitude", 0), data.getDoubleExtra("longitude", 0));
            category_parent_id = String.valueOf(data.getIntExtra("category_parent_id", 0));
            category_id = String.valueOf(data.getIntExtra("category_id", 0));
            if (!category_parent_id.equals("0")) {
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT));
                isFirst = false;
                isLoadFirst = true;
            }
        }

        if (!isLoadFirst) {
            isLoadFirst = true;
            if (gpsTracker.canGetLocation()) {
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), ZOOM_DEFAULT));
            } else
                Toast.makeText(TimViecActivity.this, getString(R.string.toast_vuilongbatvitri), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (sessionUser.getUserDetails().getToken() != null) {
            int dd = findPositionMarker(marker);
            if (dd != -1) {
                int temp = postResultDTOList.get(dd).getId();
                Log.e("TEMP", temp + "");
                Intent intent = new Intent(TimViecActivity.this, ChiTietCongViecTimViecActivity.class);
                intent.putExtra(Common.JsonKey.KEY_USER_ID, temp);
                intent.putExtra("type", 1);
                startActivityForResult(intent, CODE_REQUEST);
            }
        } else {
            showDialog();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onCameraIdle() {
        String radius = String.valueOf(((double) getRadiusOfCircle() / 1000));
        SessionLocation.getInstance().setLocation(gMap.getCameraPosition().target);
        SessionLocation.getInstance().setType(1);
        SessionLocation.getInstance().setRadius((double) getRadiusOfCircle() / 1000);
        if (!isFirst) {
            Log.e("CATEGORY", category_parent_id + " " + category_id);
            latLngPrevous = gMap.getCameraPosition().target;
            LatLng location = gMap.getCameraPosition().target;
            if (category_parent_id != null)
                loadDataToMaps(location, radius, category_parent_id, null);
            isFirst = true;
        } else {
            centerScreen = gMap.getCameraPosition().target;
            if (latLngPrevous != null && calculationByDistance(latLngPrevous, centerScreen) > getDistanceToRefresh()) {
                LatLng location = gMap.getCameraPosition().target;
                if (category_id == null || category_id.equals("0")) {
                    category_id = null;
                }
                loadDataToMaps(location, radius, category_parent_id, category_id);
                latLngPrevous = centerScreen;
            }
        }

    }

    //Another Function
    private void loadDataToMaps(LatLng location, String radius, String category_parent_id, String category_id) {
        gMap.clear();
        GetPostServer getPostServer = new GetPostServer();
        String latitude = location.latitude + "";
        String longtiude = location.longitude + "";
        String type = Common.Type.TYPE_ENROLLMENT;
        getPostServer.execute(type, latitude, longtiude, radius, category_parent_id, category_id);
    }

    synchronized private void getMarkerBitmapFromBitmap(final CallbackBitmap callBackB, final String uri) {
        final View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        final CircleImageView markerImageView = (CircleImageView) customMarkerView.findViewById(R.id.profile_image);
        Glide.with(getBaseContext()).load(uri).asBitmap().into(new BitmapImageViewTarget(markerImageView) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                super.onResourceReady(bitmap, anim);
                if (getBaseContext() != null) {
                    customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
                    customMarkerView.buildDrawingCache();
                    Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                            Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(returnedBitmap);
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    Drawable drawable = customMarkerView.getBackground();
                    if (drawable != null)
                        drawable.draw(canvas);
                    customMarkerView.draw(canvas);
                    callBackB.onSuccess(returnedBitmap);
                }
            }
        });
//        Picasso.with(getBaseContext()).load(uri).error(R.mipmap.ic_man).into(markerImageView, new Callback() {
//            @Override
//            public void onSuccess() {
//                if (getBaseContext() != null) {
//                    customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                    customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
//                    customMarkerView.buildDrawingCache();
//                    Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
//                            Bitmap.Config.ARGB_8888);
//                    Canvas canvas = new Canvas(returnedBitmap);
//                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
//                    Drawable drawable = customMarkerView.getBackground();
//                    if (drawable != null)
//                        drawable.draw(canvas);
//                    customMarkerView.draw(canvas);
//                    callBackB.onSuccess(returnedBitmap);
//                }
//            }
//
//            @Override
//            public void onError() {
//                Log.e("ERRORRRRR", "TRUEEEEEE");
//            }
//        });
        /*Picasso.with(getBaseContext()).load(uri).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                markerImageView.setImageBitmap(bitmap);
                customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
                customMarkerView.buildDrawingCache();
                Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);
                canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
                Drawable drawable = customMarkerView.getBackground();
                if (drawable != null)
                    drawable.draw(canvas);
                customMarkerView.draw(canvas);
                callBackB.onSuccess(returnedBitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });*/
    }

    public int getDistanceToRefresh() {
        return getRadiusOfCircle() * 2 / 5;
    }

    public int getRadiusOfCircle() {
        if (gMap != null) {
            //13 meters in zoom 21
            int raidus = (int) (13 * Math.pow(2, 21 - gMap.getCameraPosition().zoom)) + 50;
            return raidus;
        }
        return -1;
    }

    public int calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        DecimalFormat newFormat = new DecimalFormat("####");
        double meter = (valueResult / 1) * 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));

        return meterInDec;
    }

    private Marker createAvatarMarkerNew(LatLng location, String title, String description, double rating, Bitmap bitmap) {

        Marker marker = gMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .title(title)
                .snippet(description)
                .zIndex((float) rating)

        );
        if (!isFirstMarker) {
            isFirstMarker = true;
            marker.showInfoWindow();
        }
        return marker;
    }
   /* private void createAvatarMarker(LatLng location, String title, String description, double rating) {

        Marker marker = gMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.avatar)))
                .title(title)
                .snippet(description)
                .zIndex((float) rating)

        );
        marker.showInfoWindow();
    }*/

    private int findPositionMarker(Marker marker) {

        for (int i = 0; i < postResultDTOList.size(); i++) {
            if (marker.equals(postResultDTOList.get(i).getMarker()))
                return i;
        }
        return -1;
    }

    private class getNotificationServer_Counter extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_NOTIFICATION_TOKEN, urls[0]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_NOTIFICATION_COUNTER, "GET", params);

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
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {

                        JSONObject jsondata = new JSONObject(jsonResult.getJSONObject(Common.JsonKey.KEY_DATA).toString());

                        if (!jsondata.isNull(Common.JsonKey.KEY_TOTAL)) {
                            String slTuyenDung = String.valueOf(jsondata.getInt(Common.JsonKey.KEY_TOTAL));
                            tvNumNotification.setText(slTuyenDung);
                        }

                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

    private class GetPostServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TYPE, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LATITUDE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LONGITUDE, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_RADIUS, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID, urls[4]));
                if (urls[5] != null) {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_CATEGORY_ID, urls[5]));
                }
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_LIMIT, LIMIT_OF_LOAD_DATA_TO_MAP));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST, "GET", params);

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

                Log.e("caca", "post:" + result);

                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();
                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            postResultDTOList = new ArrayList<>();
                            isFirstMarker = false;
                            JSONArray jsonArray = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                final PostResultDTO postResultDTO = new PostResultDTO();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_TITLE))
                                    postResultDTO.setTitle(jsonObject.getString(Common.JsonKey.KEY_POST_TITLE));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_ID))
                                    postResultDTO.setId(jsonObject.getInt(Common.JsonKey.KEY_POST_ID));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_RATING))
                                    postResultDTO.setStar(jsonObject.getDouble(Common.JsonKey.KEY_POST_RATING));
                                else postResultDTO.setStar(0);
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_USER_ID))
                                    postResultDTO.setUser_id(jsonObject.getInt(Common.JsonKey.KEY_POST_USER_ID));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_DISTANCE))
                                    postResultDTO.setDistance(jsonObject.getDouble(Common.JsonKey.KEY_POST_DISTANCE));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_CATEGORY_ID))
                                    postResultDTO.setCategory_id(jsonObject.getInt(Common.JsonKey.KEY_POST_CATEGORY_ID));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_DESCRIPTION))
                                    postResultDTO.setDescription(jsonObject.getString(Common.JsonKey.KEY_POST_DESCRIPTION));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID))
                                    postResultDTO.setCategory_parent_id(jsonObject.getInt(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_LATITUDE))
                                    postResultDTO.setLatitude(Float.parseFloat(jsonObject.getString(Common.JsonKey.KEY_POST_LATITUDE)));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_LONGITUDE))
                                    postResultDTO.setLongitude(Float.parseFloat(jsonObject.getString(Common.JsonKey.KEY_POST_LONGITUDE)));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_THUMBNAIL))
                                    postResultDTO.setThumbnail(jsonObject.getString(Common.JsonKey.KEY_POST_THUMBNAIL));
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_STATUS)) {
                                    postResultDTO.setStatus(jsonObject.getInt(Common.JsonKey.KEY_POST_STATUS));
                                    if (postResultDTO.getStatus() == 2) {
                                        postResultDTO.setTitle("(<i>" + getString(R.string.datat) + "</i>)" + postResultDTO.getTitle());
                                    }
                                }
                                if (!jsonObject.isNull(Common.JsonKey.KEY_POST_USER)) {
                                    JSONObject user = jsonObject.getJSONObject(Common.JsonKey.KEY_POST_USER);
                                    UserDTO userDTO = new UserDTO();
                                    userDTO.setId(user.getInt(Common.JsonKey.KEY_USER_ID));
                                    userDTO.setPhone(user.getString(Common.JsonKey.KEY_USER_PHONE));
                                    userDTO.setFull_name(user.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                                    userDTO.setAvatar(user.getString(Common.JsonKey.KEY_USER_AVATAR));
                                    postResultDTO.setUser(userDTO);
                                }
                                CallbackBitmap callBackB = new CallbackBitmap() {
                                    @Override
                                    public void onSuccess(Bitmap bitmap) {
                                        postResultDTO.setMarker(createAvatarMarkerNew(new LatLng(postResultDTO.getLatitude(), postResultDTO.getLongitude()), postResultDTO.getTitle(), postResultDTO.getDescription(), postResultDTO.getStar(), bitmap));
                                    }
                                };
                                getMarkerBitmapFromBitmap(callBackB, postResultDTO.getUser().getAvatar());
                                postResultDTOList.add(postResultDTO);
                            }
                        }
                    }
//                    postResultDTOList.get(0).getMarker().showInfoWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }

    private class CategoryServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
                            if (i == 0 && (getIntent().getExtras() == null || category_parent_id == null) || category_parent_id.equals("0")) {
                                category_parent_id = categoryDTO.getId() + "";
                            }
                            View subview = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_tab_filter, null);
                            TextView tv_namefilter = (TextView) subview.findViewById(R.id.tv_name_filter);
                            ImageView imgIcon = (ImageView) subview.findViewById(R.id.imgIcon);
                            tv_namefilter.setText(categoryDTO.getName());
//                            AQuery aq;
//                            aq = new AQuery(getBaseContext());
//                            aq.id(R.id.imgIcon).image(categoryDTO.getIcon(),
//                                    true, true, 0, R.drawable.avatar);
                            Picasso.with(getBaseContext()).load(categoryDTO.getIcon()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imgIcon);
                            lllist_filter.addView(subview);
                            categoryDTOList.get(i).setTxtParent(tv_namefilter);
                            final int k = i;
                            subview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    category_parent_id = String.valueOf(categoryDTO.getId());
                                    lnFooter.setVisibility(View.GONE);
                                    lnFooterChild.setVisibility(View.VISIBLE);
                                    try {
                                        category_id = categoryDTO.getChild().get(0).getId() + "";
                                        loadDataToMaps(gMap.getCameraPosition().target, String.valueOf(((double) getRadiusOfCircle() / 1000)), category_parent_id, category_id);
                                    } catch (Exception ex) {
                                        category_id = null;
                                        loadDataToMaps(gMap.getCameraPosition().target, String.valueOf(((double) getRadiusOfCircle() / 1000)), category_parent_id, category_id);
                                    }
                                    if (categoryDTO.getChild() != null) {
                                        List<TextView> listTxtChild = new ArrayList<TextView>();
                                        for (int i = 0; i < categoryDTO.getChild().size(); i++) {
                                            View subChild = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_tab_filter_child, null);
                                            CategoryDTO cateChild = categoryDTO.getChild().get(i);
                                            RelativeLayout rela = (RelativeLayout) subChild.findViewById(R.id.item_tab_filter);
                                            TextView tv_namefilter = (TextView) subChild.findViewById(R.id.tv_name_filter);
                                            ImageView imgIcon = (ImageView) subChild.findViewById(R.id.imgIcon);
                                            if (i == 0) {
                                                rela.setBackgroundColor(getResources().getColor(R.color.colorMainAccent));
                                                rela_temp = rela;
                                            }
                                            tv_namefilter.setText(cateChild.getName());
                                            Picasso.with(getBaseContext()).load(cateChild.getIcon()).into(imgIcon);
                                            tv_namefilter.setTextColor(Color.parseColor("#33ccff"));
                                            lllist_child.addView(subChild);
                                            listTxtChild.add(tv_namefilter);
                                            final String cate_child_id = cateChild.getId() + "";
                                            subChild.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    RelativeLayout rela = (RelativeLayout) view.findViewById(R.id.item_tab_filter);
                                                    if (rela != rela_temp) {
                                                        rela.setBackgroundColor(getResources().getColor(R.color.colorMainAccent));
                                                        rela_temp.setBackgroundColor(getResources().getColor(R.color.colorTextWhite));
                                                        rela_temp = rela;
                                                    }
                                                    category_id = cate_child_id;
                                                    loadDataToMaps(gMap.getCameraPosition().target, String.valueOf(((double) getRadiusOfCircle() / 1000)), category_parent_id, category_id);
                                                }
                                            });
                                        }
                                        categoryDTOList.get(k).setListTxtChild(listTxtChild);
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(TimViecActivity.this, "Hiện tại chưa có danh sách loại hình", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            tvTitle.setText(Html.fromHtml(marker.getTitle()));
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
            String s;
            if (marker.getSnippet().length() > 52)
                s = marker.getSnippet().substring(0, 52) + "...";
            else s = marker.getSnippet();
            tvSnippet.setText(s);
            RatingBar rtbDanhGia = (RatingBar) myContentsView.findViewById(R.id.rtbDanhGia);
            rtbDanhGia.setRating(marker.getZIndex());
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    @Override
    protected void onDestroy() {
        if (gMap != null) {
            gMap.clear();
        }
        unbindDrawables(findViewById(R.id.ln_timnguoi_td));
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();

    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public void onLowMemory() {
        fragmentMap.onLowMemory();
        super.onLowMemory();
    }

    @Override
    protected void onPause() {
        fragmentMap.onPause();
        super.onPause();


    }


    @Override
    protected void onResume() {
        fragmentMap.onResume();
        super.onResume();


    }
}
