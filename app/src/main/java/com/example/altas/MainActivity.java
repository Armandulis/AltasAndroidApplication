package com.example.altas;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final String ALTAS_PREF_NAME = "altas_pref_name";
    public static final String BASKET_UUID = "basket_uuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_shop, R.id.navigation_basket, R.id.navigation_about)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        handleBasketUUID();
    }

    /**
     * Checks for basket_uuid in shared preferences and sets it if it doesn't
     **/
    private void handleBasketUUID() {
        // Check if BASKET_UUID value is null
        SharedPreferences prefs = getSharedPreferences(ALTAS_PREF_NAME, MODE_PRIVATE);
        String basketUUID = prefs.getString(BASKET_UUID, null);
        if (basketUUID == null) {
            // Create UUID for basket
            SharedPreferences.Editor editor = getSharedPreferences(ALTAS_PREF_NAME, MODE_PRIVATE).edit();
            String uniqueID = UUID.randomUUID().toString().replace("-", "");

            // Save basket UUID
            editor.putString(BASKET_UUID, uniqueID);
            editor.apply();
        }
    }
}
