package com.example.qr_check_in;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_check_in.ModelClasses.AttendeeCount;
import com.example.qr_check_in.databinding.AttendeeRowBinding;

import java.util.ArrayList;
import java.util.Objects;

public class AttendeesAdapter extends RecyclerView.Adapter<AttendeesAdapter.ViewHolder> {
    private ArrayList<String> mlist;
    private Context context;

    private ArrayList<String> ids;
    private Integer attendeeCounts;
    private String deviceIdCurrent;

    private ArrayList<AttendeeCount> sharedList;

    public AttendeesAdapter(ArrayList<String> mlist, ArrayList<String> deviceId, Context context, Integer countAttendeeLogin, String deviceIdCurrent, ArrayList<AttendeeCount> sharedList) {
        this.mlist = mlist;
        this.ids = deviceId;
        this.context = context;
        this.attendeeCounts = countAttendeeLogin;
        this.deviceIdCurrent= deviceIdCurrent;
        this.sharedList = sharedList;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AttendeeRowBinding binding;

        public ViewHolder(@NonNull AttendeeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public AttendeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AttendeeRowBinding binding = AttendeeRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeesAdapter.ViewHolder holder, int position) {
        holder.binding.attendeeName.setText(mlist.get(position));
        Log.d("kjskdjskfj", "onBindViewHolder: "+deviceIdCurrent);

        for(int i=0;i<sharedList.size();i++)
        {
            if (Objects.equals(sharedList.get(i).getdeviceId(), ids.get(position)))
            {
                holder.binding.count.setText("Number of times Login "+sharedList.get(i).getNumberOfTimesLogin());
            }
        }
//        if (ids.get(position).equals(deviceIdCurrent))
//        {
//            holder.binding.count.setText("Number of times Login "+attendeeCounts.toString());
//
//        }


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}


