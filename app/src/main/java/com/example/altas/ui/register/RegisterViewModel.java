package com.example.altas.ui.register;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.altas.repositories.AuthenticationRepository;

/**
 * public class RegisterViewModel that extends ViewModel
 */
public class RegisterViewModel extends ViewModel {

    private AuthenticationRepository authRepo;

    /**
     * RegisterViewModel constructor
     */
    public RegisterViewModel() {

        // Initialize variables
        authRepo = new AuthenticationRepository();
    }

    /**
     * Calls authRepo to register user
     * @param usersEmail user's email
     * @param usersPassword user's password
     */
    public boolean registerUser(String usersEmail, String usersPassword) {

        // Register user
        boolean successfulRegistration = authRepo.registerUser(usersEmail, usersPassword);
        return successfulRegistration;
    }
}
