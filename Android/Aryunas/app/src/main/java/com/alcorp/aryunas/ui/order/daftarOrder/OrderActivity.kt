package com.alcorp.aryunas.ui.order.daftarOrder

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.aryunas.R
import com.alcorp.aryunas.databinding.ActivityOrderBinding
import com.alcorp.aryunas.ui.order.OrderViewModel
import com.alcorp.aryunas.ui.order.tambahOrder.TambahOrderActivity
import com.alcorp.aryunas.util.convertDate
import com.alcorp.aryunas.util.setDatePickerNormal
import com.alcorp.aryunas.viewmodel.ViewModelFactory
import java.util.*

class OrderActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var viewModel: OrderViewModel

    private var hour: Int = 0
    private var minute: Int = 0

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        binding.toolbar.tvToolbar.text = "Daftar Booking"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        itemTouchHelper.attachToRecyclerView(binding.recOrder)

        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        hour = calendar[Calendar.HOUR_OF_DAY]
        minute = calendar[Calendar.MINUTE]

        orderAdapter = OrderAdapter()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[OrderViewModel::class.java]

        setDefault()

        binding.edtCari.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (binding.spTampil.selectedItem == resources.getStringArray(R.array.listTgl)[0]) {
                    viewModel.searchOrder(s.toString()).observe(this@OrderActivity) { results ->
                        if (results != null) {
                            orderAdapter.submitList(results)
                        }

                        with(binding.recOrder) {
                            layoutManager = LinearLayoutManager(this@OrderActivity)
                            setHasFixedSize(true)
                            adapter = orderAdapter
                        }
                    }
                } else {
                    filter(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.spTampil.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    binding.cTanggal.visibility = View.GONE
                    val param = binding.spTampil.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(16, 8, 16, 24)
                    binding.spTampil.layoutParams = param
                    setDefault()
                } else {
                    binding.cTanggal.visibility = View.VISIBLE
                    binding.btnDari.text = setDatePickerNormal(year, month + 1, day)
                    binding.btnKe.text = setDatePickerNormal(year, month + 1, day)
                    filter(binding.edtCari.text.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.btnAdd.setOnClickListener(this)
        binding.btnDari.setOnClickListener(this)
        binding.btnKe.setOnClickListener(this)
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)


        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val swipedPosition = viewHolder.adapterPosition
            val orderEntity = orderAdapter.getSwipedData(swipedPosition)

            val builder = AlertDialog.Builder(this@OrderActivity)
            builder.setMessage("Anda yakin ingin menghapus data ini?")
            builder.setPositiveButton("Ya") { _, _ ->
                viewModel.deleteOrder(orderEntity.id_order)
                viewModel.deleteOrderDetailByIdOrder(orderEntity.id_order)
                Toast.makeText(this@OrderActivity, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
                setDefault()
            }
            builder.setNegativeButton("Tidak") { _, _ ->
                setDefault()

            }
            builder.create().show()
        }
    })

    private fun setDate(i: Int) = showDialog(i)

    override fun onCreateDialog(id: Int): Dialog {
        return if (id == 1) {
            DatePickerDialog(this, tglDari, year, month, day)
        } else {
            DatePickerDialog(this, tglKe, year, month, day)
        }
    }

    private fun filter(query: String) {
        viewModel.searchTglOrder(
            query,
            convertDate(binding.btnDari.text.toString()),
            convertDate(binding.btnKe.text.toString())
        ).observe(this@OrderActivity) { results ->
            if (results != null) {
                orderAdapter.submitList(results)
            }

            with(binding.recOrder) {
                layoutManager = LinearLayoutManager(this@OrderActivity)
                setHasFixedSize(true)
                adapter = orderAdapter
            }
        }
    }

    private fun setDefault() {
        viewModel.order?.observe(this) { results ->
            if (results != null) {
                orderAdapter.submitList(results)
            }

            with(binding.recOrder) {
                layoutManager = LinearLayoutManager(this@OrderActivity)
                setHasFixedSize(true)
                adapter = orderAdapter
            }
        }
    }

    private val tglDari =
        DatePickerDialog.OnDateSetListener { tgl, thn, bln, hari ->
            binding.btnDari.text = setDatePickerNormal(thn, bln + 1, hari)
            filter(binding.edtCari.text.toString())
        }

    private val tglKe =
        DatePickerDialog.OnDateSetListener { tgl, thn, bln, hari ->
            binding.btnKe.text = setDatePickerNormal(thn, bln + 1, hari)
            filter(binding.edtCari.text.toString())
        }

    override fun onClick(view: View) {
        when (view) {
            binding.btnAdd -> {
                val intent = Intent(this, TambahOrderActivity::class.java)
                startActivity(intent)
            }
            binding.btnDari -> {
                setDate(1)
            }
            binding.btnKe -> {
                setDate(2)
            }
        }
    }
}