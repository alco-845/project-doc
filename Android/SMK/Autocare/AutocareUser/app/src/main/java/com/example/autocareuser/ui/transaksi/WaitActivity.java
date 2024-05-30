package com.example.autocareuser.ui.transaksi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.autocareuser.R;
import com.example.autocareuser.viewmodel.StatusViewModel;

public class WaitActivity extends AppCompatActivity {

    Handler mHandler;
    StatusViewModel statusViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        statusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        statusViewModel.setTrans();
        statusViewModel.getTrans().observe(WaitActivity.this, status -> {
            if (status.equals("selesai")) {
                Intent intent = new Intent(WaitActivity.this, ConfirmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable,2000);
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            statusViewModel.setTrans();
            statusViewModel.getTrans().observe(WaitActivity.this, status -> {
                if (status.equals("selesai")) {
                    Intent intent = new Intent(WaitActivity.this, ConfirmActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            WaitActivity.this.mHandler.postDelayed(m_Runnable, 2000);
        }

    };

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
                .setTitle("Kembali ke mainmenu?")
                .setMessage("Anda tidak dapat melihat apakah bengkel sudah menkonfirmasi atau belum")
                .setPositiveButton("Ya", (dialog12, which) -> {
                    Intent a = new Intent(this, MainActivity.class);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);

                })
                .setNegativeButton("Tidak", (dialog1, id) -> dialog1.cancel());
        dialog.create();
        dialog.show();
    }
}