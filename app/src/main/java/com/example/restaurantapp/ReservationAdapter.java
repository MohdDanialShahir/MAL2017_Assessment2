package com.example.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {

    Context context;
    ArrayList<Reservation> resList;

    public ReservationAdapter(Context context, ArrayList<Reservation> resList) {
        this.context = context;
        this.resList = resList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_reservation_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reservation res = resList.get(position);
        holder.name.setText(res.getCustomerName());
        holder.date.setText(res.getDate());

        // When "View" is clicked, go to the Details Activity
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReservationDetailsActivity.class);
                intent.putExtra("ID", res.getId());
                intent.putExtra("NAME", res.getCustomerName());
                intent.putExtra("DATE", res.getDate());
                intent.putExtra("TIME", res.getTime());
                intent.putExtra("PAX", res.getPartySize());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;
        Button btnView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvResName);
            date = itemView.findViewById(R.id.tvResDate);
            btnView = itemView.findViewById(R.id.btnViewRes);
        }
    }
}