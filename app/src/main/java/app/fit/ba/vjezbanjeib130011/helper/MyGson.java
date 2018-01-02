package app.fit.ba.vjezbanjeib130011.helper;

/**
 * Created by HOME on 20.10.2016.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyGson
{
    public static Gson build()
    {
        GsonBuilder builder = new GsonBuilder();
        return builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    }
}
