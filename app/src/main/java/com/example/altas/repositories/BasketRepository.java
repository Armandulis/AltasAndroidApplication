package com.example.altas.repositories;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.altas.Models.Product;
import com.example.altas.repositories.BaseRepositories.BaseVolleyRepository;

import java.util.ArrayList;

public class BasketRepository extends BaseVolleyRepository {

    static final String API_PRODUCTS_PATH = "http://altas.gear.host/api/basket";
    public MutableLiveData<ArrayList<Product>> basketProductsMutableLiveData;

    MutableLiveData<Boolean> basketAddMutableLiveData;
    MutableLiveData<Boolean> basketRemoveMutableLiveData;


    /**
     * Calls RestAPI with GET request for basket Products, puts them in MutableLiveData
     *
     * @param basketUUID user's basket id, either phone generated or email
     * @param queue      API request queue
     */
    public void getBasket(String basketUUID, RequestQueue queue) {
        basketProductsMutableLiveData = new MutableLiveData<>();

        // Call API with GET request to a provided url: "/basket/userUUID"
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_PRODUCTS_PATH + "/" + basketUUID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Set value to a MutableLiveData once we get a request
                        basketProductsMutableLiveData.setValue(extractProduct(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //NO-OP
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Calls RestAPI with PUT request to update basket, puts true in MutableLiveData if it was successful
     *
     * @param basketUUID user's basket id, either phone generated or email
     * @param queue      API request queue
     */
    public void addProductToBasket(String basketUUID, String productId, RequestQueue queue) {

        basketAddMutableLiveData = new MutableLiveData<>();

        // Set up url
        String url = API_PRODUCTS_PATH + "/" + basketUUID + "?productId=" + productId;

        // Call API with GET request to a provided url: "/basket/userUUID?productId=productId"
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Set value to a MutableLiveData once we get a request
                        basketAddMutableLiveData.setValue(Boolean.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Set false to LiveData to inform that action failed
                basketAddMutableLiveData.setValue(false);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Calls RestAPI with DELETE request for basket Products, puts true in MutableLiveData if it was successful
     *
     * @param basketUUID user's basket id, either phone generated or email
     * @param queue      API request queue
     */
    public void removeProductFromBasket(String basketUUID, String productId, RequestQueue queue) {

        basketRemoveMutableLiveData = new MutableLiveData<>();

        // Set up url
        String url = API_PRODUCTS_PATH + "/" + basketUUID + "?ProductId=" + productId;

        // Call API with DELETE request to a provided url: "/basket/userUUID?productId=productId"
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Set value to a MutableLiveData once we get a request
                        basketRemoveMutableLiveData.setValue(Boolean.valueOf(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Set false to LiveData to inform that action failed
                basketRemoveMutableLiveData.setValue(false);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
