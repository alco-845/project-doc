package com.alcorp.aryunas.ui.kamar.daftarKamar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.aryunas.databinding.ActivityDaftarKamarBinding
import com.alcorp.aryunas.ui.kamar.KamarViewModel
import com.alcorp.aryunas.ui.kamar.editTambahKamar.EditTambahKamarActivity
import com.alcorp.aryunas.ui.order.tambahOrder.BookingAdapter
import com.alcorp.aryunas.viewmodel.ViewModelFactory

class DaftarKamarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarKamarBinding
    private lateinit var kamarAdapter: KamarAdapter
    private lateinit var viewModel:KamarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarKamarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.tvToolbar.text = "Daftar Kamar"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[KamarViewModel::class.java]

        if (intent.extras == null) {
            kamarAdapter = KamarAdapter()
            setDefault()
            itemTouchHelper.attachToRecyclerView(binding.rvKamar)

            binding.btnAdd.setOnClickListener {
                val intent = Intent(this, EditTambahKamarActivity::class.java)
                startActivity(intent)
            }
        } else {
            binding.btnAdd.visibility = View.GONE
            val data = viewModel.getKamarBooking()
            with(binding.rvKamar) {
                layoutManager = LinearLayoutManager(this@DaftarKamarActivity)
                setHasFixedSize(true)
                adapter = BookingAdapter(data)
            }
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)


        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val swipedPosition = viewHolder.adapterPosition
            val orderEntity = kamarAdapter.getSwipedData(swipedPosition)

            val jumlah = viewModel.countKamar(orderEntity.id_kamar)

//            if (jumlah > 0) {
//                val builder = AlertDialog.Builder(this@DaftarKamarActivity)
//                builder.setTitle("Anda yakin ingin menghapus data ini?")
//                builder.setMessage("Data ini digunakan di menu transaksi, maka data yang ada di menu transaksi juga akan ikut terhapus")
//                builder.setPositiveButton("Ya") { _, _ ->
//                    viewModel.deleteKamar(orderEntity.id_kamar)
//                    Toast.makeText(this@DaftarKamarActivity, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
//                    setDefault()
//                }
//                builder.setNegativeButton("Tidak") { _, _ ->
//                    setDefault()
//                }
//                builder.create().show()
//            } else {
                val builder = AlertDialog.Builder(this@DaftarKamarActivity)
                builder.setMessage("Anda yakin ingin menghapus data ini?")
                builder.setPositiveButton("Ya") { _, _ ->
                    viewModel.deleteKamar(orderEntity.id_kamar)
                    Toast.makeText(this@DaftarKamarActivity, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
                    setDefault()
                }
                builder.setNegativeButton("Tidak") { _, _ ->
                    setDefault()
                }
                builder.create().show()
//            }
        }
    })

    private fun setDefault() {
        viewModel.kamarEntity?.observe(this) { results ->
            if (results != null) {
                kamarAdapter.submitList(results)
            }

            with(binding.rvKamar) {
                layoutManager = LinearLayoutManager(this@DaftarKamarActivity)
                setHasFixedSize(true)
                adapter = kamarAdapter
            }
        }
    }
}