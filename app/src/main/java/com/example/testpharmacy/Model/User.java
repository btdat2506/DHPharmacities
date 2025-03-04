package com.example.testpharmacy.Model;

public class User {
    private long userId;         // Maps to COLUMN_USER_ID in DatabaseHelper
    private String name = "";         // Maps to COLUMN_USER_NAME
    private String email = "";        // Maps to COLUMN_USER_EMAIL
    private String phoneNumber = "";  // Maps to COLUMN_USER_PHONE
    private String password = ""; // Maps to COLUMN_USER_PASSWORD
    private String address = "";      // Maps to COLUMN_USER_ADDRESS
    private String medicalNotice = ""; // Maps to COLUMN_USER_MEDICAL_NOTICE
    // Declaration all variables blank at first will make sure no error.
    // In User.java, add an isAdmin field
    private boolean isAdmin;


    public User() {
        // Default constructor (required for some operations)
    }

    // Constructor with all fields (except userId, which is auto-generated)
    public User(String name, String email, String phoneNumber, String password, String address, String medicalNotice) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
        this.medicalNotice = medicalNotice;
    }

    // Getters and Setters for all fields

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMedicalNotice() {
        return medicalNotice;
    }

    public void setMedicalNotice(String medicalNotice) {
        this.medicalNotice = medicalNotice;
    }


    public boolean isAdmin() {
        return true;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
