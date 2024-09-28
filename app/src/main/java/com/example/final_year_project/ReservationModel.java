package com.example.final_year_project;

import java.util.List;

public class ReservationModel {
    private String date, timeSlot, userEmail, userName, userId, stylistName;
    private List<ItemModel> services;  // Updated from ServiceModel to ItemModel

    public ReservationModel() {
        // Required empty constructor
    }

    public ReservationModel(String date, String timeSlot, String userEmail, String userName, String userId, String stylistName, List<ItemModel> services) {
        this.date = date;
        this.timeSlot = timeSlot;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userId = userId;
        this.stylistName = stylistName;
        this.services = services;
    }

    // Getters and setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getStylistName() { return stylistName; }
    public void setStylistName(String stylistName) { this.stylistName = stylistName; }

    public List<ItemModel> getServices() { return services; }  // Updated to use ItemModel
    public void setServices(List<ItemModel> services) { this.services = services; }
}
