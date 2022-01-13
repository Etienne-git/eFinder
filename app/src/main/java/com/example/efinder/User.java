package com.example.efinder;

import java.util.Vector;

public class User {

    public User(String mail){
        eMail = mail;
    }

    public String getEmail() {
        return eMail;
    }

    public void setEmail(String eMail) { this.eMail = eMail; }


    private String eMail;
    private boolean admin = false;
    FavoriteManager favorites = new FavoriteManager();

}
