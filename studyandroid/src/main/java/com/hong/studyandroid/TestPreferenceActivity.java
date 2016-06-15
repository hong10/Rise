package com.hong.studyandroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Administrator on 2016/6/15.
 */
public class TestPreferenceActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
