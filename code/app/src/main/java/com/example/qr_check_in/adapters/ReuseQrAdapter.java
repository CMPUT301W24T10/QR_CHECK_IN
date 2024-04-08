package com.example.qr_check_in.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qr_check_in.ModelClasses.Event;
import com.example.qr_check_in.R;
import com.example.qr_check_in.data.QRCodeGenerator;

import java.util.ArrayList;

public class ReuseQrAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;
    public ReuseQrAdapter(Context context, ArrayList<Event> events){
        super(context,0,events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.reuse_qr_list_item,parent,false); //inflate the layout for the list item
        }
        Event event = events.get(position);  // Get the Event object at the current position
        Bitmap qrCode = QRCodeGenerator.generateQRCodeImage(event.getEventID(),512,512);
        // getting the text views
        TextView eventName = view.findViewById(R.id.reuseEventNameItem);
        ImageView qrCodeImage = view.findViewById(R.id.reuseQRcodeItem);
        // populating the views
        eventName.setText("Event Name: "+event.getTitle());
        qrCodeImage.setImageBitmap(qrCode);

        return view;
    }
}
