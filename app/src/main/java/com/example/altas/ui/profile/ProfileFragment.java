package com.example.altas.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.altas.MainActivity;
import com.example.altas.R;

/**
 * public class ProfileFragment that extends Fragment,
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize variables
        View profileFragmentRoot = inflater.inflate(R.layout.fragment_profile, container, false);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        // Set up Action bar
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Disable back button in toolbar and change it's title if it exists
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.profile_title);
        }

        return profileFragmentRoot;
    }

}
