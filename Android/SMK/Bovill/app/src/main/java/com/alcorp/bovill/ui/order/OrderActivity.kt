package com.alcorp.bovill.ui.order

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.bovill.R
import com.alcorp.bovill.ui.addEditBooking.AddEditBookingActivity
import com.alcorp.bovill.utils.Helper
import com.alcorp.bovill.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_order.*
import java.util.*

class OrderActivity : AppCompatActivity(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var viewModel: OrderViewModel

    private var hour: Int = 0
    private var minute: Int = 0

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        itemTouchHelper.attachToRecyclerView(recOrder)

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

        spTampil.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                if (position == 0) {
                    cTanggal.visibility = View.GONE
                    val param = spTampil.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(16, 8, 16, 24)
                    spTampil.layoutParams = param
                    setDefault()
                } else  {
                    cTanggal.visibility = View.VISIBLE
                    btnDari.text = Helper.setDatePickerNormal(year, month + 1, day)
                    btnKe.text = Helper.setDatePickerNormal(year, month + 1, day)
                    filter(searchView.query.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        btnAdd.setOnClickListener(this)
        btnDari.setOnClickListener(this)
        btnKe.setOnClickListener(this)

        refresh_main.setOnRefreshListener(this)
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
                makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)


        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val swipedPosition = viewHolder.adapterPosition
            val orderEntity = orderAdapter.getSwipedData(swipedPosition)

            val builder = AlertDialog.Builder(this@OrderActivity)
            builder.setMessage(resources.getString(R.string.txt_dialog_pesan))
            builder.setPositiveButton(resources.getString(R.string.txt_dialog_ya)) { _, _ ->
                viewModel.removeOrder(orderEntity.id_order)
                Toast.makeText(this@OrderActivity, resources.getString(R.string.txt_toast_hapus), Toast.LENGTH_SHORT).show()
                setDefault()
            }
            builder.setNegativeButton(resources.getString(R.string.txt_dialog_tidak)) { _, _ ->
                setDefault()
                
            }
            builder.create().show()
        }
    })

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.txt_cari)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (spTampil.selectedItem == resources.getString(R.string.txt_semua)){
                    viewModel.searchOrder(query).observe(this@OrderActivity, Observer { results ->
                        if (results != null) {
                            orderAdapter.submitList(results)
                        }

                        with(recOrder) {
                            layoutManager = LinearLayoutManager(this@OrderActivity)
                            setHasFixedSize(true)
                            adapter = orderAdapter
                        }
                    })
                } else {
                    filter(query)
                }
                return true
            }

        })

        return true
    }

    private fun setDate(i: Int) = showDialog(i)

    override fun onCreateDialog(id: Int): Dialog {
        return if (id == 1) {
            DatePickerDialog(this, tglDari, year, month, day)
        } else {
            DatePickerDialog(this, tglKe, year, month, day)
        }
    }

    private fun filter(query: String) {
        if (spTampil.selectedItem == resources.getString(R.string.txt_cek_in)) {
            viewModel.searchTglIn(query, Helper.convertDate(btnDari.text.toString()), Helper.convertDate(btnKe.text.toString())).observe(
                    this@OrderActivity,
                    Observer { results ->
                        if (results != null) {
                            orderAdapter.submitList(results)
                        }

                        with(recOrder) {
                            layoutManager = LinearLayoutManager(this@OrderActivity)
                            setHasFixedSize(true)
                            adapter = orderAdapter
                        }
                    })
        } else if (spTampil.selectedItem == resources.getString(R.string.txt_cek_out)) {
            viewModel.searchTglOut(query, Helper.convertDate(btnDari.text.toString()), Helper.convertDate(btnKe.text.toString())).observe(
                    this@OrderActivity,
                    Observer { results ->
                        if (results != null) {
                            orderAdapter.submitList(results)
                        }

                        with(recOrder) {
                            layoutManager = LinearLayoutManager(this@OrderActivity)
                            setHasFixedSize(true)
                            adapter = orderAdapter
                        }
                    })
        }
    }

    private fun setDefault() {
        viewModel.order?.observe(this, Observer { results ->
            if (results != null) {
                orderAdapter.submitList(results)
            }

            with(recOrder) {
                layoutManager = LinearLayoutManager(this@OrderActivity)
                setHasFixedSize(true)
                adapter = orderAdapter
            }
        })
    }

    private val tglDari =
            OnDateSetListener { tgl, thn, bln, hari ->
                btnDari.text = Helper.setDatePickerNormal(thn, bln + 1, hari)
                filter(searchView.query.toString())
            }

    private val tglKe =
            OnDateSetListener { tgl, thn, bln, hari ->
                btnKe.text = Helper.setDatePickerNormal(thn, bln + 1, hari)
                filter(searchView.query.toString())
            }

    override fun onClick(view: View) {
        if (view == btnAdd) {
            val intent = Intent(this, AddEditBookingActivity::class.java)
            startActivity(intent)
        } else if (view == btnDari) {
            setDate(1)
        } else if (view == btnKe) {
            setDate(2)
        }
    }

    override fun onRefresh() {
        setDefault()
        refresh_main.isRefreshing = false
    }
}