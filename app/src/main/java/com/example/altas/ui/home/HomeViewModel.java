package com.example.altas.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.altas.Models.Product;
import com.example.altas.repositories.ProductRepository;

import java.util.ArrayList;

/**
 * Public class HomeViewModel that extends ViewModel
 */
public class HomeViewModel extends ViewModel {

    MutableLiveData<ArrayList<Product>> productsListMutableLiveData;

    private ProductRepository productRepository;
    private ArrayList<Product> productsList;

    private static final int SUGGESTED_PRODUCTS_NUMBER = 3;

    /**
     * HomeViewModel constructor
     */
    public HomeViewModel() {

        // Initialize variables
        productRepository = new ProductRepository();
        productsList = new ArrayList<>();
        productsListMutableLiveData = new MutableLiveData<>();

        // Get first page products
        productsList.addAll(productRepository.getProducts(SUGGESTED_PRODUCTS_NUMBER));

        // Set set products list to MutableLiveData
        productsListMutableLiveData.setValue(productsList);
    }
}