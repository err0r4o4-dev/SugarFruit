package com.example.project;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration configuration = new Configuration(
                newBase.getResources().getConfiguration());
        configuration.fontScale = AppSettings.getFontScale(newBase);
        super.attachBaseContext(newBase.createConfigurationContext(configuration));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        int requestedMode = AppSettings.getNightMode(this);
        if (AppCompatDelegate.getDefaultNightMode() != requestedMode) {
            AppCompatDelegate.setDefaultNightMode(requestedMode);
        }
        super.onCreate(savedInstanceState);
    }
}
