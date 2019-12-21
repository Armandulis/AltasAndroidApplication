package com.example.altas.Models;

public class User {

    private static final User user = new User();

    public static User getInstance() {
        return user;
    }

    public String idToken;
    public String email;
    public String expiresIn;
    public String localId;
}
