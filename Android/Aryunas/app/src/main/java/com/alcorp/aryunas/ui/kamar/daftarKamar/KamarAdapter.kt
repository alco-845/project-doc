package com.alcorp.aryunas.ui.kamar.daftarKamar

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
import com.alcorp.aryunas.data.local.entity.KamarEntity
import com.alcorp.aryunas.ui.kamar.editTambahKamar.EditTambahKamarActivity
import com.alcorp.aryunas.util.withNumberingFormat

class KamarAdapter internal constructor() : PagedListAdapter<KamarEntity, KamarAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kamar, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kamarEntity = getItem(position) as KamarEntity
        holder.tvNama.text = kamarEntity.nama_kamar
        holder.tvHarga.text = "Rp. ${kamarEntity.harga_kamar.toString().withNumberingFormat()}"

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditTambahKamarActivity::class.java)
            intent.putExtra(EditTambahKamarActivity.EXTRA_ID, kamarEntity.id_kamar)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun getSwipedData(swipedPosition: Int) : KamarEntity = getItem(swipedPosition) as KamarEntity

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNama: TextView = itemView.findViewById(R.id.tvNama)
        var tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<KamarEntity>() {
            override fun areItemsTheSame(oldItem: KamarEntity, newItem: KamarEntity): Boolean {
                return oldItem.id_kamar == newItem.id_kamar
            }

            override fun areContentsTheSame(oldItem: KamarEntity, newItem: KamarEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}