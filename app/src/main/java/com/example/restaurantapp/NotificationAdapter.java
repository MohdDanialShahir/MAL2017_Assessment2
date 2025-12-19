package com.example.restaurantapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;
    ArrayList<Notification> notifList;

    public NotificationAdapter(Context context, ArrayList<Notification> notifList) {
        this.context = context;
        this.notifList = notifList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notification notif = notifList.get(position);
        holder.message.setText(notif.getMessage());
        
        if (notif.getIsRead() == 0) {
            holder.status.setText("Unread");
            holder.status.setTextColor(Color.RED);
        } else {
            holder.status.setText("Read");
            holder.status.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tvNotifMessage);
            status = itemView.findViewById(R.id.tvNotifStatus);
        }
    }
}
