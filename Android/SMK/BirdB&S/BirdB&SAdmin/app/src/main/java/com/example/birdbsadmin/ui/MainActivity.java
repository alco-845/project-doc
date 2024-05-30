package com.example.birdbsadmin.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.birdbsadmin.R;
import com.example.birdbsadmin.ui.item.ItemListActivity;
import com.example.birdbsadmin.ui.transaction.TransactionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences sh = getSharedPreferences("AdminApp", MODE_PRIVATE);
//        int id = sh.getInt("id", 0);
//        if (id == 0) {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
    }

    public void itemList(View view) {
        Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
        startActivity(intent);
    }

    public void transaction(View view) {
        Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
        startActivity(intent);
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

    public void logout(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
                .setMessage("Logout?")
                .setPositiveButton("Yes", (dialog12, which) -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("AdminApp", MODE_PRIVATE);
                    SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                    prefEdit.clear();
                    prefEdit.apply();
                    finish();
                })
                .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
        dialog.create();
        dialog.show();
    }
}