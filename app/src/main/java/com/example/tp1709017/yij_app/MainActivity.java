package com.example.tp1709017.yij_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView textTotal;
    private TextView btnAdd;
    private TextView btnMinus;
    private TextView btnViewHistoricalData;
    private int totalAmount;
    SharedPreferences data;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTotal = (TextView) findViewById(R.id.totalNum);
        btnAdd = (TextView) findViewById(R.id.add);
        btnMinus = (TextView) findViewById(R.id.minus);
        btnViewHistoricalData = (TextView) findViewById(R.id.historicalData);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, AddActivity.class);
                startActivity(it);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, MinusActivity.class);
                startActivity(it);
            }
        });

        btnViewHistoricalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this,HistoricalDataActivity.class);
                startActivity(it);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        data = getSharedPreferences("data", MODE_PRIVATE);
        totalAmount = data.getInt("total", 0);
        textTotal.setText("" + totalAmount);
        if (totalAmount < 0)
            textTotal.setTextColor(0xffff4444); // red
        else
            textTotal.setTextColor(0xff33b5e5); // blue
    }
}
