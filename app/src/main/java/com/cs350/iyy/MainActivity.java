package com.cs350.iyy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.Date;

public class MainActivity extends AppCompatActivity {

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
    }

    public void onSettingButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        startActivity(intent);
    }

    public void onStatisticsButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        startActivity(intent);
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

    public void insertPostingData(View view) {
        String phoneID = "1";
        String sns = "Facebook";
        Date d = new Date();
        String date = d.toString();

        insertToDatabase(phoneID, sns, date);
    }

    private void insertToDatabase(String phoneID, String sns, String date) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    String phoneID = (String) params[0];
                    String sns = (String) params[1];
                    String date = (String) params[2];

                    String link="http://192.168.0.17/~jaewook/insert.php"; // Server IP address
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
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        InsertData task = new InsertData();
        task.execute(phoneID, sns, date);
    }
}