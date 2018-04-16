package com.hunegroup.hune.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hunegroup.hune.R;

import java.util.List;
import java.util.Locale;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

public class BanDoActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private ImageView imgBack;
    private double lat;
    private double log;
    private String diadiem = "";
    private FloatingActionButton actionButton;

    public String getDiadiem() {
        return diadiem;
    }

    public void setDiadiem(String diadiem) {
        this.diadiem = diadiem;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    private boolean isNull = false;
    public static int KEY_CODE = 222;
    private Marker marker;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_ban_do);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initView();

    }

    private void initView() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);

        actionButton = (FloatingActionButton) findViewById(R.id.btn_XacNhan);
        actionButton.setOnClickListener(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                LatLng mylocation = place.getLatLng();
                //getCompleteAddressString(getLat(),getLog());
                setLat(mylocation.latitude);
                setLog(mylocation.longitude);
                String s = place.getAddress().toString();
                diadiem = place.getName() + " - " + s;
                //addMarker(diadiem, mylocation);
                moveCamera(mylocation, 15f);
                //Toast.makeText(getBaseContext(), diadiem, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                //Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLatLog();
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
        mMap.setMyLocationEnabled(true);

        /*mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng latLng=mMap.getCameraPosition().target;
                addMarker("",latLng);
            }
        });*/
        mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {

            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng=mMap.getCameraPosition().target;
                setLat(latLng.latitude);
                setLog(latLng.longitude);
                //addMarker("",latLng);
                getCompleteAddressString(getLat(),getLog());
                //Toast.makeText(getBaseContext(), diadiem, Toast.LENGTH_SHORT).show();
            }
        });

        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setLat(latLng.latitude);
                setLog(latLng.longitude);
                getCompleteAddressString(getLat(),getLog());
                addMarker("",latLng);
                moveCamera(latLng,15f);
            }
        });*/
        // Add a marker in Sydney and move the camera


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
            case R.id.btn_XacNhan:
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("lat",getLat());
                returnIntent.putExtra("log",getLog());
                returnIntent.putExtra("diadiem",getDiadiem());
                setResult(BanDoActivity.KEY_CODE, returnIntent);
                finish();
                break;
            }

        }
    }
    public void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                /*StringBuilder strReturnedAddress = new StringBuilder("");
                StringBuilder diadiem = new StringBuilder("");*/
                setDiadiem(returnedAddress.getAddressLine(0));
               /* for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    if(i!=returnedAddress.getMaxAddressLineIndex()-1)
                        diadiem.append(returnedAddress.getAddressLine(i)).append(", ");
                    else
                        diadiem.append(returnedAddress.getAddressLine(i));
                }
                setDiadiem(diadiem.toString());*/
                //Toast.makeText(getBaseContext(), strReturnedAddress.toString(), Toast.LENGTH_SHORT).show();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }

    }
    private void addMarker(String locality,LatLng latLng){
        if(marker!=null)
            marker.remove();
        MarkerOptions markerOptions=new MarkerOptions()
                .title(locality)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(latLng);
        marker=mMap.addMarker(markerOptions);
    }
    private void moveCamera(LatLng latLng,float f)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, f));
    }
    private void makeText(String text)
    {
        Toast.makeText(getBaseContext(),text+"",Toast.LENGTH_SHORT).show();
    }
    private void getLatLog()
    {
        Intent intent=getIntent();
        String mylocation=intent.getStringExtra("mylocation");
        Geocoder gc=new Geocoder(this);
        if(mylocation==null) {
            try {
                List<Address> list=gc.getFromLocationName("Hồ Chí Minh",1);
                Address address=list.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                moveCamera(latLng,15f);
            } catch (Exception ex) {

            }
            isNull = true;
            return;
        }
        try {
            setDiadiem(mylocation+"");
            List<Address> list=gc.getFromLocationName(mylocation,1);
            if(list.isEmpty()) {
                makeText("Địa điểm nhập không tồn tại hoặc không tìm thấy!");
                isNull = true;
                return;
            }
            Address address=list.get(0);

            String locality=address.getLocality();

            setLat(address.getLatitude());
            setLog(address.getLongitude());

            if(!isNull) {
                LatLng latLng = new LatLng(getLat(), getLog());
                //addMarker(locality,latLng);
                moveCamera(latLng,15f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
