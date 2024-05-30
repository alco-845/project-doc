package com.example.hi_kinguser.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.hi_kinguser.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SharedPreferences sh = getSharedPreferences("UserApp", MODE_PRIVATE);
        int id = sh.getInt("id", 0);
        if (id == 0) {
            Intent intent = new Intent(MainActivity.this, CoverActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
                .setMessage("Exit App?")
                .setPositiveButton("Yes", (dialog12, which) -> {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);

                })
                .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
        dialog.create();
        dialog.show();
    }

    public void reload(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}