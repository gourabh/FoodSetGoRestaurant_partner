package com.example.foodsetgorestaurant.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgorestaurant.Interface.ItemClickListener;
import com.example.foodsetgorestaurant.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView resName, resPrice, resType, resRating,txtMenuName;
    public ImageView resLogo,imageView;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        //txtMenuName=(TextView)itemView.findViewById(R.id.menu_name);
        //imageView=(ImageView)itemView.findViewById(R.id.menu_image);

       /* resName = itemView.findViewById(R.id.res_name);
        resPrice = itemView.findViewById(R.id.res_price);
        resType = itemView.findViewById(R.id.res_type);
        resLogo = itemView.findViewById(R.id.res_logo);
        resRating = itemView.findViewById(R.id.res_rating);
*/
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
