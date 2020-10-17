package com.niro.niroapp.pubnub_chat.chatNav;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.niro.niroapp.pubnub_chat.grouplist.GroupsList;
import com.niro.niroapp.pubnub_chat.onetoone.OneToOne;


public class TabAdapter extends FragmentStateAdapter {

    public TabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Hardcoded in this order, you'll want to use lists and make sure the titles match
        if (position == 0) {
            return new GroupsList();
        }
        return new OneToOne();
    }

    @Override
    public int getItemCount() {
        // Hardcoded, use lists
        return 2;
    }
}