package com.hunegroup.hune.util;

public class Common {

    /*SERVER URL*/
    //http://mmy.asquare.vn/api/v1
    //http://hune.flychicken.net/api/v1
    //http://hune.flychicken.net/api/v1 14/8/17
    public final static String SERVER_URL = "https://hunegroup.com/api/v1";
    public final static String ADS_BANNER = "ca-app-pub-9404749943049506/4282565720";
    public final static String PAYMENT_SECRET_KEY = "123";
    public final static int PAYMENT_ID = 500002150;
    public final static String PAYMENT_RECEIVER_ACCOUNT = "0964328327";

    public class RequestCode {

        public static final int CHOOSE_IMAGE = 100;
        public static final int CAMERA = 101;
        public static final int CROP_IMAGE = 102;
        public static final int EDIT_USER = 1000;
        public static final int DAT_HANG_USER = 2000;

    }

    public static class RequestURL {

        /* USER */
        public static final String API_USER_REGISTER = SERVER_URL + "/user/register";
        public static final String API_USER_CHANGE_PASSWORD = SERVER_URL + "/user/change-password";
        public static final String API_USER_COMFIRM_PASSWORD = SERVER_URL + "/user/confirm-reset-password";
        public static final String API_USER_RESET_PASSWORD = SERVER_URL + "/user/reset-password";
        public static final String API_USER_LOGIN_FACBOOK = SERVER_URL + "/user/login-facebook";
        public static final String API_USER_LOGIN = SERVER_URL + "/user/login-account";
        public static final String API_USER_VERIFY = SERVER_URL + "/user/verify";
        public static final String API_USER_PROFILE = SERVER_URL + "/user/profile";
        public static final String API_USER_FORGOT_PASSWORD = SERVER_URL + "/user/forget-password";
        /* VOTE */
        public static final String API_VOTE = SERVER_URL + "/vote";
        /* POST */
        public static final String API_POST = SERVER_URL + "/post";
        public static final String API_POST_MY_POST = SERVER_URL + "/post/my-post";
        public static final String API_POST_ID_POST = SERVER_URL + "/post/";
        public static final String API_POST_DELETE_POST = SERVER_URL + "/post/";
        public static final String API_POST_GET_POST = SERVER_URL + "/post/";
        public static final String API_POST_PUT_POST = SERVER_URL + "/post/";
        public static final String API_POST_RELATE = SERVER_URL + "/post/relate";
        public static final String API_POST_CONTACT = "/contact";
        public static final String API_POST_UPDATE_LOCATION = SERVER_URL + "/post/update-location";
        /* NOTIFICATION */
        public static final String API_NOTIFICATION = SERVER_URL + "/notification";
        public static final String API_NOTIFICATION_MARK_READ = SERVER_URL + "/notification/mark-read";
        public static final String API_NOTIFICATION_COUNTER = SERVER_URL + "/notification/counter";
        /* FAVOURITE */
        public static final String API_FAVOURITE = SERVER_URL + "/favourite";
        /*  CATEGORY */
        public static final String API_CATEGORY = SERVER_URL + "/category";

        /* UPLOAD_FILE */
        public static final String API_UPLOAD_FILE = SERVER_URL + "/file/upload";

        /*SHARE LINK*/
        public static final String API_SHARE_LINK = SERVER_URL + "/share";

        //TODO: Hune V2
        /* TASK */
        public static final String API_TASK = SERVER_URL + "/task";

        /* WALLET */
        public static final String API_WALLET_DEPOSIT_COIN = SERVER_URL + "/wallet/deposit/coin";
        public static final String API_WALLET_DEPOSIT_CASH = SERVER_URL + "/wallet/deposit/cash";
        public static final String API_WALLET_TRANSACTIONS = SERVER_URL + "/wallet/transactions";
        public static final String API_WALLET_WITHDRAW_CASH = SERVER_URL + "/wallet/withdraw/cash";


        public static final String API_COUPON = SERVER_URL + "/coupon";
        public static final String API_COUPON_GROUP = SERVER_URL + "/coupon/group/";
        public static final String API_MY_COUPON = SERVER_URL + "/coupon/my";

        /* ADS */
        public static final String API_ADS_PROMOTION = SERVER_URL + "/ads/promotion/";
        public static final String API_ADS = SERVER_URL + "/ads/";
        public static final String API_ADS_BANNER = SERVER_URL + "/ads/banner";
        public static final String API_ADS_LOCATION = SERVER_URL + "/ads/location";
        public static final String API_ADS_PAY = SERVER_URL + "/ads/pay";
    }

    public class Type {
        public static final String TYPE_SEARCH_JOB = "2";
        public static final String TYPE_ENROLLMENT = "1";
        public static final String TYPE_USER = "1";
        public static final String TYPE_POST = "2";
        public static final String TYPE_STATUS_ON = "1";
        public static final String TYPE_STATUS_OFF = "2";

        public static final int TYPE_STATUS_INT_ON = 1;
        public static final int TYPE_STATUS_INT_OFF = 2;
        public static final String TYPE_SHARE_POST = "post";
        public static final String TYPE_SHARE_APP = "app";

        //TODO: Hune V2
        public static final String TYPE_STATUS_STARTING = "starting";
        public static final String TYPE_STATUS_REJECT = "reject";
        public static final String TYPE_STATUS_COMPLETE = "completed";

        public static final String TYPE_MONEY_CASH = "1";
        public static final String TYPE_MONEY_COIN = "2";
    }

    public class JsonKey {

        /* LIMIT PAGE */
        public static final String KEY_LIMIT = "limit";
        public static final String KEY_PAGE = "page";

        /*USER */
        //TODO:Hune V2
        public static final String KEY_USER_TOTAL_CASH = "total_cash";
        public static final String KEY_USER_BALANCE_CASH = "balance_cash";
        public static final String KEY_USER_TOTAL_COIN = "total_coin";
        public static final String KEY_USER_BALANCE_COIN = "balance_coin";
        //
        public static final String KEY_USER_FACEBOOK_ID = "facebook_id";
        public static final String KEY_USER_FACEBOOK_TOKEN = "facebook_token";
        public static final String KEY_USER_FULL_NAME = "full_name";
        public static final String KEY_USER_PHONE = "phone";
        public static final String KEY_USER_PASS = "password";
        public static final String KEY_USER_NEW_PASS = "new_password";
        public static final String KEY_USER_RE_PASS = "re_password";
        public static final String KEY_USER_ID = "id";
        public static final String KEY_USER_TOKEN = "token";
        public static final String KEY_USER_VERIFY_CODE = "code";
        public static final String KEY_USER_SEX = "sex";
        public static final String KEY_USER_BIRTHDAY = "birthday";
        public static final String KEY_USER_ADDRESS = "address";
        public static final String KEY_USER_AVATAR = "avatar";
        public static final String KEY_USER_DESCRIPTION = "description";
        public static final String KEY_USER_STATUS = "status";
        public static final String KEY_USER_FAVOURITE_COUNT = "favourite_count";
        public static final String KEY_USER_RATING = "rating";
        public static final String KEY_USER_FCM_TOKEN = "fcm_token";
        public static final String KEY_USER_USER_ID = "user_id";
        public static final String KEY_USER_IS_FAVOURITE = "is_favourite";
        public static final String KEY_USER_ACK_TOKEN = "ack_token";
        /* CATEGORY */
        public static final String KEY_CATEGORY_PARENT_ID = "parent_id";
        public static final String KEY_CATEGORY_ID = "id";
        public static final String KEY_CATEGORY_NAME = "name";
        public static final String KEY_CATEGORY_ICON = "icon";
        public static final String KEY_CATEGORY_CHILD = "childs";
        public static final String KEY_CATEGORY_LANGUAGE = "Accept-Language";

        /* POST */
        public static final String KEY_POST_TOKEN = "token";
        public static final String KEY_POST_TYPE = "type";
        public static final String KEY_POST_LATITUDE = "latitude";
        public static final String KEY_POST_LONGITUDE = "longitude";
        public static final String KEY_POST_RADIUS = "radius";
        public static final String KEY_POST_CATEGORY = "category";
        public static final String KEY_POST_SEX = "sex";
        public static final String KEY_POST_SALARY = "salary";
        public static final String KEY_POST_SALARY_TYPE = "salary_type";
        public static final String KEY_POST_ID = "id";
        public static final String KEY_POST_TITLE = "title";
        public static final String KEY_POST_RATING = "rating";
        public static final String KEY_POST_DESCRIPTION = "description";
        public static final String KEY_POST_CATEGORY_ID = "category_id";
        public static final String KEY_POST_USER_ID = "user_id";
        public static final String KEY_POST_CATEGORY_PARENT_ID = "category_parent_id";
        public static final String KEY_POST_STATUS = "status";
        public static final String KEY_POST_USER = "user";
        public static final String KEY_POST_PARENT_CATEGORY = "parent_category";
        public static final String KEY_POST_DISTANCE = "distance";
        public static final String KEY_POST_QUANTITY = "quantity";
        public static final String KEY_POST_ADDRESS = "address";
        public static final String KEY_POST_START_DATE = "start_date";
        public static final String KEY_POST_END_DATE = "end_date";
        public static final String KEY_POST_PHONE = "phone";
        public static final String KEY_POST_FULL_NAME = "full_name";
        public static final String KEY_POST_AVATAR = "avatar";
        public static final String KEY_POST_IMAGES = "images";
        public static final String KEY_POST_ICON = "icon";
        public static final String KEY_POST_NAME = "name";
        public static final String KEY_POST_THUMBNAIL = "thumbnail";
        public static final String KEY_POST_IMAGE1 = "image1";
        public static final String KEY_POST_IMAGE2 = "image2";
        public static final String KEY_POST_IMAGE3 = "image3";
        public static final String KEY_POST_CREATED_AT = "created_at";
        public static final String KEY_POST_UPDATED_AT = "updated_at";
        public static final String KEY_POST_URL = "url";
        public static final String KEY_POST_MSG = "msg";
        /* FAVOURITE */
        public static final String KEY_FAVOURITE_ID = "id";
        public static final String KEY_FAVOURITE_TOKEN = "token";
        public static final String KEY_FAVOURITE_TYPE = "type";
        public static final String KEY_FAVOURITE_TYPE_POST = "type_post";
        public static final String KEY_FAVOURITE_SOURCE_ID = "source_id";
        public static final String KEY_FAVOURITE_CREATED_AT = "created_at";
        public static final String KEY_FAVOURITE_UPDATED_AT = "updated_at";
        public static final String KEY_FAVOURITE_USER_ID = "user_id";
        public static final String KEY_FAVOURITE_USER = "user";
        public static final String KEY_FAVOURITE_POST = "post";
        public static final String KEY_FAVOURITE_TITLE = "title";
        public static final String KEY_FAVOURITE_DESCRIPTION = "description";
        public static final String KEY_FAVOURITE_CATEGORY_ID = "category_id";
        public static final String KEY_FAVOURITE_CATEGORY_PARENT_ID = "category_parent_id";
        public static final String KEY_FAVOURITE_RATING = "rating";
        public static final String KEY_FAVOURITE_CATEGORY = "category";
        public static final String KEY_FAVOURITE_NAME = "name";
        public static final String KEY_FAVOURITE_ICON = "icon";
        public static final String KEY_FAVOURITE_PARENT_CATEGORY = "parent_category";


        /* RESULT */
        public static final String KEY_MESSAGE = "msg";
        public static final String KEY_DATA = "data";
        public static final String KEY_CODE = "code";
        public static final String KEY_META_DATA = "meta_data";

        /* STATUS CODE */
        public static final String KEY_SUCCESSFULLY = "200";
        public static final String KEY_ACCESS_DENIED = "403";
        public static final String KEY_NOT_FOUND = "404";
        public static final String KEY_EXISTS = "409";
        public static final String KEY_ERROR = "500";

        /* NOTIFICATION */
        public static final String KEY_NOTIFICATION_TOKEN = "token";
        public static final String KEY_NOTIFICATION_ID = "id";
        public static final String KEY_NOTIFICATION_TITLE = "title";
        public static final String KEY_NOTIFICATION_CONTENT = "content";
        public static final String KEY_NOTIFICATION_CHANEL = "chanel";
        public static final String KEY_NOTIFICATION_TYPE = "type";
        public static final String KEY_NOTIFICATION_USER_ID = "user_id";
        public static final String KEY_NOTIFICATION_POST_ID = "post_id";
        public static final String KEY_NOTIFICATION_OWNER_POST = "owner_post";
        public static final String KEY_NOTIFICATION_CREATED_AT = "created_at";
        public static final String KEY_NOTIFICATION_SEARCH_JOB = "search_job";
        public static final String KEY_NOTIFICATION_ENROLLMENT = "enrollment";
        public static final String KEY_NOTIFICATION_SYSTEM = "system";
        public static final String KEY_NOTIFICATION_EXTRA = "extra";


        /* VOTE */
        public static final String KEY_VOTE_TOKEN = "token";
        public static final String KEY_VOTE_SOURCE_ID = "source_id";
        public static final String KEY_VOTE_TYPE = "type";
        public static final String KEY_VOTE_ID = "id";
        public static final String KEY_VOTE_USER_ID = "user_id";
        public static final String KEY_VOTE_RATE = "rate";
        public static final String KEY_VOTE_USER = "user";
        public static final String KEY_VOTE_COMMENT = "comment";
        public static final String KEY_POST_UPDATED_FILE = "file";
        public static final String KEY_VOTE_OWNER = "owner";

        /* SHARE LINK */
        public static final String KEY_SHARE_TOKEN = "token";
        public static final String KEY_SHARE_TYPE = "type";
        public static final String KEY_SHARE_POST_ID = "post_id";
        public static final String KEY_SHARE_URL = "url";

        /* META DATA */
        public static final String KEY_META_DATA_TOTAL = "total";
        public static final String KEY_META_DATA_CURRENT_PAGE = "current_page";
        public static final String KEY_META_DATA_HAS_MORE_PAGE = "has_more_pages";
        public static final String KEY_META_DATA_NEXT_LINK = "next_link";

        //TODO: Hune V2
        /* TASK */
        public static final String KEY_TASK_TOKEN = "token";
        public static final String KEY_TASK_ID = "id";
        public static final String KEY_TASK_PAYMENT_TYPE = "payment_type";
        public static final String KEY_TASK_USER_ID = "user_id";
        public static final String KEY_TASK_OWNER = "owner_id";
        public static final String KEY_TASK_NAME = "name";
        public static final String KEY_TASK_CREATED_AT = "created_at";
        public static final String KEY_TASK_STATUS_PAYMENT = "status_payment";
        public static final String KEY_TASK_STATUS = "status";
        public static final String KEY_TASK_DELETE_AT = "deleted_at";
        public static final String KEY_TASK_UPDATE_AT = "updated_at";
        public static final String KEY_TASK_RATE = "rate";
        public static final String KEY_TASK_USER = "user";
        public static final String KEY_TASK_POST = "post";
        public static final String KEY_TASK_POST_ID = "post_id";
        public static final String KEY_TASK_AMOUNT = "amount";
        public static final String KEY_TASK_START_DATE = "start_date";
        public static final String KEY_TASK_END_DATE = "end_date";
        public static final String KEY_TASK_START_HOUR = "start_hour";
        public static final String KEY_TASK_END_HOUR = "end_hour";
        public static final String KEY_TASK_DESCRIPTION = "description";

        /* WALLET */
        public static final String KEY_WALLET_TOKEN = "token";
        public static final String KEY_WALLET_AMOUNT = "amount";
        public static final String KEY_WALLET_TYPE_MONEY = "type_money";
        public static final String KEY_WALLET_TRANSACTION_ID = "transaction_id";
        public static final String KEY_WALLET_HASH = "hash";
        /* TRANSACTION */
        public static final String KEY_TRANSACTION_NAME = "name";
        public static final String KEY_TRANSACTION_AMOUNT = "amount";
        public static final String KEY_TRANSACTION_TYPE_MONEY = "type_money";
        public static final String KEY_TRANSACTION_DESCRIPTION = "description";
        public static final String KEY_TRANSACTION_TXID = "txid";
        public static final String KEY_TRANSACTION_TYPE = "type";
        public static final String KEY_TRANSACTION_DEFINE_TRANSACTION = "define_transaction";
        public static final String KEY_TRANSACTION_CREATED_AT = "created_at";

        /* COUPON */
        public static final String KEY_COUPON_TOKEN = "token";
        public static final String KEY_COUPON_PAGE = "page";
        public static final String KEY_COUPON_LIMIT = "limit";
        public static final String KEY_COUPON_ID = "id";
        public static final String KEY_COUPON_PARTNER_ID = "partner_id";
        public static final String KEY_COUPON_NAME = "name";
        public static final String KEY_COUPON_IMAGE = "image";
        public static final String KEY_COUPON_PRICE = "price";
        public static final String KEY_COUPON_TO_DATE = "to_date";
        public static final String KEY_COUPON_FROM_DATE = "from_date";
        public static final String KEY_COUPON_CREATED_AT = "created_at";
        public static final String KEY_COUPON_UPDATED_AT = "updated_at";
        public static final String KEY_COUPON_CODE = "code";
        public static final String KEY_COUPON_GROUP_ID = "group_id";
        public static final String KEY_COUPON_USER_ID = "user_id";
        public static final String KEY_COUPON_COUPON_GROUP = "coupon_group";
        /* DEPOSIT CASH */
        public static final String KEY_DEPOSIT_ID = "id";
        public static final String KEY_DEPOSIT_USER_ID = "user_id";
        public static final String KEY_DEPOSIT_AMOUNT = "amount";
        public static final String KEY_DEPOSIT_TYPE_MONEY = "type_money";
        public static final String KEY_DEPOSIT_DESCRIPTION = "description";
        public static final String KEY_DEPOSIT_TXID = "txid";
        public static final String KEY_DEPOSIT_TYPE = "type";
        public static final String KEY_DEPOSIT_DEFINE_TRANSACTION = "define_transaction";
        public static final String KEY_DEPOSIT_STATUS = "status";
        public static final String KEY_DEPOSIT_UPDATED_AT = "updated_at";
        public static final String KEY_DEPOSIT_CREATED_AT = "created_at";

        public static final String KEY_TOKEN = "token";
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_BRANCH = "branch";
        public static final String KEY_START_HOUR = "start_hour";
        public static final String KEY_END_HOUR = "end_hour";
        public static final String KEY_START_DATE = "start_date";
        public static final String KEY_END_DATE = "end_date";
        public static final String KEY_TOTAL_COUPON = "total_coupon";
        public static final String KEY_DISCOUNT = "discount";
        public static final String KEY_PRICE = "price";
        public static final String KEY_LOGO = "logo";
        public static final String KEY_COVER = "cover";
        public static final String KEY_LAT = "lat";
        public static final String KEY_LONG = "long";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_LOCATION = "location";
        public static final String KEY_COUPON = "coupon";
        public static final String KEY_POSITION = "position";
        public static final String KEY_DATES = "dates";
        public static final String KEY_URL = "url";
        public static final String KEY_TOTAL = "total";
        public static final String KEY_BRANCH_KEY = "branch_key";
    }

    public class ExtraName {
        public static final String API = "API";
        public static final String IS_CHANGE_LANGUAGE = "ChangeLanguage";

    }

    public class IntentKey {
        public static final String KEY_TASK = "task";
    }
}
