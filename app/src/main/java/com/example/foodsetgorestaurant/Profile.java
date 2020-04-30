package com.example.foodsetgorestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodsetgorestaurant.Common.Common;
import com.example.foodsetgorestaurant.model.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

/**
 * Fragment representing the profile screen for FoodSetGo Restaurant.
 */
public class Profile extends AppCompatActivity {

    public TextView a,b,c,d,e,f,g,h;
    public ImageView resLogo;
    Restaurant restaurant;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference reff = database.getReference("Restaurant");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        a=(TextView)findViewById(R.id.tv_name);
        b=(TextView)findViewById(R.id.tv_email);
        c=(TextView)findViewById(R.id.tv_phone);
        d=(TextView)findViewById(R.id.tv_address);
        e=(TextView)findViewById(R.id.tv_price);
        f=(TextView)findViewById(R.id.tv_rating);
        g=(TextView)findViewById(R.id.tv_type);
        h=(TextView)findViewById(R.id.tv_available);
        resLogo=(ImageView)findViewById(R.id.res_logo);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_menu:
                        startActivity(new Intent(getApplicationContext(),
                                HomeActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_signout:
                        Paper.book().destroy();
                        Intent intent=new Intent(Profile.this,MainActivity.class);
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



        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                restaurant = dataSnapshot.child((String) Paper.book().read(Common.USER_KEY)).getValue(Restaurant.class);

                a.setText(restaurant.getName());
                b.setText(restaurant.getEmail());
                c.setText(restaurant.getMobile().toString());
                d.setText(restaurant.getAddress());
                e.setText(restaurant.getPrice());
                f.setText(restaurant.getRating().toString());
                g.setText(restaurant.getType());
                h.setText(Common.convertCodeToAvailable(restaurant.getAvailable()));
                Picasso.get().load(restaurant.getImg()).into(resLogo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
