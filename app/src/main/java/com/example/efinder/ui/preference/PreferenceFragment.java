package com.example.efinder.ui.preference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.efinder.MainActivity;
import com.example.efinder.R;
import com.example.efinder.databinding.FragmentPreferenceBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PreferenceFragment extends Fragment  {

    private PreferenceViewModel preferenceViewModel;
    private FragmentPreferenceBinding binding;
    private String id;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preferenceViewModel =
                new ViewModelProvider(this).get(PreferenceViewModel.class);
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://efinder-1640105181864-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference currentUserDb = database.getReference().child("users").child(id).child("admin");


        View view = inflater.inflate(R.layout.fragment_preference, container, false);

        binding = FragmentPreferenceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        final TextView textView = binding.textPreference;
        preferenceViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Switch admin_switch = (Switch) root.findViewById(R.id.admin_rights);
        changeAdminRights(admin_switch, currentUserDb);
        final Button btnSignOut = root.findViewById(R.id.sign_out);
        UserSignOut(btnSignOut);

        return root;
    }

    private void changeAdminRights(Switch admin_switch, DatabaseReference ref) {
        admin_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ref.setValue(true);
                } else {
                    ref.setValue(false);
                }
            }
        });
    }

    private void UserSignOut(Button btnSignOut) {
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
