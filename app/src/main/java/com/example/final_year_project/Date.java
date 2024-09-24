package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Date extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date);
    }

    public void toStylist(View view) {
        Intent intent = new Intent(this, Stylist.class);
        startActivity(intent);
    }
}
