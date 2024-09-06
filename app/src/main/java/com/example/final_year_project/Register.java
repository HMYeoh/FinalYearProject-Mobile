package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText registerName, registerEmail, registerPassword, registerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerPhone = findViewById(R.id.registerPhone);
        Button registerButton = findViewById(R.id.registerButton);
        TextView toLoginPage = findViewById(R.id.toLoginPage); // Get the TextView

        registerButton.setOnClickListener(v -> registerUser());

        // Set an OnClickListener for the "Already have an account? Login" TextView
        toLoginPage.setOnClickListener(v -> {
            // Start the Login activity when the TextView is clicked
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String name = registerName.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();
        String phone = registerPhone.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserData(user.getUid(), name, email, phone);
                        }
                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Register", "Registration Failed", task.getException());
                        Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(String userId, String name, String email, String phone) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User data saved"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error saving user data", e));
    }
}
