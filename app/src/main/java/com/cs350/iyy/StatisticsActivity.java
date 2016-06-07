package com.cs350.iyy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class StatisticsActivity extends AppCompatActivity {

    private final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
            if (isFrom)
            {
                BasicInfo.DATE_FROM = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth) + " 00:00:00";
            }
            else
            {
                BasicInfo.DATE_TO = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth) + " 00:00:00";
            }
        }
    };

    private static boolean checked = false;
    private static boolean checked2 = false;
    private static boolean isFrom = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
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
        Log.e("TEST", "HERE I AM1");
        final ToggleButton tb = (ToggleButton) this.findViewById(R.id.type_of_sns);
        tb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tb.isChecked()) {
                    BasicInfo.TYPE_OF_SNS = "Facebook";
                    checked = true;
                } else {
                    BasicInfo.TYPE_OF_SNS = "Twitter";
                    checked = false;
                }
            }
        });
        tb.setChecked(checked);

        final ToggleButton cv = (ToggleButton) this.findViewById(R.id.check_value);
        cv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cv.isChecked()) {
                    BasicInfo.CHECK_VALUE = "Time";
                    checked2 = true;
                } else {
                    BasicInfo.CHECK_VALUE = "Posting";
                    checked2 = false;
                }
            }
        });
        cv.setChecked(checked2);
    }
    public void onFromButtonClicked(View v) {
        DatePickerDialog dialog = new DatePickerDialog(this, listener, 2016, 5, 8);
        isFrom = true;
        dialog.show();
    }
    public void onToButtonClicked(View v) {
        DatePickerDialog dialog = new DatePickerDialog(this, listener, 2016, 5, 8);
        isFrom = false;
        dialog.show();
    }
    public void onRunButtonClicked(View v) {
        Log.e("TEST", "HERE I AM");
        Intent intent = new Intent(getApplicationContext(), Chart2Activity.class);
        startActivity(intent);
    }
    public void onBackButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
