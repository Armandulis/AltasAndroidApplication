package com.example.altas.ui.basket;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.altas.Models.Product;
import com.example.altas.Models.ProductStatus;
import com.example.altas.repositories.BasketRepository;
import com.example.altas.repositories.ProductStatusRepository;

import java.util.ArrayList;

/**
 * Public class BasketViewModel that extends ViewModel
 */
public class BasketViewModel extends ViewModel {

    MutableLiveData<ArrayList<Product>> productsListMutableLiveData;
    MutableLiveData<Integer> totalProductsMutableLiveData;
    MutableLiveData<Double> totalPriceMutableLiveData;
    private BasketRepository basketRepository;
    private ProductStatusRepository statusRepository;
    private ArrayList<Product> products;

    /**
     * BasketViewModel constructor
     */
    public BasketViewModel() {
        // Initialize variables
        products = new ArrayList<>();
        basketRepository = new BasketRepository();
        statusRepository = new ProductStatusRepository();
        productsListMutableLiveData = new MutableLiveData<>();
        totalProductsMutableLiveData = new MutableLiveData<>();
        totalPriceMutableLiveData = new MutableLiveData<>();
    }

    /**
     * Returns Basket products in a way that we can observe changes
     *
     * @return LiveData of ArrayList<Product> that were added to basket
     */
    public LiveData<ArrayList<Product>> getBasketProductsLive() {
        return productsListMutableLiveData;
    }

    /**
     * Returns amount of products in the basket
     *
     * @return Live data of amount of products in it
     */
    public LiveData<Integer> getProductCountProductsLive() {
        return totalProductsMutableLiveData;
    }

    /**
     * Returns a total price of all products in the basket
     *
     * @return LiveData of double that holds total price
     */
    public LiveData<Double> getTotalPriceProductsLive() {
        return totalPriceMutableLiveData;
    }

    /**
     * Calls BasketActivity to get products and returns them, set price and amount
     *
     * @param basketUUID unique identifier for basket and phone
     */
    public void initializeBasketProducts(String basketUUID) {
        products = basketRepository.getBasket(basketUUID);
        productsListMutableLiveData.setValue(products);
        totalProductsMutableLiveData.postValue(products.size());
        totalPriceMutableLiveData.postValue(getProductsTotalPrice());
    }

    /**
     * Calcuclates total price of products that were added to basket
     *
     * @return double
     */
    private double getProductsTotalPrice() {

        double price = 0;
        // Tries to parse and add all Product's prices
        for (Product product : products) {
            try {
                price = price + Double.parseDouble(product.price);
                Log.d("PRICE", "" + price);
            } catch (NumberFormatException nfe) {
                // Handle parse error.
            }
        }
        return price;
    }


    /**
     * Calls repo to remove product and remove product locally, update price and amount
     *
     * @param position   of product in array
     * @param basketId   user's basket id
     * @param productsId product's id that will be removed
     */
    public void removeProductFromBasket(int position, String basketId, String productsId) {

        basketRepository.removeProductFromBasket(basketId, productsId);
        products.remove(position);
        productsListMutableLiveData.postValue(products);
        totalProductsMutableLiveData.postValue(products.size());
        totalPriceMutableLiveData.postValue(getProductsTotalPrice());
    }


    /**
     *
     * Creates productStatuses for each product in basket, once purchased, remove product from basket
     * @param userId user's id that is also a basket's id
     */
    public void purchaseProducts(String userId) {

        // Create productStatus for each product
        for ( Product product: products) {
            ProductStatus productStatus = new ProductStatus();
            productStatus.status = "Product Purchased";
            productStatus.userId = userId;

            // Purchase item and remove it from basket
            statusRepository.addProductStatuses(product.id, productStatus);
            basketRepository.removeProductFromBasket(userId, product.id);
        }

        // Reset basket Locally
        products = new ArrayList<>();
        productsListMutableLiveData.postValue(products);

    }
}
