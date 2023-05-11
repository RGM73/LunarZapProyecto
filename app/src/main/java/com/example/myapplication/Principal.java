package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Principal extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_empty);
        getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_view);
        Home fragment = new Home();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                Home fragment = new Home();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                return true;
                            case R.id.action_search:
                                Search fragment2 = new Search();
                                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                                transaction2.replace(R.id.fragment_container, fragment2);
                                transaction2.addToBackStack(null);
                                transaction2.commit();
                                return true;
                            case R.id.action_profile:
                                Profile fragment3 = new Profile();
                                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                                transaction3.replace(R.id.fragment_container, fragment3);
                                transaction3.addToBackStack(null);
                                transaction3.commit();
                                return true;

                        }
                        return false;
                    }
                });


    }
}