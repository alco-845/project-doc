package com.alcorp.aryunas.ui.order.detailOrder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.aryunas.R
import com.alcorp.aryunas.data.local.entity.OrderDetail
import com.alcorp.aryunas.util.dateToNormal
import com.alcorp.aryunas.util.timeToNormal
import com.alcorp.aryunas.util.withNumberingFormat

class DetailAdapter(val listData: List<OrderDetail>) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_detail, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderDetail = listData[position]
        holder.tvNama.text = orderDetail.nama_kamar
        holder.tvTglIn.text = "Check In : ${dateToNormal(orderDetail.tglin)} | ${timeToNormal(orderDetail.jamin)}"
        holder.tvTglOut.text = "Check Out : ${dateToNormal(orderDetail.tglout)} | ${timeToNormal(orderDetail.jamout)}"
        holder.tvJumlahHari.text = "Jumlah Hari : ${orderDetail.total_hari}"
        holder.tvHargaPerHari.text = "Harga Per Hari : Rp. ${orderDetail.harga_kamar.toString().withNumberingFormat()}"
        holder.tvTotal.text = "Total Harga : Rp. ${orderDetail.total_harga.toString().withNumberingFormat()}"
        holder.ivDeleteSweep.visibility = View.GONE
    }

    override fun getItemCount(): Int = listData.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNama: TextView = itemView.findViewById(R.id.tvNama)
        var tvTglIn: TextView = itemView.findViewById(R.id.tvTglIn)
        var tvTglOut: TextView = itemView.findViewById(R.id.tvTglOut)
        var tvJumlahHari: TextView = itemView.findViewById(R.id.tvHari)
        var tvHargaPerHari: TextView = itemView.findViewById(R.id.tvHargaPerHari)
        var tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        var ivDeleteSweep: ImageView = itemView.findViewById(R.id.ivDeleteSweep)
    }
}