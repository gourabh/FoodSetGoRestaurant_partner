package com.example.foodsetgorestaurant;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodsetgorestaurant.Common.Common;
import com.example.foodsetgorestaurant.model.Restaurant;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.SharedPreferences;

import io.paperdb.Paper;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Fragment representing the login screen for FoodSetGo.
 */


public class LoginActivity extends Fragment {

    TextView name;
    TextView email;



    @Override
    public View onCreateView(


            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.login_activity, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final EditText phoneEditText = view.findViewById(R.id.phone_edit_text);
        MaterialButton loginButton = view.findViewById(R.id.login_button);


        //Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference restaurantTable = database.getReference("Restaurant");
        Paper.init(getContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this.getContext());
                mDialog.setMessage("Please wait...");
                mDialog.show();

                restaurantTable.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("TAG", phoneEditText.getText().toString());

                        if (dataSnapshot.child(phoneEditText.getText().toString()).exists())
                        {
                            Log.d("TAG", "Inside if");
                            mDialog.dismiss();
                            Restaurant restaurant = dataSnapshot.child(phoneEditText.getText().toString()).getValue(Restaurant.class);

                            if (restaurant.getPassword().equals(passwordEditText.getText().toString()))
                            {

                                Intent i = new Intent(LoginActivity.super.getActivity(), HomeActivity.class);
                                Common.currentUser = restaurant;
                                Paper.book().write(Common.USER_KEY, phoneEditText.getText().toString());
                                Paper.book().write(Common.USER_NAME, restaurant.getName());
                                Toast.makeText(getContext(), "Logged In", Toast.LENGTH_SHORT).show();
                                startActivity(i);
                                getActivity().finish();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(getContext() , "Restaurant not registered. Please Signup.", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        return view;
    }
}