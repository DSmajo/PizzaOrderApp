package app.fit.ba.vjezbanjeib130011.helper;

import android.app.Application;
import android.content.Context;

/**
 * Created by HOME on 20.10.2016.
 */

public class AppContext extends Application
{
    public static Context getContext()
    {
        return context;
    }

    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();

        context = getApplicationContext();
    }
}
