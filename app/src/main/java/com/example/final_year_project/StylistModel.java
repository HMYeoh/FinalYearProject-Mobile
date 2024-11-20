package com.example.final_year_project;

public class StylistModel {
    private String username;
    private String profilePictureURL;
    private String branchName;

    public StylistModel() {
        // Required for Firebase
    }

    public StylistModel(String username, String profilePictureURL, String branchName) {
        this.username = username;
        this.profilePictureURL = profilePictureURL;
        this.branchName = branchName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
