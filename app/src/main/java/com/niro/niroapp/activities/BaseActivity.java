package com.niro.niroapp.activities;


import androidx.appcompat.app.AppCompatActivity;
import com.niro.niroapp.utils.LocaleUtils;


public class BaseActivity extends AppCompatActivity {
    public BaseActivity() {
        LocaleUtils.updateConfig(this);
    }
}

