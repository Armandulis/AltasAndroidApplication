package com.example.altas.ui.shop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.altas.Models.Product;
import com.example.altas.R;
import com.example.altas.repositories.BasketRepository;
import com.example.altas.ui.list.adepters.IRecyclerViewSupport.IRecyclerViewButtonClickListener;
import com.example.altas.ui.list.adepters.ItemClickSupport;
import com.example.altas.ui.list.adepters.ShopListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.altas.MainActivity.ALTAS_PREF_NAME;
import static com.example.altas.MainActivity.BASKET_UUID;

/**
 * public class ShopFragment that extends Fragment,
 */
public class ShopFragment extends Fragment {

    public static final int PAGE_SIZE = 10;
    public static final String SELECTED_PRODUCT_KEY = "SELECTED_PRODUCT_KEY";

    private ShopViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private ShopListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private EditText mEditTextSearch;
    private Button mButtonSearch;
    private Button mButtonReset;
    private Spinner spinnerOrder;
    private ConstraintLayout filterLayout;

    private String orderValue;

    private boolean isSearchBarHidden = true;

    private BasketRepository basketRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Initialize variables
        View shopFragmentRoot = inflater.inflate(R.layout.fragment_shop, container, false);
        basketRepository = new BasketRepository();
        mViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);

        mEditTextSearch = shopFragmentRoot.findViewById(R.id.edit_text_search_product);
        mButtonSearch = shopFragmentRoot.findViewById(R.id.button_search_product);
        filterLayout = shopFragmentRoot.findViewById(R.id.shop_filtering_layout);
        spinnerOrder = shopFragmentRoot.findViewById(R.id.shop_spinner_order);
        mRecyclerView = shopFragmentRoot.findViewById(R.id.shop_products_recycler_view);
        mButtonReset = shopFragmentRoot.findViewById(R.id.button_reset_search);

        // Start with filter layout out of the screen
        filterLayout.animate().translationY(-500);
        setHasOptionsMenu(true);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.order_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerOrder.setAdapter(adapter);

        // Use a linear layout manager on RecyclerView
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Add On Click Listener to spinner
        spinnerOrder.setOnItemSelectedListener(handleOnItemSelectedListener());

        // Pagination
        mRecyclerView.addOnScrollListener(getProductsListScrollListener());

        // Observe product list changes
        mViewModel.productsListMutableLiveData.observe(this, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                addAdapterToProductsView(products);
            }
        });

        // Set up on click Listeners
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchButton();

            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResetButton();
            }
        });
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

        return shopFragmentRoot;
    }

    /**
     * Handles onItemSelected action that get's selected sorting value and gets sorted products
     *
     * @return OnItemSelectedListener
     **/
    private AdapterView.OnItemSelectedListener handleOnItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get sort by as a string value
                String[] orderTypes = getResources().getStringArray(R.array.order_options);
                orderValue = orderTypes[position];

                // Call ViewModel to get sorted products
                mViewModel.filter.orderBy = orderValue;
                mViewModel.getPaginatedProductList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    /**
     * Creates and handles shop adapter and ads it to RecyclerView
     *
     * @param products products from the database
     **/
    private void addAdapterToProductsView(ArrayList<Product> products) {
        // Get basket's id
        SharedPreferences prefs = getActivity().getSharedPreferences(ALTAS_PREF_NAME, MODE_PRIVATE);
        final String basketUUID = prefs.getString(BASKET_UUID, null);

        // Specify an adapter and pass in our data model list
        mAdapter = new ShopListAdapter(products, getContext(), new IRecyclerViewButtonClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Handle on "add to basket" button clicked action
                Product product = mAdapter.getItemFromList(position);
                basketRepository.addProductToBasket(basketUUID, product.id);
                // Inform user that product was added
                Snackbar.make(getParentFragment().getView(), product.name + " " + getString(R.string.product_was_added), Snackbar.LENGTH_SHORT)
                        .show();
            }
        }, false);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Resets filters and gets products
     */
    private void handleResetButton() {
        mEditTextSearch.setText("");
        spinnerOrder.setSelection(0);
        orderValue = spinnerOrder.getSelectedItem().toString();
        mViewModel.clearSearchAndFilter();
        // Get products without any values set to filter
        mViewModel.getPaginatedProductList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.shop_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Gets searched word value and calls ViewModel to get products
     **/
    private void handleSearchButton() {
        // Check if search value is not empty
        String searchWord = mEditTextSearch.getText().toString();
        if (!searchWord.equals("")) {
            // Set up filter
            mViewModel.filter.searchWord = searchWord;
            orderValue = spinnerOrder.getSelectedItem().toString();
            mViewModel.filter.orderBy = orderValue;

            // Get products with search and order values
            mViewModel.getPaginatedProductList();

        } else {
            // Inform user about empty search value
            Snackbar.make(getParentFragment().getView(), R.string.no_search_was_provided, Snackbar.LENGTH_SHORT)
                    .show();
        }
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

    /**
     * Handles pagination when user reaches end of PAGE_SIZE
     *
     * @return Set up OnScrollListener
     */
    private RecyclerView.OnScrollListener getProductsListScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                // !isLoading && !isLastPage
                if (!mViewModel.isLoading() && !mViewModel.isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {

                        // Get paginated list
                        mViewModel.getPaginatedProductList();
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
    }

    /**
     * Hides and shows filter layout when user clicks on MenuButton - filter
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Set visibility of layout to visible
        filterLayout.setVisibility(View.VISIBLE);

        // Hide layout
        if (!isSearchBarHidden) {
            isSearchBarHidden = true;
            filterLayout.animate().translationY(-500);
        } else {

            // Show Layout
            isSearchBarHidden = false;
            filterLayout.animate().translationY(0);
        }

        return super.onOptionsItemSelected(item);
    }

}
