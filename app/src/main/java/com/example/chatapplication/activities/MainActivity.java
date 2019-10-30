package com.example.chatapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chatapplication.R;
import com.example.chatapplication.adapters.TabAdapter;
import com.example.chatapplication.fragments.ListFriendFragment;
import com.example.chatapplication.fragments.ListChatFragment;
import com.example.chatapplication.fragments.ProfileFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private ViewPager pager;
    private TabLayout tabLayout;
    private TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapped();
    }

    private void mapped() {
        pager = findViewById(R.id.vp_main);
        tabLayout = findViewById(R.id.tl_main);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new ListFriendFragment(), "list friend");
        adapter.addFragment(new ListChatFragment(), "list chat");
        adapter.addFragment(new ProfileFragment(), "Profile");

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
