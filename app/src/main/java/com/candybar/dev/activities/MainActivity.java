package com.candybar.dev.activities;

import androidx.annotation.NonNull;

import com.candybar.dev.licenses.License;

import candybar.lib.activities.CandyBarMainActivity;

public class MainActivity extends CandyBarMainActivity {

    @NonNull
    @Override
    public ActivityConfiguration onInit() {
        return new ActivityConfiguration();
    }
}
