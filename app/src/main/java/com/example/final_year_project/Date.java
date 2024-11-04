package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Date extends AppCompatActivity {

    private TextView selectedDateTextView;
    private RecyclerView timeSlotRecyclerView;
    private TimeSlotAdapter timeSlotAdapter;
    private List<String> timeSlots;
    private String selectedDate;
    private String stylistName; // To store stylist name
    private String branchName;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date);

        CalendarView calendarView = findViewById(R.id.calendarView);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        timeSlotRecyclerView = findViewById(R.id.timeSlotRecyclerView);

        // Set the minimum date to the current date
        calendarView.setMinDate(System.currentTimeMillis());

        // Set up RecyclerView for time slots
        timeSlots = new ArrayList<>();
        timeSlotAdapter = new TimeSlotAdapter(timeSlots, this, null); // Initialize with null
        timeSlotRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timeSlotRecyclerView.setAdapter(timeSlotAdapter);

        // Fetch the stylist name and branch name from Firestore
        fetchStylistAndBranchName();

        // Handle calendar date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            selectedDateTextView.setText("Selected Date: " + selectedDate);

            // Load available time slots for the selected date
            loadTimeSlots();

            // Update the adapter with the new selected date
            timeSlotAdapter.setSelectedDate(selectedDate);
        });

        // Assuming you have your back button set up
        findViewById(R.id.backToStylist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCurrentUserData();
                finish(); // Close the current activity
            }
        });
    }

    private void fetchStylistAndBranchName() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Get current user's email

        // Fetch stylist data based on user email from the "stylists" collection
        db.collection("stylists")
                .whereEqualTo("userEmail", userEmail) // Query by userEmail
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        stylistName = document.getString("stylistName");
                        branchName = document.getString("branchName");
                    } else {
                        Log.w("FetchStylist", "No stylist data found for the user.");
                    }
                });
    }

    private void loadTimeSlots() {
        // Clear the current list
        timeSlots.clear();

        // Add time slots (hardcoded for now)
        timeSlots.add("9:00 AM - 10:00 AM");
        timeSlots.add("10:00 AM - 11:00 AM");
        timeSlots.add("11:00 AM - 12:00 PM");
        timeSlots.add("1:00 PM - 2:00 PM");
        timeSlots.add("2:00 PM - 3:00 PM");
        timeSlots.add("3:00 PM - 4:00 PM");
        timeSlots.add("4:00 PM - 5:00 PM");
        timeSlots.add("5:00 PM - 6:00 PM");

        // Load booked time slots from Firestore "reservations" collection
        db.collection("reservations")
                .whereEqualTo("date", selectedDate)
                .whereEqualTo("stylistName", stylistName)
                .whereEqualTo("branchName", branchName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> bookedSlots = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            String bookedSlot = document.getString("timeSlot");
                            if (bookedSlot != null) {
                                bookedSlots.add(bookedSlot);
                            }
                        }
                        // Update the adapter with booked slots
                        timeSlotAdapter.setBookedTimeSlots(bookedSlots); // Set booked time slots
                        timeSlotAdapter.notifyDataSetChanged(); // Notify adapter of data change
                    }
                });

        // Notify the adapter to update the RecyclerView
        timeSlotAdapter.notifyDataSetChanged();
    }



    private void deleteCurrentUserData() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Get current user's email
        if (userEmail != null) {
            db.collection("stylists")
                    .whereEqualTo("userEmail", userEmail) // Query for the current user's stylist document
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Delete the document
                                db.collection("stylists").document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Delete", "User data successfully deleted!");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w("Delete", "Error deleting user data", e);
                                        });
                            }
                        } else if (task.isSuccessful()) {
                            Log.w("Delete", "No user data found for deletion");
                        } else {
                            Log.w("Delete", "Error fetching user data", task.getException());
                        }
                    });
        } else {
            Log.w("Delete", "User email is null");
        }
    }



}
