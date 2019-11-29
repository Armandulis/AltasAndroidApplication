package com.example.altas.repositories;

import com.example.altas.Models.Product;

import java.util.ArrayList;

public class BasketRepository {

    public ArrayList<Product> getBasket(String basketUUID)
    {
        ArrayList<Product> paginatedProducts = new ArrayList<>();
        int i = 0;
        while (i < 5) {
            Product product = new Product();

            product.id = i + "";
            product.name = "Very Important Item";
            product.brand = "Gucci";
            product.description = "Very nice item, please buy me";
            product.price = i + "1.99";
            paginatedProducts.add(product);
            i++;
        }

        return paginatedProducts;
    }

    public void addProductToBasket(String basketUUID, String productId)
    {

    }

    public void removeProductFromBasket(String basketUUID, String productId) {
    }
}
