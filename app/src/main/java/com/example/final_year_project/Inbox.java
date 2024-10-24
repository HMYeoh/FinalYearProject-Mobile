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

import androidx.appcompat.app.AppCompatActivity;

public class Inbox extends AppCompatActivity {

    ImageButton openDrawer;
    DrawerLayout drawerLayout;
    ImageButton logoutButton;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<MessageModel> messageList;
    private FirebaseFirestore db;
    private String userId;
    private TextView noMessagesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView userNameTextView = findViewById(R.id.userName);
        noMessagesText = findViewById(R.id.noMessagesText);

        if (currentUser != null) {
            userId = currentUser.getUid(); // Get current user UID
            db = FirebaseFirestore.getInstance();
            setupRecyclerView();
            fetchMessages();
        }

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
        logoutButton = findViewById(R.id.logout);  // Find the logout button

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

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
    }

    private void fetchMessages() {
        db.collection("inbox")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MessageModel message = document.toObject(MessageModel.class);
                            messageList.add(message);
                        }
                        messageAdapter.notifyDataSetChanged();

                        // Check if there are no messages and update the visibility of the TextView
                        if (messageList.isEmpty()) {
                            noMessagesText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noMessagesText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Inbox", "Error getting documents: ", task.getException());
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

    public void toInbox(View view) {
        Intent intent = new Intent(this, Inbox.class);
        TextView toInbox = findViewById(R.id.inbox);
        startActivity(intent);
    }

    // Logout method
    private void logout() {
        FirebaseAuth.getInstance().signOut();  // Sign out from Firebase
        Toast.makeText(Inbox.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Redirect to the login screen
        Intent intent = new Intent(Inbox.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear task to prevent returning back
        startActivity(intent);
        finish();  // Finish the Home activity
    }
}
