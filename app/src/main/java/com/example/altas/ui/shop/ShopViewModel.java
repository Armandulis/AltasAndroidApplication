package com.example.altas.ui.shop;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.example.altas.Models.Filter;
import com.example.altas.Models.Product;
import com.example.altas.repositories.ProductRepository;

import java.util.ArrayList;

/**
 * public class ShopViewModel that extends ViewModel
 */
public class ShopViewModel extends ViewModel {

    MutableLiveData<ArrayList<Product>> productsListMutableLiveData;

    private ProductRepository productRepository;
    private ArrayList<Product> productsList;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    Filter filter;

    /**
     * ShopViewModel constructor
     */
    public ShopViewModel() {

        // Initialize variables
        productRepository = new ProductRepository();
        productsList = new ArrayList<>();
        productsListMutableLiveData = new MutableLiveData<>();
        filter = new Filter();
    }

    /**
     * Requests Repository for products and listens for response
     *
     * @param queue RequestQueue for API requests
     */
    void initializeProducts(RequestQueue queue) {

        // While getting products we set loading to true
        isLoading = true;

        // Request for products
        productRepository.getPaginatedProducts(filter, queue);

        // Get value from request
        productRepository.productsListMutableLiveData.observeForever(new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {

                // Validate if it is a last page
                if (products.size() == 0 ){

                    isLastPage = true;
                }else
                {
                    productsList.addAll(products);
                    productsListMutableLiveData.setValue(products);
                    isLastPage = false;
                }

                // After we got products we put loading to false
                isLoading = false;
            }
        });
    }

    /**
     * Gets paginated products list and puts it in the list
     *
     * @param queue RequestQueue for API requests
     */
    void getPaginatedProductList(RequestQueue queue) {

        // If list is size of 0, we want to get first page of products
        if (productsList.size() != 0) {
            // Get last item for pagination reasons
            Product product = productsList.get(productsList.size() - 1);
            filter.lastProductId = product.id;
        }

        initializeProducts(queue);
    }

    /**
     * @return boolean, False if there are no more than 10 products left
     */
    boolean isLastPage() {
        return isLastPage;
    }

    /**
     * @return boolean State of getting products from database
     */
    boolean isLoading() {
        return isLoading;
    }

    /**
     * Resets values
     */
    void clearSearchAndFilter() {
        filter = new Filter();
        productsList = new ArrayList<>();

        this.productsListMutableLiveData.postValue(productsList);
    }
}
