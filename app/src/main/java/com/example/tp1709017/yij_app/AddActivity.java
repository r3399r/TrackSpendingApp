package com.example.tp1709017.yij_app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.DateTime;

public class AddActivity extends Activity {

    private TextView btnOkayAdd;
    private TextView btnCancelAdd;
    private EditText inputMoneyAdd;
    private EditText inputPsAdd;
    private DbAdapter dbAdapter;
    private SharedPreferences data;
    private int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnOkayAdd = (TextView) findViewById(R.id.btn_OK_add);
        btnCancelAdd = (TextView) findViewById(R.id.btn_cancel_add);
        inputMoneyAdd = (EditText) findViewById(R.id.inputMoneyAdd);
        inputPsAdd = (EditText) findViewById(R.id.inputPsAdd);

        dbAdapter = new DbAdapter(this);
        dbAdapter.open();

        data = getSharedPreferences("data", MODE_PRIVATE);
        totalAmount = data.getInt("total", 0);

        btnOkayAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!inputMoneyAdd.getText().toString().equals("")) {

                    DateTime dateTime = new DateTime();
                    dbAdapter.insertNewData(dateTime.getMillis(), Integer.valueOf(inputMoneyAdd.getText().toString()), inputPsAdd.getText().toString());

                    totalAmount += Integer.valueOf(inputMoneyAdd.getText().toString());
                    SharedPreferences.Editor editor = data.edit();
                    editor.putInt("total", totalAmount).apply();

                    finish();
                }
            }
        });

        btnCancelAdd.setOnClickListener(new View.OnClickListener() {
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
