package com.example.qr_check_in.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_check_in.data.Notification;
import com.example.qr_check_in.databinding.NotificationRowBinding;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<Notification> mlist;
    private Context context;

    public NotificationAdapter(ArrayList<Notification> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private NotificationRowBinding binding;

        public ViewHolder(@NonNull NotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationRowBinding binding = NotificationRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        holder.binding.NotificationTitle.setText(mlist.get(position).getNotificationTitle());
        holder.binding.Notification.setText(mlist.get(position).getNotification());
        holder.binding.date.setText(mlist.get(position).getDateandTime());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}


