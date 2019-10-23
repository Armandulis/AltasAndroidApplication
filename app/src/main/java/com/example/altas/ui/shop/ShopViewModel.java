package com.example.altas.ui.shop;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
    public boolean isLoading = false;
    public boolean isLastPage = false;

    public ShopViewModel() {

        // Initialize variables
        productRepository = new ProductRepository();
        productsList = new ArrayList<>();
        productsListMutableLiveData = new MutableLiveData<>();

        // Get first page products
        productsList.addAll(productRepository.getPaginatedProducts(ShopFragment.DEFAULT_PAGE, "", ""));

        // Set set products list to MutableLiveData
        productsListMutableLiveData.setValue(productsList);
    }

    /**
     * Gets paginated products list and puts it in the list
     * @param currentPage Int, Current page of the product List
     * @param orderBy     String, Order argument
     * @param filterBy    String, Filtering argument
     */
    public void getPaginatedProductList(int currentPage, String orderBy, String filterBy) {
        // While getting products we set loading to true
        this.isLoading = true;
        productsList.addAll(productRepository.getPaginatedProducts(currentPage, orderBy, filterBy));
        // After we got products we put loading to false
        this.isLoading = false;
    }

    /**
     * @return boolean, False if there are no more than 10 products left
     */
    public boolean isLastPage() {
        return isLastPage;
    }

    /**
     * @return boolean State of getting products from database
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * Clears productsList and mutableLiveData
     */
    public void clearList() {
        productsListMutableLiveData = new MutableLiveData<>();
        productsList = new ArrayList<>();
    }
}
