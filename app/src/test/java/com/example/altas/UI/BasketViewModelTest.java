package com.example.altas.UI;

import com.example.altas.Models.Product;
import com.example.altas.ui.basket.BasketViewModel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests BasketViewModel
 */
public class BasketViewModelTest {

    private BasketViewModel basketViewModel;

    /**
     * BasketViewModelTest constructor
     */
    public BasketViewModelTest() {

        // Initialize test variables
        basketViewModel = new BasketViewModel();
    }

    @Test
    public void GetProductsTotalPriceTest() {

        basketViewModel.getTotalPriceProductsLive();
    }

    /**
     * Creates a list of products and adds wanted amount of products to the list
     *
     * @param amount amount of wanted products
     * @return list with wanted amount of products
     */
    private ArrayList<Product> getAmountOfProducts(int amount) {

        ArrayList<Product> products = new ArrayList<>();

        // Create products
        for (int i = 0; i > amount; i++) {
            Product product = new Product();
            product.price = i + "";
            products.add(product);
        }

        return products;
    }


}
