package com.example.altas.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.example.altas.Models.LoginInput;
import com.example.altas.Models.User;
import com.example.altas.repositories.AuthenticationRepository;


/**
 * public class LoginViewModel that extends ViewModel
 */
public class LoginViewModel extends ViewModel {

    private AuthenticationRepository authRepository;

    public MutableLiveData<User> userMutableLiveData;
    /**
     * LoginViewModel constructor
     */
    public LoginViewModel() {
        // Initialize variables
        authRepository = new AuthenticationRepository();
        userMutableLiveData = new MutableLiveData<>();
    }

    /**
     * Call authRepo to log in user with given email and password
     *
     * @param loginInput    user's email and password
     * @param queue API request queue
     */
    public boolean loginUser(LoginInput loginInput, RequestQueue queue) {

        authRepository.loginUser(loginInput, queue);
        authRepository.userMutableLiveData.observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userMutableLiveData.setValue(user);
            }
        });

        // Log in user
        return true ;
    }
}
