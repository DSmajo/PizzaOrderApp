package app.fit.ba.vjezbanjeib130011.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.fit.ba.vjezbanjeib130011.OrdersActivity;
import app.fit.ba.vjezbanjeib130011.R;
import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.Session;
import app.fit.ba.vjezbanjeib130011.model.OrdersVM;
import app.fit.ba.vjezbanjeib130011.model.ProductsVM;


public class ManageOrderItemsFragment extends DialogFragment {

    private static final String ARG_MODEL1 = "itemPosition";
    private int itemPosition;
    private OrdersVM.OrderDetailsVM item;
    private OrdersVM.ClientOrders listOrderItems;

    public static ManageOrderItemsFragment newInstance(int itemPosition)
    {
        ManageOrderItemsFragment fragment = new ManageOrderItemsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MODEL1, itemPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            itemPosition = (int) getArguments().getSerializable(ARG_MODEL1);
            listOrderItems = Session.getCurrentOrder();
            item = listOrderItems.OrderItems.get(itemPosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_manage_order_items, container, false);

        final Dialog dialog = getDialog();
        dialog.setTitle("Manage order item");

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black);

        final EditText etChangeQuantity = (EditText) view.findViewById(R.id.etChangeQuantity);
        Button btnRemoveItem = (Button) view.findViewById(R.id.btnRemoveItem);
        Button btnCloseFragment = (Button) view.findViewById(R.id.btnCloseFragment);

        etChangeQuantity.setText(item.Quantity + "");

        btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderItems.OrderItems.remove(item);
                Session.setCurrentOrder(listOrderItems);

                dialog.dismiss();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                getActivity().overridePendingTransition(0,0);
                intent.putExtra("firstCall", false);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        });

        btnCloseFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(etChangeQuantity.getText().toString()) < 1){
                    etChangeQuantity.requestFocus();
                    etChangeQuantity.setError("Quantity cannot be less than 1");
                }else{
                    listOrderItems.OrderItems.get(itemPosition).Quantity = Integer.parseInt(etChangeQuantity.getText().toString());
                    Session.setCurrentOrder(listOrderItems);

                    dialog.dismiss();
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    getActivity().overridePendingTransition(0,0);
                    intent.putExtra("firstCall", false);
                    startActivity(intent);
                    getActivity().overridePendingTransition(0,0);
                }
            }
        });

        return view;
    }

}
