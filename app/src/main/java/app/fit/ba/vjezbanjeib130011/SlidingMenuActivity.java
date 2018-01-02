package app.fit.ba.vjezbanjeib130011;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import app.fit.ba.vjezbanjeib130011.fragments.HomeFragment;
import app.fit.ba.vjezbanjeib130011.fragments.LoginFragment;
import app.fit.ba.vjezbanjeib130011.fragments.RegisterFragment;
import app.fit.ba.vjezbanjeib130011.fragments.UserProfileFragment;
import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.Session;
import app.fit.ba.vjezbanjeib130011.model.OrdersVM;

public class SlidingMenuActivity extends FragmentActivity {

    private DrawerLayout drawerLayout;
    private ListView leftDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        leftDrawer = (ListView) findViewById(R.id.leftDrawer);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow_custom, GravityCompat.START);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewHeader = inflater.inflate(R.layout.title_sliding_menu, null, false);
        leftDrawer.addHeaderView(viewHeader,null, false);

        leftDrawer.setAdapter(new ArrayAdapter<>(this, R.layout.item_drawer_list, getTitles()));

        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawerOpen,  /* "open drawer" description for accessibility */
                R.string.drawerClosed  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        selectItem(1);
    }

    private void selectItem(int position)
    {
        Fragment fragment = null;

        if(Session.getLoggedUser() == null)
        {
            if(position == 1)
                fragment = new LoginFragment();
            if(position == 2)
                fragment = new RegisterFragment();
            if(position == 3)
            {
                Toast.makeText(AppContext.getContext(), "See you soon!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
        {
            if(Session.getCurrentOrder() == null) {
                OrdersVM.ClientOrders listOrderItems = new OrdersVM.ClientOrders();
                listOrderItems.OrderItems = new ArrayList<>();
                Session.setCurrentOrder(listOrderItems);
            }

            if(position == 1)
                fragment = new HomeFragment();
            if(position == 2) {
                fragment = new UserProfileFragment();
                Toast.makeText(AppContext.getContext(), "You can edit your profile here!", Toast.LENGTH_SHORT).show();
            }
            if(position == 3) {
                Intent intent = new Intent(AppContext.getContext(), OrdersActivity.class);
                startActivity(intent);
                leftDrawer.setItemChecked(position, true);
                leftDrawer.setAdapter(new ArrayAdapter<>(this, R.layout.item_drawer_list, getTitles()));
                drawerLayout.closeDrawer(leftDrawer);
            }
            if(position == 4){
                Session.removeLoggedUser();
                Session.removeCurrentOrder();

                Intent intent = getIntent();
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0,0);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        }

        if (fragment!=null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentPlace, fragment).addToBackStack("appFragments").commit();

            // update selected item and title, then close the drawer
            leftDrawer.setItemChecked(position, true);
            leftDrawer.setAdapter(new ArrayAdapter<>(this, R.layout.item_drawer_list, getTitles()));
            drawerLayout.closeDrawer(leftDrawer);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack("appFragments", android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        else
            super.onBackPressed();
    }

    private String[] getTitles()
    {
        if(Session.getLoggedUser() == null)
        {
            return new String[]{
                    "Login",
                    "Register",
                    "Exit"
            };
        }
        else
        {
            return new String[]{
                    "Home",
                    "My profile",
                    "Orders",
                    "Logout"
            };
        }

    }

}
