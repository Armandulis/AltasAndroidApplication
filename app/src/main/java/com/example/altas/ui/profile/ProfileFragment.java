package com.example.altas.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.altas.MainActivity;
import com.example.altas.Models.Product;
import com.example.altas.Models.ProductStatus;
import com.example.altas.Models.User;
import com.example.altas.R;
import com.example.altas.ui.list.adepters.IRecyclerViewSupport.IRecyclerViewButtonClickListener;
import com.example.altas.ui.list.adepters.ItemClickSupport;
import com.example.altas.ui.list.adepters.OrderListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.altas.MainActivity.ALTAS_PREF_NAME;
import static com.example.altas.MainActivity.BASKET_UUID;
import static com.example.altas.ui.shop.ShopFragment.SELECTED_PRODUCT_KEY;

/**
 * public class ProfileFragment that extends Fragment,
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private TextView textViewUserEmail;
    private TextView textViewNoPurchases;
    private RecyclerView recyclerViewProductStatus;

    private OrderListAdapter mAdapter;
    private String basketUUID;
    private RequestQueue queue;

    private LinearLayoutManager mLayoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize variables
        View profileFragmentRoot = inflater.inflate(R.layout.fragment_profile, container, false);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        textViewUserEmail = profileFragmentRoot.findViewById(R.id.status_text_view_user_email);
        textViewNoPurchases = profileFragmentRoot.findViewById(R.id.text_view_no_purchases);
        recyclerViewProductStatus = profileFragmentRoot.findViewById(R.id.profile_status_recycler_view);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(getContext());

        User user = User.getInstance();
        if (user.email != null && !user.email.equals("")){
            basketUUID = user.email;
        }
        else {
            // Get basketId
            SharedPreferences prefs = getActivity().getSharedPreferences(ALTAS_PREF_NAME, MODE_PRIVATE);
            basketUUID = prefs.getString(BASKET_UUID, null);
        }

        // Initialize suggested productStatuses with email
        mViewModel.getAllUserProductStatus(basketUUID, queue);

        Log.d("UUID", "onClick: " + basketUUID);

        // Use a linear layout manager on RecyclerView
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewProductStatus.setLayoutManager(mLayoutManager);
        recyclerViewProductStatus.setHasFixedSize(true);

        // Observe productStatus list changes
        mViewModel.productStatusListMutableLiveData.observe(this, getObserver());

        ItemClickSupport.addTo(recyclerViewProductStatus)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // Get productStatus that was clicked
                        ProductStatus productStatus = mAdapter.getItemFromList(position);
                        // Handle navigation
                        openProductDetails(productStatus.product);
                    }
                });

        // Set up Action bar
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            // Disable back button in toolbar and change it's title if it exists
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.profile_title);
        }

        return profileFragmentRoot;
    }

    private Observer<ArrayList<ProductStatus>> getObserver() {
        return new Observer<ArrayList<ProductStatus>>() {
            @Override
            public void onChanged(ArrayList<ProductStatus> productStatuses) {
                // Check if there are any productStatus in the list
                if (productStatuses.size() == 0) {
                    textViewNoPurchases.setVisibility(View.VISIBLE);

                } else {
                    textViewNoPurchases.setVisibility(View.GONE);
                }

                addAdapterToStatusView(productStatuses);
            }
        };
    }

    /**
     * Sets up adapter with productStatuses, validates if we need to confirm delivery or
     * remove productStatus from the database
     *
     * @param productStatuses array list of product statuses that will be show to the user
     */
    private void addAdapterToStatusView(ArrayList<ProductStatus> productStatuses) {

        // Specify an adapter and pass in our data model list
        mAdapter = new OrderListAdapter(productStatuses, getContext(), new IRecyclerViewButtonClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Check on productStatus status
                ProductStatus productStatus = mAdapter.getItemFromList(position);

                if (productStatus.status.equals(getString(R.string.confirmed_delivery))) {
                    handleProductStatusRemoval(productStatus);
                } else {
                    handleProductStatusConfirmation(productStatus, position);
                }
            }
        });

        recyclerViewProductStatus.setAdapter(mAdapter);
    }

    /**
     * Changes productStatus status and informs user about it
     * @param productStatus product status that will be updated
     * @param position at which productStatus was clicked
     */
    private void handleProductStatusConfirmation(ProductStatus productStatus, int position) {
        productStatus.status = getString(R.string.confirmed_delivery);

        // If status is not "confirmed delivery" user should only be able to confirm it
        mViewModel.confirmDelivered(productStatus, position, queue);

        // Inform user that user confirmed delivery
        Snackbar.make(getParentFragment().getView(),
                productStatus.product.name + " " + getString(R.string.confirm_delivery_message), Snackbar.LENGTH_SHORT)
                .show();
    }

    /**
     *
     * Removed productStatus and informs user about it
     * @param productStatus that will be removed
     */
    private void handleProductStatusRemoval(ProductStatus productStatus) {
        // If status is confirmed delivery by user, we can only remove productStatus
        // from the list
        mViewModel.removeProductStatus(productStatus, queue);

        // Inform user that productStatus was removed
        Snackbar.make(getParentFragment().getView(),
                productStatus.product.name + " " + getString(R.string.confirm_removed_product), Snackbar.LENGTH_SHORT)
                .show();
    }

    /**
     * Navigates user to the ProductDetailsFragment with the product that was clicked on
     *
     * @param product Product that was clicked on
     */
    private void openProductDetails(Product product) {
        // Set up a bundle that we will pass to ProductDetailsFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_PRODUCT_KEY, product);

        // Navigate user
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.product_details_fragment, bundle);
    }


}
