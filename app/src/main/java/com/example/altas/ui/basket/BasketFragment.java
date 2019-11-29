package com.example.altas.ui.basket;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.altas.MainActivity;
import com.example.altas.Models.Product;
import com.example.altas.R;
import com.example.altas.repositories.BasketRepository;
import com.example.altas.ui.list.adepters.IRecyclerViewSupport.IRecyclerViewButtonClickListener;
import com.example.altas.ui.list.adepters.ItemClickSupport;
import com.example.altas.ui.list.adepters.ShopListAdapter;
import com.example.altas.ui.shop.ShopFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * public class BasketFragment that extends Fragment
 */
public class BasketFragment extends Fragment {

    private TextView textViewPrice;
    private TextView textViewAmountOfProducts;

    private Button buttonProceed;

    private BasketViewModel basketViewModel;
    private LinearLayoutManager mLayoutManager;
    private ShopListAdapter mAdapter;
    private RecyclerView mRecyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize fragment's layout
        View basketFragmentRoot = inflater.inflate(R.layout.fragment_basket, container, false);

        textViewPrice = basketFragmentRoot.findViewById(R.id.text_view_basket_price);
        textViewAmountOfProducts = basketFragmentRoot.findViewById(R.id.text_view_basket_amount_products);
        buttonProceed = basketFragmentRoot.findViewById(R.id.button_basket_proceed);

        // Initialize ViewModel
        basketViewModel = ViewModelProviders.of(this).get(BasketViewModel.class);

        // Initialize RecyclerView that will hold list of products
        mRecyclerView = basketFragmentRoot.findViewById(R.id.basket_products_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set up on click Listener
        ItemClickSupport.addTo(mRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // Get product that was clicked
                        Product product = mAdapter.getItemFromList(position);
                        // Handle navigation
                        openProductDetails(product);
                    }
                });

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonProceed();
            }
        });

        initializeBasketProducts();
        handleTotalPriceText();
        handleProductsCountText();

        return basketFragmentRoot;
    }


    /**
     * Sets observer on price and updates textView
     */
    private void handleTotalPriceText() {
        basketViewModel.getTotalPriceProductsLive().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                // Update TextView with product price value
                textViewPrice.setText("" + aDouble);
            }
        });

    }

    /**
     * Sets observer on count and updates textView
     */
    private void handleProductsCountText() {
        basketViewModel.getProductCountProductsLive().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                // Update TextView with product count value
                textViewAmountOfProducts.setText(integer + "");
            }
        });
    }

    /**
     * Gets basket's products and put them in adapter
     */
    private void initializeBasketProducts() {
        // Get basket's id from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.ALTAS_PREF_NAME, FragmentActivity.MODE_PRIVATE);
        String basketUUID = prefs.getString(MainActivity.BASKET_UUID, null);
        basketViewModel.initializeBasketProducts(basketUUID);

        // Observe products list, user might remove a product from basket
        basketViewModel.getBasketProductsLive().observe(this, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                // Specify an adapter and pass in our data model which is products from basket
                mAdapter = new ShopListAdapter(products, getContext(), handleRemoveButtonClicked(), true);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    /**
     * Creates and handles item's button click action that removes product from basket
     *
     * @return IRecyclerViewButtonClickListener
     */
    private IRecyclerViewButtonClickListener handleRemoveButtonClicked() {
        return new IRecyclerViewButtonClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Get basket's id from SharedPreferences
                SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.ALTAS_PREF_NAME, FragmentActivity.MODE_PRIVATE);
                String basketUUID = prefs.getString(MainActivity.BASKET_UUID, null);

                // get Product for id and removes it from basket with it
                Product productClicked = mAdapter.getItemFromList(position);
                basketViewModel.removeProductFromBasket(position, basketUUID, productClicked.id);

                // Inform user that product was removed
                Snackbar.make(getParentFragment().getView(), productClicked.name + " " + R.string.product_was_removed, Snackbar.LENGTH_SHORT)
                        .show();
            }
        };
    }

    /**
     * Navigates user to checkout after user clicked proceed button
     */
    private void handleButtonProceed() {
        // TODO NAVIGATE TO CHECKOUT
    }

    /**
     * Navigates user to the ProductDetailsFragment with the product that was clicked on
     *
     * @param product Product that was clicked on
     */
    private void openProductDetails(Product product) {
        // Set up a bundle that we will pass to ProductDetailsFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShopFragment.SELECTED_PRODUCT_KEY, product);

        // Navigate user
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.product_details_fragment, bundle);
    }

    /**
     * We want to get products again on resume because user might have added more products while
     * looking at product's details
     */
    @Override
    public void onResume() {
        super.onResume();
        // Get basket's id from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.ALTAS_PREF_NAME, FragmentActivity.MODE_PRIVATE);
        String basketUUID = prefs.getString(MainActivity.BASKET_UUID, null);
        // Initialize products again
        basketViewModel.initializeBasketProducts(basketUUID);
    }
}
