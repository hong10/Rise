package com.hong.rise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.hong.rise.fragment.FragmentOne;
import com.hong.rise.fragment.FragmentThree;
import com.hong.rise.fragment.FragmentTwo;

public class FragmentStackDemoActivity extends AppCompatActivity implements FragmentOne.FrOneBtListener, FragmentTwo.FrTwoBtListener {

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_stack_demo);

        fragmentOne = new FragmentOne();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_content, fragmentOne, "one");
        transaction.commit();

        fragmentOne.setFrOneBtListener(this);

    }

    @Override
    public void onFrOneBtClick() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragmentTwo = new FragmentTwo();
        transaction.replace(R.id.fl_content, fragmentTwo, "two");
        transaction.addToBackStack(null);
        transaction.commit();

        fragmentTwo.setFrTwoBtListener(this);
    }

    @Override
    public void onFrTwoBtClick() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragmentThree = new FragmentThree();
        transaction.replace(R.id.fl_content, fragmentThree, "three");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
