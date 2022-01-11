package com.example.efinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewChargingStationActivity extends AppCompatActivity {

    TextView description;
    Button addfavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_charging_station);
        addfavorite = findViewById(R.id.addfavorite);
        description = findViewById(R.id.chargingstation_id);
        Intent intent = getIntent();
        description.setText(intent.getStringExtra("id"));

        addfavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<ChargingStation> chargingStations = StationManager.getStation_list();
                ArrayList<ChargingStation> favoriteStations = FavoriteManager.getStation_list();


                int chargingStationID = Integer.valueOf(description.getText().toString());
                System.out.println("Folgende Ladestation wurde aufgerufen: ");
                System.out.println(chargingStationID);


                System.out.println("Folgend Ladestation wurde gefunden: ");
                System.out.println(chargingStations.get(chargingStationID).getLocation());

                favoriteStations.add(chargingStations.get(chargingStationID-1));
                FavoriteManager.setStation_list(favoriteStations);
            }
        });

    }
}