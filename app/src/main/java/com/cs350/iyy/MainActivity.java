package com.cs350.iyy;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * for twitter connection
 */
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity {

    private TimerTask timerTask = null;
    private static Timer timer = null;

    private final Handler mHandler = new Handler();

    private static Boolean checked = false;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final ToggleButton tb = (ToggleButton) this.findViewById(R.id.startStopButton);
        tb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tb.isChecked()) {
                    timerJob();
                    timer = new Timer();
                    timer.schedule(timerTask, 0, 60000);
                    checked = true;
                } else {
                    timer.cancel();
                    timer = null;
                    checked = false;
                }
            }
        });
        tb.setChecked(checked);
    }

    private void timerJob() {
        try {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (BasicInfo.collectFacebook)
                        connect_facebook();
                    if (BasicInfo.collectTwitter)
                        connect_twitter();
                }
            };
        } catch (Exception ignored) {

        }
    }

    public void onSettingButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        startActivity(intent);
    }

    public void onStatisticsButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        startActivity(intent);
    }

    public void onProcessList(View v) {
        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
        boolean facebook_check = false;
        for(int i = 0; i < appList.size(); i++) {
            ActivityManager.RunningAppProcessInfo rapi = appList.get(i);
            Log.d("run Process","Package Name : " + rapi.processName);
            if(rapi.processName.equals(BasicInfo.FacebookProcessName)  ) {
                Log.d("run Process", "I FIND!!!!!!");
                facebook_check = true;
            }
        }
        if(facebook_check && !BasicInfo.FacebookProcessStatus) {
            Log.e("run Process", "Facebook Start");
            BasicInfo.FacebookProcessStatus = true;

        }else if(!facebook_check && BasicInfo.FacebookProcessStatus){
            Log.e("run Process", "Facebook End");
            BasicInfo.FacebookProcessStatus = false;
        }


    }

    private void connect_twitter() {
        if (BasicInfo.TwitLogin) {
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();

                builder.setOAuthAccessToken(BasicInfo.TWIT_KEY_TOKEN);
                builder.setOAuthAccessTokenSecret(BasicInfo.TWIT_KEY_TOKEN_SECRET);
                builder.setOAuthConsumerKey(BasicInfo.TWIT_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(BasicInfo.TWIT_CONSUMER_SECRET);

                Configuration config = builder.build();
                TwitterFactory tFactory = new TwitterFactory(config);
                BasicInfo.TwitInstance = tFactory.getInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            showUserTimeline();
        } else {
            RequestTokenThread thread = new RequestTokenThread();
            thread.start();
        }
    }
    private void connect_facebook() {
        if(BasicInfo.FacebookLogin) {
            GraphRequest request = new GraphRequest(
                    BasicInfo.loginResult.getAccessToken(),
                    "/me/feed",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(
                                GraphResponse response) {
                            try{
                                JSONArray k = response.getJSONObject().getJSONArray("data");
                                for(int i = 0 ; i < k.length() ; i++){
                                    String date = k.getJSONObject(i).getString("created_time").substring(0, 10);
                                    String time = k.getJSONObject(i).getString("created_time").substring(11, 19);
                                    insertFacebookPostingData(date + " " + time);
                                }

                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
            request.executeAsync();
        }
        else {
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,
                    Arrays.asList("public_profile","user_posts", "email"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest request = new GraphRequest(
                            loginResult.getAccessToken(),
                            "/me/feed",
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                @Override
                                public void onCompleted(
                                        GraphResponse response) {
                                    try {
                                        JSONArray k = response.getJSONObject().getJSONArray("data");
                                        for (int i = 0; i < k.length(); i++) {
                                            String date = k.getJSONObject(i).getString("created_time").substring(0, 10);
                                            String time = k.getJSONObject(i).getString("created_time").substring(11, 19);
                                            insertFacebookPostingData(date + " " + time);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    BasicInfo.loginResult = loginResult;
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "in LoginResult on cancel", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getApplicationContext(), "in LoginResult on error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    /**
     * RequestToken 요청 스레드
     */
    private class RequestTokenThread extends Thread {
        public void run() {

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setDebugEnabled(true);
                builder.setOAuthConsumerKey(BasicInfo.TWIT_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(BasicInfo.TWIT_CONSUMER_SECRET);

                TwitterFactory factory = new TwitterFactory(builder.build());
                Twitter mTwit = factory.getInstance();
                final RequestToken mRequestToken = mTwit.getOAuthRequestToken();

                BasicInfo.TwitInstance = mTwit;
                BasicInfo.TwitRequestToken = mRequestToken;

                mHandler.post(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), TwitLogin.class);
                        intent.putExtra("authUrl", mRequestToken.getAuthorizationURL());
                        startActivityForResult(intent, BasicInfo.REQ_CODE_TWIT_LOGIN);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 다른 액티비티로부터의 응답 처리
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);

        if (resultCode == RESULT_OK) {
            /**
             * Twitter로부터의 응답
             */
            if (requestCode == BasicInfo.REQ_CODE_TWIT_LOGIN) {

                OAuthAccessTokenThread thread = new OAuthAccessTokenThread(resultIntent);
                thread.start();
            }
            else if(requestCode == BasicInfo.REQ_CODE_FACEBOOK_LOGIN){
                BasicInfo.FacebookLogin = true;
                callbackManager.onActivityResult(requestCode, resultCode, resultIntent);
            }
        }
        else {
            if(requestCode == BasicInfo.REQ_CODE_FACEBOOK_LOGIN)
                callbackManager.onActivityResult(requestCode, resultCode, resultIntent);
        }
    }
    class OAuthAccessTokenThread extends Thread {
        final Intent resultIntent;

        public OAuthAccessTokenThread(Intent intent) {
            resultIntent = intent;
        }

        public void run() {
            try {
                Twitter mTwit = BasicInfo.TwitInstance;

                AccessToken mAccessToken = mTwit.getOAuthAccessToken(BasicInfo.TwitRequestToken, resultIntent.getStringExtra("oauthVerifier"));

                BasicInfo.TwitLogin = true;
                BasicInfo.TWIT_KEY_TOKEN = mAccessToken.getToken();
                BasicInfo.TWIT_KEY_TOKEN_SECRET = mAccessToken.getTokenSecret();

                BasicInfo.TwitAccessToken = mAccessToken;

                BasicInfo.TwitScreenName = mTwit.getScreenName();

                mHandler.post(new Runnable() {
                    public void run() {
                        showUserTimeline();
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showUserTimeline() {
        GetUserTimelineThread thread = new GetUserTimelineThread();
        thread.start();
    }

    class GetUserTimelineThread extends Thread {
        public void run() {
            getUserTimeline();
        }

        /**
         * UserTimeline 요청
         */
        public void getUserTimeline() {
            Twitter mTwit = BasicInfo.TwitInstance;

            try {
                final List<Status> status = mTwit.getUserTimeline();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                for (int i = 0; i < status.size(); i++) {
                    String dateTime = sdf.format(status.get(i).getCreatedAt());
                    insertTwitterPostingData(dateTime);
                }

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void onPause() {
        super.onPause();
        saveProperties();
    }

    protected void onResume() {
        super.onResume();
        loadProperties();
    }

    private void saveProperties() {
        SharedPreferences pref = getSharedPreferences("TWIT", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("TwitLogin", BasicInfo.TwitLogin);
        editor.putString("TWIT_KEY_TOKEN", BasicInfo.TWIT_KEY_TOKEN);
        editor.putString("TWIT_KEY_TOKEN_SECRET", BasicInfo.TWIT_KEY_TOKEN_SECRET);

        editor.apply();
    }

    private void loadProperties() {
        SharedPreferences pref = getSharedPreferences("TWIT", MODE_PRIVATE);

        BasicInfo.TwitLogin = pref.getBoolean("TwitLogin", false);
        BasicInfo.TWIT_KEY_TOKEN = pref.getString("TWIT_KEY_TOKEN", "");
        BasicInfo.TWIT_KEY_TOKEN_SECRET = pref.getString("TWIT_KEY_TOKEN_SECRET", "");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertTwitterPostingData(String date) {
        String phoneID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        insertToDatabase(phoneID, "Twitter", date);
    }

    private void insertFacebookPostingData(String date) {
        String phoneID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        insertToDatabase(phoneID, "Facebook", date);
    }

    private void insertToDatabase(String phoneID, String sns, String date) {

        class InsertData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    String phoneID = params[0];
                    String sns = params[1];
                    String date = params[2];

                    String link="http://192.168.0.42/~jaewook/insert.php"; // Server IP address
                    String data = URLEncoder.encode("phoneID", "UTF-8") + "=" + URLEncoder.encode(phoneID, "UTF-8");
                    data += "&" + URLEncoder.encode("sns", "UTF-8") + "=" + URLEncoder.encode(sns, "UTF-8");
                    data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e) {
                    return "Exception: " + e.getMessage();
                }
            }
        }

        InsertData task = new InsertData();
        task.execute(phoneID, sns, date);
    }

}