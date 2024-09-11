package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    ImageButton openDrawer;
    DrawerLayout drawerLayout;
    ImageButton logoutButton;  // Add a reference for the logout button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);
        logoutButton = findViewById(R.id.logout);  // Find the logout button

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

        // Set the OnClickListener for the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();  // Call the logout method
            }
        });
    }

    public void toAbout(View view) {
        Intent intent = new Intent(this, About.class);
        TextView toAbout = findViewById(R.id.about);
        startActivity(intent);
    }

    public void toProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        TextView toProfile = findViewById(R.id.profile);
        startActivity(intent);
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, Home.class);
        ImageButton toHome = findViewById(R.id.home);
        startActivity(intent);
    }

    public void toContactUs(View view) {
        Intent intent = new Intent(this, Contact_us.class);
        TextView toContactUs = findViewById(R.id.contactUs);
        startActivity(intent);
    }

    // Logout method
    private void logout() {
        FirebaseAuth.getInstance().signOut();  // Sign out from Firebase
        Toast.makeText(Home.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Redirect to the login screen
        Intent intent = new Intent(Home.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear task to prevent returning back
        startActivity(intent);
        finish();  // Finish the Home activity
    }
}
