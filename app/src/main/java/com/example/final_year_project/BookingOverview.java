package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class BookingOverview extends AppCompatActivity {

    ImageButton openDrawer;
    DrawerLayout drawerLayout;
    ImageButton logoutButton;
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<BookingModel> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_overview);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList);
        recyclerView.setAdapter(bookingAdapter);

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

        openDrawer.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerMenu))) {
                drawerLayout.closeDrawer(findViewById(R.id.drawerMenu));
            } else {
                drawerLayout.openDrawer(findViewById(R.id.drawerMenu));
            }
        });

        logoutButton.setOnClickListener(v -> logout());

        loadBookings();
    }

    private void loadBookings() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("bookings").whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String serviceName = document.getString("serviceName");
                            String servicePrice = document.getString("servicePrice");
                            String serviceDetails = document.getString("serviceDetails");

                            BookingModel booking = new BookingModel(serviceName, servicePrice, serviceDetails);
                            bookingList.add(booking);
                        }
                        bookingAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(BookingOverview.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void toAbout(View view) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    public void toProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void toContactUs(View view) {
        Intent intent = new Intent(this, Contact_us.class);
        startActivity(intent);
    }

    public void toService(View view) {
        Intent intent = new Intent(this, Service.class);
        intent.putExtra("HAS_BOOKINGS", !bookingList.isEmpty());
        startActivity(intent);
    }

    public void toBookingHistory(View view) {
        Intent intent = new Intent(this, BookingHistory.class);
        TextView toBookingHistory = findViewById(R.id.bookingHistory);
        startActivity(intent);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(BookingOverview.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(BookingOverview.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void toStylist(View view) {
        Intent intent = new Intent(this, Stylist.class);
        startActivity(intent);
    }
}
