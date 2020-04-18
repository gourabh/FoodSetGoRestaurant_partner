package com.example.foodsetgorestaurant.ViewHolder;



import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgorestaurant.Interface.ItemClickListener;
import com.example.foodsetgorestaurant.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView orderRes, orderId, orderStatus, orderAddress, orderDeliverer, orderTotal, orderTime;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        orderRes = itemView.findViewById(R.id.order_restaurant);
        orderId = itemView.findViewById(R.id.order_id);
        orderStatus = itemView.findViewById(R.id.order_status);
        orderAddress = itemView.findViewById(R.id.order_address);
        orderTotal = itemView.findViewById(R.id.order_total);
        orderDeliverer = itemView.findViewById(R.id.order_delivered_by);
        orderTime = itemView.findViewById(R.id.order_time);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the Action");
        menu.add(0,0,getAdapterPosition(),"Update");
        /*menu.add(0,1,getAdapterPosition(),"Delete");*/

    }
}

