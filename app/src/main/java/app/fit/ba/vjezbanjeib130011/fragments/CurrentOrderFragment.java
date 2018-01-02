package app.fit.ba.vjezbanjeib130011.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.fit.ba.vjezbanjeib130011.R;
import app.fit.ba.vjezbanjeib130011.api.OrdersApi;
import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.DateDecimalConverterHelper;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.helper.Session;
import app.fit.ba.vjezbanjeib130011.model.OrdersVM;


public class CurrentOrderFragment extends Fragment {

    private OrdersVM.ClientOrders currentOrder = Session.getCurrentOrder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_order, container, false);

        ListView listViewOrderItems = (ListView) view.findViewById(R.id.listViewOrderItems);
        Button btnConfirmOrder = (Button) view.findViewById(R.id.btnConfirmOrder);

        do_fillList(currentOrder,listViewOrderItems);

        listViewOrderItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                DialogFragment dialogFragment= ManageOrderItemsFragment.newInstance(position);
                dialogFragment.setCancelable(false);
                openFragmentAsDialog(dialogFragment);
            }
        });

        if(currentOrder != null && currentOrder.OrderItems.size() != 0) {
            btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    do_confirmOrder_click();
                }
            });
        }else {
            btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AppContext.getContext(), "There are no items to order!", Toast.LENGTH_LONG).show();
                }
            });
        }

        return view;
    }

    private void do_confirmOrder_click()
    {
        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Confirm order?");
        adb.setMessage("Are you sure?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float totalPrice = 0;
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Order Confirmation");
                progressDialog.setMessage("In progress...");
                progressDialog.show();

                for(int i=0; i< currentOrder.OrderItems.size(); i++)
                {
                    totalPrice += currentOrder.OrderItems.get(i).Price * currentOrder.OrderItems.get(i).Quantity;
                }

                currentOrder.ClientId = Session.getLoggedUser().ClientId;
                currentOrder.TotalPrice = totalPrice;

                OrdersApi.ConfirmOrder(currentOrder, new MyRunnable<OrdersApi.OrderStatusMessageVM>() {
                    @Override
                    public void run(OrdersApi.OrderStatusMessageVM successMessageVM) {
                        if(successMessageVM.successMessage != null)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(AppContext.getContext(), successMessageVM.successMessage, Toast.LENGTH_LONG).show();
                            Session.removeCurrentOrder();

                            getActivity().finish();
                            getActivity().overridePendingTransition(0,0);

                            OrdersVM.ClientOrders listOrderItems = new OrdersVM.ClientOrders();
                            listOrderItems.OrderItems = new ArrayList<>();
                            Session.setCurrentOrder(listOrderItems);
                        }
                        else if(successMessageVM.errorMessage != null){
                            progressDialog.dismiss();
                            Toast.makeText(AppContext.getContext(), successMessageVM.errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        adb.setCancelable(false);
        adb.show();


    }

    private void do_fillList(final OrdersVM.ClientOrders currentOrder, ListView listViewOrderItems)
    {
        listViewOrderItems.setAdapter(new BaseAdapter()
        {
            @Override
            public int getCount() {
                return currentOrder != null ? currentOrder.OrderItems.size() : 0;
            }

            @Override
            public Object getItem(int position) {
                return currentOrder.OrderItems.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent)
            {
                OrdersVM.OrderDetailsVM orderDetailsVM = currentOrder.OrderItems.get(position);

                if(view == null) {
                    final LayoutInflater layoutInflater = (LayoutInflater) AppContext.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.item_current_order, parent, false);
                }

                TextView tvPizzaValue = (TextView) view.findViewById(R.id.tvPizzaValue);
                TextView tvPizzaTypeValue = (TextView) view.findViewById(R.id.tvTypeValue);
                TextView tvPizzaPriceValue = (TextView) view.findViewById(R.id.tvPizzaPriceValue);
                TextView tvQuantityValue = (TextView) view.findViewById(R.id.tvPizzaQuantityValue);

                tvPizzaValue.setText(orderDetailsVM.Pizza);
                tvPizzaTypeValue.setText(orderDetailsVM.Type);
                tvPizzaPriceValue.setText(DateDecimalConverterHelper.decimal_0_00(orderDetailsVM.Price) + " KM");
                tvQuantityValue.setText(Integer.toString(orderDetailsVM.Quantity));

                return view;
            }
        });
    }

    private void openFragmentAsDialog(DialogFragment dialogFragment)
    {
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        dialogFragment.show(fm,"openDialog");
    }
}
