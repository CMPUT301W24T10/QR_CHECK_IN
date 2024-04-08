package com.example.qr_check_in.ui.listOfAttendee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.qr_check_in.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class ListOfAttendeeAdapter extends ArrayAdapter<CheckInCount> {

    private ArrayList<CheckInCount> checkInCounts;
    private Context context;

    public ListOfAttendeeAdapter(Context context, ArrayList<CheckInCount> checkInCounts){
        super(context,0,checkInCounts);
        this.checkInCounts = checkInCounts;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.attendee_list_element,parent,false);
        }
        CheckInCount checkInCount = checkInCounts.get(position);
        // getting the text views
        TextView name = view.findViewById(R.id.attendee_name);
        TextView count = view.findViewById(R.id.checkInCount);
        // populating the views
        name.setText("Name: "+checkInCount.getAttendeeName());
        count.setText("Count: "+checkInCount.getCheckInCount());

        return view;
    }

}
