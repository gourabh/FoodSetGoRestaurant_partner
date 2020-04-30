package com.example.foodsetgorestaurant;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsetgorestaurant.ViewHolder.CartViewHolder;
import com.example.foodsetgorestaurant.model.Order;
import com.example.foodsetgorestaurant.model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * Fragment representing the Itemised Order screen for FoodSetGo Restaurant.
 */
public class OrderDetailsItemClicked extends AppCompatActivity {

    //Firebase
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference reqTable = database.getReference("Request");
    DatabaseReference foodList;
    public Button confirmBtn, cancelBtn;

    Locale locale = new Locale("en", "IN");
    final NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

    RecyclerView recyclerView;
    Request request;

    FirebaseRecyclerAdapter<Order, CartViewHolder> adapter;

    String passedArg;
    Double billdiscount=0.0, billTotal=0.0;

    public TextView  orderId, address, status, date_time, total, delivery_charge, itemTotal, discount, taxes, delivery_man;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_item_clicked);

        orderId = findViewById(R.id.order_id);
        address = findViewById(R.id.order_address);
        status = findViewById(R.id.order_status);
        total = findViewById(R.id.total_price);
        date_time = findViewById(R.id.order_placed_time);
        delivery_charge  = findViewById(R.id.item_delivery);
        itemTotal = findViewById(R.id.item_total);
        discount = findViewById(R.id.discount);
        taxes = findViewById(R.id.taxes);
        delivery_man = findViewById(R.id.order_delivered_by);
        /*confirmBtn=findViewById(R.id.confirmBtn);
        cancelBtn=findViewById(R.id.cancelBtn);*/
        recyclerView = findViewById(R.id.order_details_clicked_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent()!=null)
            passedArg = getIntent().getStringExtra("arg");

        DatabaseReference db = reqTable;
        foodList = (DatabaseReference) database.getReference().child("Request");
        Query query = foodList.child(passedArg);

        loadItemList(passedArg);

        reqTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                request = dataSnapshot.child(passedArg).getValue(Request.class);
                orderId.setText(passedArg);
                address.setText(request.getAddress());

                status.setText(convertCodeToStatus(request.getStatus()));
                date_time.setText(request.getDate_time());
                total.setText(request.getTotal());
                delivery_charge.setText(fmt.format(25));
                itemTotal.setText(fmt.format(billTotal));
                taxes.setText(fmt.format(billTotal*0.05));
                discount.setText("- "+fmt.format(billdiscount));
                delivery_man.setText(request.getDelivery());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        /*confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.setStatus(String.valueOf(1));
                Map<String,Object> update=new HashMap<>();
                update.put("status",String.valueOf(1));

             *//*   reqTable.child()
                        .updateChildren(update)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(OrderDetailsItemClicked.this, "Order Accepted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetailsItemClicked.this,e.getMessage(),Toast.LENGTH_SHORT); *//*                   }
                });

            }
        });*/
        /*cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.setStatus(String.valueOf(4));
                Toast.makeText(OrderDetailsItemClicked.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
            }
        });*/



    }

    private void loadItemList(final String passedArg){

        foodList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(passedArg);
                DataSnapshot productsSnapshot = ds.child("foods");

                for (DataSnapshot productSnapshot: productsSnapshot.getChildren()) {
                    billdiscount += Integer.parseInt((productSnapshot.child("discount").getValue(String.class)))
                            *Integer.parseInt((productSnapshot.child("price").getValue(String.class)))
                            *Integer.parseInt((productSnapshot.child("quantity").getValue(String.class)))
                            *0.01;
                    billTotal += Integer.parseInt((productSnapshot.child("quantity").getValue(String.class)))
                            *Integer.parseInt((productSnapshot.child("price").getValue(String.class)));
                }

                final FirebaseRecyclerOptions<Order> options =
                        new FirebaseRecyclerOptions.Builder<Order>()
                                .setQuery(productsSnapshot.getRef().orderByKey(), Order.class)
                                .build();

                adapter = new FirebaseRecyclerAdapter<Order, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Order model) {
                        holder.itemName.setText(model.getName());
                        String qty = model.getQuantity();
                        holder.itemCount.setText(qty);
                        String pri = model.getPrice();
                        holder.itemPrice.setText("₹"+pri);
                        int sum = Integer.valueOf(qty) * Integer.valueOf(pri);
                        holder.totalPrice.setText(fmt.format(sum));

                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.order_details_item_clicked_cardview, parent, false);
                        return new CartViewHolder(view);
                    }
                };
                adapter.startListening();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

    private String  convertCodeToStatus(String status){
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Accepted";
        else if (status.equals("2"))
            return "On the way";
        else if (status.equals("3"))
            return "Delivered";
        else
            return "Rejected";
    }
}
