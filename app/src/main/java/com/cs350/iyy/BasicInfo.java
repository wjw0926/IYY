package com.cs350.iyy;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

class BasicInfo {
    public static final String TWIT_API_KEY = "xRoDCNvHVfoQToAWyebf4g";
    public static final String TWIT_CONSUMER_KEY = "xRoDCNvHVfoQToAWyebf4g";
    public static final String TWIT_CONSUMER_SECRET = "LooMN0gPSrc0j1ddX8AV8tmxBsA28rLWxNhFo0pNJg";
    public static final String TWIT_CALLBACK_URL = "http://android-town.org";

    public static final int REQ_CODE_TWIT_LOGIN = 1001;

    public static boolean TwitLogin = false;
    public static Twitter TwitInstance = null;
    public static AccessToken TwitAccessToken = null;
    public static RequestToken TwitRequestToken = null;

    public static String TWIT_KEY_TOKEN = "";
    public static String TWIT_KEY_TOKEN_SECRET = "";
    public static String TwitScreenName = "";
}
