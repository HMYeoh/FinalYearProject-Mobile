package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView userNameTextView = findViewById(R.id.userName);

        if (currentUser != null) {
            String userId = currentUser.getUid(); // Get current user UID
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Fetch user data from Firestore
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userName = document.getString("name"); // Assuming your field is named "name"
                                userNameTextView.setText(userName != null ? userName : "User Name");
                            } else {
                                Log.d("HomeActivity", "No such user document");
                                userNameTextView.setText("User Name"); // Fallback
                            }
                        } else {
                            Log.d("HomeActivity", "Get failed with ", task.getException());
                            userNameTextView.setText("User Name"); // Fallback
                        }
                    });
        } else {
            Log.d("HomeActivity", "No user is logged in.");
            userNameTextView.setText("User Name"); // Fallback
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.menu);
        logoutButton = findViewById(R.id.logout);
        recyclerView = findViewById(R.id.recyclerView);
        footer = findViewById(R.id.footer);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(serviceList);
        recyclerView.setAdapter(serviceAdapter);

        // Fetch and display all services when the page loads
        getAllServices();

        // Get references to the ImageButtons
        ImageButton menuButton = findViewById(R.id.menu);
        ImageButton logoutButton = findViewById(R.id.logout);

        // Load the animations
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);

        // Apply animations on click for the Menu button
        menuButton.setOnClickListener(view -> {
            view.startAnimation(zoomIn);
            // Perform other actions (e.g., open the drawer)
        });

        // Apply animations on click for the Logout button
        logoutButton.setOnClickListener(view -> {
            view.startAnimation(zoomIn);
            // Perform other actions (e.g., log out)
        });

        // Optionally add zoom-out animation when the button is released
        menuButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    v.startAnimation(zoomOut);
                    break;
            }
            return false;
        });

        logoutButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    v.startAnimation(zoomOut);
                    break;
            }
            return false;
        });

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

        boolean hasBookings = getIntent().getBooleanExtra("HAS_BOOKINGS", false);

        // Show or hide the footer based on the flag
        if (hasBookings) {
            footer.setVisibility(View.VISIBLE);
        } else {
            footer.setVisibility(View.GONE);
        }
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

    public void toBookingOverview(View view) {
        Intent intent = new Intent(this, BookingOverview.class);
        startActivity(intent);
    }

    public void toContactUs(View view) {
        Intent intent = new Intent(this, Contact_us.class);
        TextView toContactUs = findViewById(R.id.contactUs);
        startActivity(intent);
    }

    public void toService(View view) {
        Intent intent = new Intent(this, Service.class);
        TextView toService = findViewById(R.id.service);
        startActivity(intent);
    }

    public void toBookingHistory(View view) {
        Intent intent = new Intent(this, BookingHistory.class);
        TextView toBookingHistory = findViewById(R.id.bookingHistory);
        startActivity(intent);
    }
}
