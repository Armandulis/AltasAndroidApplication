package com.example.altas.repositories;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.altas.Models.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BasketRepository {

    public static final String API_PRODUCTS_PATH = "http://altas.gear.host/api/basket";

    public MutableLiveData<ArrayList<Product>> basketProductsMutableLiveData;

    public MutableLiveData<Boolean> basketAddMutableLiveData;

    public MutableLiveData<Boolean> basketRemoveMutableLiveData;


    private ArrayList<Product> products;

    public ArrayList<Product> getBasket(String basketUUID, RequestQueue queue)
    {
        basketProductsMutableLiveData = new MutableLiveData<>();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_PRODUCTS_PATH + "/" + basketUUID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        basketProductsMutableLiveData.setValue(extractProduct(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return products;
    }

    public void addProductToBasket(String basketUUID, String productId, RequestQueue queue)
    {

        basketAddMutableLiveData = new MutableLiveData<>();

        String url = API_PRODUCTS_PATH + "/" + basketUUID + "?productId=" + productId;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        basketAddMutableLiveData.setValue(Boolean.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                basketAddMutableLiveData.setValue(false);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void removeProductFromBasket(String basketUUID, String productId, RequestQueue queue) {

        basketRemoveMutableLiveData = new MutableLiveData<>();

        String url = API_PRODUCTS_PATH + "/" + basketUUID + "?ProductId=" + productId;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        basketRemoveMutableLiveData.setValue(Boolean.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                basketRemoveMutableLiveData.setValue(false);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private ArrayList<Product> extractProduct(String response) {

        ArrayList<Product> productList = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(response);

            for (int i = 0; i < array.length(); i++) {

                Product product = new Product();

                JSONObject jsonObject = array.getJSONObject(i);

                product.id = jsonObject.getString("id");
                product.description = jsonObject.getString("description");
                product.name = jsonObject.getString("title");
                product.price = jsonObject.getString("price");
                product.pictureUrl = jsonObject.getString("pictureUrl");
                product.brand = jsonObject.getString("brand");

                productList.add(product);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }

}
