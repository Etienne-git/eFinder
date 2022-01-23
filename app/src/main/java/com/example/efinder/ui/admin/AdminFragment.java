package com.example.efinder.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efinder.ChargingStation;
import com.example.efinder.MainActivity;
import com.example.efinder.R;
import com.example.efinder.RecyclerViewAdapter;
import com.example.efinder.databinding.FragmentAdminBinding;

import java.util.ArrayList;

public class AdminFragment extends Fragment {

    private AdminViewModel adminViewModel;
    private FragmentAdminBinding binding;
    private RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private ArrayList<ChargingStation> defectStations = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminViewModel =
                new ViewModelProvider(this).get(AdminViewModel.class);

        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        defectStations = ((MainActivity)getActivity()).defectStations;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.repair_recyclerView);
        ArrayList<String> defectStationsList = new ArrayList();
        adapter = new RecyclerViewAdapter(R.layout.list_item, defectStationsList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < defectStations.size(); i++) {

            String chargingStationOverview =
                    defectStations.get(i).getId()
                            + " "
                            + defectStations.get(i).getLocation()
                            + defectStations.get(i).getStreet()
                            + "  "
                            + defectStations.get(i).getNumber()
                            + "\n" + defectStations.get(i).getOperator();

            defectStationsList.add(chargingStationOverview);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}