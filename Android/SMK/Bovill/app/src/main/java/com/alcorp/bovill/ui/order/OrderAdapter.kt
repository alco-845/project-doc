package com.alcorp.bovill.ui.order

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.bovill.R
import com.alcorp.bovill.data.source.local.entity.OrderEntity
import com.alcorp.bovill.ui.addEditBooking.AddEditBookingActivity
import com.alcorp.bovill.utils.Helper
import kotlinx.android.synthetic.main.order_item.view.*

class OrderAdapter internal constructor() : PagedListAdapter<OrderEntity, OrderAdapter.MainViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderEntity>() {
            override fun areItemsTheSame(oldItem: OrderEntity, newItem: OrderEntity): Boolean {
                return oldItem.id_order == newItem.id_order
            }

            override fun areContentsTheSame(oldItem: OrderEntity, newItem: OrderEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val booking = getItem(position) as OrderEntity
        holder.bind(booking)
    }

    fun getSwipedData(swipedPosition: Int) : OrderEntity = getItem(swipedPosition) as OrderEntity

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(booking: OrderEntity) {
            with(itemView) {
                tvNama.text = booking.nama
                tvTglOrder.text = resources.getString(R.string.txt_order) + "    : "+ Helper.dateToNormal(booking.tglorder)
                tvHarga.text = resources.getString(R.string.txt_harga) + " : " + Helper.removeE(booking.subtotal)

                setOnClickListener {
                    val intent = Intent(context, AddEditBookingActivity::class.java)
                    intent.putExtra(AddEditBookingActivity.EXTRA_ID, booking.id_order)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}