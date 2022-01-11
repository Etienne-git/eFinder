package com.example.efinder.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.efinder.ChargingStation;
import com.example.efinder.MainActivity;
import com.example.efinder.R;
import com.example.efinder.StationManager;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.example.efinder.databinding.FragmentMapBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mapViewModel;
    private FragmentMapBinding binding;
    GoogleMap mGoogleMap;
    private LocationRequest locationRequest;


    SupportMapFragment mapFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);


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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {


        InsertChargingStationsInMap(googleMap);


        //Placeholder for focusing current place:

        LatLng home = new LatLng(49.873187637553286, 8.641962680299816);
        mGoogleMap.addMarker(new MarkerOptions().position(home).title("Home"));

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(home));



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



    public void InsertChargingStationsInMap(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        ArrayList<ChargingStation> chargingStations =  StationManager.getStation_list();


        for(int i = 0; i < chargingStations.size(); i++){
            double lat = chargingStations.get(i).getLat();
            double lon = chargingStations.get(i).getLon();

            LatLng chargingStation = new LatLng(lat, lon);

            String address = "Ladesäule "
                    + chargingStations.get(i).getLocation()
                    + "  "
                    + chargingStations.get(i).getState()
                    + "  "
                    + chargingStations.get(i).getStreet()
                    + " "
                    + chargingStations.get(i).getNumber();

            mGoogleMap.addMarker(new MarkerOptions().position(chargingStation).title(address));

        }



    }




}

