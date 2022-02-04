package com.example.efinder;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/**
 * User class that holds all user information
 */
public class User {

    public User(String mail){
        eMail = mail;
        database = FirebaseDatabase.getInstance("https://efinder-1640105181864-default-rtdb.europe-west1.firebasedatabase.app");
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        isAdmin = false;
    }
    /**
     * getter for email adress
     * @return the email of the user
     */
    public String getEmail() {
        return eMail;
    }
    /**
     * setter for email adress
     * @param eMail the email to set to
     */
    public void setEmail(String eMail) { this.eMail = eMail; }
    /**
     * setter for admin rights bool
     * @param a bool value to set to
     */
    public void setIsAdmin(boolean a){ this.isAdmin = a;}
    /**
     * getter for admin rights
     * @return true if admin and false if not
     */
    public boolean getIsAdmin(){ return isAdmin; }
    /**
     * favorite charging stations of the user
     */
    public static ArrayList<ChargingStation> favorites = new ArrayList<>();



    private String eMail;
    private boolean isAdmin;
    private final FirebaseDatabase database;
    private DatabaseReference currentUserDb;
    private String id;

}
