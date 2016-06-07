package com.cs350.iyy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {

    private static Boolean checked = false;
    private static Boolean checked2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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

        final Switch fbCollectSwitch = (Switch) findViewById(R.id.facebook_collect_switch);
        fbCollectSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if (isChecked) {
                    BasicInfo.collectFacebook = true;
                    checked = true;
                } else {
                    BasicInfo.collectFacebook = false;
                    checked = false;
                }
            }
        });
        fbCollectSwitch.setChecked(checked);

        final Switch twCollectSwitch = (Switch) findViewById(R.id.twitter_collect_switch);
        twCollectSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if (isChecked) {
                    BasicInfo.collectTwitter = true;
                    checked2 = true;
                } else {
                    BasicInfo.collectTwitter = false;
                    checked2 = false;
                }
            }
        });
        twCollectSwitch.setChecked(checked2);

        final Spinner alarmIntervalSpinner = (Spinner) findViewById(R.id.alarm_interval_spinner);
        alarmIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BasicInfo.ALARM_INTERVAL = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Spinner alarmTypeSpinner = (Spinner) findViewById(R.id.alarm_type_spinner);
        alarmTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BasicInfo.ALARM_TYPE = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Spinner alarmToneSpinner = (Spinner) findViewById(R.id.alarm_tone_spinner);
        alarmToneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BasicInfo.ALARM_TONE = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void onBackButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
