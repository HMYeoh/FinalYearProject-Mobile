package com.example.final_year_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<BookingModel> bookingList;

    public BookingAdapter(List<BookingModel> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);
        holder.serviceName.setText(booking.getServiceName());
        holder.servicePrice.setText(String.format(booking.getServicePrice()));
        holder.serviceDetails.setText(booking.getServiceDetails());

        // Set up clear button click listener
        holder.clearService.setOnClickListener(v -> {
            // Call the delete method with the booking details
            deleteBookingFromFirestore(booking, position);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    // Method to delete booking from Firestore
    private void deleteBookingFromFirestore(BookingModel booking, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("bookings")
                .whereEqualTo("serviceName", booking.getServiceName())
                .whereEqualTo("servicePrice", booking.getServicePrice())
                .whereEqualTo("serviceDetails", booking.getServiceDetails())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Get the document ID to delete it
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("bookings").document(document.getId()).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Remove the item from the list and notify adapter
                                        bookingList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, bookingList.size());
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure (optional: show a toast or log the error)
                                    });
                        }
                    }
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, servicePrice, serviceDetails;
        ImageButton clearService; // Button to clear service

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceDetails = itemView.findViewById(R.id.serviceDetails);
            clearService = itemView.findViewById(R.id.clearService); // Reference to the clear button
        }
    }
}
