package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText emailInput;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_linear);
        //setContentView(R.layout.activity_main_grid);
        // setContentView(R.layout.activity_main_relative);
         setContentView(R.layout.activity_main_lab3);
         login = findViewById(R.id.Lab3Button1);
         emailInput =  findViewById(R.id.emailInput);
         displayEmail(findViewById(R.id.emailInput));
         login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                startProfileActivity(emailInput.getText().toString());
             }
         });


    }
    @Override
    protected void onPause() {
        super.onPause();
        saveEmail(findViewById(R.id.emailInput));

    }


    public void saveEmail(View view) {
        SharedPreferences sharedPref = getSharedPreferences("emailInfo",  Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", emailInput.getText().toString());
        editor.apply();
    }
    public void displayEmail(View view) {
        SharedPreferences sharedPref = getSharedPreferences("emailInfo",  Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        emailInput.setText(email);

    }

    public void startProfileActivity(CharSequence email) {
        Intent profileActivity = new Intent(this, ProfileActivity.class);
        profileActivity.putExtra("email", email);
        startActivity(profileActivity);
    }



}
