package app.fit.ba.vjezbanjeib130011.api;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.helper.UrlApiConfig;
import app.fit.ba.vjezbanjeib130011.model.ProductsVM;

/**
 * Created by HOME on 7.11.2016.
 */

public class ProductsApi
{
    public class VMProductsHelper
    {
        public List<ProductsVM> productsList;
    }

    public static void GetAll(final MyRunnable<VMProductsHelper> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(AppContext.getContext());

        String url = "Products/GetAll";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlApiConfig.urlApi + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        final Gson gson = new GsonBuilder().create();
                        final VMProductsHelper model = gson.fromJson(response, VMProductsHelper.class);
                        onSuccess.run(model);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(AppContext.getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public static void GetById(int Id, Integer typeId, final MyRunnable<ProductsVM> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(AppContext.getContext());

        String url = "Products/GetById?Id=" + Id + "&typeId=" + typeId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlApiConfig.urlApi + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        final Gson gson = new GsonBuilder().create();
                        final ProductsVM model = gson.fromJson(response, ProductsVM.class);
                        onSuccess.run(model);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(AppContext.getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }
}
