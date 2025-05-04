package com.example.matematika_cer.guru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel

class TopikGridAdapter(
    private val list: List<TopikModel>,
    private val onItemClick: (TopikModel) -> Unit
) : RecyclerView.Adapter<TopikGridAdapter.TopikViewHolder>() {

    inner class TopikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.card_topik)
        val nama: TextView = itemView.findViewById(R.id.nama_topik)
        val durasi: TextView = itemView.findViewById(R.id.durasi)
        val totalSoal: TextView = itemView.findViewById(R.id.total_soal)
        val peserta: TextView = itemView.findViewById(R.id.peserta)
        val tanggal: TextView = itemView.findViewById(R.id.tanggal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopikViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topik_card, parent, false)
        return TopikViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopikViewHolder, position: Int) {
        val topik = list[position]
        holder.nama.text = topik.namaTopik
        holder.durasi.text = "Durasi: ${topik.durasi}"
        holder.totalSoal.text = "Jumlah soal: ${topik.jumlahSoal}"
        holder.peserta.text = "${topik.jumlahMenjawab ?: 0}/${topik.totalPeserta ?: 0}"
        holder.tanggal.text = "Tanggal: ${topik.tanggal}"

        holder.cardView.setOnClickListener {
            onItemClick(topik)
        }
    }

    override fun getItemCount(): Int = list.size
}
