package com.example.altas.ui.product.details;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.altas.MainActivity;
import com.example.altas.Models.Product;
import com.example.altas.R;
import com.example.altas.ui.shop.ShopFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Public class ProductDetailFragment that extends Fragments, handles single Product's details
 */
public class ProductDetailsFragment extends Fragment {

    private Product product;
    private TextView textViewProductDescription;
    private TextView textViewProductTitle;
    private TextView textViewProductPrice;
    private TextView textViewProductBrand;
    private TextView textViewProductAmount;
    private ImageView textViewProductImage;
    private Button buttonEdit;

    private FloatingActionButton buttonAddToCart;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Get product
        this.product = (Product) getArguments().getSerializable(ShopFragment.SELECTED_PRODUCT_KEY);

        // Initialize fragment's layout
        View root = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Set up Action bar
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Disable back button in toolbar and change it's title if it exists
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(product.name);
        }

        // Initialize class variables
        textViewProductDescription = root.findViewById(R.id.text_view_product_details_description);
        textViewProductDescription.setMovementMethod(new ScrollingMovementMethod());
        textViewProductTitle = root.findViewById(R.id.text_view_product_details_title);
        textViewProductPrice = root.findViewById(R.id.text_view_product_details_price);
        textViewProductBrand = root.findViewById(R.id.text_view_product_details_brand);
        textViewProductAmount = root.findViewById(R.id.text_view_product_details_amount);
        textViewProductImage = root.findViewById(R.id.image_view_details_image);
        buttonAddToCart = root.findViewById(R.id.button_details_add_to_cart);
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
        buttonEdit = root.findViewById(R.id.button_edit_product_details);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEdit();
            }
        });


        putProductsValuesToLayout();


        return root;
    }

    /**
     * Handles edit button after user clicked on button
     */
    private void handleEdit() {
        // TODO add to cart
    }

    /**
     * Handles adding product to cart
     */
    private void addToCart() {
        // TODO add to cart
    }

    /**
     * Places values from product to Layout for user to see
     */
    private void putProductsValuesToLayout() {
        textViewProductDescription.setText(product.description);
        textViewProductTitle.setText(product.name);
        textViewProductPrice.setText(product.price);
        textViewProductBrand.setText(product.brand);
        textViewProductAmount.setText(product.amount);
    }
}
