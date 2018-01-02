package app.fit.ba.vjezbanjeib130011.api;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.helper.UrlApiConfig;
import app.fit.ba.vjezbanjeib130011.helper.volley.GsonRequest;
import app.fit.ba.vjezbanjeib130011.model.ClientUserVM;

/**
 * Created by HOME on 28.11.2016.
 */

public class UsersApi
{
    public static class RegistrationStatusMessageVM
    {
        public String successMessage;
        public String errorMessage;
    }

    public static class UpdateProfileStatusMessageVM
    {
        public String successMessage;
        public String errorMessage;
    }

    public static void Registration(ClientUserVM model, final MyRunnable<RegistrationStatusMessageVM> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(AppContext.getContext());
        String url = "Users/Registration";

        GsonRequest<RegistrationStatusMessageVM> gsonRequest = new GsonRequest<>(Request.Method.POST, UrlApiConfig.urlApi + url, RegistrationStatusMessageVM.class, null, model,
                new Response.Listener<RegistrationStatusMessageVM>() {
                    @Override
                    public void onResponse(RegistrationStatusMessageVM response) {
                        response.successMessage = "Successfully registered";
                        onSuccess.run(response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        NetworkResponse networkResponse = error.networkResponse;
                        if(networkResponse != null && networkResponse.statusCode == 409) {
                            RegistrationStatusMessageVM response = new RegistrationStatusMessageVM();
                            response.errorMessage = "That username and/or phone already exists!";
                            onSuccess.run(response);
                        }
                        else if(networkResponse != null && networkResponse.statusCode == 400){
                            RegistrationStatusMessageVM response = new RegistrationStatusMessageVM();
                            response.errorMessage = "Bad request!";
                            onSuccess.run(response);
                        }
                        else{
                            RegistrationStatusMessageVM response = new RegistrationStatusMessageVM();
                            response.errorMessage = "Error!";
                            onSuccess.run(response);
                        }
                    }
                }
        );

        gsonRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(gsonRequest);
    }

    public static void Authentication(String username, String password, final MyRunnable<ClientUserVM> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(AppContext.getContext());
        String url = "Users/Authentication?username=" + username + "&password=" + password;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlApiConfig.urlApi + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        final Gson gson = new GsonBuilder().create();
                        final ClientUserVM model = gson.fromJson(response, ClientUserVM.class);
                        onSuccess.run(model);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(AppContext.getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void UpdateUserProfile(ClientUserVM model, final MyRunnable<UpdateProfileStatusMessageVM> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(AppContext.getContext());
        String url = "Users/UpdateUserProfile";

        GsonRequest<UpdateProfileStatusMessageVM> gsonRequest = new GsonRequest<>(Request.Method.POST, UrlApiConfig.urlApi + url, UpdateProfileStatusMessageVM.class, null, model,
                new Response.Listener<UpdateProfileStatusMessageVM>() {
                    @Override
                    public void onResponse(UpdateProfileStatusMessageVM response) {
                        response = new UpdateProfileStatusMessageVM();
                        response.successMessage = "Successfully updated!";
                        onSuccess.run(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if(networkResponse != null && networkResponse.statusCode == 400){
                            UpdateProfileStatusMessageVM response = new UpdateProfileStatusMessageVM();
                            response.errorMessage = "Bad request!";
                            onSuccess.run(response);
                        }
                        else{
                            UpdateProfileStatusMessageVM response = new UpdateProfileStatusMessageVM();
                            response.errorMessage = "Error!";
                            onSuccess.run(response);
                        }
                    }
                });


        gsonRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(gsonRequest);
    }
}
