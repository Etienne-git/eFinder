package com.example.efinder.ui.preference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.efinder.MainActivity;
import com.example.efinder.R;
import com.example.efinder.databinding.FragmentPreferenceBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/**
 * fragment where a user can see and change his admin status and where he is able to log out his
 * user
 */
public class PreferenceFragment extends Fragment  {

    private PreferenceViewModel preferenceViewModel;
    private FragmentPreferenceBinding binding;
    private String id;
    private boolean admin;
    Switch adminSwitch;


    /**
     * initializes the view and retrieves admin rights status from Main Activity
     * @param inflater instantiates xml file into corresponding view object
     * @param container container for fragment to be inserted
     * @param savedInstanceState Bundle passed back to onCreate if activity needs to be recreated
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preferenceViewModel =
                new ViewModelProvider(this).get(PreferenceViewModel.class);
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://efinder-1640105181864-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference currentUserDb = database.getReference().child("users").child(id).child("admin");
        admin = ((MainActivity)getActivity()).getCurrentUser().getIsAdmin();

        View view = inflater.inflate(R.layout.fragment_preference, container, false);

        binding = FragmentPreferenceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adminSwitch = (Switch) root.findViewById(R.id.admin_rights);

        if(!admin)
            adminSwitch.setChecked(false);

        changeAdminRights(adminSwitch, currentUserDb);
        final Button btnSignOut = root.findViewById(R.id.sign_out);
        setBtnSignOut(btnSignOut);

        return root;
    }
    /**
     * set switch for admin rights and changes status change of admin rights in the database
     * @param adminSwitch the switch to change admin from false to true and vice versa
     * @param ref database reference to save status of admin rights
     */
    private void changeAdminRights(Switch adminSwitch, DatabaseReference ref) {
        adminSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ref.setValue(true);
                } else {
                    ref.setValue(false);
                }
            }
        });
    }
    /**
     * sets Button sign out so sign out function gets called from Main Activity
     * @param btnSignOut the sign out butten
     */

    private void setBtnSignOut(Button btnSignOut) {
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).signOut();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
