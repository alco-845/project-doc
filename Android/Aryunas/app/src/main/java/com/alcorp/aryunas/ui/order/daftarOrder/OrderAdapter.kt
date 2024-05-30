package com.alcorp.aryunas.ui.order.daftarOrder

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.aryunas.R
import com.alcorp.aryunas.data.local.entity.Order
import com.alcorp.aryunas.ui.kamar.daftarKamar.DaftarKamarActivity
import com.alcorp.aryunas.ui.order.detailOrder.DetailOrderActivity
import com.alcorp.aryunas.ui.order.detailOrder.DetailOrderActivity.Companion.EXTRA_ID_ORDER
import com.alcorp.aryunas.util.dateToNormal
import com.alcorp.aryunas.util.withNumberingFormat

class OrderAdapter internal constructor() : PagedListAdapter<Order, OrderAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderEntity = getItem(position) as Order
        holder.tvNama.text = orderEntity.nama
        holder.tvTglOrder.text = "Tanggal booking : ${dateToNormal(orderEntity.tgl_order.toString())}"
        holder.tvHarga.text = "Total : Rp. ${orderEntity.total.toString().withNumberingFormat()}"
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailOrderActivity::class.java)
            intent.putExtra(EXTRA_ID_ORDER, orderEntity.id_order)
            holder.itemView.context.startActivity(intent)
            (holder.itemView.context as OrderActivity).finish()
        }
    }

    fun getSwipedData(swipedPosition: Int) : Order = getItem(swipedPosition) as Order

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNama: TextView = itemView.findViewById(R.id.tvNama)
        var tvTglOrder: TextView = itemView.findViewById(R.id.tvTglOrder)
        var tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.id_order == newItem.id_order
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem == newItem
            }
        }
    }
}