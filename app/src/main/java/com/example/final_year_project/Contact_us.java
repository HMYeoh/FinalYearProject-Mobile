package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Contact_us extends AppCompatActivity {
    EditText inputUsername, inputSubject, inputMessage;
    Button submitBtn;
    FirebaseFirestore db;
    FirebaseAuth mAuth; // Firebase Auth instance to get the current user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        // Initialize Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        inputUsername = findViewById(R.id.inputUsername);
        inputSubject = findViewById(R.id.inputSubject);
        inputMessage = findViewById(R.id.inputMessage);
        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current logged-in user
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid(); // Get the user ID
                    String Username = inputUsername.getText().toString();
                    String Subject = inputSubject.getText().toString();
                    String Message = inputMessage.getText().toString();

                    // Create a map with the contact details
                    Map<String, Object> contact = new HashMap<>();
                    contact.put("userId", userId); // Save the user ID
                    contact.put("Username", Username);
                    contact.put("Subject", Subject);
                    contact.put("Message", Message);

                    // Save the details to Firestore
                    db.collection("contact")
                            .add(contact)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(Contact_us.this, "Message sent successfully", Toast.LENGTH_SHORT).show();

                                    // Navigate to Home.java after success
                                    Intent intent = new Intent(Contact_us.this, Home.class);
                                    startActivity(intent);
                                    finish(); // Optionally, close the current activity
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Contact_us.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // If user is not logged in, show a message or redirect to login
                    Toast.makeText(Contact_us.this, "Please log in to send a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to navigate back to home when ImageButton is clicked
    public void toHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}
