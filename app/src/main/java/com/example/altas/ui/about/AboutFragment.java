package com.example.altas.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.altas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Public class AboutFragment that extends Fragment
 */
public class AboutFragment extends Fragment {

    private TextView textViewAboutEmail;
    private TextView textViewAboutPhone;
    private TextView textViewAboutAboutUs;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate about
        View aboutFragmentRoot = inflater.inflate(R.layout.fragment_about, container, false);


        // Set up TextView that contains email address
        this.textViewAboutEmail = aboutFragmentRoot.findViewById(R.id.text_email);

        textViewAboutEmail.setText(getString(R.string.about_email_address));

        // Set up TextView that contains phone number
        this.textViewAboutPhone = aboutFragmentRoot.findViewById(R.id.text_phone);
        textViewAboutPhone.setText(getString(R.string.about_phone_number));

        // Set up TextView that contains about us text
        this.textViewAboutAboutUs = aboutFragmentRoot.findViewById(R.id.text_about_us);
        this.textViewAboutAboutUs.setMovementMethod(new ScrollingMovementMethod());
        textViewAboutAboutUs.setText(getString(R.string.about_us_text));

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
}
