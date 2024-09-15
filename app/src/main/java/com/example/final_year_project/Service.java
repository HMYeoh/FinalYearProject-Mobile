package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class Service extends AppCompatActivity {

    ImageButton openDrawer, logoutButton;
    DrawerLayout drawerLayout;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    ServiceAdapter serviceAdapter;
    ArrayList<ServiceModel> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service);

        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);
        logoutButton = findViewById(R.id.logout);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(serviceList);
        recyclerView.setAdapter(serviceAdapter);

        // Fetch and display all services when the page loads
        getAllServices();

        // Open/Close Drawer functionality
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

        // Set OnClickListener for the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();  // Call the logout method
            }
        });

        // Setup button for "Hair Cut" service
        Button haircutButton = findViewById(R.id.service1);
        haircutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHairCutServices();  // Fetch and display "Hair Cut" services
            }
        });
    }

    // Method to fetch and display all services
    private void getAllServices() {
        db.collection("services")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        serviceList.clear();  // Clear previous data
                        for (DocumentSnapshot document : task.getResult()) {
                            ServiceModel service = document.toObject(ServiceModel.class);
                            serviceList.add(service);  // Add each service to the list
                        }
                        serviceAdapter.notifyDataSetChanged();  // Notify adapter of data changes
                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    // Method to fetch "Hair Cut" services from Firestore
    private void getHairCutServices() {
        db.collection("services")
                .whereEqualTo("serviceCategories", "Hair Cut")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        serviceList.clear();  // Clear the previous data before adding new data
                        for (DocumentSnapshot document : task.getResult()) {
                            ServiceModel service = document.toObject(ServiceModel.class);
                            serviceList.add(service);  // Add each service to the list
                        }
                        serviceAdapter.notifyDataSetChanged();  // Notify adapter of data changes
                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    // Method to navigate to the About page
    public void toAbout(View view) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    // Method to navigate to the Profile page
    public void toProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    // Method to navigate to the Home page
    public void toHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    // Method to navigate to the Contact Us page
    public void toContactUs(View view) {
        Intent intent = new Intent(this, Contact_us.class);
        startActivity(intent);
    }

    // Method to navigate to the Service page
    public void toService(View view) {
        Intent intent = new Intent(this, Service.class);
        startActivity(intent);
    }

    // Logout method
    private void logout() {
        FirebaseAuth.getInstance().signOut();  // Sign out from Firebase
        Toast.makeText(Service.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Redirect to the login screen
        Intent intent = new Intent(Service.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear task to prevent returning back
        startActivity(intent);
        finish();  // Finish the Service activity
    }
}
