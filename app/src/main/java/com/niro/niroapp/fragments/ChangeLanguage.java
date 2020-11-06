package com.niro.niroapp.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.niro.niroapp.Niro;
import com.niro.niroapp.R;
import com.niro.niroapp.activities.BaseActivity;
import com.niro.niroapp.activities.SplashActivity;
import com.niro.niroapp.utils.LocaleUtils;
import com.niro.niroapp.viewmodels.LocaleHelper;

import java.util.Locale;
import java.util.Objects;

import carbon.widget.Button;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChangeLanguage extends Fragment {
    private View rootView;
    private RadioGroup radioGroup;
    private RadioButton selectedLanguage;
    private Button save;
    Context context;
    Resources resources;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate( R.layout.layout_select_language, container, false);
        addListenerOnButton();
        return rootView;
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        int mPendingIntentId = 1;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }


    public void addListenerOnButton() {

        radioGroup = rootView.findViewById(R.id.radioGroup);
        save = rootView.findViewById(R.id.btnSave);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                int selectedId = radioGroup.getCheckedRadioButtonId();


                selectedLanguage =  rootView.findViewById(selectedId);
                Resources res = getResources();
                Configuration conf = res.getConfiguration();
                Toast.makeText(getActivity(), "Default Language Set to: "+selectedLanguage.getText(), Toast.LENGTH_SHORT).show();
                if (selectedLanguage.getText().equals("Hindi"))
                {

                    LocaleUtils.setLocale(getActivity(),new Locale("hi"));
                    Intent refresh = new Intent(getActivity(), getActivity()
                            .getClass());
                    startActivity(refresh);


                }
                else {
                    LocaleUtils.setLocale(getActivity(),new Locale("en"));
                    Intent refresh = new Intent(getActivity(), getActivity()
                            .getClass());
                    startActivity(refresh);
                }

              //  getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());


            }

        });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }
}
