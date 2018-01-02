package app.fit.ba.vjezbanjeib130011.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import app.fit.ba.vjezbanjeib130011.model.ClientUserVM;
import app.fit.ba.vjezbanjeib130011.model.OrdersVM;
import app.fit.ba.vjezbanjeib130011.model.ProductsVM;

/**
 * Created by HOME on 20.10.2016.
 */

public class Session
{
    public static ClientUserVM getLoggedUser()
    {
        SharedPreferences preferences = AppContext.getContext().getSharedPreferences("my_app", Context.MODE_PRIVATE);

        String user = preferences.getString("loggedUser","");

        if(user.equals(""))
            return null;

        return MyGson.build().fromJson(user, ClientUserVM.class);
    }

    public static void setLoggedUser(ClientUserVM loggedUser)
    {
        SharedPreferences preferences = AppContext.getContext().getSharedPreferences("my_app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("loggedUser", loggedUser != null ? MyGson.build().toJson(loggedUser) : "");
        editor.apply();
    }

    public static void removeLoggedUser()
    {
        SharedPreferences preferences = AppContext.getContext().getSharedPreferences("my_app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("loggedUser", "");
        editor.apply();
    }

    public static OrdersVM.ClientOrders getCurrentOrder()
    {
        SharedPreferences preferences = AppContext.getContext().getSharedPreferences("my_app", Context.MODE_PRIVATE);

        String orderCart = preferences.getString("cart", "");

        if(orderCart.equals(""))
            return  null;

        return MyGson.build().fromJson(orderCart, OrdersVM.ClientOrders.class);
    }

    public static void setCurrentOrder(OrdersVM.ClientOrders currentOrderItems)
    {
        SharedPreferences preferences = AppContext.getContext().getSharedPreferences("my_app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cart", currentOrderItems != null ? MyGson.build().toJson(currentOrderItems) : "");
        editor.apply();
    }

    public static void removeCurrentOrder()
    {
        SharedPreferences preferences = AppContext.getContext().getSharedPreferences("my_app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cart", "");
        editor.apply();
    }
}
