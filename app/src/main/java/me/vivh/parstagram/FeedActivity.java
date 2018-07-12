package me.vivh.parstagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedActivity extends AppCompatActivity {

    @BindView(R.id.navigationView) BottomNavigationView bottomNavView;
    @BindView(R.id.toolbar) Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);

        //  set top toolbar displaying Instagram logo
        setSupportActionBar(toolbar);
        // if you want shadow
        getSupportActionBar().setElevation(
                getResources().getDimensionPixelSize(R.dimen.action_bar_elevation)
        );

        // set bottom navigation toolbar
        bottomNavView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.ibHome:
                                selectedFragment = new FeedFragment();
                                break;
                            case R.id.ibAdd:
                                selectedFragment = new AddPostFragment();
                                break;
                            case R.id.ibProfile:
                                selectedFragment = new ProfileFragment();
                                break;
                        }
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.fragmentPlace, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        //default fragment
        FeedFragment feedFragment = new FeedFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentPlace, feedFragment);
        ft.commit();
    }

}
