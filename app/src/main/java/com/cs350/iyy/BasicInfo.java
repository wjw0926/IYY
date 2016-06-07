package com.cs350.iyy;

import com.facebook.login.LoginResult;

import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

class BasicInfo {
    public static final String TWIT_API_KEY = "xRoDCNvHVfoQToAWyebf4g";
    public static final String TWIT_CONSUMER_KEY = "xRoDCNvHVfoQToAWyebf4g";
    public static final String TWIT_CONSUMER_SECRET = "LooMN0gPSrc0j1ddX8AV8tmxBsA28rLWxNhFo0pNJg";
    public static final String TWIT_CALLBACK_URL = "http://android-town.org";

    public static final int REQ_CODE_TWIT_LOGIN = 1001;
    public static final int REQ_CODE_FACEBOOK_LOGIN = 64206;

    public static boolean TwitLogin = false;
    public static Twitter TwitInstance = null;
    public static AccessToken TwitAccessToken = null;
    public static RequestToken TwitRequestToken = null;

    public static String TWIT_KEY_TOKEN = "";
    public static String TWIT_KEY_TOKEN_SECRET = "";
    public static String TwitScreenName = "";

    public static boolean FacebookLogin = false;
    public static LoginResult loginResult;
    
    public static boolean collectFacebook = false;
    public static boolean collectTwitter = false;

    /**
     * For drawing charts
     */
    public static String TYPE_OF_SNS = "Twitter";
    public static String CHECK_VALUE = "Posting";
    public static String DATE_FROM = "2015-01-01 00:00:00";

    private static final Date globalDate = new Date();
    private static final SimpleDateFormat globalSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String globalDateTime = globalSDF.format(globalDate);
    public static String DATE_TO = globalDateTime;

    public static final String FacebookProcessName = "com.facebook.katana";
    public static final String TwitterProcessName = "com.twitter.android";
    public static boolean previousFacebookStatus = false;
    public static boolean previousTwitterStatus = false;

    /**
     * For alarm feedback
     */
    public static String ALARM_INTERVAL = "10 minutes";
    public static String ALARM_TYPE = "Sound";
    public static String ALARM_TONE = "A";
}
