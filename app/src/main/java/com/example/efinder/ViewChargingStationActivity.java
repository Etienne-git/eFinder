package com.example.efinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ViewChargingStationActivity extends AppCompatActivity implements OnMapReadyCallback {

    ListView detailsList;
    ListView descriptionList;
    Button addfavorite;
    GoogleMap mGoogleMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {


        //Get location of chargingStation
        Intent intent = getIntent();
        int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());
        ArrayList<ChargingStation> chargingStations = StationManager.getStation_list();
        ChargingStation chargingStation = chargingStations.get(chargingStationID);

        LatLng chargingStationLocation = new LatLng(chargingStation.getLat(), chargingStation.getLon());

        mGoogleMap = googleMap;

        mGoogleMap.addMarker(new MarkerOptions().position(chargingStationLocation).title("Chargingstation"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(chargingStationLocation));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_charging_station);
        addfavorite = findViewById(R.id.addfavorite);
        detailsList = findViewById(R.id.details_list);
        descriptionList = findViewById(R.id.description_list);

        Intent intent = getIntent();


        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        addfavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                ArrayList<ChargingStation> chargingStations = StationManager.getStation_list();
                ArrayList<ChargingStation> favoriteStations = FavoriteManager.getStation_list();

                int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());


                ArrayList<String> descriptionArray = new ArrayList();
                descriptionArray.add("Detail1");
                descriptionArray.add("Details2");
                descriptionArray.add("Details3");
                descriptionArray.add("Details4");
                descriptionArray.add("Details5");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewChargingStationActivity.this, android.R.layout.simple_list_item_1, descriptionArray);
                detailsList.setAdapter(arrayAdapter);


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