package com.hunegroup.hune.util;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by apple on 11/23/17.
 */

public class EncodeUtils {
    public static String EncodeToPayment(String secret, String text) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hash = Base64.encodeToString(sha256_HMAC.doFinal(text.getBytes()), Base64.DEFAULT);
            return hash;
        } catch (Exception e) {
            Log.e("hash", "ERROR");
        }
        return null;
    }
}
