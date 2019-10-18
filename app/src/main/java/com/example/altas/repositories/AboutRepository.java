package com.example.altas.repositories;

public class AboutRepository {

    private String aboutEmail;
    private String aboutPhone;
    private String aboutUs;

    public String getAboutEmail() {
        // Get aboutEmail from database
       // this.aboutEmail = "Email@email.com";
        return this.aboutEmail;
    }

    public String getAboutPhone() {
        // Get aboutPhone from database
       // this.aboutPhone = "02031520";
        return this.aboutPhone;
    }

    public String getAboutUs() {
       // this.aboutDescription = "Long Description";
        return this.aboutUs;
    }

    public boolean setAboutEmail(String aboutEmail) {
        // Update aboutEmail in the database
        this.aboutEmail = aboutEmail;
        return true;
    }

    public boolean setAboutPhone(String aboutPhone) {
        // Update aboutPhone in the database
        this.aboutPhone = aboutPhone;
        return true;
    }

    public boolean setAboutUs(String aboutUs) {
        // Update aboutDescription in the database
        this.aboutUs = aboutUs;
        return true;
    }
}
