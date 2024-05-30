package com.cristiawan.goparty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cristiawan.goparty.ui.account.AccountFragment;
import com.cristiawan.goparty.ui.history.HistoryFragment;
import com.cristiawan.goparty.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

public class Beranda extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        loadFragment(new HomeFragment());

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.fr_home:
                fragment = new HomeFragment();
                break;

            case R.id.fr_history:
                fragment = new HistoryFragment();
                break;

            case R.id.fr_account:
                fragment = new AccountFragment();
                break;

        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}