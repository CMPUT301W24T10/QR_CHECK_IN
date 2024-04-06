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
import java.util.List;
import java.util.Map;

public class AttendeesAdapter extends RecyclerView.Adapter<AttendeesAdapter.ViewHolder> {
    private ArrayList<AttendeeCount> mlist;
    private Context context;

    private String[] ids;
    private AttendeeCount[] attendeeCounts;

    public AttendeesAdapter(ArrayList<AttendeeCount> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;

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
       // holder.binding.attendeeName.setText(mlist.get(position).getAttendee());

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}


