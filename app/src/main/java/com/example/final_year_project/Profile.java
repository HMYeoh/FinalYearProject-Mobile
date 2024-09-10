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

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText profileName, profileEmail, profilePhone;
    private TextView welcomeText, profilePassword;
    private Button saveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize the TextViews
        welcomeText = findViewById(R.id.welcomeText);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhone = findViewById(R.id.profilePhone);
        profilePassword = findViewById(R.id.profilePassword);
        saveProfile = findViewById(R.id.saveProfile);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Get user ID from the currently logged-in user
            String userId = currentUser.getUid();

            // Retrieve the user details from Firestore
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Extract user details from Firestore
                                String name = document.getString("name");
                                String email = document.getString("email");
                                String phone = document.getString("phone");

                                // Display the user details
                                welcomeText.setText("Welcome, " + name + "!");
                                profileName.setText(name);
                                profileEmail.setText(email);
                                profilePhone.setText(phone);

                                // Since password is not saved in Firestore, you can't fetch it directly
                                profilePassword.setText("Password: ****");
                            } else {
                                Toast.makeText(Profile.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Profile.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Save button functionality
        saveProfile.setOnClickListener(v -> saveUserProfile());
    }

    private void saveUserProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        String updatedName = profileName.getText().toString();
        String updatedEmail = profileEmail.getText().toString();
        String updatedPhone = profilePhone.getText().toString();

        if (!updatedName.isEmpty() && !updatedEmail.isEmpty() && !updatedPhone.isEmpty()) {
            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("name", updatedName);
            userUpdates.put("email", updatedEmail);
            userUpdates.put("phone", updatedPhone);

            db.collection("users").document(userId)
                    .update(userUpdates)
                    .addOnSuccessListener(aVoid -> Toast.makeText(Profile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(Profile.this, "Error updating profile", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void toHome(View view){
        Intent intent = new Intent(this, Home.class);
        ImageButton toHome= findViewById(R.id.home);
        startActivity(intent);
    }
}
