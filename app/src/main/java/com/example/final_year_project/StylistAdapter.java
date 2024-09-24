package com.example.final_year_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StylistAdapter extends RecyclerView.Adapter<StylistAdapter.ViewHolder> {

    private List<StylistModel> staffList;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private Context context;

    public StylistAdapter(List<StylistModel> staffList, Context context) {
        this.staffList = staffList;
        this.firestore = FirebaseFirestore.getInstance(); // Initialize Firestore
        this.auth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth to get current user
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stylist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StylistModel stylist = staffList.get(position);
        holder.username.setText(stylist.getUsername());

        // Load profile picture using Glide
        Glide.with(holder.itemView.getContext())
                .load(stylist.getProfilePictureURL())
                .circleCrop()
                .into(holder.profilePicture);

        // Handle the "Book" button click
        holder.bookButton.setOnClickListener(v -> {
            // Get current user's email
            String currentUserEmail = auth.getCurrentUser().getEmail();

            // Fetch the current user's name from Firestore
            firestore.collection("users")
                    .whereEqualTo("email", currentUserEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String currentUserName = document.getString("name"); // Fetch name from Firestore

                            // Get reference to user's bookings in the "bookings" collection
                            CollectionReference bookingsRef = firestore.collection("bookings");

                            // Fetch the services booked by the current user
                            bookingsRef.whereEqualTo("userEmail", currentUserEmail)
                                    .get()
                                    .addOnSuccessListener(bookingsQueryDocumentSnapshots -> {
                                        if (!bookingsQueryDocumentSnapshots.isEmpty()) {
                                            // Prepare booking details
                                            List<BookingDetail> bookingDetailsList = new ArrayList<>();

                                            // Add booked services to the list
                                            for (QueryDocumentSnapshot bookingDocument : bookingsQueryDocumentSnapshots) {
                                                BookingDetail bookingDetail = new BookingDetail(
                                                        bookingDocument.getString("serviceName"),
                                                        bookingDocument.getString("servicePrice"),
                                                        bookingDocument.getString("serviceDetails"),
                                                        bookingDocument.getString("estimatedTime")
                                                );
                                                bookingDetailsList.add(bookingDetail);
                                            }

                                            // Create a StylistBookingModel with the services
                                            StylistBookingModel stylistBooking = new StylistBookingModel(
                                                    currentUserEmail,
                                                    currentUserName, // Use the retrieved user name
                                                    stylist.getUsername(),
                                                    stylist.getProfilePictureURL(),
                                                    bookingDetailsList
                                            );

                                            // Save to the "stylists" collection
                                            firestore.collection("stylists")
                                                    .add(stylistBooking)
                                                    .addOnSuccessListener(documentReference -> {
                                                        // Navigate to Date activity
                                                        Intent intent = new Intent(context, Date.class);
                                                        context.startActivity(intent);
                                                        Toast.makeText(context, "Stylist booked successfully!", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(context, "Failed to book stylist", Toast.LENGTH_SHORT).show();
                                                    });
                                        } else {
                                            Toast.makeText(context, "No services booked!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Error fetching bookings!", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(context, "User details not found!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error fetching user details!", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView profilePicture;
        Button bookButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }
}
