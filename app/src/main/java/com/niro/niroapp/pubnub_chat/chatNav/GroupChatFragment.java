package com.niro.niroapp.pubnub_chat.chatNav;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.niro.niroapp.R;


public class GroupChatFragment extends Fragment {

    private String[] titles = new String[]{"GROUPS", "MESSAGES"};
    TabLayout tabLayout;



    private View rootView;
    ViewPager2 myViewPager2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView=inflater.inflate(
                R.layout.activity_chat, container, false);
        tabLayout = rootView.findViewById(R.id.tabs);

        myViewPager2 = rootView.findViewById(R.id.viewpager);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        final TabAdapter adapter = new TabAdapter(fm, getLifecycle());
        myViewPager2.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText("GROUPS"));
        tabLayout.addTab(tabLayout.newTab().setText("MESSAGES"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myViewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        new TabLayoutMediator(tabLayout, myViewPager2,
                (tab, position) -> tab.setText(titles[position])).attach();

        return rootView;
    }


}