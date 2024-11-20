package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about); // Set the about layout
    }

    public void toHome(View view){
        Intent intent = new Intent(this, Home.class);
        ImageButton toHome= findViewById(R.id.home);
        startActivity(intent);
    }
}
