package com.example.altas.ui.shop;

import android.content.ClipData;
import android.os.Bundle;
import android.provider.DocumentsContract;
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
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.altas.MainActivity;
import com.example.altas.Models.Filter;
import com.example.altas.Models.Product;
import com.example.altas.R;
import com.example.altas.repositories.ProductRepository;
import com.example.altas.ui.list.adepters.ItemClickSupport;
import com.example.altas.ui.list.adepters.ShopListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * public class ShopFragment that extends Fragment,
 */
public class ShopFragment extends Fragment {

    public static final int PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE = 1;
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

    private int currentPage = DEFAULT_PAGE;
    private boolean isSearchBarHidden = true;


    // Not sure why we need this, keep it here until we find out
    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate shop fragment's view
        View shopFragmentRoot = inflater.inflate(R.layout.fragment_shop, container, false);
        mEditTextSearch = shopFragmentRoot.findViewById(R.id.edit_text_search_product);
        mButtonSearch = shopFragmentRoot.findViewById(R.id.button_search_product);

        // Initialize ViewModel
        mViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);

        // Initialize RecyclerView that will hold list of products
        mRecyclerView = shopFragmentRoot.findViewById(R.id.shop_products_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        filterLayout = shopFragmentRoot.findViewById(R.id.shop_filtering_layout);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        filterLayout.animate().translationY(-500);

        setHasOptionsMenu(true);

        spinnerOrder = shopFragmentRoot.findViewById(R.id.shop_spinner_order);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.order_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerOrder.setAdapter(adapter);

        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSortSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Pagination
        mRecyclerView.addOnScrollListener(getProductsListScrollListener());

        mViewModel.productsListMutableLiveData.observe(this, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {

                // Specify an adapter and pass in our data model list
                mAdapter = new ShopListAdapter(mViewModel.productsListMutableLiveData.getValue(), getContext());
                mRecyclerView.setAdapter(mAdapter);
            }
        });


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

        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchButton();

            }
        });

        mButtonReset = shopFragmentRoot.findViewById(R.id.button_reset_search);
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResetButton();
            }
        });

        return shopFragmentRoot;
    }

    private void handleSortSelected(int position) {
        String[] orderTypes = getResources().getStringArray(R.array.order_options);
        orderValue = orderTypes[position];
        mViewModel.filter.orderBy = orderValue;
        mViewModel.getPaginatedProductList();
    }

    /**
     * Resets filters and gets products
     */
    private void handleResetButton() {
        mEditTextSearch.setText("");
        spinnerOrder.setSelection(0);
        orderValue = spinnerOrder.getSelectedItem().toString();
        mViewModel.clearSearch();
        mViewModel.getPaginatedProductList();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.shop_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void handleSearchButton() {
        String searchWord = mEditTextSearch.getText().toString();

        if (!searchWord.equals("")) {
            mViewModel.filter.searchWord = searchWord;
            orderValue = spinnerOrder.getSelectedItem().toString();
            mViewModel.filter.orderBy = orderValue;
            mViewModel.getPaginatedProductList();

        } else {
            // Show SnackBar and and close dialog
            Snackbar.make(getParentFragment().getView(), R.string.no_search_was_provided, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Clears Search word and changes button text
     */
    private void handleClearSearch() {

    }

    /**
     * Gets input from edit text and searches for product with it, changes button text
     */
    private void handleSearchProduct() {


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
                        currentPage++;
                    }
                }
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        filterLayout.setVisibility(View.VISIBLE);
        if (!isSearchBarHidden) {
            isSearchBarHidden = true;
            filterLayout.animate().translationY(-500);
        } else {

            isSearchBarHidden = false;
            filterLayout.animate().translationY(0);
        }
        return super.onOptionsItemSelected(item);
    }

}
