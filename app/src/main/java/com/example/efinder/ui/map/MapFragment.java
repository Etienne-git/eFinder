package com.example.efinder.ui.map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.efinder.ChargingStation;
import com.example.efinder.R;
import com.example.efinder.StationManager;
import com.example.efinder.ViewChargingStationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.example.efinder.databinding.FragmentMapBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback  {

    private MapViewModel mapViewModel;
    private FragmentMapBinding binding;
    GoogleMap mGoogleMap;
    private LocationRequest locationRequest;
    TextView tvProgressLabel;
    Button searchButton;
    FloatingActionButton focusButton;
    Button changeLocationBtn;
    double distance = 10000;
    int distanceMax = 100000;
    ArrayList<Marker> markerArray = new ArrayList();
    EditText latitudeInput, longitudeInput;
    LatLng home;
    Location homeLocation;



    SupportMapFragment mapFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);



        home = new LatLng(49.873187637553286, 8.641962680299816);
        homeLocation = new Location("home");
        homeLocation.setLatitude(49.873187637553286);
        homeLocation.setLongitude(8.641962680299816);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        SeekBar seekBar = getView().findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        double distanceTemp= seekBar.getProgress();
        tvProgressLabel = getView().findViewById(R.id.textView);
        tvProgressLabel.setText("Distance: "  + distanceTemp + " km");

        searchButton = getView().findViewById(R.id.searchBtn);
        focusButton = getView().findViewById(R.id.focusBtn);
        changeLocationBtn = getView().findViewById(R.id.changeLocationBtn);
        longitudeInput = getView().findViewById(R.id.longitudeBtn);
        latitudeInput = getView().findViewById(R.id.latitudeBtn);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick (View view){
                double distanceCurrent = seekBar.getProgress() * 1000;
                System.out.println("Current progress on seekBar: " + distanceCurrent);

                Context context = getActivity().getApplicationContext();

                CharSequence text1 = "Starte Suche in Umkreis von " + seekBar.getProgress() + " km";
                int duration1 = Toast.LENGTH_SHORT;
                Toast toast1 = Toast.makeText(context, text1, duration1);
                toast1.show();
                InsertChargingStationsInMap(distanceCurrent, mGoogleMap);


                CharSequence text2 = "Suche beendet";
                int duration2 = Toast.LENGTH_SHORT;

                Toast toast2 = Toast.makeText(context, text2, duration2);
                toast2.show();

            }
        });

        focusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //home = new LatLng(49.873187637553286, 8.641962680299816);
                float zoomLevel = 16.0f;
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoomLevel));
            }
        });




        changeLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String longitude = longitudeInput.getText().toString();
                String latitude = latitudeInput.getText().toString();
                home = new LatLng(Float.parseFloat(latitude), Float.parseFloat(longitude));
                homeLocation.setLatitude(Float.parseFloat(latitude));
                homeLocation.setLongitude(Float.parseFloat(longitude));
                InsertChargingStationsInMap(distance, mGoogleMap);
                float zoomLevel = 16.0f;
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoomLevel));

            }
        });





    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {


        InsertChargingStationsInMap(distance,googleMap);



        Marker homeMarker = mGoogleMap.addMarker(new MarkerOptions().position(home).title("Home"));
        homeMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(home));

        //Zu jetzigem Standpunkt zoomen
        float zoomLevel = 16.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoomLevel));
        googleMap.getUiSettings().setZoomControlsEnabled(true);



        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getActivity(), "Infowindow clicked", Toast.LENGTH_SHORT).show();
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                int markerIndex = Integer.parseInt(marker.getTag().toString());

                ChargingStation station = StationManager.getStation_list().get(markerIndex);
                String is_used_str = "";
                if(station.isIs_used() == true){
                    is_used_str = getResources().getString(R.string.snackbar_used);
                }else{
                    is_used_str = getResources().getString(R.string.snackbar_notused);
                };

                Snackbar.make(getActivity().findViewById(R.id.mainLayout), is_used_str , Snackbar.LENGTH_LONG).setAction("SHOW DETAILS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), ViewChargingStationActivity.class);
                        intent.putExtra("id", Integer.toString(markerIndex));
                        intent.putExtra("role","search");
                        startActivity(intent);
                    }
                }).show();

                //Toast.makeText(getActivity(), "Marker Clicked " + marker.getTag(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }




    private boolean isGPSEnabled() {
        LocationManager locationManager = null;

        boolean isEnabled = false;

        if(locationManager == null){
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }



    public ArrayList<Marker> InsertChargingStationsInMap(double distance, GoogleMap googleMap) {




        mGoogleMap = googleMap;
        mGoogleMap.clear();
        Marker marker_temp;


        //home = new LatLng(49.873187637553286, 8.641962680299816);
        Marker homeMarker = mGoogleMap.addMarker(new MarkerOptions().position(home).title("Home"));
        homeMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        //Zu jetzigem Standpunkt zoomen
        float zoomLevel = 16.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoomLevel));
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        ArrayList<ChargingStation> chargingStations =  StationManager.getStation_list();

        //LatLng home = new LatLng(49.873187637553286, 8.641962680299816);
        System.out.println("Home: "  + home.toString());

        double distanceTemp = 0;
        for(int i = 0; i < chargingStations.size(); i++){
            double lat = chargingStations.get(i).getLat();
            double lon = chargingStations.get(i).getLon();




            LatLng chargingStation = new LatLng(lat, lon);

            SphericalUtil.computeDistanceBetween(chargingStation, home);


            //Check if chargingStation is less than 50km away from home
            /*
            Location selected_location = new Location("home");
            selected_location.setLatitude(49.873187637553286);
            selected_location.setLongitude(8.641962680299816);
            */


            Location near_locations = new Location("ChargingStation");
            near_locations.setLatitude(lat);
            near_locations.setLongitude(lon);

            distanceTemp = homeLocation.distanceTo(near_locations);


            if(distanceTemp < distance){
                String address = "Ladesäule "
                        + chargingStations.get(i).getLocation()
                        + "  "
                        + chargingStations.get(i).getState()
                        + "  "
                        + chargingStations.get(i).getStreet()
                        + " "
                        + chargingStations.get(i).getNumber();

                marker_temp = mGoogleMap.addMarker(new MarkerOptions().position(chargingStation).title(address));
                marker_temp.showInfoWindow();
                marker_temp.setTag(i);
                markerArray.add(marker_temp);


            }

        }
        return markerArray;
    }



    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tvProgressLabel.setText("Distance: " + progress + " km");
            System.out.println("Search geladen....");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


}

