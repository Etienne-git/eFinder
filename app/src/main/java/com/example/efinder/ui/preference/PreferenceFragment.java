package com.example.efinder.ui.preference;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.efinder.LoginActivity;
import com.example.efinder.MainActivity;
import com.example.efinder.R;
import com.example.efinder.databinding.FragmentPreferenceBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PreferenceFragment extends Fragment  {

    private PreferenceViewModel preferenceViewModel;
    private FragmentPreferenceBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preferenceViewModel =
                new ViewModelProvider(this).get(PreferenceViewModel.class);


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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}