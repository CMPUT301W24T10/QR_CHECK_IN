package com.example.qr_check_in.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_check_in.ModelClasses.User;
import com.example.qr_check_in.R;

import java.util.ArrayList;

public class UserListAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;
    public UserListAdapter(Context context, ArrayList<User> users){
        super(context,0,users);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.user_profile_item,parent,false);
        }
        User user = users.get(position);
        // getting the text views
        TextView username = view.findViewById(R.id.userNameTextView);
        TextView email = view.findViewById(R.id.emailTextView);
        TextView phoneNumber = view.findViewById(R.id.phoneNumberTextView);

        // populating the views
        username.setText("Username: "+user.getUsername());
        email.setText("Email: "+user.getEmail());
        phoneNumber.setText("Phone Number: "+user.getPhoneNumber());
        return view;
    }

}
