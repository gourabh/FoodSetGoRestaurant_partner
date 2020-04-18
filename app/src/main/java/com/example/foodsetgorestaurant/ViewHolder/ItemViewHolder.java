package com.example.foodsetgorestaurant.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgorestaurant.Interface.ItemClickListener;
import com.example.foodsetgorestaurant.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemName, itemPrice, itemType, itemRating, itemCount;
    public ImageView itemLogo;
    public Button editBtn, removeBtn;

    private ItemClickListener itemClickListener;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        itemName = itemView.findViewById(R.id.item_name);
        itemPrice = itemView.findViewById(R.id.item_price);
        itemType = itemView.findViewById(R.id.item_type);
        itemLogo = itemView.findViewById(R.id.item_logo);
        itemRating = itemView.findViewById(R.id.item_rating);
        editBtn=itemView.findViewById(R.id.editBtn);
        removeBtn=itemView.findViewById(R.id.removeBtn);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
