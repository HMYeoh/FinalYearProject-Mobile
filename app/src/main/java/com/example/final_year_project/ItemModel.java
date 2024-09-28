package com.example.final_year_project;

public class ItemModel {
    private String serviceName, serviceDetails, estimatedTime, servicePrice;

    public ItemModel() {
        // Required empty constructor
    }

    public ItemModel(String serviceName, String serviceDetails, String estimatedTime, String servicePrice) {
        this.serviceName = serviceName;
        this.serviceDetails = serviceDetails;
        this.estimatedTime = estimatedTime;
        this.servicePrice = servicePrice;
    }

    // Getters and setters
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getServiceDetails() { return serviceDetails; }
    public void setServiceDetails(String serviceDetails) { this.serviceDetails = serviceDetails; }

    public String getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }

    public String getServicePrice() { return servicePrice; }
    public void setServicePrice(String servicePrice) { this.servicePrice = servicePrice; }
}
