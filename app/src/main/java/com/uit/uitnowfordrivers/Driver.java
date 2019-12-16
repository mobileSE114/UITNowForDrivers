package com.uit.uitnowfordrivers;

import com.google.firebase.firestore.GeoPoint;

public class Driver {
    String id;
    String name;
    String email;
    String phone;
    String photo;

    public Driver() {
    }

    public Driver(String id, String name,String email, String phone,String photo) {
        this.id = id;
        this.name = name;
        this.email=email;
        this.phone = phone;
        this.photo=photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
