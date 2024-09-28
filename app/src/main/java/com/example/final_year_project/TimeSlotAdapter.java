package com.example.final_year_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    private List<String> timeSlotList;
    private Context context;
    private String selectedDate;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public TimeSlotAdapter(List<String> timeSlotList, Context context, String selectedDate) {
        this.timeSlotList = timeSlotList;
        this.context = context;
        this.selectedDate = selectedDate;
        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        db = FirebaseFirestore.getInstance(); // Initialize FirebaseFirestore
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String timeSlot = timeSlotList.get(position);
        holder.timeSlotTextView.setText(timeSlot);

        holder.bookSlotButton.setOnClickListener(v -> {
            // Fetch the stylist data and save the reservation
            fetchStylistData(timeSlot);
        });
    }

    @Override
    public int getItemCount() {
        return timeSlotList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeSlotTextView;
        Button bookSlotButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlotTextView = itemView.findViewById(R.id.timeSlotTextView);
            bookSlotButton = itemView.findViewById(R.id.bookSlotButton);
        }
    }

    private void fetchStylistData(String timeSlot) {
        String userEmail = mAuth.getCurrentUser().getEmail(); // Get current user's email

        // Fetch stylist data based on user email from the "stylists" collection
        db.collection("stylists")
                .whereEqualTo("userEmail", userEmail) // Query by userEmail
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Extract stylist data
                                String stylistName = document.getString("stylistName");
                                List<Map<String, String>> services = (List<Map<String, String>>) document.get("services");

                                // Fetch user name and save reservation
                                fetchUserNameAndSaveReservation(userEmail, selectedDate, timeSlot, stylistName, services);
                            }
                        } else {
                            Toast.makeText(context, "Stylist data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Error fetching stylist data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserNameAndSaveReservation(String userEmail, String date, String timeSlot, String stylistName, List<Map<String, String>> services) {
        db.collection("users")
                .whereEqualTo("email", userEmail) // Assuming you have the user email stored in the "users" collection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Extract user name and ID
                                String userName = document.getString("name"); // Assuming the name field is "name"
                                String userId = document.getId(); // Get the document ID which is the user ID

                                // Save reservation with stylist and user data
                                saveReservation(date, timeSlot, stylistName, userEmail, userName, userId, services);
                            }
                        } else {
                            Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveReservation(String date, String timeSlot, String stylistName, String userEmail, String userName, String userId, List<Map<String, String>> services) {
        // Check if the stylist is already booked for the selected date and time slot
        db.collection("reservations")
                .whereEqualTo("date", date)
                .whereEqualTo("timeSlot", timeSlot)
                .whereEqualTo("stylistName", stylistName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Stylist is already booked for this time slot, show error message
                            Toast.makeText(context, "This time slot is already booked for the selected stylist.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Stylist is not booked, proceed with reservation
                            Map<String, Object> reservation = new HashMap<>();
                            reservation.put("date", date);
                            reservation.put("timeSlot", timeSlot);
                            reservation.put("stylistName", stylistName);
                            reservation.put("userEmail", userEmail);
                            reservation.put("userName", userName); // Add the user name to the reservation
                            reservation.put("userId", userId); // Add the user ID to the reservation
                            reservation.put("services", services); // Store the services as an array
                            reservation.put("createdAt", FieldValue.serverTimestamp()); // Add server timestamp

                            // Add the reservation to the "reservations" collection
                            db.collection("reservations")
                                    .add(reservation)
                                    .addOnSuccessListener(documentReference -> {
                                        // Show a success message
                                        Toast.makeText(context, "Reservation Successful!", Toast.LENGTH_SHORT).show();

                                        // Delete user data from "stylists" and "bookings" collections
                                        deleteUserDataFromStylists(userEmail);
                                        deleteUserDataFromBookings(userEmail);

                                        // Navigate to Home.java after successful reservation
                                        Intent intent = new Intent(context, Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Ensure intent works from adapter
                                        context.startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Show an error message if the reservation fails
                                        Toast.makeText(context, "Error booking reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(context, "Error checking for existing reservations", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    // Delete user data from the "stylists" collection based on email
    private void deleteUserDataFromStylists(String userEmail) {
        db.collection("stylists")
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            db.collection("stylists").document(document.getId()).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Optionally, show a message or log deletion success
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Error deleting stylist data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(context, "Error fetching stylist data for deletion", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Delete user data from the "bookings" collection based on email
    private void deleteUserDataFromBookings(String userEmail) {
        db.collection("bookings")
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            db.collection("bookings").document(document.getId()).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Optionally, show a message or log deletion success
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Error deleting booking data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(context, "Error fetching booking data for deletion", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
