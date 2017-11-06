package com.example.tp1709017.yij_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by tp1709017 on 2017/10/30.
 */

public class ListViewAdapter extends ArrayAdapter<ListViewAdapter.SampleItem> {

    public ListViewAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        final Holder holder;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.data_list, null);
            holder = new Holder();
            holder.dateTimeDisplay = (TextView) v.findViewById(R.id.date_time_historicalData);
            holder.moneyDisplay = (TextView) v.findViewById(R.id.money_historicalData);
            holder.psDisplay = (TextView) v.findViewById(R.id.ps_historicalData);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }

        holder.dateTimeDisplay.setText(getItem(position).dateTimeDisplay);
        holder.moneyDisplay.setText("" + getItem(position).moneyDisplay);

        if (getItem(position).moneyDisplay < 0)
            holder.moneyDisplay.setTextColor(0xffff4444); // red
        else
            holder.moneyDisplay.setTextColor(0xff33b5e5); // blue

        holder.psDisplay.setText(getItem(position).psDisplay);

        return v;
    }

    /**
     * View holder for the views we need access to
     */
    private class Holder {
        public TextView dateTimeDisplay;
        public TextView moneyDisplay;
        public TextView psDisplay;
    }

    public static class SampleItem {
        public String dateTimeDisplay;
        public int moneyDisplay;
        public String psDisplay;

        public SampleItem(String dateTime, int money, String ps) {
            this.dateTimeDisplay = dateTime;
            this.moneyDisplay = money;
            this.psDisplay = ps;
        }
    }
}
