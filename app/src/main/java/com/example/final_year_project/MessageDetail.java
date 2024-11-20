package com.example.final_year_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MessageDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail);

        TextView subjectTextView = findViewById(R.id.subjectTextView);
        TextView createdAtTextView = findViewById(R.id.createdAtView);
        TextView messageTextView = findViewById(R.id.messageTextView);
        TextView originalMessageTextView = findViewById(R.id.originalMessageTextView); // New TextView
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView backButton = findViewById(R.id.backButton);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String subject = intent.getStringExtra("subject");
        String createdAt = intent.getStringExtra("createdAt");
        String message = intent.getStringExtra("message");
        String originalMessage = intent.getStringExtra("originalMessage"); // Get originalMessage
        String username = intent.getStringExtra("username");

        // Set the text views
        subjectTextView.setText(subject);
        createdAtTextView.setText(createdAt);
        messageTextView.setText(message);
        originalMessageTextView.setText(originalMessage); // Display originalMessage
        usernameTextView.setText(username);

        // Set the back button action
        backButton.setOnClickListener(v -> finish());
    }
}
