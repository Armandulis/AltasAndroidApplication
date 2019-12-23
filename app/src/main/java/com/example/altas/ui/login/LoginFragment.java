package com.example.altas.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.altas.MainActivity;
import com.example.altas.Models.LoginInput;
import com.example.altas.Models.User;
import com.example.altas.R;

/**
 * public class LoginFragment that extends Fragment,
 */
public class LoginFragment extends Fragment {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewError;

    private Button buttonShowPurchases;

    private LoginViewModel mViewModel;

    private RequestQueue queue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        User user = User.getInstance();
        if (user.email != null && !user.email.equals("")) {
            // Navigate user to profile fragment
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_profile);
        }

        // Initialize variables
        View loginFragmentRoot = inflater.inflate(R.layout.fragment_login, container, false);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        // Implement redirect if user is logged in

        editTextEmail = loginFragmentRoot.findViewById(R.id.edit_text_login_email);
        editTextPassword = loginFragmentRoot.findViewById(R.id.edit_text_login_password);
        textViewError = loginFragmentRoot.findViewById(R.id.text_view_login_error);
        Button buttonLogin = loginFragmentRoot.findViewById(R.id.button_login_login);
        TextView textRegister = loginFragmentRoot.findViewById(R.id.text_view_register_here);
        buttonShowPurchases = loginFragmentRoot.findViewById(R.id.button_login_show_purchases);


        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(getContext());

        // Set up Action bar
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Disable back button in toolbar and change it's title if it exists
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.login_title);
        }

        // Add on click listeners
        buttonLogin.setOnClickListener(handleButtonLoginListener());
        textRegister.setOnClickListener(handleButtonRegisterListener());
        buttonShowPurchases.setOnClickListener(handleShowPurchasesListener());

        return loginFragmentRoot;
    }

    /**
     * Returns a click listener that handles on click action
     */
    private View.OnClickListener handleShowPurchasesListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate user to profile fragment
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_profile);
            }
        };
    }

    /**
     * Returns a click listener that handles on click action
     */
    private View.OnClickListener handleButtonRegisterListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegisterFragment();
            }
        };
    }

    /**
     * Navigates user to RegisterFragment
     */
    private void navigateToRegisterFragment() {
        // Navigate user to register fragment
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.navigation_register);
    }

    /**
     * Returns onclick listener that validates user's input and logins user
     */
    private View.OnClickListener handleButtonLoginListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get user's provided data
                String userEmail = editTextEmail.getText().toString();
                String userPassword = editTextPassword.getText().toString();

                // Check if submitted email is not empty and is valid
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!userEmail.matches(emailPattern) || userEmail.equals("")) {
                    editTextEmail.requestFocus();
                    editTextEmail.setError(getString(R.string.incorrect_email));
                    return;
                }

                // Check if submitted password is not empty
                if (userPassword.equals("")) {
                    editTextPassword.requestFocus();
                    editTextPassword.setError(getString(R.string.incorrect_password));
                    return;
                }

                LoginInput loginInput = new LoginInput();
                loginInput.email = userEmail;
                loginInput.password = userPassword;

                // Try to log in user
                loginUser(loginInput);
            }
        };
    }

    /**
     * Tries to log in user, if it fails, inform user
     *
     * @param loginInput user's email and password
     */
    private void loginUser(LoginInput loginInput) {

        // Log in user
        mViewModel.loginUser(loginInput, queue);

        // Observe for login response
        mViewModel.userMutableLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // Check if user was logged in
                if (user.email != null && !user.email.equals("")) {
                    // Navigate user to profile fragment
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_profile);
                }
                // Show Error message
                textViewError.setText(getString(R.string.login_failed_login));
            }
        });

    }
}
