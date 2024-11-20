package com.example.final_year_project;

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
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private ArrayList<ServiceModel> serviceList;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ServiceAdapter(ArrayList<ServiceModel> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);  // service_item.xml layout
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceModel service = serviceList.get(position);

        holder.serviceName.setText(service.getServiceName());
        holder.serviceDescription.setText(service.getServiceDetails());
        holder.servicePrice.setText(service.getServicePrice());
        holder.estimatedTime.setText(service.getEstimatedTime());

        // Load service image using Glide
        Glide.with(holder.itemView.getContext())
                .load(service.getServiceImageURL())
                .into(holder.serviceImage);

        // Handle "Book" button click
        holder.bookButton.setOnClickListener(v -> {
            String userEmail = auth.getCurrentUser().getEmail();
            String userId = auth.getCurrentUser().getUid();

            // Fetch the user's name from Firestore
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name"); // Retrieve user's name from Firestore

                            // Prepare booking data
                            Map<String, Object> bookingData = new HashMap<>();
                            bookingData.put("serviceName", service.getServiceName());
                            bookingData.put("serviceDetails", service.getServiceDetails());
                            bookingData.put("servicePrice", service.getServicePrice());
                            bookingData.put("estimatedTime", service.getEstimatedTime());
                            bookingData.put("userEmail", userEmail);
                            bookingData.put("userName", userName);  // Use the retrieved name
                            bookingData.put("userId", userId);  // Add user ID to the booking data

                            // Save booking to Firestore
                            db.collection("bookings")
                                    .add(bookingData)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(holder.itemView.getContext(), "Booking confirmed!", Toast.LENGTH_SHORT).show();

                                        // Navigate to BookingOverview activity
                                        Intent intent = new Intent(holder.itemView.getContext(), BookingOverview.class);
                                        holder.itemView.getContext().startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(holder.itemView.getContext(), "Failed to book service!", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(holder.itemView.getContext(), "Failed to retrieve user details!", Toast.LENGTH_SHORT).show();
                    });
        });
    }


    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName, serviceDescription, servicePrice, estimatedTime;
        ImageView serviceImage;
        Button bookButton;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            estimatedTime = itemView.findViewById(R.id.estimatedTime);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }
}
