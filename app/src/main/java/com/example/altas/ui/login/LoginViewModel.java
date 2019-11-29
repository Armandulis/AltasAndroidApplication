package com.example.altas.ui.login;

import androidx.lifecycle.ViewModel;

import com.example.altas.repositories.AuthenticationRepository;


/**
 * public class LoginViewModel that extends ViewModel
 */
public class LoginViewModel extends ViewModel {

    private AuthenticationRepository authRepository;

    /**
     * LoginViewModel constructor
     */
    public LoginViewModel() {
        // Initialize variables
        authRepository = new AuthenticationRepository();
    }

    /**
     * Call authRepo to log in user with given email and password
     *
     * @param userEmail    user's email
     * @param userPassword user's password
     */
    public boolean loginUser(String userEmail, String userPassword) {
        // Log in user
        return authRepository.loginUser(userEmail, userPassword);
    }
}