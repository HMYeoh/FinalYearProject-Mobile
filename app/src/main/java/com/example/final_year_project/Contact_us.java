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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Contact_us extends AppCompatActivity {
    EditText inputUsername, inputSubject, inputMessage;
    Button submitBtn;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        inputUsername = findViewById(R.id.inputUsername);
        inputSubject = findViewById(R.id.inputSubject);
        inputMessage = findViewById(R.id.inputMessage);
        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = inputUsername.getText().toString().trim();
                String Subject = inputSubject.getText().toString().trim();
                String Message = inputMessage.getText().toString().trim();

                if (Username.isEmpty()) {
                    Toast.makeText(Contact_us.this, "Please enter your Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Subject.isEmpty()) {
                    Toast.makeText(Contact_us.this, "Please enter the Subject", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Message.isEmpty()) {
                    Toast.makeText(Contact_us.this, "Please enter your Message", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    Map<String, Object> contact = new HashMap<>();
                    contact.put("userId", userId);
                    contact.put("Username", Username);
                    contact.put("Subject", Subject);
                    contact.put("Message", Message);
                    contact.put("createdAt", FieldValue.serverTimestamp());

                    db.collection("contact")
                            .add(contact)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(Contact_us.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Contact_us.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Contact_us.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(Contact_us.this, "Please log in to send a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}
