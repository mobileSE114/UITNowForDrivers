package com.uit.uitnowfordrivers;

import com.google.firebase.firestore.GeoPoint;

public class Request {
    String id;
    String orderId;
    int status;
    String storeAddress;
    GeoPoint storeLocation;
    String storeName;
    String total;
    String userAddress;
    GeoPoint userLocation;
    String userName;
    GeoPoint driverLocation;
    String driverName;
    String driverId;
    public Request()
    {

    }

    public Request(String id, int status, String storeAddress, GeoPoint storeLocation, String storeName, String total, String userAddress, GeoPoint userLocation, String userName, GeoPoint driverLocation, String driverName,String driverId) {
        this.id = id;
        this.status = status;
        this.storeAddress = storeAddress;
        this.storeLocation = storeLocation;
        this.storeName = storeName;
        this.total = total;
        this.userAddress = userAddress;
        this.userLocation = userLocation;
        this.userName = userName;
        this.driverLocation = driverLocation;
        this.driverName = driverName;
        this.driverId=driverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public GeoPoint getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(GeoPoint storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public GeoPoint getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(GeoPoint userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public GeoPoint getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(GeoPoint driverLocation) {
        this.driverLocation = driverLocation;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
