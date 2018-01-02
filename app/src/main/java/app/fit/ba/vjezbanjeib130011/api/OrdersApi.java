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

import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.MyGson;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.helper.UrlApiConfig;
import app.fit.ba.vjezbanjeib130011.helper.volley.GsonRequest;
import app.fit.ba.vjezbanjeib130011.model.OrdersVM;

/**
 * Created by HOME on 12.11.2016.
 */

public class OrdersApi
{
    public static class OrderStatusMessageVM
    {
        public String successMessage;
        public String errorMessage;
    }

    public static void GetOrdersByClientId(int clientId,  final MyRunnable<OrdersVM> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(AppContext.getContext());
        String url = "Orders/GetOrdersByClientId?clientId=" + clientId;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlApiConfig.urlApi + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        final Gson gson = MyGson.build();
                        final OrdersVM model = gson.fromJson(response, OrdersVM.class);
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

    public static void ConfirmOrder(OrdersVM.ClientOrders model, final MyRunnable<OrderStatusMessageVM> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(AppContext.getContext());
        String url = "Orders/ConfirmOrder";

        GsonRequest<OrderStatusMessageVM> gsonRequest = new GsonRequest<>(Request.Method.POST, UrlApiConfig.urlApi + url, OrderStatusMessageVM.class, null, model,
                new Response.Listener<OrderStatusMessageVM>() {
                    @Override
                    public void onResponse(OrderStatusMessageVM response) {
                        response.successMessage = "Successfully ordered! Your order is on the way!";
                        onSuccess.run(response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        NetworkResponse networkResponse = error.networkResponse;
                        if(networkResponse != null && networkResponse.statusCode == 400){
                            OrderStatusMessageVM response = new OrderStatusMessageVM();
                            response.errorMessage = "Bad request!";
                            onSuccess.run(response);
                        }
                        else{
                            OrderStatusMessageVM response = new OrderStatusMessageVM();
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
}
