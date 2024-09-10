package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends AppCompatActivity {

    private EditText resetEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        resetEmail = findViewById(R.id.resetEmail);
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);

        mAuth = FirebaseAuth.getInstance();

        TextView toLoginPage = findViewById(R.id.toLoginPage); // Get the TextView

        resetPasswordButton.setOnClickListener(v -> sendPasswordResetEmail());

        // Set an OnClickListener for the "Already have an account? Login" TextView
        toLoginPage.setOnClickListener(v -> {
            // Start the Login activity when the TextView is clicked
            Intent intent = new Intent(Forgot_password.this, Login.class);
            startActivity(intent);
        });
    }

    private void sendPasswordResetEmail() {
        String email = resetEmail.getText().toString().trim();

        // Validate email
        if (email.isEmpty()) {
            resetEmail.setError("Email is required");
            resetEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            resetEmail.setError("Please provide a valid email");
            resetEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Forgot_password.this, "Reset email sent", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity after success
                    } else {
                        Toast.makeText(Forgot_password.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
