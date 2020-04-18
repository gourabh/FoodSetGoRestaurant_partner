package com.example.foodsetgorestaurant.model;

import java.util.List;

public class Request {
    private String phone, name, address, status, date_time, total, paymentState, delivery, resName;
    private List<Order> foods;

    public Request(){
    }

    public Request(String phone, String name, String address, String status, String date_time, String total, String paymentState, String delivery, String resName, List<Order> foods){
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.status = status; //0:Placed 1:Accepted 2:On the way 3:Delivered 4:Rejected
        this.date_time = date_time;
        this.total = total;
        this.paymentState = paymentState;
        this.delivery = delivery;
        this.resName = resName;
        //this.latlong = latlong;
        this.foods = foods;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    /*public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }*/
}
