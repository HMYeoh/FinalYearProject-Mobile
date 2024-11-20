// MessageModel.java
package com.example.final_year_project;

import com.google.firebase.Timestamp;

public class MessageModel {
    private String Message;
    private String Subject;
    private String Username;
    private String userId;
    private String originalMessage; // New field
    private Timestamp createdAt;

    public MessageModel() {
        // Empty constructor needed for Firestore
    }

    public MessageModel(String message, String subject, String username, String userId, Timestamp createdAt, String originalMessage) {
        this.Message = message;
        this.Subject = subject;
        this.Username = username;
        this.userId = userId;
        this.createdAt = createdAt;
        this.originalMessage = originalMessage; // New field initialization
    }

    public String getMessage() {
        return Message;
    }

    public String getSubject() {
        return Subject;
    }

    public String getUsername() {
        return Username;
    }

    public String getUserId() {
        return userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getOriginalMessage() {
        return originalMessage; // New getter method
    }
}
