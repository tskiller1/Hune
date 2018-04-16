package com.hunegroup.hune.uiv2;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hunegroup.hune.R;
import com.hunegroup.hune.app.App;
import com.hunegroup.hune.dto.PositionBanner;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Validate;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hunegroup.hune.util.Common.JsonKey.KEY_POST_URL;
import static com.hunegroup.hune.util.Common.RequestCode.CHOOSE_IMAGE;
import static com.hunegroup.hune.util.Utilities.getClient;

/**
 * Created by apple on 12/12/17.
 */

public class QuangCaoBannerTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private LinearLayout lnlTarget;
    private Spinner spnTarget;
    private TextView tvSelectNumDay;
    private LinearLayout lnlUrl;
    private EditText edUrl;
    private ImageView imvBanner;
    private RelativeLayout rlUpload;
    private ImageView imvUpload;
    private Button btnContinue;

    private Dialog dialogProgress;

    //TODO:Declaring
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    Gson gson = new Gson();
    List<PositionBanner> positionBannerList;
    PositionAdapter adapter;
    List<Date> selectDateList;
    List<String> dateList;
    String imageURL;
    int position;
    String URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_quangcaobanner_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        lnlTarget = (LinearLayout) findViewById(R.id.lnl_target);
        spnTarget = (Spinner) findViewById(R.id.spn_target);
        tvSelectNumDay = (TextView) findViewById(R.id.tv_select_num_day);
        lnlUrl = (LinearLayout) findViewById(R.id.lnl_url);
        edUrl = (EditText) findViewById(R.id.ed_url);
        imvBanner = (ImageView) findViewById(R.id.imv_banner);
        rlUpload = (RelativeLayout) findViewById(R.id.rl_upload);
        imvUpload = (ImageView) findViewById(R.id.imv_upload);
        btnContinue = (Button) findViewById(R.id.btn_continue);

        spnTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = App.getInstance().getPositionBannerList().get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rlUpload.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        tvSelectNumDay.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        positionBannerList = App.getInstance().getPositionBannerList();
        adapter = new PositionAdapter(this, positionBannerList);
        spnTarget.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.tv_select_num_day:
                startActivityForResult(new Intent(this, ChonNgayTuyenDungActivity.class), 101);
                break;
            case R.id.rl_upload:
                showPhoto();
                break;
            case R.id.btn_continue:
                if (position != 0
                        && !TextUtils.isEmpty(tempPath)
                        && (selectDateList != null && selectDateList.size() != 0)
                        && !TextUtils.isEmpty(edUrl.getText())) {
                    if (URLUtil.isHttpUrl(edUrl.getText().toString()) || URLUtil.isHttpsUrl(edUrl.getText().toString())) {
                        UploadPhoto uploadPhoto = new UploadPhoto();
                        uploadPhoto.execute(tempPath);
                    /*PostAdsServer postAdsServer = new PostAdsServer();
                    postAdsServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(position), gson.toJson(dateList), edUrl.getText().toString(), imageURL);*/
                    } else {
                        Toast.makeText(this, R.string.urldungdinhdang, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.toast_vuilongnhapthongtin), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /* TODO: Pick Image */
    int i = 0;
    private String filePath = null;
    private File fileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    String tempPath = null;
    private String originImagePath;
    private Uri outputFileUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 200) {
            if (data.getExtras() != null) {
                Type listType = new TypeToken<ArrayList<Date>>() {
                }.getType();
                selectDateList = gson.fromJson(data.getStringExtra("days"), listType);
                if (selectDateList != null && selectDateList.size() != 0) {
                    String text = getString(R.string.numday_selected, selectDateList.size());
                    tvSelectNumDay.setText(text);
                    dateList = new ArrayList<>();
                    for (Date date : selectDateList) {
                        dateList.add(Validate.DateToStringUpload(date));
                    }
                }
            }
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Common.RequestCode.CHOOSE_IMAGE:
                    checkImageSize(data, "");
                    break;
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == CHOOSE_IMAGE) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Common.RequestCode.CHOOSE_IMAGE);
            }
        }
    }

    private void showPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                List<String> permissions = new ArrayList<String>();
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                requestPermissions(permissions.toArray(new String[permissions.size()]), CHOOSE_IMAGE);
            } else {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, CHOOSE_IMAGE);
            }
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, CHOOSE_IMAGE);
        }
    }


    private void checkImageSize(Intent intent, String path) {
        try {
            if (intent != null) {
                filePath = imagePick(intent);
            }
            Uri file = Uri.fromFile(new File(filePath));
            if (getDropboxIMGSize(file)) {
                if (!path.isEmpty()) {
                    tempPath = path;
                } else {
                    tempPath = filePath;
                }
                imvBanner.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imvBanner.setImageBitmap(BitmapFactory.decodeFile(filePath));
            } else {
                Toast.makeText(this, R.string.image_size, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("caca", "" + e.getMessage());
        }
    }

    private boolean getDropboxIMGSize(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (imageHeight == 100 && imageWidth == 640) {
            return true;
        }
        return false;
    }

    private String imagePick(Intent intent) {
        long seconds = System.currentTimeMillis();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            fileTemp = new File(Environment.getExternalStorageDirectory(), "" + seconds + TEMP_PHOTO_FILE_NAME);

        } else {

            fileTemp = new File(getFilesDir(), "" + seconds + TEMP_PHOTO_FILE_NAME);
        }

        ContentResolver resolver = getContentResolver();
        try {
            InputStream inputStream = resolver.openInputStream(intent.getData());
            FileOutputStream fos = new FileOutputStream(fileTemp);
            copyStream(inputStream, fos);
            inputStream.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            return null;
        }
        return fileTemp.getPath();
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private class UploadPhoto extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            dialogProgress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                String temppath = "/storage/emulated/0/temp_photo.jpg";//filePath
                HttpClient client = getClient();
                String urlpost = Common.RequestURL.API_UPLOAD_FILE + "/?token=" + sessionUser.getUserDetails().getToken();
                Log.e("caca", "urlpost:" + urlpost);
                HttpPost httppost = new HttpPost(urlpost);
                FileBody filebodyImage = new FileBody(new File(params[0]));
                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart(Common.JsonKey.KEY_POST_UPDATED_FILE, filebodyImage);
                httppost.setEntity(reqEntity);
                HttpResponse response = client.execute(httppost);
                // int statusCode = response.getStatusLine().getStatusCode();
                result = EntityUtils.toString(response.getEntity());
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (getBaseContext() == null)
                return;
            if (result != null) {
                // prbAvatar.setVisibility(View.GONE);
                try {
                    Log.e("caca", "upimage: " + result);

                    JSONObject jsonResult = new JSONObject(result);
                    String checkstate = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (checkstate.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        JSONObject jsonData = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA);
                        imageURL = jsonData.getString(KEY_POST_URL);
                        dialogProgress.dismiss();
                        PostAdsServer postAdsServer = new PostAdsServer();
                        postAdsServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(position), gson.toJson(dateList), edUrl.getText().toString(), imageURL);
                    }
                } catch (Exception e) {
                    dialogProgress.dismiss();
                    Log.e("ERROR", e.getMessage());
                }
            }
        }
    }

//TODO: End Pick Image

    private class PostAdsServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            dialogProgress.show();
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POSITION, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_DATES, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_URL, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_COVER, urls[4]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_ADS_BANNER, "POST", params);

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
                    Log.e("Create Notification Ads", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        int id = jsonResult.getJSONObject("data").getInt("id");
                        double amount = jsonResult.getJSONObject("data").getDouble("amount");
                        Intent intent = new Intent(QuangCaoBannerTuyenDungActivity.this, ThanhToanAdsTuyenDungActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("amount", amount);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

    /* todo :  Adapter Loai Hinh */
    class PositionAdapter extends ArrayAdapter<PositionBanner> {

        public PositionAdapter(Context context, List<PositionBanner> objects) {
            super(context, R.layout.item_loaihinh, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return initView(position, convertView);
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return initView(position, convertView);
        }

        private View initView(int position, View convertView) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_loaihinh, null);
            }

            final PositionBanner item = getItem(position);
            TextView tvNameLoaihinh = (TextView) convertView.findViewById(R.id.tv_name_loaihinh);
            tvNameLoaihinh.setText(item.getName());

            return convertView;
        }
    }
}
