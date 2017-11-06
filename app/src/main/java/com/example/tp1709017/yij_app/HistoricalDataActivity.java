package com.example.tp1709017.yij_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HistoricalDataActivity extends Activity {

    private DbAdapter dbAdapter;
    private ListView mListView;
    private Cursor cursor;
    private ListViewAdapter adapter;
    private SharedPreferences data;
    private TextView showTotalMoney;
    private int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_data);

        mListView = (ListView) findViewById(R.id.list_historical_data);
        showTotalMoney = (TextView) findViewById(R.id.show_total_money);
        dbAdapter = new DbAdapter(HistoricalDataActivity.this);
        dbAdapter.open();

        adapter = new ListViewAdapter(HistoricalDataActivity.this);

        data = getSharedPreferences("data", MODE_PRIVATE);
        totalAmount = data.getInt("total", 0);
        showTotalMoney.setText(totalAmount + "");
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.clear();

        try {
            cursor = dbAdapter.getHistoryData();
            cursor.moveToFirst();

            if (cursor.getCount() != 0) {
                do {
                    String epoch_time = cursor.getString(cursor.getColumnIndex("epoch_time"));
                    int money = cursor.getInt(cursor.getColumnIndex("money"));
                    String ps = cursor.getString(cursor.getColumnIndex("ps"));

                    DateTime Date = new DateTime(Long.valueOf(epoch_time));
                    Date = Date.withZone(DateTimeZone.forOffsetHours(getTimeZone()));

                    String date_time = "";
                    date_time += (Date.getYear() - 1911) + "/";
                    date_time += Date.getMonthOfYear() + "/";
                    date_time += Date.getDayOfMonth();
                    date_time += "(" + weekDay(Date) + ") ";
                    date_time += Date.getHourOfDay() + ":";

                    if (Date.getMinuteOfHour() < 10)
                        date_time += "0";

                    date_time += Date.getMinuteOfHour();

                    adapter.add(new ListViewAdapter.SampleItem(date_time, money, ps));

                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final int rowId;
                final int money;
                final String ps;

                try {
                    cursor = dbAdapter.getHistoryData();
                    cursor.moveToPosition(position);

                    rowId = cursor.getInt(0);
                    money = cursor.getInt(cursor.getColumnIndex("money"));
                    ps = cursor.getString(cursor.getColumnIndex("ps"));

                } finally {
                    if (cursor != null) {
                        cursor.close();
                        cursor = null;
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(HistoricalDataActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("想做什麼呢?");
                builder.setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbAdapter.deleteData(rowId);
                        dialog.dismiss();

                        totalAmount -= money;
                        SharedPreferences.Editor editor = data.edit();
                        editor.putInt("total", totalAmount).apply();

                        Intent it = new Intent(HistoricalDataActivity.this, HistoricalDataActivity.class);
                        startActivity(it);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                });
                builder.setNeutralButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent it = new Intent(HistoricalDataActivity.this, UpdateActivity.class);
                        it.putExtra("rowId", rowId);
                        it.putExtra("money", money);
                        it.putExtra("ps", ps);
                        startActivity(it);
                        dialog.dismiss();

                        finish();
                    }
                });
                builder.setNegativeButton("沒事", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
    }

    public static int getTimeZone() {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        if (localTime.contains("+"))
            localTime = localTime.substring(1);
        return Integer.parseInt(localTime) / 100;
    }

    public String weekDay(DateTime date) {
        if (date.getDayOfWeek() == 1)
            return "一";
        else if (date.getDayOfWeek() == 2)
            return "二";
        else if (date.getDayOfWeek() == 3)
            return "三";
        else if (date.getDayOfWeek() == 4)
            return "四";
        else if (date.getDayOfWeek() == 5)
            return "五";
        else if (date.getDayOfWeek() == 6)
            return "六";
        else
            return "日";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
}
