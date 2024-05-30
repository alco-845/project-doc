package com.alcorp.aryunas.ui.order.tambahOrder

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.aryunas.R
import com.alcorp.aryunas.data.local.entity.KamarEntity
import com.alcorp.aryunas.ui.kamar.daftarKamar.DaftarKamarActivity
import com.alcorp.aryunas.util.withNumberingFormat

class BookingAdapter(val listData: List<KamarEntity>) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kamar, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kamarEntity = listData[position]
        holder.tvNama.text = kamarEntity.nama_kamar
        holder.tvHarga.text = "Rp. ${kamarEntity.harga_kamar.toString().withNumberingFormat()}"
        holder.ivDelete.visibility = View.GONE

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, TambahOrderActivity::class.java)
            intent.putExtra("id_kamar", kamarEntity.id_kamar)
            intent.putExtra("kamar", kamarEntity.nama_kamar)
            intent.putExtra("harga", kamarEntity.harga_kamar)
            (holder.itemView.context as DaftarKamarActivity).setResult(1, intent)
            (holder.itemView.context as DaftarKamarActivity).finish()
        }
    }

    override fun getItemCount(): Int = listData.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNama: TextView = itemView.findViewById(R.id.tvNama)
        var tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
        var ivDelete: ImageView = itemView.findViewById(R.id.ivDeleteSweep)
    }
}