package app.fit.ba.vjezbanjeib130011.helper.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import app.fit.ba.vjezbanjeib130011.helper.MyGson;

/**
 * Created by HOME on 20.10.2016.
 */

public class GsonRequest<T> extends JsonRequest<T>
{

    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url     URL of the request to make
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Object object,Response.Listener<T> listener, Response.ErrorListener errorListener)
    {
        super(method, url, object!=null? MyGson.build().toJson(object):null, listener, errorListener);

        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response)
    {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        try {
            String strJson = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Response<T> success = Response.success(MyGson.build().fromJson(strJson, clazz), HttpHeaderParser.parseCacheHeaders(response));
            return success;
        } catch (UnsupportedEncodingException e) {
            Response<T> error = Response.error(new ParseError(e));
            return error;
        } catch (JsonSyntaxException e) {
            Response<T> error = Response.error(new ParseError(e));
            return error;
        }
    }
}