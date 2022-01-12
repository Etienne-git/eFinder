package com.example.efinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewChargingStationActivity extends AppCompatActivity {

    TextView description;
    Button addfavorite;
    String id;
    ArrayList<ChargingStation> favoriteStations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_charging_station);
        addfavorite = findViewById(R.id.addfavorite);
        description = findViewById(R.id.chargingstation_id);
        Intent intent = getIntent();
        description.setText(intent.getStringExtra("id"));
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://efinder-1640105181864-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = database.getReference().child("users").child(id).child("favorites");

        getFavoriteStations(ref);

        addfavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<ChargingStation> chargingStations = StationManager.getStation_list();


                int chargingStationID = Integer.valueOf(description.getText().toString());
                System.out.println("Folgende Ladestation wurde aufgerufen: ");
                System.out.println(chargingStationID);


                System.out.println("Folgend Ladestation wurde gefunden: ");
                System.out.println(chargingStations.get(chargingStationID).getLocation());

                favoriteStations.add(chargingStations.get(chargingStationID-1));
                FavoriteManager.setStation_list(favoriteStations);
                ref.setValue(favoriteStations);
            }
        });

    }

    private void getFavoriteStations(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteStations.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                ChargingStation station = postSnapshot.getValue(ChargingStation.class);
                favoriteStations.add(station);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });
    }
}