package com.example.altas.ui.about.edit.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.altas.R;
import com.example.altas.repositories.AboutRepository;
import com.google.android.material.snackbar.Snackbar;

public class EditAboutPhoneDialogFragment extends DialogFragment {

    public static final int REQUEST_CODE = 858;
    public static final String NEW_PHONE_KEY = "NEW_PHONE_KEY";

    private EditText editPhoneTextField;
    private AboutRepository aboutRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View editAboutPhoneDialogRoot = inflater.
                inflate(R.layout.fragment_edit_about_phone_dialog, container, false);

        // Get about repository
        this.aboutRepository = new AboutRepository();

        // Get EditText for new about phone value
        editPhoneTextField = editAboutPhoneDialogRoot.findViewById(R.id.edit_text_about_phone);

        // Get Button for save and add onClickListener
        Button buttonSave = editAboutPhoneDialogRoot.findViewById(R.id.button_edit_about_phone_dialog_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSaveButtonClicked(editPhoneTextField.getText().toString());
            }
        });

        // Get Button for cancel and add on click listener
        Button buttonCancel = editAboutPhoneDialogRoot.findViewById(R.id.button_edit_about_phone_dialog_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCancelButtonClicked();
            }
        });

        // Inflate the layout for this fragment
        return editAboutPhoneDialogRoot;
    }



    /**
     * Sets new value for about phone in database, sends new phone to AboutFragment and closes dialog
     *
     * @param newPhone new phone that will be saved to DB
     */
    private void dialogSaveButtonClicked(String newPhone) {
        // Check if submitted phone is not empty and is valid
        if ( !newPhone.equals( "" ) && android.util.Patterns.PHONE.matcher(newPhone).matches() ) {
            // Set new phone in database
            aboutRepository.setAboutPhone(newPhone);

            // Let AboutFragment know about new phone
            sendResult(newPhone);
            // Show SnackBar and and close dialog
            Snackbar.make(getParentFragment().getView(), R.string.update_phone_success, Snackbar.LENGTH_SHORT)
                    .show();
            this.dismiss();
        }
        editPhoneTextField.setError(getString(R.string.incorrect_phone));
        editPhoneTextField.requestFocus();
    }

    /**
     * Sends new value to About fragment to instantly update it
     *
     * @param newPhone new phone that will be sent to about Fragment
     */
    private void sendResult(String newPhone) {
        Intent intent = new Intent();
        intent.putExtra(NEW_PHONE_KEY, newPhone);
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
