package com.example.final_year_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    private ArrayList<BranchModel> branchList;
    private Context context;

    public BranchAdapter(Context context, ArrayList<BranchModel> branchList) {
        this.context = context;
        this.branchList = branchList;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_item, parent, false);
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        BranchModel branchModel = branchList.get(position);
        holder.branchName.setText(branchModel.getName());
        holder.branch.setText(branchModel.getBranch());
        holder.branchPhone.setText(branchModel.getPhone());

        // Set onClickListener to navigate to BranchDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BranchDetail.class);
            intent.putExtra("branchName", branchModel.getName());
            intent.putExtra("branch", branchModel.getBranch());
            intent.putExtra("branchPhone", branchModel.getPhone());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public static class BranchViewHolder extends RecyclerView.ViewHolder {
        TextView branchName, branch, branchPhone;

        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            branchName = itemView.findViewById(R.id.branchName);
            branch = itemView.findViewById(R.id.branch);
            branchPhone = itemView.findViewById(R.id.branchPhone);
        }
    }
}
