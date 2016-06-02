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
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

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
                if (isChecked)
                    Toast.makeText(getApplication(), "Start collecting Facebook data", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplication(), "Stop collecting Facebook data", Toast.LENGTH_SHORT).show();
            }
        });
        final Switch twCollectSwitch = (Switch) findViewById(R.id.twitter_collect_switch);
        twCollectSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if (isChecked)
                    Toast.makeText(getApplication(), "Start collecting Twitter data", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplication(), "Stop collecting Twitter data", Toast.LENGTH_SHORT).show();
            }
        });

        final Spinner alarmIntervalSpinner = (Spinner) findViewById(R.id.alarm_interval_spinner);
        alarmIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Nothing", Toast.LENGTH_SHORT).show();
            }
        });
        final Spinner alarmTypeSpinner = (Spinner) findViewById(R.id.alarm_type_spinner);
        alarmTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Nothing", Toast.LENGTH_SHORT).show();
            }
        });
        final Spinner alarmToneSpinner = (Spinner) findViewById(R.id.alarm_tone_spinner);
        alarmToneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Nothing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
