package com.example.altas.repositories;

import android.util.Log;

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
import com.example.altas.Models.LoginInput;
import com.example.altas.Models.Product;
import com.example.altas.Models.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.UnsupportedEncodingException;

public class AuthenticationRepository {

    public static final String API_PRODUCTS_PATH = "http://altas.gear.host/api/auth";

    public MutableLiveData<User> userMutableLiveData;

    public AuthenticationRepository() {
        userMutableLiveData = new MutableLiveData<>();
    }

    public boolean registerUser(LoginInput loginInput, RequestQueue queue) {

        final String stringLoginInputBody = getStringBody(loginInput);
        final String url = API_PRODUCTS_PATH + "/register";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        userMutableLiveData.setValue(getUserFromResponse(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return stringLoginInputBody == null ? null : stringLoginInputBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringLoginInputBody, "utf-8");
                    return null;
                }
            }

        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return true;
    }

    private User getUserFromResponse(String response) {

        User user = User.getInstance();
        try {

            JSONObject jsonObject = new JSONObject(response);

            user.email = jsonObject.getString("email");
            user.expiresIn = jsonObject.getString("expiresIn");
            user.idToken = jsonObject.getString("idToken");
            user.localId = jsonObject.getString("localId");

        }
        catch (Exception e){

            Log.d("ERROR", "getUserFromResponse: " + e.getMessage());
        }
        return user;
    }

    private String getStringBody(LoginInput loginInput) {

        JSONObject jsonLoginInputBody = new JSONObject();

        try {
            jsonLoginInputBody.put("email", loginInput.email);
            jsonLoginInputBody.put("password", loginInput.password);
        } catch (Exception e) {

        }

        return jsonLoginInputBody.toString();

    }

    public boolean loginUser(LoginInput loginInput, RequestQueue queue) {

        final String stringLoginInputBody = getStringBody(loginInput);
        final String url = API_PRODUCTS_PATH + "/login";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        userMutableLiveData.setValue(getUserFromResponse(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return stringLoginInputBody == null ? null : stringLoginInputBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringLoginInputBody, "utf-8");
                    return null;
                }
            }

        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return true;
    }
}
