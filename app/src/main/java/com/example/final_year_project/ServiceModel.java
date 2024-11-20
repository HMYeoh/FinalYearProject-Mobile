package com.example.final_year_project;

public class ServiceModel {
    private String serviceName;
    private String serviceDetails;
    private String servicePrice;
    private String serviceImageURL;
    private String estimatedTime;

    public ServiceModel() {
        // Needed for Firestore's deserialization
    }

    public ServiceModel(String serviceName, String serviceDetails, String servicePrice, String serviceImageURL, String estimatedTime) {
        this.serviceName = serviceName;
        this.serviceDetails = serviceDetails;
        this.servicePrice = servicePrice;
        this.serviceImageURL = serviceImageURL;
        this.estimatedTime = estimatedTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDetails() {
        return serviceDetails;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public String getServiceImageURL() {
        return serviceImageURL;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }
}
