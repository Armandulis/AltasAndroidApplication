package com.example.altas.ui.product.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.altas.MainActivity;
import com.example.altas.Models.Product;
import com.example.altas.R;
import com.example.altas.ui.shop.ShopFragment;

/**
 * Public class ProductDetailFragment that extends Fragments, handles single Product's details
 */
public class ProductDetailsFragment extends Fragment {

    private Product product;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize fragment's layout
        View root = inflater.inflate(R.layout.fragment_product_details, container, false);


        this.product = (Product) getArguments().getSerializable(ShopFragment.SELECTED_PRODUCT_KEY);

        TextView tx = root.findViewById(R.id.textView5);
        tx.setText(product.id + product.description);

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        // Disable back button in toolbar if it exists
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(product.name);

        }
        return root;
    }
}
