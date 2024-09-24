package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stylist);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stylistList = new ArrayList<>();
        stylistAdapter = new StylistAdapter(stylistList, this);
        recyclerView.setAdapter(stylistAdapter);

        loadStylists();
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

                            StylistModel stylist = new StylistModel(username, profilePictureURL);
                            stylistList.add(stylist);
                        }
                        stylistAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Stylist.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void toBookingOverview(View view) {
        Intent intent = new Intent(this, BookingOverview.class);
        startActivity(intent);
    }
}
