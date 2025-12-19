package com.example.restaurantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Locale;

public class GuestMenuAdapter extends RecyclerView.Adapter<GuestMenuAdapter.MyViewHolder> {

    Context context;
    ArrayList<MenuItem> menuList;

    public GuestMenuAdapter(Context context, ArrayList<MenuItem> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_guest_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MenuItem item = menuList.get(position);
        holder.name.setText(item.getName());
        // Fixed string concatenation and locale warnings
        holder.price.setText(String.format(Locale.US, "$%.2f", item.getPrice()));

        // --- FIXED IMAGE LOADING LOGIC (Using Glide) ---
        String imageUri = item.getImageUri();

        // 1. Clear any previous color filters (tints)
        holder.imgFood.setColorFilter(null);

        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(context)
                    .load(imageUri) // Handles both Web URLs and Local Paths
                    .placeholder(android.R.drawable.ic_menu_gallery) // Loading placeholder
                    .error(android.R.drawable.ic_delete) // Error placeholder
                    .into(holder.imgFood);
        } else {
            holder.imgFood.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView imgFood;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvGuestItemName);
            price = itemView.findViewById(R.id.tvGuestItemPrice);
            imgFood = itemView.findViewById(R.id.imgGuestFoodItem);
        }
    }
}
