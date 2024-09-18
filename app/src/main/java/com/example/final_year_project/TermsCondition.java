package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class TermsCondition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_condition); // Set the about layout
    }

    public void toRegister(View view){
        Intent intent = new Intent(this, Register.class);
        ImageButton toHome= findViewById(R.id.register);
        startActivity(intent);
    }

}
