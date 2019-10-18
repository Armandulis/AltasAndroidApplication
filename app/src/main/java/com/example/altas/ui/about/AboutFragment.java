package com.example.altas.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.altas.R;
import com.example.altas.ui.about.edit.dialogs.EditAboutEmailDialogFragment;
import com.example.altas.ui.about.edit.dialogs.EditAboutPhoneDialogFragment;
import com.example.altas.ui.about.edit.dialogs.EditAboutUsDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Public class AboutFragment that extends Fragment
 */
public class AboutFragment extends Fragment {

    private TextView textViewAboutEmail;
    private TextView textViewAboutPhone;
    private TextView textViewAboutAboutUs;

    private AboutViewModel aboutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View aboutFragmentRoot = inflater.inflate(R.layout.fragment_about, container, false);

        aboutViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);


        // Set up TextView that contains email address
        this.textViewAboutEmail = aboutFragmentRoot.findViewById(R.id.text_email);
        aboutViewModel.getAboutEmail().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String email) {
                if (email != null && !email.equals("")) {
                    textViewAboutEmail.setText(email);
                }
            }
        });

        // Set up TextView that contains phone number
        this.textViewAboutPhone = aboutFragmentRoot.findViewById(R.id.text_phone);
        aboutViewModel.getAboutPhone().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String phone) {
                if (phone != null && !phone.equals("")) {
                    textViewAboutPhone.setText(phone);
                }
            }
        });

        // Set up TextView that contains about us text
        this.textViewAboutAboutUs = aboutFragmentRoot.findViewById(R.id.text_about_us);
        this.textViewAboutAboutUs.setMovementMethod(new ScrollingMovementMethod());
        aboutViewModel.getAboutUs().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String description) {
                if (description != null && !description.equals("")) {
                    textViewAboutAboutUs.setText(description);
                }
            }
        });

        // Get dial button and add onClickListener
        final FloatingActionButton buttonDial = aboutFragmentRoot.findViewById(R.id.button_dial_about_phone);
        buttonDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialPhone();
            }
        });

        // Get SendEmail button and add onClickListener
        final FloatingActionButton buttonEmail = aboutFragmentRoot.findViewById(R.id.button_send_about_email);
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSendEmail();
            }
        });

        // Get SendEmail button and add onClickListener
        final Button buttonEditEmail = aboutFragmentRoot.findViewById(R.id.button_edit_about_email);
        buttonEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditEmailDialog();
            }
        });

        // Get SendEmail button and add onClickListener
        final Button buttonEditPhone = aboutFragmentRoot.findViewById(R.id.button_edit_about_phone);
        buttonEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditPhoneDialog();
            }
        });

        // Get SendEmail button and add onClickListener
        final Button buttonEditDescription = aboutFragmentRoot.findViewById(R.id.button_edit_about_us);
        buttonEditDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditAboutUsDialog();
            }
        });

        // Initialize Fragment's layout
        return aboutFragmentRoot;
    }


    /**
     * Opens email activity if text view doesn't contain no_current_information
     */
    private void openSendEmail() {
        // Open email activity with about email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] receivers = {(String) textViewAboutEmail.getText()};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        startActivity(emailIntent);
    }

    /**
     * Opens dial activity if text view doesn't contain no_current_information
     */
    private void openDialPhone() {
        // Open Phone activity with about phone
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + textViewAboutPhone.getText()));
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up with wanted dialog data
        if (requestCode == EditAboutEmailDialogFragment.REQUEST_CODE) {
            String newEmail = data.getStringExtra(
                    EditAboutEmailDialogFragment.NEW_EMAIL_KEY);
            this.aboutViewModel.setAboutEmail(newEmail);
        }
        else if (requestCode == EditAboutPhoneDialogFragment.REQUEST_CODE) {
            String newPhone = data.getStringExtra(
                    EditAboutPhoneDialogFragment.NEW_PHONE_KEY);
            this.aboutViewModel.setAboutPhone(newPhone);
        }
        else if (requestCode == EditAboutUsDialogFragment.REQUEST_CODE) {
            String newAboutUs = data.getStringExtra(
                    EditAboutUsDialogFragment.NEW_ABOUT_KEY);
            this.aboutViewModel.setAboutUs(newAboutUs);
        }
    }

    /**
     * Opens EditEmailDialogFragment
     */
    private void openEditEmailDialog() {
        // EditAboutEmailDialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("editEmailDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        EditAboutEmailDialogFragment editAboutEmailDialogFragment = new EditAboutEmailDialogFragment();
        editAboutEmailDialogFragment.setTargetFragment(this, EditAboutEmailDialogFragment.REQUEST_CODE);
        editAboutEmailDialogFragment.show(ft, "editEmailDialog");
    }

    /**
     * Opens EditPhoneDialogFragment
     */
    private void openEditPhoneDialog() {
        // EditAboutPhoneDialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("editPhoneDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        EditAboutPhoneDialogFragment editAboutPhoneDialogFragment = new EditAboutPhoneDialogFragment();
        editAboutPhoneDialogFragment.setTargetFragment(this, EditAboutPhoneDialogFragment.REQUEST_CODE);
        editAboutPhoneDialogFragment.show(ft, "editPhoneDialog");
    }

    /**
     * Opens EditAboutUsDialogFragment
     */
    private void openEditAboutUsDialog() {
        // EditAboutUsDialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("editAboutUsDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        EditAboutUsDialogFragment editAboutUsDialogFragment = new EditAboutUsDialogFragment();
        editAboutUsDialogFragment.setTargetFragment(this, EditAboutUsDialogFragment.REQUEST_CODE);
        editAboutUsDialogFragment.show(ft, "editAboutUsDialog");
    }
}
