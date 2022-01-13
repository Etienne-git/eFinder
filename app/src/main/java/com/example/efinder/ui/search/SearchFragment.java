package com.example.efinder.ui.search;





import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.efinder.ChargingStation;
import com.example.efinder.DownloadService;
import com.example.efinder.R;

import com.example.efinder.StationManager;
import com.example.efinder.ViewChargingStationActivity;
import com.example.efinder.databinding.FragmentSearchBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;
    private ListView listView;
    private EditText editText;
    private Spinner dropdown_menu;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        super.onCreate(savedInstanceState);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);



        editText = (EditText) getView().findViewById(R.id.input);
        listView = (ListView) getView().findViewById(R.id.listview);
        dropdown_menu = (Spinner) getView().findViewById(R.id.dropdown_menu);

        //ArrayList<ChargingStation> chargingStations = DownloadService.getStations();
        ArrayList<ChargingStation> chargingStations = StationManager.getStation_list();
        ArrayList<String> chargingStationList = new ArrayList();

        //Create Array for ListView
        for(int i = 0; i < chargingStations.size(); i++){

            String chargingStationOverview =
                    chargingStations.get(i).getId()
                    + " "
                    + chargingStations.get(i).getLocation()
                    + chargingStations.get(i).getStreet()
                    +  "  "
                    + chargingStations.get(i).getNumber()
                    + "\n" + chargingStations.get(i).getOperator();

            chargingStationList.add(chargingStationOverview);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, chargingStationList);
        listView.setAdapter(arrayAdapter);

        //Show single ChargingStation if User selects it
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ViewChargingStationActivity.class);
                intent.putExtra("id", chargingStationList.get(position).split(" ")[0]);
                startActivity(intent);
            }
        });


        ArrayList<String> searchCriteria = new ArrayList();
        searchCriteria.add("Ort");

        ArrayAdapter<String> Dropdownadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, searchCriteria);
        Dropdownadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dropdown_menu.setAdapter(Dropdownadapter);

        Button b;
        Button download;
        download = getView().findViewById(R.id.search);
        Log.d("Debug:", "Download Button was pressed");



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}