package com.example.altas.ui.about.edit.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.altas.R;
import com.example.altas.repositories.AboutRepository;
import com.google.android.material.snackbar.Snackbar;

public class EditAboutUsDialogFragment extends DialogFragment {

    public static final int REQUEST_CODE = 859;
    public static final String NEW_ABOUT_KEY = "NEW_ABOUT_KEY";
    private static final int ABOUT_US_TEXT_MIN_LENGHT = 30;

    private EditText editAboutTextField;
    private AboutRepository aboutRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View editAboutAboutDialogRoot = inflater.
                inflate(R.layout.fragment_edit_about_us_dialog, container, false);

        // Get about repository
        this.aboutRepository = new AboutRepository();

        // Get EditText for new about about us text
        editAboutTextField = editAboutAboutDialogRoot.findViewById(R.id.edit_text_about_us);
        editAboutTextField.setMovementMethod(new ScrollingMovementMethod());


        // Get Button for save and add onClickListener
        Button buttonSave = editAboutAboutDialogRoot.findViewById(R.id.button_edit_about_us_dialog_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSaveButtonClicked(editAboutTextField.getText().toString());
            }
        });

        // Get Button for cancel and add on click listener
        Button buttonCancel = editAboutAboutDialogRoot.findViewById(R.id.button_edit_about_us_dialog_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCancelButtonClicked();
            }
        });

        // Inflate the layout for this fragment
        return editAboutAboutDialogRoot;
    }



    /**
     * Sets new value for about us test in database, sends new test to AboutFragment and closes dialog
     *
     * @param newAboutUs new text that will be saved to DB
     */
    private void dialogSaveButtonClicked(String newAboutUs) {
        // Check if submitted about us text is not empty and is valid
        if ( newAboutUs.length() > ABOUT_US_TEXT_MIN_LENGHT ) {
            // Set new about us text in database
            aboutRepository.setAboutUs(newAboutUs);

            // Let AboutFragment know about new about us text
            sendResult(newAboutUs);
            // Show SnackBar and and close dialog
            Snackbar.make(getParentFragment().getView(), R.string.update_about_us_success, Snackbar.LENGTH_SHORT)
                    .show();
            this.dismiss();
        }
        editAboutTextField.setError(getString(R.string.about_us_too_short));
        editAboutTextField.requestFocus();
    }

    /**
     * Sends new value to About fragment to instantly update it
     *
     * @param newAboutText new text that will be sent to about Fragment
     */
    private void sendResult(String newAboutText) {
        Intent intent = new Intent();
        intent.putExtra(NEW_ABOUT_KEY, newAboutText);
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
