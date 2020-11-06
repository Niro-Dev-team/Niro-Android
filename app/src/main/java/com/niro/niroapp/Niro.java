package com.niro.niroapp;

import android.app.Application;
import android.content.res.Configuration;

import com.niro.niroapp.utils.LocaleUtils;

import java.util.Locale;

public class Niro extends Application {
    private Locale locale = null;
    public void onCreate(){
        super.onCreate();

        String lang= LocaleUtils.getLanguage(getApplicationContext());
        LocaleUtils.setLocale(getApplicationContext(),new Locale(lang));
        LocaleUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (locale != null)
        {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
        LocaleUtils.updateConfig(this, newConfig);
    }
}