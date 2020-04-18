package com.example.foodsetgorestaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsetgorestaurant.Common.Common;
import com.example.foodsetgorestaurant.Interface.ItemClickListener;
import com.example.foodsetgorestaurant.ViewHolder.ItemViewHolder;
import com.example.foodsetgorestaurant.model.Item;
import com.example.foodsetgorestaurant.model.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    TextView txtFullName;
    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference itemTable = database.getReference("Items");
    FirebaseRecyclerAdapter<Item, ItemViewHolder> adapter;
    //View

    RecyclerView recyclerItemList;
    Restaurant restaurant;


    FloatingActionButton fab;
    String passedArg;
    public TextView resName, resPrice, resType, resRating;
    public ImageView resLogo;
    MaterialEditText edtName,edtPrice,edtCourse,edtRating,edtType,edtAvailable,edtDiscount,edtkey;
    MaterialButton btnSelect;
    Item newFood;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        RelativeLayout rootLayout=findViewById(R.id.rootLayout);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_menu);


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

                    case R.id.nav_signout:
                        Paper.book().destroy();
                        Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.nav_orders:
                        startActivity(new Intent(getApplicationContext(),
                                OrderDetailsActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        recyclerItemList = findViewById(R.id.recycler_menu);
        recyclerItemList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerItemList.setLayoutManager(layoutManager);
        Paper.init(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFoodDialog();
            }
        });
        loadItemList();
    }

    private void loadItemList() {
        id = Paper.book().read(Common.USER_KEY);
        final FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(itemTable.orderByChild("resid").equalTo(id), Item.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {
    //

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_cardview, parent, false);
                return new ItemViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, final int position, @NonNull final Item model) {
                holder.itemName.setText(model.getName());
                holder.itemPrice.setText("₹" + model.getPrice());
                holder.itemType.setText(model.getCourse());
                holder.itemRating.setText(model.getRating());
                if (model.getType().equals("nveg"))
                    holder.itemLogo.setImageResource(R.drawable.icon_non_veg);

                holder.editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditDialog(adapter.getRef(position).getKey(),model);
                    }
                });

                holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeShipper(adapter.getRef(position).getKey());
                    }
                });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    }
                });
            }
        };
        adapter.startListening();
        recyclerItemList.setAdapter(adapter);

    }

    private void showEditDialog(final String key, Item model) {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Update Food");

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_food_layout,null);

        edtName=add_menu_layout.findViewById(R.id.edtName);
        edtPrice=add_menu_layout.findViewById(R.id.edtPrice);
        edtCourse=add_menu_layout.findViewById(R.id.edtCourse);
        edtDiscount=add_menu_layout.findViewById(R.id.edtDiscount);
        edtRating=add_menu_layout.findViewById(R.id.edtRating);
        edtType=add_menu_layout.findViewById(R.id.edtType);
        edtAvailable=add_menu_layout.findViewById(R.id.edtAvailable);
        edtkey=add_menu_layout.findViewById((R.id.itemid));

        //setdata
        edtName.setText(model.getName());
        edtPrice.setText(model.getPrice());
        edtDiscount.setText(model.getDiscount());
        edtRating.setText(model.getRating());
        edtType.setText(model.getType());
        edtAvailable.setText(model.getAvailable());
        edtCourse.setText(model.getCourse());
        edtkey.setVisibility(View.GONE);


        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Map<String,Object> update=new HashMap<>();
                update.put("name",edtName.getText().toString());
                update.put("price",edtPrice.getText().toString());
                update.put("rating",edtRating.getText().toString());
                update.put("course",edtCourse.getText().toString());
                update.put("discount",edtDiscount.getText().toString());
                update.put("available",edtAvailable.getText().toString());
                update.put("type",edtType.getText().toString());

                itemTable.child(key)
                        .updateChildren(update)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(HomeActivity.this,"Food Item Updated",Toast.LENGTH_SHORT);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this,e.getMessage(),Toast.LENGTH_SHORT);                    }
                });

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


    }

    private void removeShipper(String key) {
        itemTable.child(key)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HomeActivity.this,"Remove Succesful",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void showAddFoodDialog() {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Add New Food");
        //alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_food_layout,null);

        edtName=add_menu_layout.findViewById(R.id.edtName);
        edtPrice=add_menu_layout.findViewById(R.id.edtPrice);
        edtCourse=add_menu_layout.findViewById(R.id.edtCourse);
        edtDiscount=add_menu_layout.findViewById(R.id.edtDiscount);
        edtRating=add_menu_layout.findViewById(R.id.edtRating);
        edtType=add_menu_layout.findViewById(R.id.edtType);
        edtAvailable=add_menu_layout.findViewById(R.id.edtAvailable);
        edtkey=add_menu_layout.findViewById(R.id.itemid);

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        newFood = new Item();
                        newFood.setName(edtName.getText().toString());
                        newFood.setAvailable(edtAvailable.getText().toString());
                        newFood.setCourse(edtCourse.getText().toString());
                        newFood.setDiscount(edtDiscount.getText().toString());
                        newFood.setPrice(edtPrice.getText().toString());
                        newFood.setRating(edtRating.getText().toString());
                        newFood.setType(edtType.getText().toString());
                        newFood.setResid(id);

                        itemTable.child(edtkey.getText().toString())
                                .setValue(newFood)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(HomeActivity.this, "New Food Item Created", Toast.LENGTH_SHORT);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null && data.getData()!=null && resultCode==RESULT_OK)
        {
            saveUri=data.getData();
        }
    }*/
}
