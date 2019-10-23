package com.example.altas.repositories;

import com.example.altas.Models.Product;

import java.util.ArrayList;

public class ProductRepository {

    private ArrayList<Product> products;

    // TODO count how many products there are in the database so we could calculate last page

    public ProductRepository() {
        products = new ArrayList<>();
    }

    /**
     * Not sure if we need this
     * Return a list of products that were stored during pagination
     *
     * @return ArrayList<Product>
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    // TODO PAGINATED QUERY FROM DB use ShopFragment.PAGE_SIZE for the amount that we need to get
    public ArrayList<Product> getPaginatedProducts(int currentPage, String orderBy, String filterBy) {
        ArrayList<Product> paginatedProducts = new ArrayList<>();
        int i = 0;
        while (i < 10) {
            Product product = new Product();

            product.id = i;
            product.name = "Very Important Item";
            product.brand = "Gucci";
            product.description = "Very nice item, please buy me";
            product.price = i + "1.99";
            paginatedProducts.add(product);
            products.add(product);
            i++;
        }

        return paginatedProducts;
    }
}
