package com.example.altas.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.example.altas.Models.LoginInput;
import com.example.altas.Models.User;
import com.example.altas.repositories.AuthenticationRepository;

/**
 * public class RegisterViewModel that extends ViewModel
 */
public class RegisterViewModel extends ViewModel {

    private AuthenticationRepository authRepo;
    MutableLiveData<User> userMutableLiveData;

    /**
     * RegisterViewModel constructor
     */
    public RegisterViewModel() {

        // Initialize variables
        authRepo = new AuthenticationRepository();
        userMutableLiveData = new MutableLiveData<>();
    }

    /**
     * Calls authRepo to register user
     * @param loginInput user's email and password
     * @param queue API request queue
     */
    void registerUser(LoginInput loginInput, RequestQueue queue) {

        // Request for user registration
        authRepo.registerUser(loginInput, queue);

        // Observe for registration status
        authRepo.userMutableLiveData.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userMutableLiveData.setValue(user);
            }
        });
    }
}
