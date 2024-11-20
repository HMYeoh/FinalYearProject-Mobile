package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private EditText profileName;
    private EditText profileEmail;
    private EditText profilePhone;
    private TextView welcomeText;
    private Button saveProfile;
    private ImageButton toHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize UI elements
        welcomeText = findViewById(R.id.welcomeText);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhone = findViewById(R.id.profilePhone);
        saveProfile = findViewById(R.id.saveProfile);
        toHome = findViewById(R.id.home);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Fetch user data from Firestore
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String email = document.getString("email");
                                String phone = document.getString("phone");

                                // Set user data to EditTexts
                                welcomeText.setText("Welcome, " + name );
                                profileName.setText(name);
                                profileEmail.setText(email);
                                profilePhone.setText(phone);
                            } else {
                                Toast.makeText(Profile.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Profile.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }

        // Set up the save button listener
        saveProfile.setOnClickListener(view -> saveProfileData(currentUser.getUid()));
    }

    private void saveProfileData(String userId) {
        // Get updated values from EditTexts
        String name = profileName.getText().toString().trim();
        String email = profileEmail.getText().toString().trim();
        String phone = profilePhone.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Map to hold the updated data
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", name);
        userUpdates.put("email", email);
        userUpdates.put("phone", phone);

        // Save data to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .update(userUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Profile.this, "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}
