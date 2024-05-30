package com.alcorp.aryunas.ui.order.tambahOrder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.aryunas.R
import com.alcorp.aryunas.data.local.entity.OrderDetail
import com.alcorp.aryunas.util.dateToNormal
import com.alcorp.aryunas.util.timeToNormal
import com.alcorp.aryunas.util.withNumberingFormat

class OrderDetailAdapter internal constructor() : PagedListAdapter<OrderDetail, OrderDetailAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_detail, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderDetail = getItem(position) as OrderDetail
        holder.tvNama.text = orderDetail.nama_kamar
        holder.tvTglIn.text = "Check In : ${dateToNormal(orderDetail.tglin)} | ${timeToNormal(orderDetail.jamin)}"
        holder.tvTglOut.text = "Check Out : ${dateToNormal(orderDetail.tglout)} | ${timeToNormal(orderDetail.jamout)}"
        holder.tvJumlahHari.text = "Jumlah Hari : ${orderDetail.total_hari}"
        holder.tvHargaPerHari.text = "Harga Per Hari : Rp. ${orderDetail.harga_kamar.toString().withNumberingFormat()}"
        holder.tvTotal.text = "Total Harga : Rp. ${orderDetail.total_harga.toString().withNumberingFormat()}"
    }

    fun getSwipedData(swipedPosition: Int) : OrderDetail = getItem(swipedPosition) as OrderDetail

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNama: TextView = itemView.findViewById(R.id.tvNama)
        var tvTglIn: TextView = itemView.findViewById(R.id.tvTglIn)
        var tvTglOut: TextView = itemView.findViewById(R.id.tvTglOut)
        var tvJumlahHari: TextView = itemView.findViewById(R.id.tvHari)
        var tvHargaPerHari: TextView = itemView.findViewById(R.id.tvHargaPerHari)
        var tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderDetail>() {
            override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean {
                return oldItem.id_order_detail == newItem.id_order_detail
            }

            override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean {
                return oldItem == newItem
            }
        }
    }
}