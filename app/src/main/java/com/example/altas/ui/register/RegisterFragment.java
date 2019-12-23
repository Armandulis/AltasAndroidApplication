package com.example.altas.ui.register;

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
 * public class RegisterFragment that extends Fragment,
 */
public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;

    private Button buttonRegister;
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;
    private EditText editTextEmail;
    private TextView textViewError;

    private RequestQueue queue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize variables
        View registerFragmentRoot = inflater.inflate(R.layout.fragment_register, container, false);
        mViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        buttonRegister = registerFragmentRoot.findViewById(R.id.button_register_register);
        editTextPassword = registerFragmentRoot.findViewById(R.id.edit_text_register_password);
        editTextPasswordRepeat = registerFragmentRoot.findViewById(R.id.edit_text_register_password_rep);
        editTextEmail = registerFragmentRoot.findViewById(R.id.edit_text_register_email);
        textViewError = registerFragmentRoot.findViewById(R.id.text_view_register_error);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(getContext());

        // Set up Action bar
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Disable back button in toolbar and change it's title if it exists
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.register_title);
        }

        // Set on click listeners
        buttonRegister.setOnClickListener(handleRegisterButtonListener());

        return registerFragmentRoot;
    }

    /**
     * Handles register button clicked, validates input and tries to register user
     */
    private View.OnClickListener handleRegisterButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get users input
                String usersPassword = editTextPassword.getText().toString();
                String usersPasswordRepeat = editTextPasswordRepeat.getText().toString();
                String usersEmail = editTextEmail.getText().toString();

                // Validate user's and register user
                if (validateInput(usersEmail, usersPassword, usersPasswordRepeat)) {

                    LoginInput loginInput = new LoginInput();
                    loginInput.email = usersEmail;
                    loginInput.password = usersPassword;

                    registerUser(loginInput);
                }
            }
        };
    }

    /**
     * Registers user and navigates user to profile fragment if it was successful
     *
     * @param loginInput    user's email and password
     */
    private void registerUser(LoginInput loginInput) {

        // Register user
        mViewModel.registerUser(loginInput, queue);

        mViewModel.userMutableLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.email != null && !user.email.equals("")) {

                    // Navigate user to profile
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_profile);
                }

                // Inform user about failed registration
                textViewError.setText(getString(R.string.register_failed_register));
            }
        });
        // Check if registration was successful

    }

    /**
     * Validates user's input
     *
     * @param usersEmail          user's email
     * @param usersPassword       user's password
     * @param usersPasswordRepeat user's repeated password
     */
    private boolean validateInput(String usersEmail, String usersPassword, String usersPasswordRepeat) {

        // Check if submitted email is not empty and is valid
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!usersEmail.matches(emailPattern) || usersEmail.equals("")) {

            // Inform user about incorrect email
            editTextEmail.requestFocus();
            editTextEmail.setError(getString(R.string.incorrect_email));
            return false;
        }

        // Check if submitted password is not empty
        if (usersPassword.equals("")) {

            // Inform user about empty password
            editTextPassword.requestFocus();
            editTextPassword.setError(getString(R.string.incorrect_password));
            return false;
        }

        // Check if passwords match
        if (!usersPassword.equals(usersPasswordRepeat)) {
            // Inform user about incorrect passwords
            textViewError.setText(getString(R.string.register_passwords_do_not_match));
            return false;
        }

        return true;
    }
}
