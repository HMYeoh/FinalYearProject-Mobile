package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BranchDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_detail);

        TextView tvBranchName = findViewById(R.id.tvBranchName);
        TextView tvBranch = findViewById(R.id.tvBranch);
        TextView tvBranchPhone = findViewById(R.id.tvBranchPhone);

        // Retrieve the data from the Intent
        String branchName = getIntent().getStringExtra("branchName");
        String branch = getIntent().getStringExtra("branch");
        String branchPhone = getIntent().getStringExtra("branchPhone");

        // Set the data to TextViews
        tvBranchName.setText(branchName);
        tvBranch.setText(branch);
        tvBranchPhone.setText(branchPhone);
    }

    public void toBranch(View view) {
        Intent intent = new Intent(this, Branch.class);
        ImageButton toBranch = findViewById(R.id.branch);
        startActivity(intent);
    }
}
