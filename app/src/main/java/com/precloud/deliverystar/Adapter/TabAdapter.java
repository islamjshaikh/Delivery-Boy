package com.precloud.deliverystar.Adapter;

import android.content.Context;

import com.precloud.deliverystar.Fragment.CancelledOrder_fragment;
import com.precloud.deliverystar.Fragment.Completed_Order_Fragment;
import com.precloud.deliverystar.Fragment.Pending_Order_Fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;


    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Pending_Order_Fragment pending_order_fragmentFragment = new Pending_Order_Fragment();
                return pending_order_fragmentFragment;

            case 1:
                Completed_Order_Fragment completed_order_fragment = new Completed_Order_Fragment();
                return completed_order_fragment;
            case 2:
                CancelledOrder_fragment cancelledOrder_fragment = new CancelledOrder_fragment();
                return cancelledOrder_fragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
