package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Date extends AppCompatActivity {

    private TextView selectedDateTextView;
    private RecyclerView timeSlotRecyclerView;
    private TimeSlotAdapter timeSlotAdapter;
    private List<String> timeSlots;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date);

        CalendarView calendarView = findViewById(R.id.calendarView);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        timeSlotRecyclerView = findViewById(R.id.timeSlotRecyclerView);

        // Set up RecyclerView for time slots
        timeSlots = new ArrayList<>();
        timeSlotAdapter = new TimeSlotAdapter(timeSlots, this, null); // Initialize with null
        timeSlotRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timeSlotRecyclerView.setAdapter(timeSlotAdapter);

        // Handle calendar date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            selectedDateTextView.setText("Selected Date: " + selectedDate);

            // Load available time slots for the selected date
            loadTimeSlots();

            // Update the adapter with the new selected date
            timeSlotAdapter.setSelectedDate(selectedDate);
        });
    }

    private void loadTimeSlots() {
        // Clear the current list
        timeSlots.clear();

        // Add time slots (hardcoded for now)
        timeSlots.add("9:00 AM - 10:00 AM");
        timeSlots.add("10:00 AM - 11:00 AM");
        timeSlots.add("11:00 AM - 12:00 PM");
        timeSlots.add("1:00 PM - 2:00 PM");
        timeSlots.add("2:00 PM - 3:00 PM");
        timeSlots.add("3:00 PM - 4:00 PM");
        timeSlots.add("4:00 PM - 5:00 PM");
        timeSlots.add("5:00 PM - 6:00 PM");

        // Notify the adapter to update the RecyclerView
        timeSlotAdapter.notifyDataSetChanged();
    }

    public void toStylist(View view) {
        Intent intent = new Intent(this, Stylist.class);
        startActivity(intent);
    }
}
