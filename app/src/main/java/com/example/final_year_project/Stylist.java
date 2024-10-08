package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class Stylist extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StylistAdapter stylistAdapter;
    private List<StylistModel> stylistList;
    private Spinner branchSpinner;
    private ArrayAdapter<String> branchAdapter;
    private List<String> branchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stylist);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stylistList = new ArrayList<>();
        stylistAdapter = new StylistAdapter(stylistList, this);
        recyclerView.setAdapter(stylistAdapter);

        branchSpinner = findViewById(R.id.branchSpinner);
        branchList = new ArrayList<>();
        branchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branchList);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);

        loadBranches();  // Load branch names into Spinner
        loadStylists();

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedBranch = branchList.get(position);
                loadStylistsByBranch(selectedBranch);  // Load stylists based on selected branch
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void loadBranches() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("branch")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        branchList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String branchName = document.getString("name");
                            branchList.add(branchName);  // Add branch name to the list
                        }
                        branchAdapter.notifyDataSetChanged();  // Update the Spinner
                    } else {
                        Toast.makeText(Stylist.this, "Error getting branches.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadStylists() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        stylistList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            String profilePictureURL = document.getString("profilePictureURL");
                            String branchName = document.getString("branchName");

                            StylistModel stylist = new StylistModel(username, profilePictureURL, branchName);
                            stylistList.add(stylist);
                        }
                        stylistAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Stylist.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadStylistsByBranch(String branchName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs")
                .whereEqualTo("branchName", branchName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        stylistList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            String profilePictureURL = document.getString("profilePictureURL");

                            // Ensure that branchName is correctly retrieved
                            if (document.contains("branchName")) {
                                String fetchedBranchName = document.getString("branchName");
                                // Optionally log or display the fetched branch name for debugging
                                System.out.println("Fetched Branch Name: " + fetchedBranchName);
                            }

                            StylistModel stylist = new StylistModel(username, profilePictureURL, branchName);
                            stylistList.add(stylist);
                        }
                        stylistAdapter.notifyDataSetChanged();  // Refresh RecyclerView with filtered stylists
                    } else {
                        Toast.makeText(Stylist.this, "Error getting stylists.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void toBookingOverview(View view) {
        Intent intent = new Intent(this, BookingOverview.class);
        startActivity(intent);
    }
}
