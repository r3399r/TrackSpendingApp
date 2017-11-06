package com.example.tp1709017.yij_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Math.abs;

public class UpdateActivity extends Activity {

    private TextView btnOkayUpdate;
    private TextView btnCancelUpdate;
    private EditText inputMoneyUpdate;
    private EditText inputPsUpdate;
    private DbAdapter dbAdapter;
    private SharedPreferences data;
    private int totalAmount;
    private int rowId;
    private int money;
    private String ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        btnOkayUpdate = (TextView) findViewById(R.id.btn_OK_update);
        btnCancelUpdate = (TextView) findViewById(R.id.btn_cancel_update);
        inputMoneyUpdate = (EditText) findViewById(R.id.inputMoneyUpdate);
        inputPsUpdate = (EditText) findViewById(R.id.inputPsUpdate);

        dbAdapter = new DbAdapter(this);
        dbAdapter.open();

        data = getSharedPreferences("data", MODE_PRIVATE);
        totalAmount = data.getInt("total", 0);

        Intent it = getIntent();
        rowId = it.getIntExtra("rowId", 0);
        money = it.getIntExtra("money", 0);
        ps = it.getStringExtra("ps");

        inputMoneyUpdate.setText(abs(money) + "");
        inputPsUpdate.setText(ps);

        btnOkayUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!inputMoneyUpdate.getText().toString().equals("")) {

                    totalAmount -= money;

                    if (money > 0) {
                        dbAdapter.updateData(rowId, Integer.valueOf(inputMoneyUpdate.getText().toString()), inputPsUpdate.getText().toString());
                        totalAmount += Integer.valueOf(inputMoneyUpdate.getText().toString());
                    } else {
                        dbAdapter.updateData(rowId, Integer.valueOf(inputMoneyUpdate.getText().toString()) * -1, inputPsUpdate.getText().toString());
                        totalAmount -= Integer.valueOf(inputMoneyUpdate.getText().toString());
                    }

                    SharedPreferences.Editor editor = data.edit();
                    editor.putInt("total", totalAmount).apply();

                    Intent it = new Intent(UpdateActivity.this, HistoricalDataActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        });

        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(UpdateActivity.this, HistoricalDataActivity.class);
                startActivity(it);
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
