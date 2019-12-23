package com.example.altas.repositories;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.altas.Models.Filter;
import com.example.altas.Models.Product;
import com.example.altas.repositories.BaseRepositories.BaseVolleyRepository;

import java.util.ArrayList;

public class ProductRepository extends BaseVolleyRepository {

    public static final String API_PRODUCTS_PATH = "http://altas.gear.host/api/products";
    public MutableLiveData<ArrayList<Product>> productsListMutableLiveData;

    /**
     * Calls RestAPI with GET request for Products, puts them in MutableLiveData
     *
     * @param filter used for query products
     * @param queue  API request queue
     */
    public void getPaginatedProducts(Filter filter, RequestQueue queue) {

        productsListMutableLiveData = new MutableLiveData<>();

        String url = setUpUrlByFilter(filter);

        // Call API with GET request to a provided url: "/products"
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Set value to a MutableLiveData once we get a request
                        productsListMutableLiveData.setValue(extractProduct(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // NO-OP
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Takes filter values and puts them in a string as url for api requests
     *
     * @param filter filter that will parsed to url
     * @return url with filter values
     */
    private String setUpUrlByFilter(Filter filter) {

        // Base API Url
        String url = API_PRODUCTS_PATH;

        // OrderBy always has value
        url = url + "?OrderBy=" + filter.orderBy;

        // Check if filter has any other values
        if (filter.lastProductId != null) {
            url = url + "&LastItemId=" + filter.lastProductId;
        }
        if (filter.searchWord != null) {
            url = url + "&SearchWord=" + filter.searchWord;
        }
        if (filter.amount != 0) {
            url = url + "&Amount=" + filter.amount;
        }

        // Return formatted url
        return url;
    }
}
