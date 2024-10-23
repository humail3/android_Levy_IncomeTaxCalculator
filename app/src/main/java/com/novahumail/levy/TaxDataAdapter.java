package com.novahumail.levy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaxDataAdapter extends RecyclerView.Adapter<TaxDataAdapter.TaxDataViewHolder> {
    private List<TaxDataModel> taxDataList;

    // Constructor to initialize the data
    public TaxDataAdapter(List<TaxDataModel> taxDataList) {
        this.taxDataList = taxDataList;
    }

    // ViewHolder class to hold the views for each item
    public static class TaxDataViewHolder extends RecyclerView.ViewHolder {
        TextView totalPayableTaxTextView;
        TextView todayDateTextView;

        public TaxDataViewHolder(@NonNull View itemView) {
            super(itemView);
            totalPayableTaxTextView = itemView.findViewById(R.id.totalPayableTaxTextView);
            todayDateTextView = itemView.findViewById(R.id.todayDateTextView);
        }
    }

    @NonNull
    @Override
    public TaxDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tax_data_item, parent, false);
        return new TaxDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxDataViewHolder holder, int position) {
        TaxDataModel taxData = taxDataList.get(position);
        holder.totalPayableTaxTextView.setText(String.valueOf(taxData.getTotalPayableTax()));
        holder.todayDateTextView.setText(taxData.getTodayDate());
    }

    @Override
    public int getItemCount() {
        return taxDataList.size();
    }
}

