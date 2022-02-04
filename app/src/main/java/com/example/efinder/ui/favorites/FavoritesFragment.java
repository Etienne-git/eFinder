package com.example.efinder.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.efinder.ChargingStation;
import com.example.efinder.MainActivity;
import com.example.efinder.R;
import com.example.efinder.User;
import com.example.efinder.ViewChargingStationActivity;
import com.example.efinder.databinding.FragmentFavoritesBinding;

import java.util.ArrayList;
/**
 * fragment where a user can see and access his favorite charging stations
 */
public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    private ListView favorite_listView;
    private FragmentFavoritesBinding binding;
    private User currentUser;
    private ArrayList<ChargingStation> chargingStations = new ArrayList<>();
    /**
     * initializes the view and retrieves current User from Main Activity
     * @param inflater instantiates xml file into corresponding view object
     * @param container container for fragment to be inserted
     * @param savedInstanceState Bundle passed back to onCreate if activity needs to be recreated
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel =
                new ViewModelProvider(this).get(FavoritesViewModel.class);

        currentUser = ((MainActivity)getActivity()).getCurrentUser();

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
    /**
     * initializes the list view, fills it trough set function and initializes the removal button
     * @param view View of fragment
     * @param savedInstanceState Bundle passed back to onCreate if activity needs to be recreated
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        favorite_listView = (ListView) getView().findViewById(R.id.favorite_listview);
        //ArrayList<ChargingStation> chargingStations = DownloadService.getStations();
        ArrayList<String> chargingStationList = new ArrayList();


        setFavoritesList(chargingStationList);


    }
    /**
     * fills the list with the favorite stations for screen output
     * @param chargingStationList the list that gets filled for output
     */
    private void setFavoritesList(ArrayList<String> chargingStationList) {
        if (currentUser.favorites != null) {
            //Create Array for ListView
            for (int i = 0; i < currentUser.favorites.size(); i++) {


                    String chargingStationOverview =
                            "\n"
                                    + getResources().getString(R.string.address)
                                    + currentUser.favorites.get(i).getLocation()
                                    + " "
                                    + currentUser.favorites.get(i).getStreet()
                                    + "  "
                                    + currentUser.favorites.get(i).getNumber()
                                    + "\n\n" + getResources().getString(R.string.module_type) + currentUser.favorites.get(i).getModule_type();

                    chargingStationList.add(chargingStationOverview);

            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.custom_text_view, chargingStationList);
            favorite_listView.setAdapter(arrayAdapter);

            //Show single ChargingStation if User selects it
            setFavoritesOnItemClick();
        }
    }
    /**
     * sets onClick for each element of favorite list view. Redirects to the corresponding view-
     * ChargingStationActivity.
     */

    private void setFavoritesOnItemClick() {
        favorite_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity().getApplicationContext(), ViewChargingStationActivity.class);
                //intent.putExtra("id", chargingStationList.get(position).split(" ")[0]);
                intent.putExtra("id", Integer.toString(position));
                intent.putExtra("role","favorites");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}