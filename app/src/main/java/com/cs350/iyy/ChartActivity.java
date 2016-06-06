package com.cs350.iyy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ChartActivity extends AppCompatActivity {

    private static final String TAG_RESULTS = "result";
    private static final String TAG_PHONEID = "PhoneID";
    private static final String TAG_SNS = "SNS";
    private static final String TAG_DATE ="Date";

    private ArrayList<HashMap<String,String>> postingList;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.listView);
        postingList = new ArrayList<>();
        String phoneID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (Objects.equals(BasicInfo.CHECK_VALUE, "Posting"))
            getData("http://192.168.0.42/~jaewook/getPosting.php", phoneID, BasicInfo.TYPE_OF_SNS, BasicInfo.DATE_FROM, BasicInfo.DATE_TO);
        else if (Objects.equals(BasicInfo.CHECK_VALUE, "Time"))
            getData("http://192.168.0.42/~jaewook/getStatus.php", phoneID, BasicInfo.TYPE_OF_SNS, BasicInfo.DATE_FROM, BasicInfo.DATE_TO);
    }

    private void showList(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray postings = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i = 0; i < postings.length(); i++){
                JSONObject c = postings.getJSONObject(i);
                String phoneID = c.getString(TAG_PHONEID);
                String sns = c.getString(TAG_SNS);
                String date = c.getString(TAG_DATE);

                HashMap<String,String> postingMap = new HashMap<>();

                postingMap.put(TAG_PHONEID, phoneID);
                postingMap.put(TAG_SNS, sns);
                postingMap.put(TAG_DATE, date);

                postingList.add(postingMap);
            }
            ListAdapter adapter = new SimpleAdapter(
                    ChartActivity.this, postingList, R.layout.list_item,
                    new String[] {TAG_PHONEID, TAG_SNS, TAG_DATE},
                    new int[] {R.id.phoneID, R.id.sns, R.id.date}
            );
            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData(String url, String phoneID, String sns, String from, String to){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                try {
                    String uri = params[0];
                    String phoneID = params[1];
                    String sns = params[2];
                    String from = params[3];
                    String to = params[4];

                    String data = URLEncoder.encode("phoneID", "UTF-8") + "=" + URLEncoder.encode(phoneID, "UTF-8");
                    data += "&" + URLEncoder.encode("sns", "UTF-8") + "=" + URLEncoder.encode(sns, "UTF-8");
                    data += "&" + URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8");
                    data += "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(to, "UTF-8");

                    URL url = new URL(uri);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String json;

                    while((json = bufferedReader.readLine()) != null) {
                        sb.append(json).append("\n");
                    }
                    return sb.toString().trim();

                } catch(Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                showList(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, phoneID, sns, from, to);
    }
}
