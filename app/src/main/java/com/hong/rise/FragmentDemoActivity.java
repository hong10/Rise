package com.hong.rise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hong.rise.fragment.ContactFragment;
import com.hong.rise.fragment.ContentFragment;
import com.hong.rise.fragment.FindFragment;
import com.hong.rise.fragment.FriendFragment;
import com.hong.rise.fragment.SettingsFragment;

public class FragmentDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvFriend, tvContact, tvFind, tvSettings;
    private Fragment frFriend;
    private Fragment frContact;
    private Fragment frFind;
    private Fragment frSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_demo);

        tvFriend = (TextView) findViewById(R.id.tv_friend);
        tvContact = (TextView) findViewById(R.id.tv_contact);
        tvFind = (TextView) findViewById(R.id.tv_find);
        tvSettings = (TextView) findViewById(R.id.tv_settings);


        tvFriend.setOnClickListener(this);
        tvContact.setOnClickListener(this);
        tvFind.setOnClickListener(this);
        tvSettings.setOnClickListener(this);

        //set default fragment
        setDefaultFragment();

    }

    private void setDefaultFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment frContent = new ContentFragment();
        transaction.replace(R.id.frame_content, frContent);

        transaction.commit();

    }


    @Override
    public void onClick(View v) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (v.getId()) {
            case R.id.tv_friend:
                if (frFriend == null) {
                    frFriend = new FriendFragment();
                }
                transaction.replace(R.id.frame_content, frFriend);
                break;

            case R.id.tv_contact:
                if (frContact == null) {
                    frContact = new ContactFragment();
                }
                transaction.replace(R.id.frame_content, frContact);

                break;

            case R.id.tv_find:
                if (frFind == null) {
                    frFind = new FindFragment();

                }
                transaction.replace(R.id.frame_content, frFind);
                break;

            case R.id.tv_settings:
                if (frSettings == null) {
                    frSettings = new SettingsFragment();
                }

                transaction.replace(R.id.frame_content, frSettings);
                break;


        }


        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.demo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.demo_activity_menu:
                Toast.makeText(this, "activitySettings", Toast.LENGTH_LONG).show();
                return true;
            default:
                //如果希望Fragment自己处理MenuItem点击事件，一定不要忘了调用super.xxx
                return super.onOptionsItemSelected(item);
        }

    }
}
