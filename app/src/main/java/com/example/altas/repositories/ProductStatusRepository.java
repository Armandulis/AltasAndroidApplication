package com.example.altas.repositories;

import com.example.altas.Models.Product;
import com.example.altas.Models.ProductStatus;

import java.util.ArrayList;

public class ProductStatusRepository {


    public ArrayList<ProductStatus> getAllProducts(String userId) {
        ArrayList<ProductStatus> list = new ArrayList<>();

        int i = 0;
        while (i < 6) {
            ProductStatus prodStatus = new ProductStatus();
            Product prod = new Product();
            prod.name = "name";
            prodStatus.purchaseDate = "2019";
            prodStatus.status = "on its way";
            prodStatus.product = prod;
            list.add(prodStatus);
            i++;
        }
        return list;
    }

    public void removeProductStatus(String productStatusId) {

    }

    public void updateProductStatus(ProductStatus productStatus) {
    }

    public void addProductStatuses(String id, ProductStatus productStatus) {
    }
}
