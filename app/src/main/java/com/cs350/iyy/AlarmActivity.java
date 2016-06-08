package com.cs350.iyy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        final TextView txtValue = (TextView) findViewById(R.id.alarm_title);
        txtValue.setText("Your SNS using time is " + Integer.toString(BasicInfo.SNS_USING_TIME_ADD / 1000) + "second");
    }
}
