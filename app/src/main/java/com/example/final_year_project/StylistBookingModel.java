package com.example.final_year_project;

import java.util.List;

public class StylistBookingModel {
    private String userEmail;
    private String userName;
    private String stylistName;
    private String stylistProfilePictureURL;
    private List<BookingDetail> services;
    private String branchName;

    public StylistBookingModel() {
        // Required for Firebase
    }

    public StylistBookingModel(String userEmail, String userName, String stylistName, String stylistProfilePictureURL, List<BookingDetail> services, String branchName) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.stylistName = stylistName;
        this.stylistProfilePictureURL = stylistProfilePictureURL;
        this.services = services;
        this.branchName = branchName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStylistName() {
        return stylistName;
    }

    public void setStylistName(String stylistName) {
        this.stylistName = stylistName;
    }

    public String getStylistProfilePictureURL() {
        return stylistProfilePictureURL;
    }

    public void setStylistProfilePictureURL(String stylistProfilePictureURL) {
        this.stylistProfilePictureURL = stylistProfilePictureURL;
    }

    public List<BookingDetail> getServices() {
        return services;
    }

    public void setServices(List<BookingDetail> services) {
        this.services = services;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}

class BookingDetail {
    private String serviceName;
    private String servicePrice;
    private String serviceDetails;
    private String estimatedTime;

    public BookingDetail() {
        // Required for Firebase
    }

    public BookingDetail(String serviceName, String servicePrice, String serviceDetails, String estimatedTime) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDetails = serviceDetails;
        this.estimatedTime = estimatedTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(String serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
