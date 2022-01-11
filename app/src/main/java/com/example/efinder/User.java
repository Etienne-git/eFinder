package com.example.efinder;

import com.example.efinder.Charger;

import java.util.Vector;

public class User {

    public String getUserID() {
        return userID;
    }

    public void setUserID(String id) { userID = id; }

    public String getEmail() {
        return eMail;
    }

    public void setEmail(String eMail) { this.eMail = eMail; }

    private String userID;
    private String eMail;
    Vector<Charger> favourites = new Vector<>();

}
