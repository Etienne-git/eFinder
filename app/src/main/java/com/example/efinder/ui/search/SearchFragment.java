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
    private EditText ortInput;
    Spinner statusInput;
    private Button searchBtn;
    public ArrayList<String> chargingStationList;
    public ArrayList<ChargingStation> chargingStationsResult;
    ArrayList<ChargingStation> chargingStations;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

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




        listView = (ListView) getView().findViewById(R.id.listview);

        statusInput = getView().findViewById(R.id.statusInput);

        ortInput = getView().findViewById(R.id.ortInput);


        //Add Choices to spinner
        String[] arraySpinnner = new String[]{"ja", "nein"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arraySpinnner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusInput.setAdapter(adapter);

        //ArrayList<ChargingStation> chargingStations = DownloadService.getStations();
        chargingStations = StationManager.getStation_list();
        chargingStationList = new ArrayList();

        //Create Array for ListView
        for(int i = 0; i < chargingStations.size(); i++){

            String chargingStationOverview =
                    "\n"
                            + getResources().getString(R.string.address)
                    + chargingStations.get(i).getLocation()
                            + " "
                            + chargingStations.get(i).getStreet()
                            +  "  "
                            + chargingStations.get(i).getNumber()
                            + "\n\n" + getResources().getString(R.string.module_type) + chargingStations.get(i).getModule_type();

            chargingStationList.add(chargingStationOverview);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.custom_text_view, chargingStationList);
        listView.setAdapter(arrayAdapter);

        //Show single ChargingStation if User selects it
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ViewChargingStationActivity.class);
                System.out.println("Position: " + Integer.toString(position));
                intent.putExtra("id", Integer.toString(position));
                intent.putExtra("role", "search");
                startActivity(intent);
            }
        });




        searchBtn = getView().findViewById(R.id.search);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity().getApplicationContext(), "Starte Suche...", Toast.LENGTH_SHORT).show();
                chargingStations = StationManager.getStation_list();
                ArrayList<ChargingStation> results = new ArrayList<ChargingStation>();
                ArrayList<String> displayResults = new ArrayList<String>();
                Boolean resultsNotEmpty = false;
                String ort = ortInput.getText().toString();
                results.clear();

                boolean is_used = false;
                String status = statusInput.getSelectedItem().toString();
                System.out.println("Status: " + status);
                if(status == "ja"){
                    is_used = true;
                }


                System.out.println("Ausgewählt: " + is_used);


                if (ort.matches("") && status.matches("")) {
                    Toast.makeText(getActivity(), "Bitte Auswahl treffen", Toast.LENGTH_SHORT).show();
                }

                //Wenn Ort angegeben
                if (!ort.matches("")) {
                    System.out.println("Ort wurde ausgefüllt");
                    System.out.println("Anzahl: " + chargingStations.size());
                    for (int i = 0; i < chargingStations.size(); i++) {
                        System.out.println("Vergleiche: " + chargingStations.get(i).getLocation().replace(" ", "") + "   " + ort  + " und " + chargingStations.get(i).isIs_used() + " mit " + is_used);
                        if (chargingStations.get(i).getLocation().replace(" ", "").equals(ort.replace(" ", "")) && chargingStations.get(i).isIs_used() == is_used) {
                            System.out.println("Fuege Ladesäule hinzu");
                            System.out.println(chargingStations.get(i).getLocation());
                            results.add(chargingStations.get(i));
                        }
                    }
                    resultsNotEmpty = true;
                }else {
                    System.out.println("Ort wurde nicht ausgefüllt");

                    for (int i = 0; i < chargingStations.size(); i++) {
                        System.out.println("Vergleiche: " + chargingStations.get(i).getLocation().replace(" ", "") + "   " + ort);
                        if (chargingStations.get(i).isIs_used() == is_used) {
                            System.out.println("Fuege Ladesäule hinzu");
                            System.out.println(chargingStations.get(i).getLocation());
                            results.add(chargingStations.get(i));
                        }
                    }
                    resultsNotEmpty = true;
                }



                    if(resultsNotEmpty){
                        chargingStations = results;
                    }


                    for(int i = 0; i < results.size(); i++){
                        String chargingStationOverview =
                                "\n"
                                + getResources().getString(R.string.address)
                                        + results.get(i).getLocation()
                                                + " "
                                        + results.get(i).getStreet()
                                        +  "  "
                                        + results.get(i).getNumber()
                                        + "\n\n" + getResources().getString(R.string.module_type) + results.get(i).getModule_type();

                        displayResults.add(chargingStationOverview);
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.custom_text_view, displayResults);
                    listView.setAdapter(arrayAdapter);
                    System.out.println("Ergebnis: " + results.size());
                    Toast.makeText(getActivity().getApplicationContext(), "Suche beendet", Toast.LENGTH_SHORT).show();

                    System.out.println("++++++++++++++++Debug++++++++++++++++");
                    for(int i = 0; i < results.size(); i++){
                        System.out.println(results.get(i).getLocation());
                    }
                }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public ArrayList<ChargingStation> getChargingStations() {
        return this.chargingStations;
    }


    private static SearchFragment instance = null;

    public static SearchFragment getInstance(){
        return instance;
    }


}