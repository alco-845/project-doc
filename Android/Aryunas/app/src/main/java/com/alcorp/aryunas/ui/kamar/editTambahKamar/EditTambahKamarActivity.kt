package com.alcorp.aryunas.ui.kamar.editTambahKamar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alcorp.aryunas.data.local.entity.KamarEntity
import com.alcorp.aryunas.databinding.ActivityEditTambahKamarBinding
import com.alcorp.aryunas.ui.kamar.KamarViewModel
import com.alcorp.aryunas.viewmodel.ViewModelFactory

class EditTambahKamarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTambahKamarBinding
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTambahKamarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[KamarViewModel::class.java]

        binding.toolbar.tvToolbar.text = "Tambah Kamar"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        val extras = intent.extras
        if (extras != null) {
            binding.toolbar.tvToolbar.text = "Edit Kamar"

            id = extras.getInt(EXTRA_ID)

            viewModel.setSelected(id)
            viewModel.kamarEntityById.observe(this) { results ->
                binding.edtNamaKamar.setText(results.nama_kamar)
                binding.edtHargaKamar.setText(results.harga_kamar.toString())
            }
        }

        binding.btnSimpan.setOnClickListener {
            val nama = binding.edtNamaKamar.text.toString().trim()
            val harga = binding.edtHargaKamar.text.toString().trim()

            if (nama.isEmpty() || harga.isEmpty()) {
                Toast.makeText(this, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                val kamarEntity = KamarEntity(
                    id,
                    nama,
                    harga.toInt()
                )

                if (extras != null) {
                    viewModel.updateKamar(kamarEntity)
                } else {
                    viewModel.insertKamar(kamarEntity)
                }

                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}