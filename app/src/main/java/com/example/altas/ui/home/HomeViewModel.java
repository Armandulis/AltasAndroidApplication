package com.example.altas.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.altas.MainActivity;
import com.example.altas.Models.Filter;
import com.example.altas.Models.Product;
import com.example.altas.repositories.BasketRepository;
import com.example.altas.repositories.ProductRepository;

import java.util.ArrayList;

/**
 * Public class HomeViewModel that extends ViewModel
 */
public class HomeViewModel extends ViewModel {

    MutableLiveData<ArrayList<Product>> productsListMutableLiveData;

    private ProductRepository productRepository;
    private ArrayList<Product> productsList;
    private BasketRepository basketRepository;
    private static final int SUGGESTED_PRODUCTS_NUMBER = 3;

    private Filter filter;

    /**
     * HomeViewModel constructor
     */
    public HomeViewModel() {

        // Initialize variables
        basketRepository = new BasketRepository();
        productRepository = new ProductRepository();
        productsList = new ArrayList<>();
        productsListMutableLiveData = new MutableLiveData<>();
        filter = new Filter();


    }

    public void initializeProducts(RequestQueue queue){

        // Set filter and call repo to get products
        filter.amount = SUGGESTED_PRODUCTS_NUMBER;
        // Calls basket repo to get certain amount of products
        // Get first page products
        productsList.addAll(productRepository.getPaginatedProducts(filter, queue));

        productsListMutableLiveData.setValue(productsList);
        productRepository.productsListMutableLiveData.observeForever(new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                productsListMutableLiveData.setValue(products);

                Log.d("qwe", "def inside frag" + products.size());
            }
        });
    }

    /**
     * Calls basket repo to add product to basket
     *
     * @param basketId  unique id for user's basket
     * @param productId product's id that will be added to basket
     */
    public void addProductToBasket(String basketId, String productId, RequestQueue queue) {

        // Add product to basket
        basketRepository.addProductToBasket(basketId, productId, queue);

    }
}