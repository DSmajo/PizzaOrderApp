package app.fit.ba.vjezbanjeib130011.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by HOME on 12.11.2016.
 */

public class OrdersVM implements Serializable
{
    public static class OrderDetailsVM implements Serializable
    {
        public int OrderDetailsId;
        public String Pizza;
        public float Price;
        public String Type;
        public int Quantity;
    }


    public static class ClientOrders implements Serializable
    {
        public int OrderId;
        public int ClientId;
        public Date OrderDate;
        public float TotalPrice;
        public List<OrderDetailsVM> OrderItems;
    }

    public List<ClientOrders> PreviousClientOrders;
}
