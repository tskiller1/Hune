package com.hunegroup.hune.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hunegroup.hune.dto.UserDTO;

public class SessionUser {

    private final String TAG = SessionUser.class.getName();

    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "UserHune";

    private static final String KEY_ID_USER = "id";
    private static final String KEY_FULLNAME = "full_name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_BIRTHDAY ="birthday";
    private static final String KEY_SEX ="sex";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_STATUS = "status";
    private static final String KEY_LANGUAGE = "language";
    // Constructor
    public SessionUser(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserSession(UserDTO userDTO) {
        if (userDTO == null) {
            //Log.e(TAG + " createLoginSession cuahang = null");
            return;
        }
        editor.putInt(KEY_ID_USER, userDTO.getId());
        editor.putInt(KEY_STATUS,userDTO.getStatus());
        editor.putString(KEY_FULLNAME,userDTO.getFull_name());
        editor.putString(KEY_PHONE, userDTO.getPhone());
        editor.putString(KEY_TOKEN, userDTO.getToken());
        editor.putString(KEY_BIRTHDAY,userDTO.getBirthday());
        editor.putString(KEY_SEX,userDTO.getSex());
        editor.commit();
    }

    //Get stored session data
    public UserDTO getUserDetails() {

        UserDTO userDTO = new UserDTO();

        userDTO.setId(pref.getInt(KEY_ID_USER, 0));

        userDTO.setFull_name(pref.getString(KEY_FULLNAME, null));
        userDTO.setPhone(pref.getString(KEY_PHONE, null));
        userDTO.setToken(pref.getString(KEY_TOKEN, null));
        userDTO.setBirthday(pref.getString(KEY_BIRTHDAY,null));
        userDTO.setSex(pref.getString(KEY_SEX,null));
        userDTO.setStatus(pref.getInt(KEY_STATUS,1));
        return userDTO;
    }


    public void setLanguage(int language){
        editor.putInt(KEY_LANGUAGE,language);
        editor.commit();
    }

    public int getLanguage(){
        return pref.getInt(KEY_LANGUAGE,-1);
    }
    public boolean isNotifacation(){
        return pref.getBoolean(KEY_NOTIFICATION,true);
    }
    public void setNotification(boolean isNotification){
        editor.putBoolean(KEY_NOTIFICATION,isNotification);
        editor.commit();
    }
    // Clear session details
    public void clearData() {

        editor.clear();
        editor.commit();

    }


}