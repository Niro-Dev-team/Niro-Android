package com.niro.niroapp.pubnub_chat;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.niro.niroapp.R;
import com.niro.niroapp.pubnub_chat.chatNav.GroupChatFragment;
import com.niro.niroapp.pubnub_chat.others.OthersDummy;


public class ActivityMain extends AppCompatActivity {
    Fragment currentFragment = null;
    FragmentTransaction ft;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_nav);
        currentFragment = new GroupChatFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, currentFragment);
        ft.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_chat:
                       currentFragment = new GroupChatFragment();
                       ft = getSupportFragmentManager().beginTransaction();
                       ft.replace(R.id.content, currentFragment);
                        ft.commit();

                        return true;
                    case R.id.action_profile:
                        currentFragment = new OthersDummy();
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content, currentFragment);
                        ft.commit();

                        return true;

                }

                return true;
            }
        });
    }
}



