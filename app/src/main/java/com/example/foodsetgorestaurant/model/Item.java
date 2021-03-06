package com.example.foodsetgorestaurant.model;

public class Item {

    private String Name, Type, Rating, Price, Available, Course, Resid, Discount;

    public  Item()
    {

    }

    public Item(String name, String type, String rating, String price, String available, String course, String resid, String discount) {
        Name = name;
        Type = type;
        Rating = rating;
        Price = price;
        Available = available;
        Course = course;
        Resid = resid;
        Discount = discount;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public String getResid() {
        return Resid;
    }

    public void setResid(String resid) {
        Resid = resid;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
