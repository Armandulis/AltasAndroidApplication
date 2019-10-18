package com.example.altas.ui.about.edit.dialogs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.altas.R;
import com.example.altas.repositories.AboutRepository;
import com.google.android.material.snackbar.Snackbar;

/**
 * Public class EditAboutEmailDialogFragment handles about email update
 */
public class EditAboutEmailDialogFragment extends DialogFragment {

    public static final int REQUEST_CODE = 857;
    public static final String NEW_EMAIL_KEY = "NEW_EMAIL_KEY";

    private EditText editEmailTextField;
    private AboutRepository aboutRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View editAboutEmailDialogRoot = inflater.
                inflate(R.layout.fragment_edit_about_email_dialog, container, false);

        // Get about repository
        this.aboutRepository = new AboutRepository();

        // Get EditText for new about email value
        editEmailTextField = editAboutEmailDialogRoot.findViewById(R.id.edit_text_about_email);

        // Get Button for save and add onClickListener
        Button buttonSave = editAboutEmailDialogRoot.findViewById(R.id.button_edit_about_email_dialog_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSaveButtonClicked(editEmailTextField.getText().toString());
            }
        });

        // Get Button for cancel and add on click listener
        Button buttonCancel = editAboutEmailDialogRoot.findViewById(R.id.button_edit_about_email_dialog_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCancelButtonClicked();
            }
        });

        // Inflate the layout for this fragment
        return editAboutEmailDialogRoot;
    }

    /**
     * Sets new value for about email in database, sends new email to AboutFragment and closes dialog
     *
     * @param newEmail new email that will be saved to DB
     */
    private void dialogSaveButtonClicked(String newEmail) {
        // Check if submitted email is not empty and is valid
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!newEmail.equals("") && newEmail.matches(emailPattern)) {
            // Set new email in database
            aboutRepository.setAboutEmail(newEmail);

            // Let AboutFragment know about new email
            sendResult(newEmail);
            // Show SnackBar and and close dialog
            Snackbar.make(getParentFragment().getView(), R.string.update_email_success, Snackbar.LENGTH_SHORT)
                    .show();
            this.dismiss();
        }
        editEmailTextField.setError(getString(R.string.incorrect_email));
        editEmailTextField.requestFocus();
    }

    /**
     * Sends new value to About fragment to instantly update it
     *
     * @param newEmail new email that will be sent to about Fragment
     */
    private void sendResult(String newEmail) {
        Intent intent = new Intent();
        intent.putExtra(NEW_EMAIL_KEY, newEmail);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }

    /**
     * Handles dialog dismiss
     */
    private void dialogCancelButtonClicked() {
        // Closes dialog
        this.dismiss();
    }
}
