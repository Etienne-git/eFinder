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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewChargingStationActivity extends AppCompatActivity implements OnMapReadyCallback {

    ListView detailsList;
    ListView descriptionList;
    Button addfavorite;
    GoogleMap mGoogleMap;
    String id;
    ArrayList<ChargingStation> favoriteStations = new ArrayList<>();

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
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.firebase_link));
        DatabaseReference ref = database.getReference().child("users").child(id).child("favorites");

        Intent intent = getIntent();

        getFavoriteStations(ref);


        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());
        ArrayList<ChargingStation> chargingStations = StationManager.getStation_list();
        ArrayList<String> descriptionArray = new ArrayList<>();
        ArrayList<String> detailsArray = new ArrayList<>();

        //Get all information about chargingStation
        String city = "Ort: " + chargingStations.get(chargingStationID).getLocation();
        String street = "Street: " + chargingStations.get(chargingStationID).getStreet();
        String status = "Status: besetzt";
        String power = "Strom: " + chargingStations.get(chargingStationID).getConn_power();
        descriptionArray.add(city);
        descriptionArray.add(street);
        descriptionArray.add(status);
        descriptionArray.add(power);


        String module_type = "Modultyp: " + chargingStations.get(chargingStationID).getModule_type();
        String connNumber = "Verbindungsanzahl: " + chargingStations.get(chargingStationID).getNumber_of_connections();
        String installation_date = "Installationsdatum: " + chargingStations.get(chargingStationID).getInstallation_date();

        String plugType1 = "Anschlusstyp 1:   ";
        String plugType2 = "Anschlusstyp 2:   ";
        String plugType3 = "Anschlusstyp 3:   ";
        String plugType4 = "Anschlusstyp 4:   ";

        ArrayList<PlugType> plugTypes1 = chargingStations.get(chargingStationID).getPlug_types_1();

        for(int i = 0; i < plugTypes1.size(); i++){
            plugType1 = plugType1 + plugTypes1.get(i);
        }

        ArrayList<PlugType> plugTypes2 = chargingStations.get(chargingStationID).getPlug_types_2();

        for(int i = 0; i < plugTypes2.size(); i++){
            plugType2 = plugType2 + plugTypes2.get(i);
        }

        ArrayList<PlugType> plugTypes3 = chargingStations.get(chargingStationID).getPlug_types_3();

        for(int i = 0; i < plugTypes3.size(); i++){
            plugType3 = plugType3 + plugTypes3.get(i);
        }

        ArrayList<PlugType> plugTypes4 = chargingStations.get(chargingStationID).getPlug_types_4();

        for(int i = 0; i < plugTypes4.size(); i++){
            plugType4 = plugType4 + plugTypes4.get(i);
        }


        double power1 = chargingStations.get(chargingStationID).getPower_1();
        double power2 = chargingStations.get(chargingStationID).getPower_2();
        double power3 = chargingStations.get(chargingStationID).getPower_3();
        double power4 = chargingStations.get(chargingStationID).getPower_4();

        String power1_str = "Stromstärke: " + Double.toString(power1);
        String power2_str = "Stromstärke: " + Double.toString(power2);
        String power3_str = "Stromstärke: " + Double.toString(power3);
        String power4_str = "Stromstärke: " + Double.toString(power4);



        detailsArray.add(plugType1);
        detailsArray.add(plugType2);
        detailsArray.add(plugType3);
        detailsArray.add(plugType4);

        detailsArray.add(power1_str);
        detailsArray.add(power2_str);
        detailsArray.add(power3_str);
        detailsArray.add(power4_str);

        detailsArray.add(module_type);
        detailsArray.add(connNumber);
        detailsArray.add(installation_date);


        ArrayAdapter<String> detailsAdapter = new ArrayAdapter<String>(ViewChargingStationActivity.this, R.layout.custom_text_view, detailsArray);
        ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<String>(ViewChargingStationActivity.this, R.layout.custom_text_view, descriptionArray);
        detailsList.setAdapter(detailsAdapter);
        descriptionList.setAdapter(descriptionAdapter);



        addfavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                ArrayList<ChargingStation> chargingStations = StationManager.getStation_list();

                int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());



                System.out.println("Folgende Ladestation wurde aufgerufen: ");
                System.out.println(chargingStationID);


                System.out.println("Folgend Ladestation wurde gefunden: ");
                System.out.println(chargingStations.get(chargingStationID).getLocation());
                favoriteStations.add(chargingStations.get(chargingStationID-1));
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

