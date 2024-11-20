package com.example.final_year_project;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private CheckBox termsCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        LinearLayout linearLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerPhone = findViewById(R.id.registerPhone);
        termsCheckbox = findViewById(R.id.termsCheckbox);
        Button registerButton = findViewById(R.id.registerButton);
        TextView toLoginPage = findViewById(R.id.toLoginPage);
        TextView toTermsCondition = findViewById(R.id.toTermsCondition);

        registerButton.setOnClickListener(v -> registerUser());

        // Set an OnClickListener for the "Already have an account? Login" TextView
        toLoginPage.setOnClickListener(v -> {
            // Start the Login activity when the TextView is clicked
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });

        toTermsCondition.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, TermsCondition.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String name = registerName.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();
        String phone = registerPhone.getText().toString().trim();

        // Validate name
        if (name.isEmpty()) {
            registerName.setError("Name is required");
            registerName.requestFocus();
            return;
        }

        // Validate email
        if (email.isEmpty()) {
            registerEmail.setError("Email is required");
            registerEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerEmail.setError("Please provide a valid email");
            registerEmail.requestFocus();
            return;
        }

        // Validate phone number
        if (phone.isEmpty()) {
            registerPhone.setError("Phone number is required");
            registerPhone.requestFocus();
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            registerPassword.setError("Password is required");
            registerPassword.requestFocus();
            return;
        }
        if (password.length() < 8) {
            registerPassword.setError("Password must be at least 8 characters long");
            registerPassword.requestFocus();
            return;
        }
        if (!password.matches(".*[A-Z].*")) {
            registerPassword.setError("Password must contain at least one capital letter");
            registerPassword.requestFocus();
            return;
        }
        if (!password.matches(".*[0-9].*")) {
            registerPassword.setError("Password must contain at least one number");
            registerPassword.requestFocus();
            return;
        }
        if (!password.matches(".*[!@#$%^&*+=?-].*")) {
            registerPassword.setError("Password must contain at least one special character");
            registerPassword.requestFocus();
            return;
        }
        if (!termsCheckbox.isChecked()) {
            Toast.makeText(Register.this, "You must agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return; // Stop registration if checkbox is not checked
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
