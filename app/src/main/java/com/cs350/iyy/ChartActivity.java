package com.cs350.iyy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
    private static final String TAG_STATUS = "Status";
    private static final String TAG_DATE ="Date";

    private final ArrayList<Entry> entries = new ArrayList<>();
    private final LineDataSet dataSet = new LineDataSet(entries, "# of Calls");
    private final ArrayList<String> labels = new ArrayList<>();

    private final int[] k = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //basic code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String phoneID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (Objects.equals(BasicInfo.CHECK_VALUE, "Posting"))
            getPostingData("http://143.248.199.109/~jaewook/getPosting.php", phoneID, BasicInfo.TYPE_OF_SNS, BasicInfo.DATE_FROM, BasicInfo.DATE_TO); // Server IP address
        else if (Objects.equals(BasicInfo.CHECK_VALUE, "Time"))
            getStatusData("http://143.248.199.109/~jaewook/getStatus.php", phoneID, BasicInfo.TYPE_OF_SNS, BasicInfo.DATE_FROM, BasicInfo.DATE_TO); // Server IP address
    }

    private void drawPostingGraph(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray postings = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i = 0; i < postings.length(); i++){
                JSONObject c = postings.getJSONObject(i);
                String date = c.getString(TAG_DATE);
                k[getIndexFromDate(date)] +=1;
            }
            int numOfData = 10;
            for(int i = 0; i < numOfData ; i++) {
                entries.add(new Entry(k[i], i));
                labels.add("6 / " + Integer.toString(i + 1));
            }
            LineData data = new LineData(labels, dataSet);
            LineChart lineChart = (LineChart) findViewById(R.id.chart);
            lineChart.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void drawStatusGraph(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray postings = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i = 0; i < postings.length(); i++){
                JSONObject c = postings.getJSONObject(i);
                String status = c.getString(TAG_STATUS);
                String date = c.getString(TAG_DATE);
                if (Objects.equals(status, "Opened"))
                    k[getIndexFromDate(date)] +=1;
            }
            int numOfData = 10;
            for(int i = 0; i < numOfData ; i++) {
                entries.add(new Entry(k[i], i));
                labels.add("6 / " + Integer.toString(i + 1));
            }
            LineData data = new LineData(labels, dataSet);
            LineChart lineChart = (LineChart) findViewById(R.id.chart);
            lineChart.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPostingData(String url, String phoneID, String sns, String from, String to){
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

                drawPostingGraph(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, phoneID, sns, from, to);
    }

    private void getStatusData(String url, String phoneID, String sns, String from, String to){
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

                drawStatusGraph(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, phoneID, sns, from, to);
    }

    private int getIndexFromDate(String d) {
        int prevYear = Integer.parseInt(BasicInfo.DATE_FROM.split(" ")[0].split("-")[0]);
        int prevMonth = Integer.parseInt(BasicInfo.DATE_FROM.split(" ")[0].split("-")[1]);
        int prevDay = Integer.parseInt(BasicInfo.DATE_FROM.split(" ")[0].split("-")[2]);
        int curYear = Integer.parseInt(d.split(" ")[0].split("-")[0]);
        int curMonth = Integer.parseInt(d.split(" ")[0].split("-")[1]);
        int curDay = Integer.parseInt(d.split(" ")[0].split("-")[2]);

        int cnt = 0;

        while((prevYear < curYear) || (prevYear == curYear && prevMonth < curMonth) || (prevYear == curYear && prevMonth == curMonth && prevDay < curDay) )
        {
            cnt+= 1;
            prevDay += 1;
            if((prevMonth == 1 || prevMonth == 3 ||prevMonth == 5 ||prevMonth == 7 ||prevMonth == 8 || prevMonth == 10 ||prevMonth == 12) && prevDay == 32 ) {
                prevDay = 1;
                prevMonth += 1;
            }else if ((prevMonth == 4 || prevMonth == 6 ||prevMonth == 9 ||prevMonth == 11) && prevDay == 31 )
            {
                prevDay = 1;
                prevMonth += 1;
            }else if (prevMonth == 2 && prevDay == 29)
            {
                prevDay = 1;
                prevMonth += 1;
            }

            if(prevMonth == 13) {
                prevMonth =1;
                prevYear+=1;
            }
        }

        return cnt;
    }
}
