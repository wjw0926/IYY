package com.cs350.iyy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

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
    public static final String TAG = "MainActivity";
    TextView nameText;
    Button connectBtn;

    Button writeBtn;
    EditText writeInput;

    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        final ToggleButton tb = (ToggleButton) this.findViewById(R.id.startstopButton);
        tb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tb.isChecked()) {
                    tb.setTextColor(Color.RED);
                } else {
                    tb.setTextColor(Color.CYAN);
                }
            }
        });

        final Button connectBtn = (Button) this.findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connect_twitter();
            }
        });
    }

    public void onSettingButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        startActivity(intent);
    }

    public void onStatisticsButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        startActivity(intent);
    }
    private void connect_twitter() {
        Log.d(TAG, "connect_twitter() called");
        if (BasicInfo.TwitLogin) {
            Log.d(TAG, "twitter already logged in.");
            Toast.makeText(getBaseContext(), "twitter already logged in.", Toast.LENGTH_LONG).show();

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();

                builder.setOAuthAccessToken(BasicInfo.TWIT_KEY_TOKEN);
                builder.setOAuthAccessTokenSecret(BasicInfo.TWIT_KEY_TOKEN_SECRET);
                builder.setOAuthConsumerKey(BasicInfo.TWIT_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(BasicInfo.TWIT_CONSUMER_SECRET);

                Configuration config = builder.build();
                TwitterFactory tFactory = new TwitterFactory(config);
                BasicInfo.TwitInstance = tFactory.getInstance();

                Toast.makeText(getBaseContext(), "twitter connected.", Toast.LENGTH_LONG).show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            showUserTimeline();

        } else {

            RequestTokenThread thread = new RequestTokenThread();
            thread.start();
        }
    }

    /**
     * RequestToken 요청 스레드
     */
    class RequestTokenThread extends Thread {
        public void run() {

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setDebugEnabled(true);
                builder.setOAuthConsumerKey(BasicInfo.TWIT_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(BasicInfo.TWIT_CONSUMER_SECRET);

                TwitterFactory factory = new TwitterFactory(builder.build());
                Twitter mTwit = factory.getInstance();
                final RequestToken mRequestToken = mTwit.getOAuthRequestToken();
                String outToken = mRequestToken.getToken();
                String outTokenSecret = mRequestToken.getTokenSecret();

                Log.d(TAG, "Request Token : " + outToken + ", " + outTokenSecret);
                Log.d(TAG, "AuthorizationURL : " + mRequestToken.getAuthorizationURL());

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
            if (requestCode == BasicInfo.REQ_CODE_TWIT_LOGIN) {

                OAuthAccessTokenThread thread = new OAuthAccessTokenThread(resultIntent);
                thread.start();
            }
        }
    }

    class OAuthAccessTokenThread extends Thread {
        Intent resultIntent;

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
                        Toast.makeText(getBaseContext(), "Twitter connection succeeded : " + BasicInfo.TWIT_KEY_TOKEN, Toast.LENGTH_LONG).show();

                        showUserTimeline();
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private void showUserTimeline() {
        Log.d(TAG, "showUserTimeline() called.");

        // UserTimeline 요청
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
                Log.d(TAG, "string inserted");

                Status stat;

                for (int i = 0; i < status.size(); i++)
                {
                    stat = status.get(i);
                    Log.d(TAG, stat.getCreatedAt().toString());
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

        editor.commit();
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
}
