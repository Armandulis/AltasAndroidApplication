package com.example.altas.repositories;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.altas.Models.LoginInput;
import com.example.altas.Models.User;
import com.example.altas.repositories.BaseRepositories.BaseVolleyRepository;

import java.io.UnsupportedEncodingException;

/**
 * Public class AuthenticationRepository
 */
public class AuthenticationRepository extends BaseVolleyRepository {

    public static final String API_PRODUCTS_PATH = "http://altas.gear.host/api/auth";

    public MutableLiveData<User> userMutableLiveData;

    /**
     * AuthenticationRepository constructor
     */
    public AuthenticationRepository() {
        userMutableLiveData = new MutableLiveData<>();
    }

    /**
     * Calls RestAPI with POST request for user register, puts userToken in MutableLiveData
     *
     * @param loginInput user's wanted LoginInput values
     * @param queue      API request queue
     */
    public void registerUser(LoginInput loginInput, RequestQueue queue) {

        // Get body formed from user's wanted LoginInput values
        final String stringLoginInputBody = getStringBody(loginInput);
        final String url = API_PRODUCTS_PATH + "/register";

        // Call API with POST request to a provided url: "/auth/register"
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Put response Token in LiveMutableData
                        userMutableLiveData.setValue(getUserFromResponse(response));
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
                    return stringLoginInputBody == null ? null : stringLoginInputBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringLoginInputBody, "utf-8");
                    return null;
                }
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Calls RestAPI with POST request for register, puts userToken in MutableLiveData
     *
     * @param loginInput user's LoginInput
     * @param queue      API request queue
     */
    public void loginUser(LoginInput loginInput, RequestQueue queue) {

        // Get body formed from user's LoginInput
        final String stringLoginInputBody = getStringBody(loginInput);
        final String url = API_PRODUCTS_PATH + "/login";

        // Call API with POST request to a provided url: "/auth/login"
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Put response Token in LiveMutableData
                        userMutableLiveData.setValue(getUserFromResponse(response));
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
                    return stringLoginInputBody == null ? null : stringLoginInputBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", stringLoginInputBody, "utf-8");
                    return null;
                }
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
