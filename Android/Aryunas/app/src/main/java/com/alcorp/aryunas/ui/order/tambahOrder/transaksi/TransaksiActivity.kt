package com.alcorp.aryunas.ui.order.tambahOrder.transaksi

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alcorp.aryunas.data.local.entity.Order
import com.alcorp.aryunas.databinding.ActivityTransaksiBinding
import com.alcorp.aryunas.ui.MainActivity
import com.alcorp.aryunas.ui.order.OrderViewModel
import com.alcorp.aryunas.util.withNumberingFormat
import com.alcorp.aryunas.viewmodel.ViewModelFactory

class TransaksiActivity : AppCompatActivity() {

    private var id_order: Int = 0
    private var subTotal: Int = 0
    private var total: Int = 0
    private var cashback: Int = 0

    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private lateinit var binding: ActivityTransaksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        binding.toolbar.tvToolbar.text = "Pembayaran"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        init()
    }

    fun calculate() {
        val diskon: Int = binding.edtDiskon.text.toString().toInt()
        val masuk: Int = binding.edtBayar.text.toString().toInt()
        val jum: Int = subTotal
        total = jum - diskon
        var kembali: Int
        if (jum > diskon) {
            kembali = jum - diskon
            binding.edtTotal.setText(kembali.toString().withNumberingFormat())
            if (masuk > total) {
                kembali = masuk - total
                cashback = kembali
                binding.edtKembali.setText(kembali.toString().withNumberingFormat())
            } else if (masuk == total) {
                kembali = 0
                cashback = kembali
                binding.edtKembali.setText(kembali.toString().withNumberingFormat())
            } else {
                kembali = total - masuk
                val min: String = if (kembali == 0) {
                    ""
                } else {
                    "-"
                }
                cashback = kembali
                binding.edtKembali.setText(min + kembali.toString().withNumberingFormat())
            }
        } else if (jum < diskon) {
            Toast.makeText(this@TransaksiActivity, "Potongan harga tidak boleh lebih dari subtotal", Toast.LENGTH_SHORT).show()
            binding.edtDiskon.setText("0")
        }
    }

    private fun init() {
        pref = getSharedPreferences("aryunas", MODE_PRIVATE)
        prefEdit = pref.edit()
        id_order = pref.getInt("id_order", 0)
        subTotal = pref.getInt("totalHarga", 0)

        binding.edtSubtotal.setText(subTotal.toString().withNumberingFormat())

        calculate()

        binding.edtDiskon.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    binding.edtDiskon.setText("0")
                } else {
                    calculate()
                }
            }
        })

        binding.edtBayar.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    binding.edtBayar.setText("0")
                } else {
                    calculate()
                }
            }
        })

        binding.btnCheckout.setOnClickListener {
            val factory = ViewModelFactory.getInstance(this)
            val viewModel = ViewModelProvider(this, factory)[OrderViewModel::class.java]

            val bayar = binding.edtBayar.text.toString()
            val diskon = binding.edtDiskon.text.toString()

            if (total > bayar.toInt()) {
                Toast.makeText(this, "Pembayaran kurang", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Konfirmasi pembayaran")
                builder.setMessage("Pembayaran tidak bisa diedit, pastikan pembayaran sudah benar")
                builder.setPositiveButton("Ya") { _, _ ->
                    val order = Order(
                        pref.getInt("id_order", 0),
                        pref.getString("atasNama", ""),
                        pref.getString("tgl_order", ""),
                        pref.getInt("totalHarga", 0),
                        diskon.toInt(),
                        total,
                        bayar.toInt(),
                        cashback
                    )
                    viewModel.insertOrder(order)
                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                    prefEdit.clear().apply()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                builder.setNegativeButton("Tidak") { _, _ ->
                }
                builder.create().show()
            }
        }
    }
}