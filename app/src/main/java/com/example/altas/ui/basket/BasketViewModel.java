package com.example.altas.ui.basket;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.altas.Models.Product;
import com.example.altas.repositories.BasketRepository;

import java.util.ArrayList;

/**
 * Public class BasketViewModel that extends ViewModel
 */
public class BasketViewModel extends ViewModel {

    private BasketRepository basketRepository;
    private ArrayList<Product> products;

    /**
     * BasketViewModel constructor
     */
    public BasketViewModel() {
        // Initialize variables
        products = new ArrayList<>();
        basketRepository = new BasketRepository();

    }

    /**
     * Calls BasketActivity to get products and returns them
     *
     * @param basketUUID unique identifier for basket and phone
     * @return ArrayList<Product> that were added to basket
     */
    public ArrayList<Product> getBasketProducts(String basketUUID) {
        products = basketRepository.getBasket(basketUUID);
        return products;
    }

    /**
     * Returns the amount of product in the basket
     *
     * @return int
     */
    public int getProductsCount() {
        return products.size();
    }

    /**
     * Calcuclates total price of products that were added to basket
     *
     * @return double
     */
    public double getProductsTotalPrice() {

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


    public void removeProductFromBasket(String productsId) {

    }


}
