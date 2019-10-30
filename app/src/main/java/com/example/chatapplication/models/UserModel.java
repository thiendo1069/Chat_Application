package com.example.chatapplication.models;

import java.util.ArrayList;

public class UserModel {
    private String userId;
    private String userName;
    private String phoneNumber = " ";
    private boolean isMale = true;
    private String address = " ";
    private String birthday = " ";
    private String ulrImage = "default";


    public String getUlrImage() {
        return ulrImage;
    }

    public void setUlrImage(String ulrImage) {
        this.ulrImage = ulrImage;
    }

    public UserModel() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
