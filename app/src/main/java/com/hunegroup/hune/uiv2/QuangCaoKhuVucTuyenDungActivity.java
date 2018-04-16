package com.hunegroup.hune.uiv2;

import android.Manifest;
import android.app.Dialog;
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
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.PostAdsNotification;
import com.hunegroup.hune.libCrop.CropImage;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.hunegroup.hune.util.Common.RequestCode.CAMERA;
import static com.hunegroup.hune.util.Common.RequestCode.CHOOSE_IMAGE;
import static com.hunegroup.hune.util.Utilities.getClient;

/**
 * Created by apple on 12/13/17.
 */

public class QuangCaoKhuVucTuyenDungActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView imvBack;
    private ImageView imvLogo;
    private RelativeLayout rlUploadLogo;
    private ImageView imvUploadLogo;
    private EditText edTitle;
    private EditText edDescription;
    private EditText edAddress;
    private Button btnContinue;

    private Dialog dialogProgress;

    //TODO:Declaring
    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;
    Gson gson = new Gson();

    PostAdsNotification request;

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
                    imvLogo.setImageBitmap(BitmapFactory.decodeFile(path));
                    /*UploadPhoto uploadPhoto = new UploadPhoto();
                    uploadPhoto.execute(path);*/
                    break;
            }
//            if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
//                AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
//                String toastMessage;
//                if (loginResult.getError() != null) {
//                    toastMessage = loginResult.getError().getErrorType().getMessage();
//                } else if (loginResult.wasCancelled()) {
//                    toastMessage = "Login Cancelled";
//                } else {
//                    if (loginResult.getAccessToken() != null) {
//                        toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
//                    } else {
//                        toastMessage = loginResult.getAuthorizationCode();
//                        Log.e("TOKEN", sessionUser.getUserDetails().getToken());
//                        Log.e("ACK_TOKEN", toastMessage);
//                        ThemViecLamTimViecActivity.VerifyServer verifyServer = new ThemViecLamTimViecActivity.VerifyServer();
//                        verifyServer.execute(sessionUser.getUserDetails().getToken(), loginResult.getAuthorizationCode());
//                    }
//                    // If you have an authorization code, retrieve it from
//                    // loginResult.getAuthorizationCode()
//                    // and pass it to your server and exchange it for an access token.
//
//                    // Success! Start your next activity...
//                }
//
//                // Surface the result to your user in an appropriate way.
//            }
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
                    imvLogo.setImageBitmap(imageBitmap);
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
//                UploadPhoto uploadPhoto = new UploadPhoto();
//                 uploadPhoto.execute(finalFile.getAbsolutePath());
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
            if (requestCode == CAMERA) {
                showCamera();
            }
        }
    }

    private void showCameraAndPhoto() {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        } catch (Exception e) {

        }

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
//		LogA.e("create new mFile", mFile.getAbsolutePath());
        return file;
    }

    private void showCamera() {

        Intent intentAmbil = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intentAmbil, Common.RequestCode.CAMERA);
        /*todo : */
        File output = createNewFile();
        originImagePath = output.getAbsolutePath();

        outputFileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", output);
        intentAmbil.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intentAmbil, CAMERA);
    }

    private void startCropImage(Intent intent, String path) {
        try {
            if (intent != null) {
                filePath = imagePick(intent);
            }
            int aspectX, aspectY;
            aspectX = 3;
            aspectY = 3;
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

    //TODO: End Pick Image

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_quangcaokhuvuc_tuyendung);
        findViews();
        initData();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvLogo = (ImageView) findViewById(R.id.imv_logo);
        rlUploadLogo = (RelativeLayout) findViewById(R.id.rl_upload_logo);
        imvUploadLogo = (ImageView) findViewById(R.id.imv_upload_logo);
        edTitle = (EditText) findViewById(R.id.ed_title);
        edDescription = (EditText) findViewById(R.id.ed_description);
        edAddress = (EditText) findViewById(R.id.ed_address);
        btnContinue = (Button) findViewById(R.id.btn_continue);

        imvBack.setOnClickListener(this);
        rlUploadLogo.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        request = new PostAdsNotification();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.rl_upload_logo:
                showCameraAndPhoto();
                break;
            case R.id.btn_continue:
                if (!TextUtils.isEmpty(edTitle.getText())
                        && !TextUtils.isEmpty(edDescription.getText())
                        && !TextUtils.isEmpty(edAddress.getText())
                        && !TextUtils.isEmpty(tempPath)) {
                    request.setTitle(edTitle.getText().toString());
                    request.setDescription(edDescription.getText().toString());
                    List<String> address = new ArrayList<>();
                    address.add(edAddress.getText().toString());
                    request.setBranch(address);
                    UploadPhoto uploadPhoto = new UploadPhoto();
                    uploadPhoto.execute(tempPath);
                } else {
                    Toast.makeText(this, getString(R.string.toast_vuilongnhapthongtin), Toast.LENGTH_SHORT).show();
                }
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
                        String imageURL = jsonData.getString(Common.JsonKey.KEY_URL);
                        request.setLogo(imageURL);
                        Intent intent = new Intent(QuangCaoKhuVucTuyenDungActivity.this, QuangCaoKhuVucStep2TuyenDungActivity.class);
                        intent.putExtra("postnotification", gson.toJson(request));
                        startActivity(intent);
                    }
                } catch (Exception e) {

                }
            }
            dialogProgress.dismiss();
        }
    }

}
