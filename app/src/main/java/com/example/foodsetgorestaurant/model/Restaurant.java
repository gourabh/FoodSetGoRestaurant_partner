package com.example.foodsetgorestaurant.model;

public class Restaurant {

    private String Name, Password, Email, Address,Available;
    private Long Mobile;
    private String Img;
    private String Price;
    private String Type;
    private Double Rating;

    public Restaurant(){

    }

    public Restaurant(String name, String password, String email, String address, Long mobile, String img, String price, String type, Double rating,String available) {
        Name = name;
        Password = password;
        Email = email;
        Address = address;
        Mobile = mobile;
        Img = img;
        Price = price;
        Type = type;
        Rating = rating;
        Available=available;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available=available;
    }

    public String  getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Double getRating() {
        return Rating;
    }

    public void setRating(Double rating) {
        Rating = rating;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Long getMobile() {
        return Mobile;
    }

    public void setMobile(Long mobile) {
        Mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}


