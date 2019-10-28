package com.example.altas.ui.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.altas.Models.Product;
import com.example.altas.R;
import com.example.altas.ui.list.adepters.ItemClickSupport;
import com.example.altas.ui.list.adepters.ShopListAdapter;

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

    private int currentPage = DEFAULT_PAGE;

    // Not sure why we need this, keep it here until we find out
    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate shop fragment's view
        View shopFragmentRoot = inflater.inflate(R.layout.fragment_shop, container, false);

        // Initialize ViewModel
        mViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);

        // Initialize RecyclerView that will hold list of products
        mRecyclerView = shopFragmentRoot.findViewById(R.id.shop_products_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Pagination
        mRecyclerView.addOnScrollListener(getProductsListScrollListener());

        // Specify an adapter and pass in our data model list
        mAdapter = new ShopListAdapter(mViewModel.productsListMutableLiveData.getValue(), getContext());
        mRecyclerView.setAdapter(mAdapter);

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

        return shopFragmentRoot;
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
                        mViewModel.getPaginatedProductList(currentPage, "", "");
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
}
