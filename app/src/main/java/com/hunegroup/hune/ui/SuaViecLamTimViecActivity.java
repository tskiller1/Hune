package com.hunegroup.hune.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.CategoryDTO;
import com.hunegroup.hune.dto.PostResultDTO;
import com.hunegroup.hune.dto.Url;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.gps.GPSTracker;
import com.hunegroup.hune.libCrop.CropImage;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Validate;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Environment.getExternalStorageDirectory;
import static com.hunegroup.hune.util.Common.JsonKey.KEY_POST_CATEGORY_ID;
import static com.hunegroup.hune.util.Common.JsonKey.KEY_POST_SALARY_TYPE;
import static com.hunegroup.hune.util.Common.RequestCode.CAMERA;
import static com.hunegroup.hune.util.Common.RequestCode.CHOOSE_IMAGE;
import static com.hunegroup.hune.util.Utilities.getClient;
import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 06/07/2017.
 */

public class SuaViecLamTimViecActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgNotify;
    private CircleImageView imgAvatar;
    private RelativeLayout rlUploadAnh;
    private ImageView imgUpload;
    private ImageView imgLeft;
    private ImageView imgRight;
    private CheckBox ckbItem;
    private Spinner spnLoaiCongViec;
    private EditText edtTieuDe;
    private Spinner spnChonBanDo;
    private EditText edtMoTa;
    private ImageView imgHinh1;
    private ImageView imgHinh2;
    private ImageView imgHinh3;
    private LinearLayout ll_list_filter;
    private RelativeLayout rlUpload3Hinh;
    private Button btnDangTin;
    private ProgressBar progressBar;
    private FrameLayout frame_progressbar;

    List<Url> urls;
    int temp;
    List<CategoryDTO> categoryDTOList;
    JSONParser jsonParser = new JSONParser();
    PostResultDTO postResultDTO;
    SessionUser sessionUser;
    String post_id;
    int i = 0;
    String url;
    int category_parent_id;
    int category_id;
    private String originImagePath;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, Common.RequestCode.CHOOSE_IMAGE);
                    return;
                } else return;
            }
        } else {
            if (requestCode == 101) {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    if (perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        showCamera();
                        return;
                    } else return;
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            originImagePath = savedInstanceState.getString("path");
        }
        switchLangauge(this, true);
        setContentView(R.layout.activity_suavieclam_timviec);
        sessionUser = new SessionUser(getBaseContext());
        urls = new ArrayList<>();
        findViews();
        Intent intent = getIntent();
        post_id = String.valueOf(intent.getIntExtra(Common.JsonKey.KEY_POST_ID, 0));
        GetPostServer getPostServer = new GetPostServer();
        getPostServer.execute(post_id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("path", originImagePath);
        Log.e("caca", originImagePath + "");
        super.onSaveInstanceState(outState);
    }

    private void findViews() {
        frame_progressbar = (FrameLayout) findViewById(R.id.frame_progressBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ll_list_filter = (LinearLayout) findViewById(R.id.ll_list_filter);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgNotify = (ImageView) findViewById(R.id.imgNotify);
        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        rlUploadAnh = (RelativeLayout) findViewById(R.id.rl_uploadAnh);
        imgUpload = (ImageView) findViewById(R.id.imgUpload);
        imgLeft = (ImageView) findViewById(R.id.imgLeft);
        imgRight = (ImageView) findViewById(R.id.imgRight);
        ckbItem = (CheckBox) findViewById(R.id.ckbItem);
        spnLoaiCongViec = (Spinner) findViewById(R.id.spnLoaiCongViec);
        edtTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        edtMoTa = (EditText) findViewById(R.id.edtMoTa);
        imgHinh1 = (ImageView) findViewById(R.id.imgHinh1);
        imgHinh2 = (ImageView) findViewById(R.id.imgHinh2);
        imgHinh3 = (ImageView) findViewById(R.id.imgHinh3);
        rlUpload3Hinh = (RelativeLayout) findViewById(R.id.rl_upload3Hinh);
        btnDangTin = (Button) findViewById(R.id.btnDangTin);

        imgAvatar.setTag(0);
        imgHinh1.setTag(0);
        imgHinh2.setTag(0);
        imgHinh3.setTag(0);

        btnDangTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTieuDe.getText().toString();
                String description = edtMoTa.getText().toString();
                String thumbnail = null;
                String image1 = null;
                String image2 = null;
                String image3 = null;
                for (int i = 0; i < urls.size(); i++) {
                    switch (urls.get(i).getId()) {
                        case 0:
                            thumbnail = urls.get(i).getUrl();
                            break;
                        case 1:
                            image1 = urls.get(i).getUrl();
                            break;
                        case 2:
                            image2 = urls.get(i).getUrl();
                            break;
                        case 3:
                            image3 = urls.get(i).getUrl();
                            break;
                    }
                }
                GPSTracker gpsTracker = new GPSTracker(getBaseContext());
                if (gpsTracker.canGetLocation()) {
//                    Log.d("URL FINAL", urls.toString() + " " + category_parent_id + " " + category_id + " " + thumbnail + " " + image1 + " " + image2 + " " + image3 + " " + gpsTracker.getLatitude() + " " + gpsTracker.getLongitude());
                    if (!Validate.isEmpty(title) || !Validate.isEmpty(description)) {
                        if (!imgAvatar.getTag().equals(1)) {
                            thumbnail = null;
                        }
                        if (!imgHinh1.getTag().equals(1)) {
                            image1 = null;
                        }
                        if (!imgHinh2.getTag().equals(1)) {
                            image2 = null;
                        }
                        if (!imgHinh3.getTag().equals(1)) {
                            image3 = null;
                        }
                        PostServer postServer = new PostServer();
                        postServer.execute(sessionUser.getUserDetails().getToken(),
                                post_id,
                                category_parent_id + "",
                                category_id + "",
                                title,
                                gpsTracker.getLatitude() + "",
                                gpsTracker.getLongitude() + "",
                                description,
                                "2",
                                thumbnail,
                                image1,
                                image2,
                                image3);
                    } else {
                        Toast.makeText(SuaViecLamTimViecActivity.this, getString(R.string.toast_suathongtin_vuilongnhapdayduthongtin), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getBaseContext(), getString(R.string.toast_vuilongbatvitridedangtintimviec), Toast.LENGTH_SHORT).show();
            }
        });

        rlUploadAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                showCameraAndPhoto();
            }
        });

        rlUpload3Hinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        imgHinh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1;
                showCameraAndPhoto();
            }
        });
        imgHinh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 2;
                showCameraAndPhoto();
            }
        });
        imgHinh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 3;
                showCameraAndPhoto();
            }
        });
        spnLoaiCongViec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category_id = (int) l;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuaViecLamTimViecActivity.this, ThongBaoTimViecActivity.class));
            }
        });
    }

    /* todo 8/8/17: Ca up image */
    private String filePath = null;
    private File fileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    String tempPath = null;

    private void showCameraAndPhoto() {
        try {

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.select_image));
            builder.setItems(R.array.myphoto,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // camera
                            if (which == 0) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                            || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                        List<String> permissions = new ArrayList<String>();
                                        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                        permissions.add(Manifest.permission.CAMERA);
                                        requestPermissions(permissions.toArray(new String[permissions.size()]), CAMERA);
                                    } else {
                                        showCamera();
                                        return;
                                    }
                                } else {
                                    showCamera();
                                    return;
                                }
                            }
                            // choose photo
                            if (which == 1) {
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
                        }
                    });
            android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        } catch (Exception e) {

        }

    }

    private Uri outputFileUri;

    private void showCamera() {

        Intent intentAmbil = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intentAmbil, Common.RequestCode.CAMERA);
        /*todo : */
        File output = createNewFile();
        originImagePath = output.getAbsolutePath();

        outputFileUri = FileProvider.getUriForFile(this, "com.hunegroup.hune" + ".provider", output);
        intentAmbil.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intentAmbil, Common.RequestCode.CAMERA);
    }

    private File createNewFile() {

        String temp = Environment.getExternalStorageDirectory().getPath();
        File dir = new File(temp);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//		LogA.e("create new file", file.getAbsolutePath());
        return file;
    }

    private void startCropImage(Intent intent, String path) {
        try {
            if (intent != null) {
                filePath = imagePick(intent);
            }
            int aspectX, aspectY;
            aspectX = 2;
            aspectY = 2;
            Intent intentCropImage = new Intent(this, CropImage.class);
            if (!path.isEmpty()) {
                intentCropImage.putExtra(CropImage.IMAGE_PATH, path);
            } else {
                intentCropImage.putExtra(CropImage.IMAGE_PATH, filePath);
            }

            intentCropImage.putExtra(CropImage.SCALE, true);
            intentCropImage.putExtra(CropImage.ASPECT_X, aspectX);
            intentCropImage.putExtra(CropImage.ASPECT_Y, aspectY);
            // startActivity(intentCropImage);
            intentCropImage.putExtra("i", i);
            startActivityForResult(intentCropImage, Common.RequestCode.CROP_IMAGE);
        } catch (Exception e) {
            Log.e("caca", "" + e.getMessage());
        }


    }

    private String imagePick(Intent intent) {

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            fileTemp = new File(getExternalStorageDirectory(), "" + seconds + TEMP_PHOTO_FILE_NAME);
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

        }

        return fileTemp.getPath();
    }


    /*---------------*/
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Common.RequestCode.CAMERA:
                    Log.e("caca", "000" + originImagePath);
                    File f = new File(originImagePath);
                    if (f.exists() && f.length() > 0) {
                        String path = originImagePath;
                        startCropImage(data, path);
                    }
                    //startCropImage(data);
                    break;
                case Common.RequestCode.CHOOSE_IMAGE:
                    startCropImage(data, "");
                    break;

                case Common.RequestCode.CROP_IMAGE:
                    String path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path == null) {
                        return;
                    }
                    tempPath = path;
                    temp = data.getIntExtra("i", -1);
                    switch (temp) {
                        case 0:
                            imgAvatar.setTag(1);
                            imgAvatar.setImageBitmap(BitmapFactory.decodeFile(path));
                            break;
                        case 1:
                            imgHinh1.setTag(1);
                            imgHinh1.setImageBitmap(BitmapFactory.decodeFile(path));
                            break;
                        case 2:
                            imgHinh2.setTag(1);
                            imgHinh2.setImageBitmap(BitmapFactory.decodeFile(path));
                            break;
                        case 3:
                            imgHinh3.setTag(1);
                            imgHinh3.setImageBitmap(BitmapFactory.decodeFile(path));
                            break;
                    }

                    UploadPhoto uploadPhoto = new UploadPhoto();
                    uploadPhoto.execute(path);
                    break;
            }
        }
        if (requestCode == Common.RequestCode.CAMERA && resultCode == RESULT_OK) {

            if ("OPPO".contentEquals(getDeviceName()) || "oppo".contentEquals(getDeviceName())) {
                try {
                 /**/
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    Log.e("caca", "" + finalFile.getAbsolutePath());
                    imgAvatar.setImageBitmap(imageBitmap);
                    int aspectX, aspectY;
                    aspectX = 2;
                    aspectY = 2;
                    Intent intentCropImage = new Intent(this, CropImage.class);
                    intentCropImage.putExtra(CropImage.IMAGE_PATH, finalFile.getAbsolutePath());
                    intentCropImage.putExtra(CropImage.SCALE, true);
                    intentCropImage.putExtra(CropImage.ASPECT_X, aspectX);
                    intentCropImage.putExtra(CropImage.ASPECT_Y, aspectY);
                    // startActivity(intentCropImage);
                    startActivityForResult(intentCropImage, Common.RequestCode.CROP_IMAGE);

                } catch (Exception e) {

                }
                //UploadPhoto uploadPhoto = new UploadPhoto();
                // uploadPhoto.execute(finalFile.getAbsolutePath());
            }


        }


    }

    public String getDeviceName() {
        //OPPO
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer);
            //return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
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
            progressBar.setVisibility(View.VISIBLE);
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
            if (result != null) {
                // prbAvatar.setVisibility(View.GONE);
                try {
                    Log.e("caca", "upimage: " + result.toString());

                    JSONObject jsonResult = new JSONObject(result);
                    String checkstate = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (checkstate.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        JSONObject jsonData = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA);
                        Url url = new Url();
                        url.setId(temp);
                        url.setUrl(jsonData.getString(Common.JsonKey.KEY_POST_URL));
                        urls.add(url);
                    }
                } catch (Exception e) {

                }
            }
            progressBar.setVisibility(View.GONE);
        }
    }
    /* todo 8/8/17: end Ca up image */

    private class CategoryServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                if (Build.VERSION.SDK_INT < 11) {
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
                        final JSONArray jsonArray = jsonResult.getJSONArray(Common.JsonKey.KEY_DATA);
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
                            View subview = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_loaihinh_locthongtin_timviec, null);
                            TextView tv_name_loaihinh = (TextView) subview.findViewById(R.id.txtName);
                            ImageView imgIcon = (ImageView) subview.findViewById(R.id.imgIcon);
                            LinearLayout llParent = (LinearLayout) subview.findViewById(R.id.llParent);
                            tv_name_loaihinh.setText(categoryDTO.getName());
                            Picasso.with(getBaseContext()).load(categoryDTO.getIcon()).into(imgIcon);
                            final CheckBox ckbFilter = (CheckBox) subview.findViewById(R.id.ckbItem);
                            if (categoryDTO.getId() == category_parent_id) {
                                ckbFilter.setChecked(true);
                                if (categoryDTO.getChild().size() != 0) {
                                    spnLoaiCongViec.setAdapter(new LoaiHinhAdapter(getBaseContext(), categoryDTO.getChild()));
                                    for (int j = 0; j < categoryDTO.getChild().size(); j++) {
                                        if (category_id == categoryDTO.getChild().get(j).getId())
                                            spnLoaiCongViec.setSelection(j);
                                    }
                                }
                            } else ckbFilter.setChecked(false);
                            categoryDTO.setCheckBox(ckbFilter);
                            categoryDTOList.add(categoryDTO);
                            ll_list_filter.addView(subview);
                        }
                        for (int i = 0; i < categoryDTOList.size(); i++) {
                            final int k = i;
                            categoryDTOList.get(i).getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b) {
                                        category_parent_id = categoryDTOList.get(k).getId();
                                        for (int j = 0; j < categoryDTOList.size(); j++) {
                                            if (j != k) {
                                                categoryDTOList.get(j).getCheckBox().setChecked(false);
                                            }
                                        }
                                        spnLoaiCongViec.setAdapter(new LoaiHinhAdapter(getBaseContext(), categoryDTOList.get(k).getChild()));
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(SuaViecLamTimViecActivity.this, "Hiện tại chưa có danh sách loại hình", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
            super.onPostExecute(result);
        }
    }

    private class PostServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            frame_progressbar.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                if (Build.VERSION.SDK_INT < 11) {
                    Thread.sleep(1000);
                }
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_CATEGORY_ID, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TITLE, urls[4]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LATITUDE, urls[5]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LONGITUDE, urls[6]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_DESCRIPTION, urls[7]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TYPE, urls[8]));
                if (urls[9] != null) {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_THUMBNAIL, urls[9]));
                }
                if (urls[10] != null) {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_IMAGE1, urls[10]));
                }
                if (urls[11] != null) {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_IMAGE2, urls[11]));
                }
                if (urls[12] != null) {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_IMAGE3, urls[12]));
                }
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_PUT_POST + urls[1], "PUT", params);

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
                        Toast.makeText(SuaViecLamTimViecActivity.this, getString(R.string.toast_suavieclamthanhcong), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            frame_progressbar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }

    private class GetPostServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            frame_progressbar.setVisibility(View.VISIBLE);
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
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST + "/" + urls[0], "GET", params);

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
                Log.e("RESULT " + "POST", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        postResultDTO = new PostResultDTO();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONObject jsonData = jsonResult.getJSONObject(Common.JsonKey.KEY_DATA);
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_ID))
                                postResultDTO.setId(jsonData.getInt(Common.JsonKey.KEY_POST_ID));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_USER_ID))
                                postResultDTO.setUser_id(jsonData.getInt(Common.JsonKey.KEY_POST_USER_ID));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID))
                                postResultDTO.setCategory_parent_id(jsonData.getInt(Common.JsonKey.KEY_POST_CATEGORY_PARENT_ID));
                            if (!jsonData.isNull(KEY_POST_CATEGORY_ID))
                                postResultDTO.setCategory_id(jsonData.getInt(KEY_POST_CATEGORY_ID));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_TITLE))
                                postResultDTO.setTitle(jsonData.getString(Common.JsonKey.KEY_POST_TITLE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_RATING))
                                postResultDTO.setStar(jsonData.getDouble(Common.JsonKey.KEY_POST_RATING));
                            else postResultDTO.setStar(0);
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_QUANTITY))
                                postResultDTO.setQuantity(jsonData.getInt(Common.JsonKey.KEY_POST_QUANTITY));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_ADDRESS))
                                postResultDTO.setAddress(jsonData.getString(Common.JsonKey.KEY_POST_ADDRESS));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_LATITUDE))
                                postResultDTO.setLatitude((float) jsonData.getDouble(Common.JsonKey.KEY_POST_LATITUDE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_LONGITUDE))
                                postResultDTO.setLongitude((float) jsonData.getDouble(Common.JsonKey.KEY_POST_LONGITUDE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_SALARY))
                                postResultDTO.setSalary(Double.parseDouble(jsonData.getString(Common.JsonKey.KEY_POST_SALARY)));
                            if (!jsonData.isNull(KEY_POST_SALARY_TYPE))
                                postResultDTO.setSalary_type(jsonData.getInt(KEY_POST_SALARY_TYPE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_START_DATE))
                                postResultDTO.setDate_start(Validate.StringDateFormatToSetText(jsonData.getString(Common.JsonKey.KEY_POST_START_DATE)));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_END_DATE))
                                postResultDTO.setDate_end(Validate.StringDateFormatToSetText(jsonData.getString(Common.JsonKey.KEY_POST_END_DATE)));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_DESCRIPTION))
                                postResultDTO.setDescription(jsonData.getString(Common.JsonKey.KEY_POST_DESCRIPTION));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_TYPE))
                                postResultDTO.setType(jsonData.getInt(Common.JsonKey.KEY_POST_TYPE));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_SEX))
                                if (jsonData.getInt(Common.JsonKey.KEY_POST_SEX) == 1)
                                    postResultDTO.setSex(getString(R.string.nu));
                                else postResultDTO.setSex(getString(R.string.nam));
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_USER)) {
                                UserDTO userDTO = new UserDTO();
                                JSONObject jsonUser = jsonData.getJSONObject(Common.JsonKey.KEY_POST_USER);
                                userDTO.setId(jsonUser.getInt(Common.JsonKey.KEY_USER_ID));
                                userDTO.setPhone(jsonUser.getString(Common.JsonKey.KEY_USER_PHONE));
                                userDTO.setFull_name(jsonUser.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                                postResultDTO.setUser(userDTO);
                            }
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_CATEGORY)) {
                                CategoryDTO categoryDTO = new CategoryDTO();
                                JSONObject jsonCate = jsonData.getJSONObject(Common.JsonKey.KEY_POST_CATEGORY);
                                categoryDTO.setId(jsonCate.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                categoryDTO.setName(jsonCate.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                postResultDTO.setCategory(categoryDTO);
                            }
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_PARENT_CATEGORY)) {
                                CategoryDTO categoryDTO = new CategoryDTO();
                                JSONObject jsonCateParent = jsonData.getJSONObject(Common.JsonKey.KEY_POST_PARENT_CATEGORY);
                                categoryDTO.setId(jsonCateParent.getInt(Common.JsonKey.KEY_CATEGORY_ID));
                                categoryDTO.setName(jsonCateParent.getString(Common.JsonKey.KEY_CATEGORY_NAME));
                                postResultDTO.setCategory_parent(categoryDTO);
                            }
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_IMAGES)) {
                                List<String> images = new ArrayList<>();
                                JSONArray jsonArray = jsonData.getJSONArray(Common.JsonKey.KEY_POST_IMAGES);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String img = jsonArray.getString(i);
                                    images.add(img);
                                }
                                postResultDTO.setImages(images);
                            }
                            if (!jsonData.isNull(Common.JsonKey.KEY_POST_THUMBNAIL)) {
                                String thumbnail = jsonData.getString(Common.JsonKey.KEY_POST_THUMBNAIL);
                                postResultDTO.setThumbnail(thumbnail);
                            }
                        }
                        edtTieuDe.setText(postResultDTO.getTitle());
                        edtMoTa.setText(postResultDTO.getDescription());
                        category_parent_id = postResultDTO.getCategory_parent_id();
                        category_id = postResultDTO.getCategory_id();
                        CategoryServer categoryServer = new CategoryServer();
                        categoryServer.execute("0");
                        Picasso.with(getBaseContext()).load(postResultDTO.getThumbnail()).into(imgAvatar);
                        String thumbnail = postResultDTO.getThumbnail().substring(42, postResultDTO.getThumbnail().length());
                        String image1 = null;
                        String image2 = null;
                        String image3 = null;
                        if (postResultDTO.getImages().size() != 0) {
                            if (postResultDTO.getImages().size() == 1) {
                                image1 = postResultDTO.getImages().get(0).substring(42, postResultDTO.getImages().get(0).length());
                                Picasso.with(getBaseContext()).load(postResultDTO.getImages().get(0)).into(imgHinh1);
                            }
                            if (postResultDTO.getImages().size() == 2) {
                                image1 = postResultDTO.getImages().get(0).substring(42, postResultDTO.getImages().get(0).length());
                                image2 = postResultDTO.getImages().get(1).substring(42, postResultDTO.getImages().get(1).length());
                                Picasso.with(getBaseContext()).load(postResultDTO.getImages().get(0)).into(imgHinh1);
                                Picasso.with(getBaseContext()).load(postResultDTO.getImages().get(1)).into(imgHinh2);
                            }
                            if (postResultDTO.getImages().size() == 3) {
                                image1 = postResultDTO.getImages().get(0).substring(42, postResultDTO.getImages().get(0).length());
                                image2 = postResultDTO.getImages().get(1).substring(42, postResultDTO.getImages().get(1).length());
                                image3 = postResultDTO.getImages().get(2).substring(42, postResultDTO.getImages().get(2).length());
                                Picasso.with(getBaseContext()).load(postResultDTO.getImages().get(0)).into(imgHinh1);
                                Picasso.with(getBaseContext()).load(postResultDTO.getImages().get(1)).into(imgHinh2);
                                Picasso.with(getBaseContext()).load(postResultDTO.getImages().get(2)).into(imgHinh3);
                            }
                            Url urlThumb = new Url();
                            urlThumb.setId(0);
                            urlThumb.setUrl(thumbnail);
                            Url urlImg1 = new Url();
                            urlImg1.setId(1);
                            urlImg1.setUrl(image1);
                            Url urlImg2 = new Url();
                            urlImg2.setId(2);
                            urlImg2.setUrl(image2);
                            Url urlImg3 = new Url();
                            urlImg3.setId(3);
                            urlImg3.setUrl(image3);
                            urls.add(urlThumb);
                            urls.add(urlImg1);
                            urls.add(urlImg2);
                            urls.add(urlImg3);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            frame_progressbar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }

    private class LoaiHinhAdapter extends ArrayAdapter<CategoryDTO> {

        public LoaiHinhAdapter(Context context, List<CategoryDTO> objects) {
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

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        private View initView(int position, View convertView) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_loaihinh, null);
            }

            final CategoryDTO item = getItem(position);
            TextView tvNameLoaihinh = (TextView) convertView.findViewById(R.id.tv_name_loaihinh);
            tvNameLoaihinh.setText(item.getName());

            return convertView;
        }
    }

}
