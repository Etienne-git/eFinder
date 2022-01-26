package com.example.efinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.efinder.ui.search.SearchFragment;
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
    Button addFavorite;
    Button addDefect;
    GoogleMap mGoogleMap;
    String id;
    Button useStationBtn;
    ArrayList<ChargingStation> favoriteStations = new ArrayList<>();
    ArrayList<ChargingStation> defectStations = new ArrayList<>();
    ArrayList<ChargingStation> chargingStations = new ArrayList<>();


    @Override
    public void onMapReady(GoogleMap googleMap) {


        //Get location of chargingStation
        Intent intent = getIntent();
        int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());
        ArrayList<ChargingStation> chargingStations = StationManager.getStation_list();
        ChargingStation chargingStation = chargingStations.get(chargingStationID);

        LatLng chargingStationLocation = new LatLng(chargingStation.getLat(), chargingStation.getLon());

        mGoogleMap = googleMap;

        float zoomLevel = 16.0f;
        mGoogleMap.addMarker(new MarkerOptions().position(chargingStationLocation).title("Chargingstation"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(chargingStationLocation));



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chargingStations = StationManager.getStation_list();
        setContentView(R.layout.activity_view_charging_station);
        addFavorite = findViewById(R.id.addFavorite);
        addDefect = findViewById(R.id.addDefect);
        detailsList = findViewById(R.id.details_list);
        descriptionList = findViewById(R.id.description_list);
        useStationBtn = findViewById(R.id.useStation);
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.firebase_link));
        DatabaseReference favoritesRef = database.getReference().child("users").child(id).child("favorites");
        DatabaseReference defectRef = database.getReference().child("defectStations");

        
        ArrayList<ChargingStation> chargingStations = SearchFragment.getInstance().getChargingStations();
        Intent intent = getIntent();

        getFavoriteStations(favoritesRef);
        getDefectStations(defectRef);



        int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());

        boolean chargerIsUsed = chargingStations.get(chargingStationID).isIs_used();

        if(chargerIsUsed == true){
            useStationBtn.setText(getResources().getString((R.string.leave_station)));
        }else {
            useStationBtn.setText(getResources().getString(R.string.use_this_station));
        }



        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());
        //int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());


        System.out.println("chargingStationID: " + chargingStationID);


        //ArrayList<ChargingStation> chargingStations = StationManager.getStation_list();


        ArrayList<String> descriptionArray = new ArrayList<>();
        ArrayList<String> detailsArray = new ArrayList<>();

        //Get all information about chargingStation

        String state = getResources().getString(R.string.state) + chargingStations.get(chargingStationID).getState();
        String area = getResources().getString(R.string.district) + chargingStations.get(chargingStationID).getArea();
        String city = getResources().getString(R.string.station_location) + chargingStations.get(chargingStationID).getLocation();
        System.out.println(city);
        String street = getResources().getString(R.string.street) + chargingStations.get(chargingStationID).getStreet();
        String status = getResources().getString(R.string.being_used) + getResources().getString(R.string.used);
        if(chargingStations.get(chargingStationID).isIs_used() == false){
            status = getResources().getString(R.string.being_used) + getResources().getString(R.string.not_used);
        }
        String power =  getResources().getString(R.string.power_station) + chargingStations.get(chargingStationID).getConn_power();
        String module_type =  getResources().getString(R.string.module_type)+ chargingStations.get(chargingStationID).getModule_type();
        String connNumber =  getResources().getString(R.string.connection_number) + chargingStations.get(chargingStationID).getNumber_of_connections();

        descriptionArray.add(state);
        descriptionArray.add(area);
        descriptionArray.add(city);
        descriptionArray.add(street);
        descriptionArray.add(status);
        descriptionArray.add(power);
        descriptionArray.add(module_type);
        descriptionArray.add(connNumber);



        String installation_date = getResources().getString(R.string.installation_date) + chargingStations.get(chargingStationID).getInstallation_date();

        String plugType1 = getResources().getString(R.string.connection_type)  + "1:   ";
        String plugType2 = getResources().getString(R.string.connection_type)  + "2:   ";
        String plugType3 = getResources().getString(R.string.connection_type)  + "3:   ";
        String plugType4 = getResources().getString(R.string.connection_type)  + "4:   ";

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

        String power1_str = getResources().getString(R.string.power)  +  "1:   " + Double.toString(power1);
        String power2_str = getResources().getString(R.string.power)  +  "2:   " + Double.toString(power2);
        String power3_str = getResources().getString(R.string.power)  +  "3:   " + Double.toString(power3);
        String power4_str = getResources().getString(R.string.power)  +  "4:   " + Double.toString(power4);






        System.out.println("Plugtypes1"  + plugTypes3.toString());
        if(!plugTypes1.toString().equals("Plugtypes1[null]")){
            detailsArray.add(plugType1);
        }
        if(!plugTypes2.toString().equals("Plugtypes2[null]")){
            detailsArray.add(plugType2);
        }
        if(!plugTypes3.toString().equals("Plugtypes3[null]")){
            detailsArray.add(plugType3);
        }
        if(!plugTypes4.toString().equals("Plugtypes4[null]")){
            detailsArray.add(plugType4);
        }






        if(!power1_str.equals("Stromstärke Anschluss 1: 0.0")){
            detailsArray.add(power1_str);
        }
        if(!power2_str.equals("Stromstärke Anschluss 2: 0.0")){
            detailsArray.add(power2_str);
        }
        System.out.println("power3_str: " + power3_str);
        if(!power3_str.equals("Stromstärke Anschluss 3: 0.0")){
            detailsArray.add(power3_str);
        }
        if(!power4_str.equals("Stromstärke Anschuss 4: 0.0")){
            detailsArray.add(power4_str);
        }


        detailsArray.add(installation_date);


        ArrayAdapter<String> detailsAdapter = new ArrayAdapter<String>(ViewChargingStationActivity.this, R.layout.custom_text_view, detailsArray);
        ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<String>(ViewChargingStationActivity.this, R.layout.custom_text_view, descriptionArray);
        detailsList.setAdapter(detailsAdapter);
        descriptionList.setAdapter(descriptionAdapter);



        addFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());



                System.out.println("Folgende Ladestation wurde aufgerufen: ");
                System.out.println(chargingStationID);


                System.out.println("Folgend Ladestation wurde gefunden: ");
                System.out.println(chargingStations.get(chargingStationID).getLocation());
                favoriteStations.add(chargingStations.get(chargingStationID-1));
                favoritesRef.setValue(favoriteStations);

            }
        });

        addDefect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());
                defectStations.add(chargingStations.get(chargingStationID-1));
                defectRef.setValue(defectStations);

            }
        });


        useStationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());
                ChargingStation station = chargingStations.get(chargingStationID);
                if(station.isIs_used() == true){
                    station.setIs_used(false);
                    Toast.makeText(getApplicationContext(), "Sie haben die Station verlassen", Toast.LENGTH_SHORT).show();
                }else {
                    station.setIs_used(true);
                    Toast.makeText(getApplicationContext(), "Sie benutzen nun die Station", Toast.LENGTH_SHORT).show();
                }


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
    private void getDefectStations(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                defectStations.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    ChargingStation station = postSnapshot.getValue(ChargingStation.class);
                    defectStations.add(station);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });
    }
}

