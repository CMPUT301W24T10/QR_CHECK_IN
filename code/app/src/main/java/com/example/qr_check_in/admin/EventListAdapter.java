package com.example.qr_check_in.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;
    public EventListAdapter(Context context, ArrayList<Event> events){
        super(context,0,events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list_item,parent,false);
        }
        Event event = events.get(position);
        // getting the text views
        TextView eventName = view.findViewById(R.id.eventNameTextView);
        TextView eventDescription = view.findViewById(R.id.descriptionTextView);
        TextView eventLocation = view.findViewById(R.id.locationTextView);

        // populating the views
        eventName.setText("Event Name: "+event.getTitle());
        eventDescription.setText("Description: "+event.getDescription());
        eventLocation.setText("Location: "+event.getLocation());

        return view;
    }



}
