package com.example.altas.ui.profile;

import androidx.lifecycle.ViewModel;

import com.example.altas.repositories.AuthenticationRepository;

/**
 * public class ProfileViewModel that extends ViewModel
 */
public class ProfileViewModel extends ViewModel {

    private final AuthenticationRepository authRepo;

    /**
     * ProfileViewModel constructor
     */
    public ProfileViewModel() {

        // Initialize variables
        authRepo = new AuthenticationRepository();
    }
}
