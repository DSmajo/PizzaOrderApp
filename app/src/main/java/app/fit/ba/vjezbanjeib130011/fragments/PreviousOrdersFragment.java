package app.fit.ba.vjezbanjeib130011.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import app.fit.ba.vjezbanjeib130011.R;
import app.fit.ba.vjezbanjeib130011.api.OrdersApi;
import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.DateDecimalConverterHelper;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.helper.Session;
import app.fit.ba.vjezbanjeib130011.model.OrdersVM;


public class PreviousOrdersFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_previous_orders, container, false);

        OrdersApi.GetOrdersByClientId(Session.getLoggedUser().ClientId, new MyRunnable<OrdersVM>() {
            @Override
            public void run(final OrdersVM ordersVM)
            {
                ExpandableListView exListView = (ExpandableListView) view.findViewById(R.id.previousOrdersExpandableListView);
                exListView.setAdapter(new BaseExpandableListAdapter() {
                    @Override
                    public int getGroupCount() {
                        return ordersVM == null ? 0 : ordersVM.PreviousClientOrders.size();
                    }

                    @Override
                    public int getChildrenCount(int groupPosition) {
                        return ordersVM.PreviousClientOrders.get(groupPosition).OrderItems.size();
                    }

                    @Override
                    public Object getGroup(int groupPosition) {
                        return null;
                    }

                    @Override
                    public Object getChild(int groupPosition, int childPosition) {
                        return null;
                    }

                    @Override
                    public long getGroupId(int groupPosition) {
                        return 0;
                    }

                    @Override
                    public long getChildId(int groupPosition, int childPosition) {
                        return 0;
                    }

                    @Override
                    public boolean hasStableIds() {
                        return false;
                    }

                    @Override
                    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent)
                    {
                        if(view == null)
                        {
                            final LayoutInflater inflater = (LayoutInflater) AppContext.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            view = inflater.inflate(R.layout.item_previous_orders, parent, false);
                        }

                        final OrdersVM.ClientOrders clientOrders = ordersVM.PreviousClientOrders.get(groupPosition);

                        TextView tvOrderDateValue = (TextView) view.findViewById(R.id.tvOrderDateValue);
                        TextView tvTotalPriceValue = (TextView) view.findViewById(R.id.tvTotalPriceValue);

                        tvOrderDateValue.setText(DateDecimalConverterHelper.to_dd_mm_yyyy(clientOrders.OrderDate));
                        tvTotalPriceValue.setText(DateDecimalConverterHelper.decimal_0_00(clientOrders.TotalPrice) + " KM");

                        return view;
                    }

                    @Override
                    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent)
                    {
                        if(view == null)
                        {
                            final LayoutInflater inflater = (LayoutInflater) AppContext.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            view = inflater.inflate(R.layout.item_previous_orders_expand, parent, false);
                        }

                        final OrdersVM.OrderDetailsVM orderDetails = ordersVM.PreviousClientOrders.get(groupPosition).OrderItems.get(childPosition);

                        TextView tvPizzaValue = (TextView) view.findViewById(R.id.tvPizzaValue);
                        TextView tvTypeValue = (TextView) view.findViewById(R.id.tvTypeValue);
                        TextView tvPriceValue = (TextView) view.findViewById(R.id.tvPizzaPriceValue);
                        TextView tvQuantityValue = (TextView) view.findViewById(R.id.tvPizzaQuantityValue);

                        tvPizzaValue.setText(orderDetails.Pizza);
                        tvTypeValue.setText(orderDetails.Type);
                        tvPriceValue.setText(DateDecimalConverterHelper.decimal_0_00(orderDetails.Price) + " KM");
                        tvQuantityValue.setText(Integer.toString(orderDetails.Quantity));

                        return view;
                    }

                    @Override
                    public boolean isChildSelectable(int groupPosition, int childPosition) {
                        return false;
                    }
                });
            }
        });

        return view;
    }

}
