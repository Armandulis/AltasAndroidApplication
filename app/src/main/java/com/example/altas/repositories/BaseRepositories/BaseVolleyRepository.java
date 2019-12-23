package com.example.altas.repositories.BaseRepositories;

import com.example.altas.Models.LoginInput;
import com.example.altas.Models.Product;
import com.example.altas.Models.ProductStatus;
import com.example.altas.Models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * public class BaseVolleyRepository
 * Handles string to object conversion and vise versa
 */
public class BaseVolleyRepository {

    // Methods for extracting string response to Objects

    /**
     * Extracts Product from a JSONArray received as a string
     *
     * @param response String response from API
     * @return Extracted Product
     */
    public ArrayList<Product> extractProduct(String response) {

        ArrayList<Product> productList = new ArrayList<>();

        try {

            // Create JSONArray from provided response
            JSONArray array = new JSONArray(response);

            // For each object in array create Product
            for (int i = 0; i < array.length(); i++) {

                Product product = new Product();

                // Put values from JSONObject to Product
                JSONObject jsonObject = array.getJSONObject(i);

                // Get values
                product.id = jsonObject.getString("id");
                product.description = jsonObject.getString("description");
                product.name = jsonObject.getString("title");
                product.price = jsonObject.getString("price");
                product.pictureUrl = jsonObject.getString("pictureUrl");
                product.brand = jsonObject.getString("brand");

                productList.add(product);
            }
        } catch (Exception e) {
            //NO-OP
        }

        // Return ArrayList of products
        return productList;
    }

    /**
     * Extracts User from a JSONObject received as a string
     * @param response String response from API
     * @return Extracted User
     */
    public User getUserFromResponse(String response) {

        User user = User.getInstance();
        try {
            JSONObject jsonObject = new JSONObject(response);

            user.email = jsonObject.getString("email");
            user.expiresIn = jsonObject.getString("expiresIn");
            user.idToken = jsonObject.getString("idToken");
            user.localId = jsonObject.getString("localId");

        } catch (Exception e) {
            // NO-OP
        }

        // Return user
        return user;
    }

    /**
     * Extracts ProductStatuses from a JSONArray received as a string
     *
     * @param response String response from API
     * @return Extracted ProductStatuses
     */
    public ArrayList<ProductStatus> extractProductStatus(String response) {

        ArrayList<ProductStatus> productStatusList = new ArrayList<>();

        try {
            // Create JSONArray from provided response
            JSONArray array = new JSONArray(response);

            // For each object in array create ProductStatus
            for (int i = 0; i < array.length(); i++) {

                ProductStatus productStatus = new ProductStatus();

                // Put values from JSONObject to ProductStatus
                JSONObject jsonObject = array.getJSONObject(i);

                productStatus.id = jsonObject.getString("id");
                productStatus.userId = jsonObject.getString("userId");
                productStatus.status = jsonObject.getString("status");
                productStatus.purchaseDate = jsonObject.getString("purchaseDate");

                // Get Product values and add it to ProductStatus
                JSONObject jsonProduct = jsonObject.getJSONObject("product");
                Product product = new Product();

                product.id = jsonProduct.getString("id");
                product.description = jsonProduct.getString("description");
                product.name = jsonProduct.getString("title");
                product.price = jsonProduct.getString("price");
                product.pictureUrl = jsonProduct.getString("pictureUrl");
                product.brand = jsonProduct.getString("brand");

                productStatus.product = product;

                productStatusList.add(productStatus);
            }


        } catch (Exception e) {
            // NO-OP
        }

        // Return ArrayList of productStatuses
        return productStatusList;
    }


    // Methods for generating body for requests

    /**
     * Formats a product in to a JSON object and returns a string version of it
     *
     * @param loginInput productStatus that will be formated
     * @return String version of JSON productStatus
     */
    public String getStringBody(LoginInput loginInput) {

        JSONObject jsonLoginInputBody = new JSONObject();

        try {
            jsonLoginInputBody.put("email", loginInput.email);
            jsonLoginInputBody.put("password", loginInput.password);
        } catch (Exception e) {
            // NO-OP
        }

        // Convert JSON body to string
        return jsonLoginInputBody.toString();
    }

    /**
     * Formats a product in to a JSON object and returns a string version of it
     *
     * @param productStatus productStatus that will be formated
     * @return String version of JSON productStatus
     */
    public String getStringBody(ProductStatus productStatus) {

        JSONObject jsonBody = new JSONObject();

        // Try to put values to a JSON body from productStatus
        try {
            jsonBody.put("Id", productStatus.id);
            jsonBody.put("userId", productStatus.userId);
            jsonBody.put("status", productStatus.status);
            jsonBody.put("purchaseDate", productStatus.purchaseDate);

            // Put values to Product JSON object
            JSONObject jsonBodyProduct = new JSONObject();

            jsonBodyProduct.put("id", productStatus.product.id);
            jsonBodyProduct.put("description", productStatus.product.description);
            jsonBodyProduct.put("title", productStatus.product.name);
            jsonBodyProduct.put("price", productStatus.product.price);
            jsonBodyProduct.put("pictureUrl", productStatus.product.pictureUrl);
            jsonBodyProduct.put("brand", productStatus.product.brand);

            // Add product to ProductStatus
            jsonBody.put("product", jsonBodyProduct);

        } catch (Exception e) {
            // NO-OP
        }

        // Convert JSON body to string
        return jsonBody.toString();
    }
}
