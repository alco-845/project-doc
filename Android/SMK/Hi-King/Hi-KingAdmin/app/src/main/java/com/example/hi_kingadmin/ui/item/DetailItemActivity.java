package com.example.hi_kingadmin.ui.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hi_kingadmin.R;

public class DetailItemActivity extends AppCompatActivity {

    int kategori = 0, iditem = 0;
    String kategoriValue = "", name = "", price = "", value = "", spek = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        setToolbar();
        setTextData();

        Button btnRent = findViewById(R.id.btnRent);
        btnRent.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddItemActivity.class);
            intent.putExtra("iditem", iditem);
            intent.putExtra("kategori", String.valueOf(kategori));
            intent.putExtra("name", name);
            intent.putExtra("spek", spek);
            intent.putExtra("value", value);
            intent.putExtra("price", price);
            startActivity(intent);
        });
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText("Detail Item");

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());

        ImageView ivCart = findViewById(R.id.btnAdd);
        ivCart.setVisibility(View.GONE);
    }

    public void setTextData() {
        Bundle extra = getIntent().getExtras();

        kategori = Integer.parseInt(extra.getString("kategori"));
        if (kategori == 0) {
            kategoriValue = "Tas Ransel";
        } else if (kategori == 1) {
            kategoriValue = "Tenda";
        } else if (kategori == 2) {
            kategoriValue = "Kompor Portable";
        }

        iditem = extra.getInt("iditem");
        name = extra.getString("name");
        spek = extra.getString("spek");
        price = extra.getString("price");
        value = extra.getString("value");

        TextView tvNameDetail = (TextView) findViewById(R.id.tvItemName);
        tvNameDetail.setText(name);

        TextView tvDescDetail = (TextView) findViewById(R.id.tvTgl);
        tvDescDetail.setText("Spesifikasi : \n\n" + spek + "\n\nKategori : " + kategoriValue + "\n\nHarga Sewa : Rp. " + price + "/ hari" + "\n\nStok : " + value);
    }
}