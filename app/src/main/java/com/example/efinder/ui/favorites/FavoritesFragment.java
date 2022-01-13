package com.example.efinder.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.efinder.ChargingStation;
import com.example.efinder.FavoriteManager;
import com.example.efinder.MainActivity;
import com.example.efinder.R;
import com.example.efinder.StationManager;
import com.example.efinder.ViewChargingStationActivity;
import com.example.efinder.databinding.FragmentFavoritesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    private ListView favorite_listView;
    private FragmentFavoritesBinding binding;
    private ArrayList<ChargingStation> chargingStations = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel =
                new ViewModelProvider(this).get(FavoritesViewModel.class);

        chargingStations = ((MainActivity)getActivity()).favoriteStations;

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        /*
        final TextView textView = binding.textFavorites;
        favoritesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        favorite_listView = (ListView) getView().findViewById(R.id.favorite_listview);
        //ArrayList<ChargingStation> chargingStations = DownloadService.getStations();
        ArrayList<String> chargingStationList = new ArrayList();


        if (chargingStations != null) {
            //Create Array for ListView
            for (int i = 0; i < chargingStations.size(); i++) {

                String chargingStationOverview =
                        chargingStations.get(i).getId()
                                + " "
                                + chargingStations.get(i).getLocation()
                                + chargingStations.get(i).getStreet()
                                + "  "
                                + chargingStations.get(i).getNumber()
                                + "\n" + chargingStations.get(i).getOperator();

                chargingStationList.add(chargingStationOverview);
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.custom_text_view, chargingStationList);
            favorite_listView.setAdapter(arrayAdapter);

            //Show single ChargingStation if User selects it
            favorite_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ViewChargingStationActivity.class);
                    intent.putExtra("id", chargingStationList.get(position).split(" ")[0]);
                    startActivity(intent);
                }
            });
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}