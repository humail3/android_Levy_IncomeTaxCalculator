package com.novahumail.levy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    public CustomSpinnerAdapter(Context context, List<String> items) {
        super(context, R.layout.spinner_item_layout, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initSpinnerView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initSpinnerView(position, convertView, parent);
    }

    private View initSpinnerView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.spinner_item_layout, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position));

        return convertView;
    }
}

