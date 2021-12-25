package com.example.drassit.ui.gas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drassit.R;
import com.example.drassit.ui.model.MyLocation;

import java.util.List;

public class FillGasAdapter extends ArrayAdapter<MyLocation> {
    public FillGasAdapter(@NonNull Context context, int resource, @NonNull List<MyLocation> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fill_gas_location_selected, parent, false);
        TextView tvFillGasLocationSelect = convertView.findViewById(R.id.tvFillGasLocationSelected);

        MyLocation myLocation = this.getItem(position);
        if(myLocation !=null){
            tvFillGasLocationSelect.setText(myLocation.getLocation());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fill_gas_location_selection, parent, false);
        TextView tvfillGasLocation = convertView.findViewById(R.id.tvFillGasLocationSelect);

        MyLocation myLocation = this.getItem(position);
        if(myLocation !=null){
            tvfillGasLocation.setText(myLocation.getLocation());
        }
        return convertView;
    }
}
