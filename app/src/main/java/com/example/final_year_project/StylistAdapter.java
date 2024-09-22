package com.example.final_year_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class StylistAdapter extends RecyclerView.Adapter<StylistAdapter.ViewHolder> {

    private List<StylistModel> staffList;

    public StylistAdapter(List<StylistModel> staffList) {
        this.staffList = staffList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stylist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StylistModel staff = staffList.get(position);
        holder.username.setText(staff.getUsername());

        // Load profile picture using Glide or any other image loading library
        Glide.with(holder.itemView.getContext())
                .load(staff.getProfilePictureURL())
                .circleCrop()
                .into(holder.profilePicture);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView profilePicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profilePicture = itemView.findViewById(R.id.profilePicture);
        }
    }
}
