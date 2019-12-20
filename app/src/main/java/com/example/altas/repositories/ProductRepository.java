package com.example.altas.repositories;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.altas.Models.Filter;
import com.example.altas.Models.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductRepository {

    public static final String API_PRODUCTS_PATH = "http://altas.gear.host/api/products";

    private ArrayList<Product> products;
    public MutableLiveData<ArrayList<Product>> productsListMutableLiveData;

    // TODO count how many products there are in the database so we could calculate last page

    public ProductRepository() {
        products = new ArrayList<>();
    }

    // TODO PAGINATED QUERY FROM DB use ShopFragment.PAGE_SIZE for the amount that we need to get
    public ArrayList<Product> getPaginatedProducts(Filter filter, RequestQueue queue) {

        productsListMutableLiveData = new MutableLiveData<>();

        String url = setUpUrlByFilter(filter);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productsListMutableLiveData.setValue(extractProduct(response));
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

    private String setUpUrlByFilter(Filter filter){
        String url = API_PRODUCTS_PATH;

        url = url + "?OrderBy=" + filter.orderBy;

        if (filter.lastProductId != null){
            url = url + "&LastItemId=" + filter.lastProductId;
        }
        if (filter.searchWord != null){
            url = url + "&SearchWord=" + filter.searchWord;
        }

        if (filter.amount != 0){
            url = url + "&Amount=" + filter.amount;
        }

        return url;
    }
}
