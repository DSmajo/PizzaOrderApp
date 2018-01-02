package app.fit.ba.vjezbanjeib130011.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.fit.ba.vjezbanjeib130011.R;
import app.fit.ba.vjezbanjeib130011.api.ProductsApi;
import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.DateDecimalConverterHelper;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.helper.Session;
import app.fit.ba.vjezbanjeib130011.helper.StringToByteArrayToImageViewHelper;
import app.fit.ba.vjezbanjeib130011.model.OrdersVM;
import app.fit.ba.vjezbanjeib130011.model.ProductsVM;


public class ItemProductDetailsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MODEL1 = "itemModel";

    private ProductsVM itemModel;
    private int counterHelper = 0;
    private ProductsVM productAfterTypeChange;

    private OrdersVM.ClientOrders listOrderItems;
    private OrdersVM.OrderDetailsVM currentOrderItem;

    public static ItemProductDetailsFragment newInstance(ProductsVM productsVM)
    {
        ItemProductDetailsFragment fragment = new ItemProductDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MODEL1, productsVM);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentOrderItem = new OrdersVM.OrderDetailsVM();
        productAfterTypeChange = null;
        listOrderItems = Session.getCurrentOrder();

        if (getArguments() != null) {
            itemModel = (ProductsVM) getArguments().getSerializable(ARG_MODEL1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_item_product_details, container, false);

        TextView tvItemProductTitle = (TextView) view.findViewById(R.id.tvItemTitle);
        final TextView tvItemProductPrice = (TextView) view.findViewById(R.id.tvItemPrice);
        TextView tvItemProductDescription = (TextView) view.findViewById(R.id.tvItemDescription);
        Spinner spItemProductType = (Spinner) view.findViewById(R.id.spItemProductType);
        ImageView ivProductPhoto = (ImageView) view.findViewById(R.id.ivItemPhoto);
        Button btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);

        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(AppContext.getContext(), R.array.arrayProductType, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spItemProductType.setAdapter(adapter);

        spItemProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ProductsApi.GetById(itemModel.Id, position + 1, new MyRunnable<ProductsVM>() {
                    @Override
                    public void run(ProductsVM productsVM)
                    {
                        productAfterTypeChange = productsVM;
                        tvItemProductPrice.setText(DateDecimalConverterHelper.decimal_0_00(productsVM.Price) + " KM");
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvItemProductPrice.setText(DateDecimalConverterHelper.decimal_0_00(itemModel.Price) + " KM");
            }
        });
        tvItemProductTitle.setText(itemModel.Name);
        //tvItemProductPrice.setText(itemModel.Price);
        tvItemProductDescription.setText(itemModel.Description);
        StringToByteArrayToImageViewHelper.StringToByteArrayToImageView(AppContext.getContext(), itemModel.Photo, ivProductPhoto);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Add to cart?");
                adb.setMessage("Are you sure?");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(productAfterTypeChange != null)
                        {
                            currentOrderItem.Pizza = productAfterTypeChange.Name;
                            currentOrderItem.Price = productAfterTypeChange.Price;
                            currentOrderItem.Type = productAfterTypeChange.Type;
                        }else
                        {
                            currentOrderItem.Pizza = itemModel.Name;
                            currentOrderItem.Price = itemModel.Price;
                            currentOrderItem.Type = itemModel.Type;
                        }

                        for(int i = 0; i<listOrderItems.OrderItems.size(); i++)
                        {
                            if(listOrderItems.OrderItems.get(i).Pizza.equals(currentOrderItem.Pizza) &&
                                    listOrderItems.OrderItems.get(i).Price == currentOrderItem.Price &&
                                    listOrderItems.OrderItems.get(i).Type.equals(currentOrderItem.Type))
                            {
                                listOrderItems.OrderItems.get(i).Quantity += 1;
                                counterHelper++;
                                Session.setCurrentOrder(listOrderItems);
                            }
                        }

                        if(counterHelper == 0){
                            currentOrderItem.Quantity = 1;
                            listOrderItems.OrderItems.add(currentOrderItem);
                            Session.setCurrentOrder(listOrderItems);
                        }
                        else {
                            counterHelper = 0;
                        }

                        Toast.makeText(AppContext.getContext(), "Product successfully added to cart!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Fragment fragment = new HomeFragment();
                        openFragmentAsReplace(fragment);
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
        });

        return view;
    }

    private void openFragmentAsReplace(Fragment fragment)
    {
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, fragment).addToBackStack("appFragments");
        fragmentTransaction.commit();
    }
}
