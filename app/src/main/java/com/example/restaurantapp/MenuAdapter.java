package com.example.restaurantapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri; // Import Uri
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView; // Import ImageView
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    Context context;
    ArrayList<MenuItem> menuList;
    DBHelper DB;

    public MenuAdapter(Context context, ArrayList<MenuItem> menuList) {
        this.context = context;
        this.menuList = menuList;
        this.DB = new DBHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_menu_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MenuItem item = menuList.get(position);
        holder.name.setText(item.getName());
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

        // Handle Delete
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.deleteMenuItem(item.getName());
                menuList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, menuList.size());
                Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        Button btnDelete;
        ImageView imgFood; // Added ImageView

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvItemName);
            price = itemView.findViewById(R.id.tvItemPrice);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
            imgFood = itemView.findViewById(R.id.imgFoodItem); // Find the Image ID
        }
    }
}
