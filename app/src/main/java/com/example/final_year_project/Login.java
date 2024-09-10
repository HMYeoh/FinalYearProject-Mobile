package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        Button loginButton = findViewById(R.id.loginButton);
        TextView toRegisterPage = findViewById(R.id.toRegisterPage);  // Get the TextView

        // Set an OnClickListener for the login button
        loginButton.setOnClickListener(v -> loginUser());

        // Set an OnClickListener for the "Don't have an account? Create One" TextView
        toRegisterPage.setOnClickListener(v -> {
            // Start the Register activity when the TextView is clicked
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        // Validate email
        if (email.isEmpty()) {
            loginEmail.setError("Email is required");
            loginEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please provide a valid email");
            loginEmail.requestFocus();
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            loginPassword.setError("Password is required");
            loginPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Home.class);
                        startActivity(intent);
                    } else {
                        // Check the exception type
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // Incorrect password
                            loginPassword.setError("Incorrect password");
                            loginPassword.requestFocus();
                        } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            // No account with this email
                            loginEmail.setError("No account found with this email");
                            loginEmail.requestFocus();
                        } else {
                            // Other failure (network issues, etc.)
                            Toast.makeText(Login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void toForgotPassword(android.view.View view) {
        Intent intent = new Intent(Login.this, Forgot_password.class);
        startActivity(intent);
    }
}
