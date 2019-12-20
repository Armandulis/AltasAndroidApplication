package com.example.altas.repositories;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.altas.Models.Product;
import com.example.altas.Models.ProductStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ProductStatusRepository {


    public static final String API_PRODUCTS_PATH = "http://altas.gear.host/api/productstatus";

    public MutableLiveData<ArrayList<ProductStatus>> productStatusListMutableLiveData;
    private ArrayList<ProductStatus> products;

    public ProductStatusRepository() {
        products = new ArrayList<>();
    }

    public ArrayList<ProductStatus> getAllProducts(String userId, RequestQueue queue) {

        productStatusListMutableLiveData = new MutableLiveData<>();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_PRODUCTS_PATH + "/" + userId ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productStatusListMutableLiveData.setValue(extractProductStatus(response));
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

    public void removeProductStatus(String productStatusId, RequestQueue queue) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, API_PRODUCTS_PATH + "/" + productStatusId ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void updateProductStatus(ProductStatus productStatus, RequestQueue queue) {

        final String stringBody = getStringBody(productStatus);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, API_PRODUCTS_PATH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            } @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return stringBody == null ? null : stringBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void addProductStatuses(ProductStatus productStatus, RequestQueue queue) {

        final String stringBody = getStringBody(productStatus);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_PRODUCTS_PATH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            } @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return stringBody == null ? null : stringBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private String getStringBody(ProductStatus productStatus) {

        JSONObject jsonBody = new JSONObject();

        try
        {
            jsonBody.put("Id", productStatus.id);
            jsonBody.put("userId", productStatus.userId);
            jsonBody.put("status", productStatus.status);
            jsonBody.put("purchaseDate", productStatus.purchaseDate);

            JSONObject jsonBodyProduct = new JSONObject();

            jsonBodyProduct.put("id", productStatus.product.id);
            jsonBodyProduct.put("description",  productStatus.product.description);
            jsonBodyProduct.put("title",  productStatus.product.name);
            jsonBodyProduct.put("price",  productStatus.product.price);
            jsonBodyProduct.put("pictureUrl",  productStatus.product.pictureUrl);
            jsonBodyProduct.put("brand",  productStatus.product.brand);

            jsonBody.put("product", jsonBodyProduct);
        }
        catch (Exception e){

        }

        return jsonBody.toString();
    }


    private ArrayList<ProductStatus> extractProductStatus(String response) {

        ArrayList<ProductStatus> productStatusList = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(response);

            for (int i = 0; i < array.length(); i++) {

                ProductStatus productStatus = new ProductStatus();

                JSONObject jsonObject = array.getJSONObject(i);

                productStatus.id = jsonObject.getString("id");
                productStatus.userId = jsonObject.getString("userId");
                productStatus.status = jsonObject.getString("status");
                productStatus.purchaseDate = jsonObject.getString("purchaseDate");

                // Get product
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
            e.printStackTrace();
        }

        return productStatusList;
    }
}
