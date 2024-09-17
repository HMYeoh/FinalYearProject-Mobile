package com.example.final_year_project;

public class BookingModel {
    private String serviceName;
    private String servicePrice;
    private String serviceDetails;

    public BookingModel() {
        // Default constructor required for Firebase
    }

    public BookingModel(String serviceName, String servicePrice, String serviceDetails) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDetails = serviceDetails;
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
}
