package com.example.final_year_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;  // Add Glide dependency for image loading
import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private ArrayList<ServiceModel> serviceList;

    public ServiceAdapter(ArrayList<ServiceModel> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceModel service = serviceList.get(position);
        holder.serviceName.setText(service.getServiceName());
        holder.serviceDescription.setText(service.getServiceDetails());
        holder.servicePrice.setText(service.getServicePrice());
        holder.estimatedTime.setText(service.getEstimatedTime());

        // Load the image using Glide
        Glide.with(holder.itemView.getContext())
                .load(service.getServiceImageURL())
                .into(holder.serviceImage);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName, serviceDescription, servicePrice, estimatedTime;
        ImageView serviceImage;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            estimatedTime = itemView.findViewById(R.id.estimatedTime);
            serviceImage = itemView.findViewById(R.id.serviceImage);
        }
    }
}
