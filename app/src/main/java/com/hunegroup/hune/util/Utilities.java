package com.hunegroup.hune.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hunegroup.hune.R;
import com.hunegroup.hune.ui.ChonChucNangActivity;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hunegroup.hune.util.Common.ADS_BANNER;

public class Utilities {
    private static HttpClient client = null;

    public static String dateStringFormatToString(String sDate, String currentFormat, String wannaFormat) {
        if (sDate == null || sDate.equals("")) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(currentFormat, Locale.US);
        try {
            Date date = sdf.parse(sDate);
            SimpleDateFormat sdfResult = new SimpleDateFormat(wannaFormat, Locale.US);
            return sdfResult.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String NubmerFormatText(String coin) {
        try {
            int a = Integer.parseInt(coin);
            NumberFormat formatter = NumberFormat.getInstance(Locale.JAPAN);
            String txt = formatter.format(a);
            txt = txt.replace(",", ".");
            return txt;
        } catch (Exception e) {
            return coin;
        }
    }

    @SuppressWarnings("deprecation")
    public static String parseDateToString(Date date) {
        if (date == null) {
            return "";
        }
        return date.getDate() + "/" + (date.getMonth() + 1) + "/"
                + (date.getYear() + 1900);
    }

    @SuppressWarnings("deprecation")
    public static String parseTimeToString(Date date) {
        if (date == null) {
            return "";
        }
        return date.getHours() + ":" + date.getMinutes();
    }

    public static String parseDateTimeToString(Date date) {
        if (date == null) {
            return "";
        }
        return Utilities.parseTimeToString(date) + " "
                + Utilities.parseDateToString(date);
    }

    public static Date parseStringToDate(String date) {
        if (date == null || date.equals("")) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            Log.e("Obuut", "ParseException: " + e.getMessage());
            e.printStackTrace();

        }
        return null;
    }

    public static String parseMilliSecondsToTimeString(int milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(new Date(milliSeconds));
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String collapseTagFriends(List<String> listWithFriend) {
        String text = "";
        if (listWithFriend.size() > 3) {
            text = String.format("%s, %s, %s and %d other people",
                    listWithFriend.get(0), listWithFriend.get(1),
                    listWithFriend.get(2), listWithFriend.size() - 3);
        } else {
            for (int i = 0; i < listWithFriend.size(); i++) {
                if (i == listWithFriend.size() - 1) {
                    text += listWithFriend.get(i);
                } else {
                    text += listWithFriend.get(i) + ", ";
                }
            }
        }
        return text;
    }

    public static String collapseComments(int numberComment) {
        return String.format("%d Comment%s", numberComment,
                numberComment < 2 ? "" : "s");
    }

    public static String collapseWows(int numberWow) {
        return String.format("%d Wow%s", numberWow, numberWow < 2 ? "" : "s");
    }

    public static String parseMtoKmWithString(float distance) {
        String value = " | ";
        if (distance >= 1000) {
            float unit = distance % 1000;
            unit = Math.round(unit / 100);
            unit = unit / 10;
            distance = Math.round(distance / 1000);
            value = value + Float.toString(distance + unit) + "km";
        } else {
            value = value + Float.toString(Math.round(distance)) + "m";
        }
        return value;
    }

    public static HttpClient getClient() {
        if (client == null) {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            client = new DefaultHttpClient(params);
        }
        return client;
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    // check Internet
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    //check install Package
    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    // function to verify if directory exists
    public static void checkAndCreateDirectory(String dirName, File rootDir) {
        File new_dir = new File(rootDir + dirName);
        if (!new_dir.exists()) {
            new_dir.mkdirs();
        }
    }

    public static JSONArray remove(JSONArray jsonArray, int index) {

        JSONArray output = new JSONArray();
        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            if (i != index) {
                try {
                    output.put(jsonArray.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return jsonArray;
                }
            }
        }
        return output;
    }

    public static String getNameDpiDevices(Context context) {
        int density = context.getResources().getDisplayMetrics().densityDpi;
        String nameDpi = "hdpi";
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                nameDpi = "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                nameDpi = "mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                nameDpi = "hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                nameDpi = "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                nameDpi = "xxhdpi";
        }
        return nameDpi;
    }

    public static String getNameScreenLayoutDevice(Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        String nameLayout = "LARGE SCREEN";
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                nameLayout = "LARGE SCREEN";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                nameLayout = "NORMAL SCREEN";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                nameLayout = "SMALL SCREEN";
                break;
            default:
                nameLayout = "EXTRA LAYOUT";
        }
        return nameLayout;
    }

    public static JSONObject createJSONOBjectFromParams(List<NameValuePair> params) {
        try {
            JSONObject json = new JSONObject();
            for (int i = 0; i < params.size(); i++) {
                NameValuePair param = params.get(i);
                json.put(param.getName(), param.getValue());
            }
            return json;
        } catch (Exception e) {

        }
        return null;
    }

    public static boolean isEmailValidNew(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void switchLangauge(Activity activity, boolean isFirst) {
        Resources res = activity.getResources();
        SessionUser sessionUser = new SessionUser(activity.getBaseContext());
        //int index = sessionUser.getLanguage();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        int index = preferences.getInt("key_language", -1);
        Configuration c = res.getConfiguration();
        if (index != -1) {
            switch (index) {
                case 0: // english
                    c.locale = Locale.ENGLISH;
                    break;
                case 1: // vietnamese
                    c.locale = new Locale("vi");
                    break;
                /*case 2: // japanese
                    c.locale = Locale.JAPANESE;
                    break;
                case 3: // thailand
                    c.locale = new Locale("th");
                    break;
                case 4: // Indonesia
                    c.locale = new Locale("in");
                    break;
                case 5: // Nga
                    c.locale = new Locale("ru");
                    break;
                //27-05-14
                case 6: // Arabic
                    c.locale = new Locale("ar");
                    break;
                case 7: // Bulgarian
                    c.locale = new Locale("bg");
                    break;
                case 8: // Catalan
                    c.locale = new Locale("ca");
                    break;
                case 9: // Czech
                    c.locale = new Locale("cs");
                    break;
                case 10: // Danish
                    c.locale = new Locale("da");
                    break;
                case 11: // German
                    c.locale = new Locale("de");
                    break;
                case 12: // Ukrainian
                    c.locale = new Locale("uk");
                    break;
                case 13: // Korean
                    c.locale = new Locale("ko");
                    break;*/
                default:
                    break;
            }
            res.updateConfiguration(c, res.getDisplayMetrics());
            if (!isFirst) {
                Intent intent = new Intent(activity, ChonChucNangActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }

    public static void showAdsBanner(Context context) {
        // --todo : ads google banner ---
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ll = layoutInflater.inflate(R.layout.ads_banner, null);
        RelativeLayout layout = (RelativeLayout) ll.findViewById(R.id.mainLayout);
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(ADS_BANNER);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
        // -- todo : ads google banner google---
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static long getDifferenceFromTodayToDays(Date d2) {
        Date d1 = Calendar.getInstance().getTime();
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
    //--------------------------------------------
}
