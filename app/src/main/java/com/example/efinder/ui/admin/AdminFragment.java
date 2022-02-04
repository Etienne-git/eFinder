package com.example.efinder.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
/**
 * fragment where an admin can report a defective station as repaired
 */
public class AdminFragment extends Fragment implements RecyclerViewAdapter.RecyclerViewClickListener {

    private AdminViewModel adminViewModel;
    private FragmentAdminBinding binding;
    private Button btRemoveDefect;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<ChargingStation> defectStations = new ArrayList<>();
    private int pos;
    DatabaseReference defectRef;
    private boolean adminRights;
    RelativeLayout deniedAccess;
    /**
     * initializes the view and firebase instance to save a repaired station. Retrieves admin
     * rights and defective stations from main activity
     * @param inflater instantiates xml file into corresponding view object
     * @param container container for fragment to be inserted
     * @param savedInstanceState Bundle passed back to onCreate if activity needs to be recreated
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminViewModel =
                new ViewModelProvider(this).get(AdminViewModel.class);

        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        deniedAccess = root.findViewById(R.id.access_denied);
        btRemoveDefect = root.findViewById(R.id.btn_report_defect);
        final FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.firebase_link));
        defectRef = database.getReference().child("defectStations");


        defectStations = ((MainActivity)getActivity()).defectStations;
        adminRights = ((MainActivity)getActivity()).adminRights;

        if(adminRights){
            deniedAccess.setVisibility(View.GONE);
        }

        return root;
    }
    /**
     * initializes the recycler view, fills it and initializes the removal button
     * @param view View of fragment
     * @param savedInstanceState Bundle passed back to onCreate if activity needs to be recreated
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.repair_recyclerView);
        ArrayList<String> defectStationsList = new ArrayList();
        adapter = new RecyclerViewAdapter(R.layout.list_item, this, defectStationsList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        fillDefectStationList(defectStationsList);
        setBtRemoveDefect(defectStationsList);
    }
    /**
     * sets button to a remove a defective station and saves the removal in the database
     * @param defectStationsList list of defect stations so the station can also be removed here
     */
    private void setBtRemoveDefect(ArrayList<String> defectStationsList) {
        btRemoveDefect.setOnClickListener(new View.OnClickListener() {
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
    /**
     * fills the recycler view list with the defective stations for screen output
     * @param defectStationsList the list that gets filled for output
     */
    private void fillDefectStationList(ArrayList<String> defectStationsList) {
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
    /**
     * sets the position of the clicked item of the recycler view
     * @param v the current View
     * @param position the position of the clicked item
     */
    public void recyclerViewListClicked(View v, int position){
        pos = position;
    }

}