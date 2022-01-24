package com.example.efinder.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminFragment extends Fragment implements RecyclerViewAdapter.RecyclerViewClickListener {

    private AdminViewModel adminViewModel;
    private FragmentAdminBinding binding;
    private Button btnAddDefect;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<ChargingStation> defectStations = new ArrayList<>();
    private int pos;
    DatabaseReference defectRef;
    private boolean adminRights;
    RelativeLayout deniedAccess;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminViewModel =
                new ViewModelProvider(this).get(AdminViewModel.class);

        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        deniedAccess = root.findViewById(R.id.access_denied);
        btnAddDefect = root.findViewById(R.id.btn_report_defect);
        final FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.firebase_link));
        defectRef = database.getReference().child("defectStations");

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        defectStations = ((MainActivity)getActivity()).defectStations;
        adminRights = ((MainActivity)getActivity()).adminRights;

        if(adminRights){
            deniedAccess.setVisibility(View.GONE);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.repair_recyclerView);
        ArrayList<String> defectStationsList = new ArrayList();
        adapter = new RecyclerViewAdapter(R.layout.list_item, this, defectStationsList);

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
        btnAddDefect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adminRights) {
                    defectStations.remove(pos);
                    defectStationsList.remove(pos);
                    defectRef.setValue(defectStations);
                    adapter.notifyItemRemoved(pos);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void recyclerViewListClicked(View v, int position){
        pos = position;
    }

}