package com.hunglq.map1.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.hunglq.map1.util.CustomerInfoFragment;
import com.hunglq.map1.util.TransactionHistoryFragment;

public class UserFragmentAdapter extends FragmentStatePagerAdapter {

    String listTab[] = {"Customer Info", "Transaction History"};
    CustomerInfoFragment customerInfoFragment;
    TransactionHistoryFragment transactionHistoryFragment;

    public UserFragmentAdapter(FragmentManager fm) {
        super(fm);
        customerInfoFragment = new CustomerInfoFragment();
        transactionHistoryFragment = new TransactionHistoryFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return customerInfoFragment;
            case 1:
                return transactionHistoryFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return listTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}
