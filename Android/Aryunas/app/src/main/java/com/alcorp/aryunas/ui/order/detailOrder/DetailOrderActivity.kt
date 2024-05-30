package com.alcorp.aryunas.ui.order.detailOrder

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.aryunas.databinding.ActivityDetailOrderBinding
import com.alcorp.aryunas.ui.order.OrderViewModel
import com.alcorp.aryunas.ui.order.tambahOrder.OrderDetailAdapter
import com.alcorp.aryunas.util.convertDate
import com.alcorp.aryunas.util.dateToNormal
import com.alcorp.aryunas.util.withNumberingFormat
import com.alcorp.aryunas.viewmodel.ViewModelFactory
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.FailureResponse
import com.gkemon.XMLtoPDF.model.SuccessResponse


class DetailOrderActivity : AppCompatActivity() {

    private var id_order: Int = 0

    private var nama: String = ""

    private lateinit var viewModel: OrderViewModel
    private lateinit var orderDetailAdapter: OrderDetailAdapter
    private lateinit var binding: ActivityDetailOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.tvToolbar.text = "Detail Booking"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        val extras = intent.extras
        id_order = extras!!.getInt(EXTRA_ID_ORDER)

        setDefault()

        binding.btnAdd.setOnClickListener {
            val namaFile = id_order.toString() + "_" + binding.tvAtasNama.text + "_" + convertDate(binding.tvTglBooking.text.toString())

            PdfGenerator.getBuilder()
                .setContext(this)
                .fromViewSource()
                .fromViewList(listOf(binding.pdfLayout, binding.rvDetailOrder))
                .setFileName(namaFile)
//                .setFolderNameOrPath("Booking")
                .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.OPEN)
                .build(object : PdfGeneratorListener() {
                    override fun onFailure(failureResponse: FailureResponse) {
                        super.onFailure(failureResponse)
                    }

                    override fun showLog(log: String) {
                        super.showLog(log)
                    }

                    override fun onStartPDFGeneration() {
                    }

                    override fun onFinishPDFGeneration() {
                    }

                    override fun onSuccess(response: SuccessResponse) {
                        super.onSuccess(response)
                        val builder = AlertDialog.Builder(this@DetailOrderActivity)
                        builder.setMessage("File pdf disimpan di folder : internal storage/android/data/com.alcorp.aryunas/files/ dengan nama file : $namaFile")
                        builder.setPositiveButton("Ok") { _, _ ->
                        }
                        builder.create().show()
                    }
                })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDefault() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[OrderViewModel::class.java]
        viewModel.setSelectedOrder(id_order)

        viewModel.orderEntityById.observe(this) { result ->
            nama = result.nama.toString()
            binding.tvAtasNama.text = result.nama
            binding.tvTglBooking.text = dateToNormal(result.tgl_order.toString())
            binding.tvTransaksi.text = """
                Subtotal : Rp. ${result.subtotal.toString().withNumberingFormat()}
                Diskon : Rp. ${result.diskon.toString().withNumberingFormat()}
                Total : Rp. ${result.total.toString().withNumberingFormat()}
                Pembayaran : Rp. ${result.bayar.toString().withNumberingFormat()}
                Kembalian : Rp. ${result.kembali.toString().withNumberingFormat()}
            """.trimIndent()
        }

        val data = viewModel.getOrderDetailByIdOrderList(id_order)
        with(binding.rvDetailOrder) {
            layoutManager = LinearLayoutManager(this@DetailOrderActivity)
            setHasFixedSize(true)
            adapter = DetailAdapter(data)
        }
    }

    companion object {
        const val EXTRA_ID_ORDER = "extra_id_order"
    }
}