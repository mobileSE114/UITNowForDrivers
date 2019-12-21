package com.uit.uitnowfordrivers;

import com.google.firebase.firestore.GeoPoint;

public class Order {
    String id;
    String idKhachHang;
    String trangThai;
    String deliveryAddress;
    String storeName;
    String storeAddress;
    String tongGia;
    GeoPoint storeLocation;
    GeoPoint deliveryLocation;
    GeoPoint driverLocation;
    String driverName;
    public Order()
    {

    }

    public Order(String id, String idKhachHang, String deliveryAddress, String storeName, GeoPoint
            deliveryLocation, GeoPoint storeLocation) {
        this.id=id;
        this.idKhachHang=idKhachHang;
        this.deliveryAddress = deliveryAddress;
        this.storeName = storeName;
        this.deliveryLocation = deliveryLocation;
        this.storeLocation = storeLocation;
        this.trangThai="Booking";
    }

    public String getTongGia() {
        return tongGia;
    }

    public void setTongGia(String tongGia) {
        this.tongGia = tongGia;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }


    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public GeoPoint getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(GeoPoint deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public GeoPoint getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(GeoPoint storeLocation) {
        this.storeLocation = storeLocation;
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

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

}
