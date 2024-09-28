package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    private List<ReservationModel> reservationList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reservationAdapter = new ReservationAdapter(reservationList);
        recyclerView.setAdapter(reservationAdapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        fetchBookings();
    }

    private void fetchBookings() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        CollectionReference reservationsRef = db.collection("reservations");

        reservationsRef.whereEqualTo("userId", currentUserId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reservationList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ReservationModel reservation = document.toObject(ReservationModel.class);
                            reservationList.add(reservation);
                        }
                        reservationAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("BookingHistory", "Fetch failed: ", task.getException());
                    }
                });
    }


    public void toHome(View view){
        Intent intent = new Intent(this, Home.class);
        ImageButton toHome= findViewById(R.id.home);
        startActivity(intent);
    }

}
