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

public class UserListAdapter extends ArrayAdapter<User> {

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<User> users;

    public UserListAdapter(Context context, int resource, ArrayList<User> users) {
        super(context, resource, users);
        this.users = users;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView nameView = (TextView) view.findViewById(R.id.usersname);


        User user = users.get(position);
        nameView.setText(user.getUserFullname());

        return view;
    }
}
