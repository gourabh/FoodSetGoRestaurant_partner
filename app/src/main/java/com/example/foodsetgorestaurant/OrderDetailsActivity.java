package com.example.foodsetgorestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodsetgorestaurant.Common.Common;
import com.example.foodsetgorestaurant.Interface.ItemClickListener;
import com.example.foodsetgorestaurant.model.Item;
import com.example.foodsetgorestaurant.model.Request;
import com.example.foodsetgorestaurant.model.Restaurant;
import com.example.foodsetgorestaurant.ViewHolder.ItemViewHolder;
import com.example.foodsetgorestaurant.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.NumberFormat;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailsActivity extends AppCompatActivity {//extends Fragment

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;


    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    Restaurant restaurant;
    Request request;

    FirebaseDatabase database;
    DatabaseReference resTable, requests, foodList;
    MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_order_details);



        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu:
                        startActivity(new Intent(getApplicationContext(),
                                HomeActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_orders:
                        startActivity(new Intent(getApplicationContext(),
                                OrderDetailsActivity.class));
                        finish();;
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_signout:
                        Paper.book().destroy();
                        Intent intent=new Intent(OrderDetailsActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Request");
        DatabaseReference db = requests;
        foodList = (DatabaseReference) database.getReference().child("Request");
        resTable = database.getReference("Restaurant");

        recyclerView = findViewById(R.id.orders_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final String id=Paper.book().read(Common.USER_KEY);
        String name=Paper.book().read(Common.USER_NAME);
        Log.d("TAG","ResName"+name);
        loadOrders(name);
    }

    private void loadOrders(String id)
    {

        //.orderByChild("itemID").equalTo(id)
        final FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(requests.orderByChild("resName").equalTo(id), Request.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position, @NonNull Request model) {
                holder.orderId.setText(adapter.getRef(position).getKey());
                holder.orderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                holder.orderAddress.setText(model.getAddress());
                holder.orderTotal.setText(model.getTotal());
                holder.orderDeliverer.setText(model.getDelivery());

                String time = model.getDate_time();
                time = time.substring(0,8);
                String apm = "am";
                int hr = Integer.parseInt(time.substring(0,2));
                if (hr>12) {
                    hr = hr - 12;
                    apm = "pm";
                }
                time = time.replace(time.substring(0,2), String.valueOf(hr));
                time = time +" " + apm;
                holder.orderTime.setText(time);

                foodList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataSnapshot ds = dataSnapshot.child(adapter.getRef(position).getKey());
                        DataSnapshot productsSnapshot = ds.child("foods");
                        DataSnapshot productSnapshot = productsSnapshot.child("0");
                        final String id = productSnapshot.child("itemID").getValue(String.class);
                        resTable.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                restaurant = dataSnapshot.child(id).getValue(Restaurant.class);
                                holder.orderRes.setText(restaurant.getName());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.orderdetailscardview, parent, false);
                return new OrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE))
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        /*else if ((item.getTitle().equals(Common.DELETE)))
            deleteOrder(adapter.getRef(item.getOrder()).getKey());*/

        return super.onContextItemSelected(item);
    }

    /*private void deleteOrder(String key) {
        requests.child(key).removeValue();
    }*/

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(OrderDetailsActivity.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please Choose Status");

        LayoutInflater inflater=this.getLayoutInflater();
        final View view=inflater.inflate(R.layout.update_order_layout,null);
        spinner=(MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","Accepted","Rejected");

        alertDialog.setView(view);
        final String localkey=key;

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int i=spinner.getSelectedIndex();
                if (i==2)
                    i=i+2;
                item.setStatus(String.valueOf(i));

                requests.child(localkey).setValue(item);

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(OrderDetailsActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }

}



