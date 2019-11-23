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

/**
 * public class BasketFragment that extends Fragment
 */
public class BasketFragment extends Fragment {

    private TextView textViewPrice;
    private TextView textViewAmountOfProducts;
    private TextView textViewDeliveryTime;

    private Button buttonProceed;

    private BasketViewModel basketViewModel;
    private LinearLayoutManager mLayoutManager;
    private ShopListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private BasketRepository basketRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize fragment's layout
        View basketFragmentRoot = inflater.inflate(R.layout.fragment_basket, container, false);

        textViewPrice = basketFragmentRoot.findViewById(R.id.text_view_basket_price);
        textViewDeliveryTime = basketFragmentRoot.findViewById(R.id.text_view_basket_delivery_time);
        textViewAmountOfProducts = basketFragmentRoot.findViewById(R.id.text_view_basket_amount_products);
        textViewPrice = basketFragmentRoot.findViewById(R.id.text_view_basket_price);
        textViewAmountOfProducts = basketFragmentRoot.findViewById(R.id.text_view_basket_amount_products);
        textViewDeliveryTime = basketFragmentRoot.findViewById(R.id.text_view_basket_delivery_time);
        buttonProceed = basketFragmentRoot.findViewById(R.id.button_basket_proceed);

        // Initialize ViewModel
        basketViewModel = ViewModelProviders.of(this).get(BasketViewModel.class);
        basketRepository = new BasketRepository();

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

        textViewAmountOfProducts.setText(basketViewModel.getProductsCount() + "");
        textViewPrice.setText("" + basketViewModel.getProductsTotalPrice());
        return basketFragmentRoot;
    }

    /**
     * Gets basket's products and put them in adapter
     */
    private void initializeBasketProducts() {
        // Get basket's id from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.ALTAS_PREF_NAME, FragmentActivity.MODE_PRIVATE);
        String basketUUID = prefs.getString(MainActivity.BASKET_UUID, null);

        // Specify an adapter and pass in our data model which is products from basket
        mAdapter = new ShopListAdapter(basketViewModel.getBasketProducts(basketUUID), getContext(), handleRemoveButtonClicked(), true);
        mRecyclerView.setAdapter(mAdapter);

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
                // get Product for id and removes it from basket with it
                Product productClicked = mAdapter.getItemFromList(position);
                basketViewModel.removeProductFromBasket(productClicked.id);
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

}
