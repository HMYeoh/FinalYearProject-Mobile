package com.example.final_year_project;

public class BranchModel {
    private String branch;
    private String name;
    private String phone;

    public BranchModel() {
        // Empty constructor required for Firestore
    }

    public BranchModel(String branch, String name, String phone) {
        this.branch = branch;
        this.name = name;
        this.phone = phone;
    }

    public String getBranch() {
        return branch;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
