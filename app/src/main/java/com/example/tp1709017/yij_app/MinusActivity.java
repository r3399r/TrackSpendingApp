package com.example.tp1709017.yij_app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.DateTime;

public class MinusActivity extends Activity {

    private TextView btnOkayMinus;
    private TextView btnCancelMinus;
    private EditText inputMoneyMinus;
    private EditText inputPsMinus;
    private DbAdapter dbAdapter;
    private SharedPreferences data;
    private int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minus);

        btnOkayMinus = (TextView) findViewById(R.id.btn_OK_minus);
        btnCancelMinus = (TextView) findViewById(R.id.btn_cancel_minus);
        inputMoneyMinus = (EditText) findViewById(R.id.inputMoneyMinus);
        inputPsMinus = (EditText) findViewById(R.id.inputPsMinus);

        dbAdapter = new DbAdapter(this);
        dbAdapter.open();

        data = getSharedPreferences("data", MODE_PRIVATE);
        totalAmount = data.getInt("total", 0);

        btnOkayMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!inputMoneyMinus.getText().toString().equals("")) {

                    DateTime dateTime = new DateTime();
                    dbAdapter.insertNewData(dateTime.getMillis(), Integer.valueOf(inputMoneyMinus.getText().toString()) * -1, inputPsMinus.getText().toString());

                    totalAmount -= Integer.valueOf(inputMoneyMinus.getText().toString());
                    SharedPreferences.Editor editor = data.edit();
                    editor.putInt("total", totalAmount).apply();

                    finish();
                }
            }
        });

        btnCancelMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
}
