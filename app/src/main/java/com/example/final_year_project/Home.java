package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.drawerlayout.widget.DrawerLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    ImageButton openDrawer;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);

        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerMenu))) {
                    drawerLayout.closeDrawer(findViewById(R.id.drawerMenu));
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.drawerMenu));
                }
            }
        });


    }

    public void toAbout(View view){
        Intent intent = new Intent(this, About.class);
        TextView toAbout= findViewById(R.id.about);
        startActivity(intent);
    }

    public void toProfile(View view){
        Intent intent = new Intent(this, Profile.class);
        TextView toProfile= findViewById(R.id.profile);
        startActivity(intent);
    }

    public void toHome(View view){
        Intent intent = new Intent(this, Home.class);
        ImageButton toHome= findViewById(R.id.home);
        startActivity(intent);
    }

    public void toContactUs(View view){
        Intent intent = new Intent(this, Contact_us.class);
        TextView toContactUs= findViewById(R.id.contactUs);
        startActivity(intent);
    }
}
