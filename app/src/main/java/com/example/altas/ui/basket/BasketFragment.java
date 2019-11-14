package com.example.altas.ui.basket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.altas.R;

import org.w3c.dom.Text;

public class BasketFragment extends Fragment {

    private TextView textViewPrice;
    private TextView textViewAmountOfProducts;
    private TextView textViewDeliveryTime;

    private Button buttonProceed;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize fragment's layout
        View basketFragmentRoot = inflater.inflate(R.layout.fragment_basket, container, false);

        textViewPrice = basketFragmentRoot.findViewById(R.id.text_view_basket_price);
        textViewAmountOfProducts = basketFragmentRoot.findViewById(R.id.text_view_basket_amount_products);
        textViewDeliveryTime = basketFragmentRoot.findViewById(R.id.text_view_basket_delivery_time);

        buttonProceed = basketFragmentRoot.findViewById(R.id.button_basket_proceed);


        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonProceed();
            }
        });
        return basketFragmentRoot;
    }

    /**
     * Navigates user to checkout after user clicked proceed button
     */
    private void handleButtonProceed() {
        // TODO NAVIGATE TO CHECKOUT
    }

}
