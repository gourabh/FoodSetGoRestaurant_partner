package com.example.foodsetgorestaurant.Common;

import com.example.foodsetgorestaurant.model.Restaurant;

public class Common {
    public static Restaurant currentUser;

    public static final String USER_KEY="user";
    public static final String USER_NAME="name";
    public static final String PWD_KEY="password";
    public static final String UPDATE="Update";
    public static final String DELETE="Delete";
    public static String  convertCodeToStatus(String status){
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Accepted";
        /*else if (status.equals("2"))
            return "On the way";
        else if (status.equals("3"))
            return "Delivered";*/
        else if (status.equals("4"))
            return "Rejected";
        else
            return null;
    }
}
