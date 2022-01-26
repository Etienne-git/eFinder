package com.example.efinder;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User {

    public User(String mail){
        eMail = mail;
        database = FirebaseDatabase.getInstance("https://efinder-1640105181864-default-rtdb.europe-west1.firebasedatabase.app");
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        isAdmin = false;
    }

    public String getEmail() {
        return eMail;
    }

    public void setEmail(String eMail) { this.eMail = eMail; }



    private String eMail;
    private boolean isAdmin;
    private static ArrayList<ChargingStation> favorites = new ArrayList<>();
    private final FirebaseDatabase database;
    private DatabaseReference currentUserDb;
    private String id;

}
