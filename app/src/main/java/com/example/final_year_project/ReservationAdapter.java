package com.example.final_year_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<ReservationModel> reservationList;

    public ReservationAdapter(List<ReservationModel> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false); // Updated to use reservation_item.xml
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        ReservationModel reservation = reservationList.get(position);
        holder.dateTextView.setText(reservation.getDate());
        holder.timeSlotTextView.setText(reservation.getTimeSlot());
        holder.stylistNameTextView.setText(reservation.getStylistName());

        // Bind the items (previously services)
        StringBuilder itemDetails = new StringBuilder();
        for (ItemModel item : reservation.getServices()) {  // Updated to use ItemModel
            itemDetails.append(item.getServiceName())
                    .append(" - ")
                    .append(item.getServicePrice())
                    .append("\n");
        }
        holder.itemsTextView.setText(itemDetails.toString());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeSlotTextView, stylistNameTextView, itemsTextView;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date);
            timeSlotTextView = itemView.findViewById(R.id.timeSlot);
            stylistNameTextView = itemView.findViewById(R.id.stylistName);
            itemsTextView = itemView.findViewById(R.id.items);  // Updated to use itemsTextView for ItemModel
        }
    }
}
