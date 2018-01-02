package app.fit.ba.vjezbanjeib130011;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import app.fit.ba.vjezbanjeib130011.api.OrdersApi;
import app.fit.ba.vjezbanjeib130011.fragments.CurrentOrderFragment;
import app.fit.ba.vjezbanjeib130011.fragments.PreviousOrdersFragment;
import app.fit.ba.vjezbanjeib130011.helper.AppContext;
import app.fit.ba.vjezbanjeib130011.helper.MyRunnable;
import app.fit.ba.vjezbanjeib130011.helper.Session;
import app.fit.ba.vjezbanjeib130011.model.OrdersVM;

public class OrdersActivity extends FragmentActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        final boolean firstCall = getIntent().getBooleanExtra("firstCall", true);

        viewPager = (ViewPager) findViewById(R.id.ordersViewPager);

        fill_ViewPager(firstCall);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);
    }

    private void fill_ViewPager(final boolean firstCall)
    {
        final String tabTitles[] = new String[] { "Current order (cart)", "Previous orders" };

        final FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentPagerAdapter(fm)
        {
            @Override
            public Fragment getItem(int position)
            {
                Fragment fragment = null;
                if(position == 0)
                {
                    if(firstCall) {
                        if(Session.getCurrentOrder() != null && Session.getCurrentOrder().OrderItems.size() != 0)
                            Toast.makeText(AppContext.getContext(), "Click on item for more options!", Toast.LENGTH_SHORT).show();
                    }
                    fragment = new CurrentOrderFragment();
                }
                if(position == 1){
                    fragment = new PreviousOrdersFragment();
                }

                return fragment;
            }

            @Override
            public int getCount()
            {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position)
            {
                // Generate title based on item position
                return tabTitles[position];
            }

        });
    }
}
