package com.example.altas.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.altas.Models.Product;
import com.example.altas.R;
import com.example.altas.ui.list.adepters.IRecyclerViewSupport.IRecyclerViewButtonClickListener;
import com.example.altas.ui.list.adepters.ItemClickSupport;
import com.example.altas.ui.list.adepters.ShopListAdapter;
import com.example.altas.ui.shop.ShopFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.altas.MainActivity.ALTAS_PREF_NAME;
import static com.example.altas.MainActivity.BASKET_UUID;

/**
 * Public class HomeFragment that extends Fragment
 */
public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShopListAdapter mAdapter;
    private HomeViewModel mViewModel;
    private ImageView mImageViewGreeting;

    private RequestQueue queue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize variables
        View homeFragmentRoot = inflater.inflate(R.layout.fragment_home, container, false);
        mImageViewGreeting = homeFragmentRoot.findViewById(R.id.image_view_home_greeting);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mRecyclerView = homeFragmentRoot.findViewById(R.id.home_products_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(getContext());

        // Initialize suggested products
        mViewModel.initializeProducts(queue);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify an adapter and pass in our data model list
        mViewModel.productsListMutableLiveData.observe(this, setUpProductsObserver());

        // Set up on click Listeners
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

        mImageViewGreeting.setOnClickListener(getListProductOnClickListener());


        return homeFragmentRoot;
    }

    /**
     *  Returns Observer with onChange, when data was changed, set adapter
     * @return Observer of products list
     */
    private Observer<ArrayList<Product>> setUpProductsObserver() {
        return new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {

                // Check if we have products
                if (products.size() != 0 ){

                    // Set up adapter
                    mAdapter = new ShopListAdapter(products, getContext(), handleBasketButtonClickListener(), false);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        };
    }


    /**
     * Handle list item's add to basket button click action that adds product to basket
     *
     * @return IRecyclerViewButtonClickListener
     */
    private IRecyclerViewButtonClickListener handleBasketButtonClickListener() {
        return new IRecyclerViewButtonClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Get basket's id
                SharedPreferences prefs = getActivity().getSharedPreferences(ALTAS_PREF_NAME, MODE_PRIVATE);
                String basketUUID = prefs.getString(BASKET_UUID, null);

                // Get Product's id
                Product product = mAdapter.getItemFromList(position);

                // Add Product to basket
                mViewModel.addProductToBasket(basketUUID, product.id, queue);

                // Inform user that product was added
                Snackbar.make(getParentFragment().getView(), product.name + " " + getString(R.string.product_was_added), Snackbar.LENGTH_SHORT)
                        .show();
            }
        };
    }

    /**
     * Handle Product onClick action that navigates user to product's info fragment
     *
     * @return OnClickListener
     */
    private View.OnClickListener getListProductOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect user
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_shop);
            }
        };
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