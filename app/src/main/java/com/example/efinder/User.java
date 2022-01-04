package com.example.efinder;

import com.example.efinder.Charger;

import java.util.Vector;

public class User {

    public int getUserID() {
        return userID;
    }

    public void setUserID(int id) {
        userID = id;
    }

    public String getEmail() {
        return eMail;
    }

    public void setEmail(String eMail) { this.eMail = eMail; }

    private int userID;
    private String eMail;
    Vector<Charger> favourites = new Vector<>();

}
