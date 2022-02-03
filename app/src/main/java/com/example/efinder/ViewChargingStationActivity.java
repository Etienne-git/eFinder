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

import com.example.efinder.ui.favorites.FavoritesFragment;
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
    Button addOrRemoveFavorite;
    Button addDefect;
    GoogleMap mGoogleMap;
    String id;
    Button useStationBtn;
    Boolean isFavorite  = false;
    int chargingStationID;
    ArrayList<ChargingStation> favoriteStations = new ArrayList<>();
    ArrayList<ChargingStation> defectStations = new ArrayList<>();
    ArrayList<ChargingStation> chargingStations = new ArrayList<>();


    @Override
    public void onMapReady(GoogleMap googleMap) {


        //Get location of chargingStation
        Intent intent = getIntent();
        chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());
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
        addOrRemoveFavorite = findViewById(R.id.addFavorite);
        addDefect = findViewById(R.id.addDefect);
        detailsList = findViewById(R.id.details_list);
        descriptionList = findViewById(R.id.description_list);
        useStationBtn = findViewById(R.id.useStation);
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.firebase_link));
        DatabaseReference favoritesRef = database.getReference().child("users").child(id).child("favorites");
        DatabaseReference defectRef = database.getReference().child("defectStations");


        Intent intent = getIntent();
        String role = intent.getStringExtra("role").toString();


        if(role.equals("favorites")){
            chargingStations = MainActivity.favoriteStations;
        }else {
            chargingStations = SearchFragment.getInstance().getChargingStations();
        }





        System.out.println("Alle Favoriten - Debug: ");
        for(int i = 0; i < chargingStations.size(); i++){
            System.out.println("Favorite: " + chargingStations.get(i).getLocation());
        }



        getFavoriteStations(favoritesRef);
        getDefectStations(defectRef);


        System.out.println("Debug: " + intent.getStringExtra("id"));
        chargingStationID = (int) Integer.valueOf(intent.getStringExtra("id").toString());

        boolean chargerIsUsed = chargingStations.get(chargingStationID).isIs_used();

        if(chargerIsUsed == true){
            useStationBtn.setText(getResources().getString((R.string.leave_station)));
        }else {
            useStationBtn.setText(getResources().getString(R.string.use_this_station));
        }



        boolean chargerIsFavorite = chargingStations.get(chargingStationID).isIs_favorite();
        if(chargerIsFavorite == false){
            addOrRemoveFavorite.setText(getResources().getString(R.string.addFavorite_string));
        }else {
            addOrRemoveFavorite.setText(getResources().getString(R.string.remove_favorite));
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


        detailsArray.add(installation_date);


        ArrayAdapter<String> detailsAdapter = new ArrayAdapter<String>(ViewChargingStationActivity.this, R.layout.custom_text_view, detailsArray);
        ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<String>(ViewChargingStationActivity.this, R.layout.custom_text_view, descriptionArray);
        detailsList.setAdapter(detailsAdapter);
        descriptionList.setAdapter(descriptionAdapter);



        addOrRemoveFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());

                    if(!favoriteStations.isEmpty() && chargingStationID < favoriteStations.size()) {
                        if (favoriteStations.get(chargingStationID).isIs_favorite() == false) {
                            //chargingStations.get(chargingStationID).set_favorite(true);
                            favoriteStations.get(chargingStationID).set_favorite(true);
                            favoriteStations.add(chargingStations.get(chargingStationID));
                            addOrRemoveFavorite.setText(getResources().getString(R.string.remove_favorite));
                        } else {
                            System.out.println("Remove: ");
                            System.out.println(chargingStations.get(chargingStationID).getLocation());
                            System.out.println(chargingStations.get(chargingStationID).getStreet());
                            //chargingStations.get(chargingStationID).set_favorite(false);
                            favoriteStations.get(chargingStationID).set_favorite(false);
                            //favoriteStations.remove(chargingStations.get(chargingStationID));
                            favoriteStations.remove(chargingStationID);
                            System.out.println(favoriteStations.size());
                            addOrRemoveFavorite.setText(getResources().getString(R.string.addFavorite_string));
                        }
                    }else {
                        System.out.println("Ich bin hier!!!!");
                        //chargingStations.get(chargingStationID).set_favorite(true);
                        favoriteStations.add(chargingStations.get(chargingStationID));
                        chargingStations.get(chargingStationID).set_favorite(true);
                        //favoriteStations.get(0).set_favorite(true);
                        addOrRemoveFavorite.setText(getResources().getString(R.string.remove_favorite));
                    }


                    favoritesRef.setValue(favoriteStations);
            }
        });

        addDefect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int chargingStationID = Integer.valueOf(intent.getStringExtra("id").toString());
                defectStations.add(chargingStations.get(chargingStationID));
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
                if(!favoriteStations.isEmpty()) {
                    for (int i = 0; i < favoriteStations.size(); i++)
                        if (favoriteStations.get(i).getId() == chargingStationID) {
                            addOrRemoveFavorite.setText(getResources().getString(R.string.remove_favorite));
                            isFavorite = true;
                        }
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

