package com.example.altas.repositories;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.altas.Models.ProductStatus;
import com.example.altas.repositories.BaseRepositories.BaseVolleyRepository;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Public class ProductStatusRepository
 */
public class ProductStatusRepository extends BaseVolleyRepository {

    public static final String API_PRODUCTS_PATH = "http://atlastrestapi.azurewebsites.net/api/productstatus";

    public MutableLiveData<ArrayList<ProductStatus>> productStatusListMutableLiveData;

    /**
     * Calls RestAPI with GET request for productStatuses, puts them in MutableLiveData
     *
     * @param userId user's id for ProductStatus
     * @param queue  API request queue
     */
    public void getAllProductStatus(String userId, RequestQueue queue) {

        productStatusListMutableLiveData = new MutableLiveData<>();

        // Call API with GET request to a provided url: "/productstatus/userId"
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_PRODUCTS_PATH + "/" + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Set value to a MutableLiveData once we get a request
                        productStatusListMutableLiveData.setValue(extractProductStatus(response));
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
     * Calls RestAPI with DELETE request to delete provided ProductStatus
     *
     * @param productStatusId id of productStatus that will be deleted
     * @param queue           API request queue
     */
    public void removeProductStatus(String productStatusId, RequestQueue queue) {

        // Call API with DELETE request to a provided url: "/productstatus/productStatusId"
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, API_PRODUCTS_PATH + "/" + productStatusId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // NO-OP
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
     * Calls RestAPI with PUT request to update provided ProductStatus
     *
     * @param productStatus productStatus that will be updated
     * @param queue         API request queue
     */
    public void updateProductStatus(ProductStatus productStatus, RequestQueue queue) {

        // Get body formed from productStatus
        final String stringBody = getStringBody(productStatus);

        // Call API with PUT request to a provided url: "/productstatus"
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, API_PRODUCTS_PATH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // NO-OP
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // NO-OP
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    // Put a body to a request
                    return stringBody == null ? null : stringBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringBody, "utf-8");
                    return null;
                }
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Calls RestAPI with POST request to create provided ProductStatus
     *
     * @param productStatus productStatus with values that will be provided
     * @param queue         API request queue
     */
    public void addProductStatuses(ProductStatus productStatus, RequestQueue queue) {

        // Get body formed from productStatus
        final String stringBody = getStringBody(productStatus);

        // Call API with POST request to a provided url: "/productstatus"
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_PRODUCTS_PATH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //NO-OP
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //NO-OP
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {

                    // Put a body to a request
                    return stringBody == null ? null : stringBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringBody, "utf-8");
                    return null;
                }
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
