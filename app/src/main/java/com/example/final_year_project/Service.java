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

        // Setup button for "Hair Wash & Blow" service
        Button hairWashButton = findViewById(R.id.service2);
        hairWashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHairWashServices();  // Fetch and display "Hair Wash & Blow" services
            }
        });

        // Setup button for "Colouring" service
        Button colouringButton = findViewById(R.id.service3);
        colouringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getColouringServices();  // Fetch and display "Colouring" services
            }
        });

        // Setup button for "Scalp Treatment" service
        Button scalpTreatmentButton = findViewById(R.id.service4);
        scalpTreatmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScalpTreatmentServices();  // Fetch and display "Scalp Treatment" services
            }
        });

        // Setup button for "Hair Treatment" service
        Button hairTreatmentButton = findViewById(R.id.service5);
        hairTreatmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHairTreatmentServices();  // Fetch and display "Hair Treatment" services
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

    // Method to fetch "Hair Wash & Blow" services from Firestore
    private void getHairWashServices() {
        db.collection("services")
                .whereEqualTo("serviceCategories", "Hair Wash & Blow")
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

    // Method to fetch "Colouring" services from Firestore
    private void getColouringServices() {
        db.collection("services")
                .whereEqualTo("serviceCategories", "Colouring")
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

    // Method to fetch "Scalp Treatment" services from Firestore
    private void getScalpTreatmentServices() {
        db.collection("services")
                .whereEqualTo("serviceCategories", "Scalp Treatment")
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

    // Method to fetch "Hair Treatment" services from Firestore
    private void getHairTreatmentServices() {
        db.collection("services")
                .whereEqualTo("serviceCategories", "Hair Treatment")
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

    // Method to logout the user
    private void logout() {
        FirebaseAuth.getInstance().signOut();  // Sign out from Firebase
        Toast.makeText(Service.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Redirect to the login screen
        Intent intent = new Intent(Service.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear task to prevent returning back
        startActivity(intent);
        finish();  // Finish the Service activity
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, Home.class);
        ImageButton toHome = findViewById(R.id.home);
        startActivity(intent);
    }
}
