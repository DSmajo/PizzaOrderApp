package app.fit.ba.vjezbanjeib130011.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import app.fit.ba.vjezbanjeib130011.OrdersActivity;
import app.fit.ba.vjezbanjeib130011.R;
import app.fit.ba.vjezbanjeib130011.api.ProductsApi;
import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.DateDecimalConverterHelper;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.model.ProductsVM;


public class HomeFragment extends Fragment {

    private ListView listViewProducts;
    private ProductsApi.VMProductsHelper listProducts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        listViewProducts = (ListView) view.findViewById(R.id.listViewProducts);
        Button btnGoToCart = (Button) view.findViewById(R.id.btnGoToCart);

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Data access");
        dialog.setMessage("In progress...");
        dialog.show();

        ProductsApi.GetAll(new MyRunnable<ProductsApi.VMProductsHelper>()
        {
            @Override
            public void run(ProductsApi.VMProductsHelper result)
            {
                if(result != null) {
                    dialog.dismiss();
                    listProducts = result;
                    do_fillList(result);
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(AppContext.getContext(), "Error!", Toast.LENGTH_LONG).show();
                }
            }
        });

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int Id = listProducts.productsList.get(position).Id;

                ProductsApi.GetById(Id, null, new MyRunnable<ProductsVM>() {
                    @Override
                    public void run(ProductsVM productsVM)
                    {
                        Fragment fragment = ItemProductDetailsFragment.newInstance(productsVM);
                        openFragmentAsReplace(fragment);
                    }
                });
            }
        });

        btnGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppContext.getContext(), OrdersActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void do_fillList(final ProductsApi.VMProductsHelper result)
    {
        listViewProducts.setAdapter(new BaseAdapter()
        {
            @Override
            public int getCount() {
                return result.productsList.size();
            }

            @Override
            public Object getItem(int position) {
                return result.productsList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent)
            {
                ProductsVM product = listProducts.productsList.get(position);

                if(view == null) {
                    final LayoutInflater layoutInflater = (LayoutInflater) AppContext.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.item_products, parent, false);
                }

                TextView tvItemProductName = (TextView) view.findViewById(R.id.tvProductName);
                TextView tvItemProductPrice = (TextView) view.findViewById(R.id.tvProductPrice);

                tvItemProductName.setText(product.Name);
                tvItemProductPrice.setText(DateDecimalConverterHelper.decimal_0_00(product.Price) + " KM");

                return view;
            }
        });
    }

    private void openFragmentAsReplace(Fragment fragment)
    {
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, fragment).addToBackStack("appFragments");
        fragmentTransaction.commit();
    }
}
