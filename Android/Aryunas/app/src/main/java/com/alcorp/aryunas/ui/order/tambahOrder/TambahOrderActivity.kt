package com.alcorp.aryunas.ui.order.tambahOrder

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.aryunas.data.local.entity.OrderDetail
import com.alcorp.aryunas.databinding.ActivityTambahOrderBinding
import com.alcorp.aryunas.ui.MainActivity
import com.alcorp.aryunas.ui.kamar.daftarKamar.DaftarKamarActivity
import com.alcorp.aryunas.ui.order.OrderViewModel
import com.alcorp.aryunas.ui.order.tambahOrder.transaksi.TransaksiActivity
import com.alcorp.aryunas.util.*
import com.alcorp.aryunas.viewmodel.ViewModelFactory
import java.util.*

class TambahOrderActivity : AppCompatActivity(), View.OnClickListener {

    private var hour: Int = 0
    private var minute: Int = 0

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    private var id_order: Int = 0
    private var id_kamar: Int = 0

    private var hargaKamar: Int = 0

    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private lateinit var viewModel: OrderViewModel
    private lateinit var orderDetailAdapter: OrderDetailAdapter
    private lateinit var binding: ActivityTambahOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        binding.toolbar.tvToolbar.text = "Tambah Booking"
        binding.toolbar.btnBack.setOnClickListener {
            onBackPressed()
        }
        
        init()

        binding.edtNamaOrder.setText(pref.getString("atasNama", ""))
        binding.edtTglIn.setText(setDatePickerNormal(year, month + 1, day))
        binding.edtTglOut.setText(setDatePickerNormal(year, month + 1, day))
        binding.tvSubTotal.text = pref.getInt("totalHarga", 0).toString().withNumberingFormat()

        binding.edtNama.setOnClickListener(this)
        binding.edtTglIn.setOnClickListener(this)
        binding.edtJamIn.setOnClickListener(this)
        binding.edtTglOut.setOnClickListener(this)
        binding.edtJamOut.setOnClickListener(this)
        binding.btnTambah.setOnClickListener(this)
        binding.btnCheckout.setOnClickListener(this)
    }
    
    private fun init() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[OrderViewModel::class.java]

        pref = getSharedPreferences("aryunas", MODE_PRIVATE)
        prefEdit = pref.edit()
        id_order = pref.getInt("id_order", 0)

        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        hour = calendar[Calendar.HOUR_OF_DAY]
        minute = calendar[Calendar.MINUTE]

        orderDetailAdapter = OrderDetailAdapter()
        setDefault()
        itemTouchHelper.attachToRecyclerView(binding.rvDetailOrder)
    }

    private fun setDate(i: Int) = showDialog(i)

    private fun setTime(i: Int) = showDialog(i)

    override fun onCreateDialog(id: Int): Dialog {
        return when (id) {
            1 -> {
                DatePickerDialog(this, tglIn, year, month, day)
            }
            3 -> {
                DatePickerDialog(this, tglOut, year, month, day)
            }
            2 -> {
                TimePickerDialog(this, jamIn, hour, minute, DateFormat.is24HourFormat(this))
            }
            else -> {
                TimePickerDialog(this, jamOut, hour, minute, DateFormat.is24HourFormat(this))
            }
        }
    }

    private val tglIn =
        DatePickerDialog.OnDateSetListener { tgl, thn, bln, hari ->
            binding.edtTglIn.setText(setDatePickerNormal(thn, bln + 1, hari))
        }

    private val tglOut =
        DatePickerDialog.OnDateSetListener { tgl, thn, bln, hari ->
            binding.edtTglOut.setText(setDatePickerNormal(thn, bln + 1, hari))
        }

    private val jamIn =
        TimePickerDialog.OnTimeSetListener { jam, hour, minute ->
            binding.edtJamIn.setText(setTimePickerNormal(hour, minute))
        }

    private val jamOut =
        TimePickerDialog.OnTimeSetListener { jam, hour, minute ->
            binding.edtJamOut.setText(setTimePickerNormal(hour, minute))
        }

    override fun onClick(view: View) {
        when(view) {
            binding.edtNama -> {
                val intent = Intent(this, DaftarKamarActivity::class.java)
                intent.putExtra("cari", "kamar")
                startActivityForResult(intent, 1)
            }
            binding.edtTglIn -> {
                setDate(1)
            }
            binding.edtJamIn -> {
                setTime(2)
            }
            binding.edtTglOut -> {
                setDate(3)
            }
            binding.edtJamOut -> {
                setTime(4)
            }
            binding.btnTambah -> {
                val atasNama = binding.edtNamaOrder.text.toString().trim()
                val namaKamar = binding.edtNama.text.toString().trim()
                val tglIn = binding.edtTglIn.text.toString().trim()
                val jamIn = binding.edtJamIn.text.toString().trim()
                val tglOut = binding.edtTglOut.text.toString().trim()
                val jamOut = binding.edtJamOut.text.toString().trim()
                val hargaPerKamar = binding.edtHarga.text.toString().trim()

                if (namaKamar == "" || tglIn == "" || jamIn == "" || tglOut == "" || jamOut == "" || hargaPerKamar == "") {
                    Toast.makeText(this, "Kolom kamar tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Berhasil menambahkan kamar", Toast.LENGTH_SHORT).show()

                    val totalHari = convertDate(tglOut).toInt() - convertDate(tglIn).toInt()
                    val totalHarga = hargaPerKamar.toInt() * totalHari

                    id_order = viewModel.countOrder() + 1

                    val hargaOld = pref.getInt("totalHarga", 0) + totalHarga

                    prefEdit.putString("atasNama", atasNama)
                    prefEdit.putInt("id_order", id_order)
                    prefEdit.putInt("totalHarga", hargaOld)
                    prefEdit.apply()

                    val orderDetail = OrderDetail(
                        0,
                        id_order,
                        id_kamar,
                        namaKamar,
                        convertDate(tglIn),
                        convertTime(jamIn),
                        convertDate(tglOut),
                        convertTime(jamOut),
                        totalHari,
                        totalHarga,
                        hargaKamar
                    )
                    viewModel.insertOrderDetail(orderDetail)
                    binding.tvSubTotal.text = "${hargaOld.toString().withNumberingFormat()}"

                    reload()
                }
            }
            binding.btnCheckout -> {
                val atasNama = binding.edtNamaOrder.text.toString().trim()
                val subTotal = pref.getInt("totalHarga", 0)

                if (atasNama == "") {
                    Toast.makeText(this, "Kolom atas nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else if (subTotal == 0) {
                    Toast.makeText(this, "Mohon lakukan booking kamar terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    prefEdit = pref.edit()
                    prefEdit.putString("tgl_order", convertDate(setDatePickerNormal(year, month + 1, day).toString()))
                    prefEdit.apply()

                    val intent = Intent(this, TransaksiActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun reload(){
        finish()
        overridePendingTransition(0, 0)
        startActivity(getIntent())
        overridePendingTransition(0, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 1) {
            id_kamar = data!!.getIntExtra("id_kamar", 0)
            hargaKamar = data.getIntExtra("harga", 0)

            binding.edtNama.setText(data.getStringExtra("kamar"))
            binding.edtHarga.setText(hargaKamar.toString())
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)


        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val swipedPosition = viewHolder.adapterPosition
            val orderDetailEntity = orderDetailAdapter.getSwipedData(swipedPosition)

            val builder = AlertDialog.Builder(this@TambahOrderActivity)
            builder.setMessage("Anda yakin ingin menghapus data ini?")
            builder.setPositiveButton("Ya") { _, _ ->
                val hargaOld = pref.getInt("totalHarga", 0)
                prefEdit.putInt("totalHarga", hargaOld - orderDetailEntity.total_harga)
                viewModel.deleteOrderDetail(orderDetailEntity.id_order_detail)
                Toast.makeText(this@TambahOrderActivity, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
                setDefault()
            }
            builder.setNegativeButton("Tidak") { _, _ ->
                setDefault()

            }
            builder.create().show()
        }
    })

    private fun setDefault() {
        viewModel.setSelectedOrder(id_order)
        viewModel.getOrderDetailByIdOrder.observe(this) { results ->
            if (results != null) {
                orderDetailAdapter.submitList(results)
            }

            with(binding.rvDetailOrder) {
                layoutManager = LinearLayoutManager(this@TambahOrderActivity)
                setHasFixedSize(true)
                adapter = orderDetailAdapter
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Anda yakin ingin keluar?")
        builder.setMessage("Jika anda keluar, data anda akan terhapus")
        builder.setPositiveButton("Ya") { _, _ ->
            viewModel.deleteOrderDetailByIdOrder(id_order)
            viewModel.deleteOrder(id_order)
            prefEdit.clear().apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("Tidak") { _, _ ->
        }
        builder.create().show()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}