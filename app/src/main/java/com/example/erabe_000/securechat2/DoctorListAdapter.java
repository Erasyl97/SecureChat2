package com.example.erabe_000.securechat2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DoctorListAdapter extends ArrayAdapter<Doctor>{

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Doctor> doctors;

    public DoctorListAdapter(Context context, int resource, ArrayList<Doctor> doctors) {
        super(context, resource, doctors);
        this.doctors = doctors;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView nameView = (TextView) view.findViewById(R.id.usersname);

        Doctor doctor = doctors.get(position);
        nameView.setText(doctor.getUserFullname());

        return view;
    }
}
