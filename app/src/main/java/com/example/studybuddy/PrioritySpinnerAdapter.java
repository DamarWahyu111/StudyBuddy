package com.example.studybuddy;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PrioritySpinnerAdapter extends ArrayAdapter<String> {
    private final String[] priorities;
    private final Context context;
    public PrioritySpinnerAdapter(Context context, String[] priorities) {
        super(context, android.R.layout.simple_spinner_item, priorities);
        this.priorities = priorities;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        getPriorityColor(view, position);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        getPriorityColor(view, position);
        return view;
    }

    private void getPriorityColor(TextView view, int position) {
        switch (position) {
            case 0: // High
                view.setTextColor(Color.RED);
                break;
            case 1: // Medium
                view.setTextColor(Color.YELLOW);
                break;
            case 2: // Low
                view.setTextColor(Color.GREEN);
                break;
        }
    }
}
