package com.example.altas.ui.register;

import androidx.lifecycle.LiveData;
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
    public MutableLiveData<User> userMutableLiveData;

    /**
     * RegisterViewModel constructor
     */
    public RegisterViewModel() {

        // Initialize variables
        authRepo = new AuthenticationRepository();
        userMutableLiveData = new MutableLiveData();
    }

    /**
     * Calls authRepo to register user
     * @param loginInput user's email and password
     * @param queue API request queue
     */
    public boolean registerUser(LoginInput loginInput, RequestQueue queue) {

        // Register user
        boolean successfulRegistration = authRepo.registerUser(loginInput, queue);

        authRepo.userMutableLiveData.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userMutableLiveData.setValue(user);
            }
        });
        return successfulRegistration;
    }
}
