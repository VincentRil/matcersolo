// TopikNilaiAdapter.kt
package com.example.matematika_cer.guru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel

class TopikNilaiAdapter(
    private val list: List<TopikModel>,
    private val onClick: (TopikModel) -> Unit
) : RecyclerView.Adapter<TopikNilaiAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaTopik: TextView = view.findViewById(R.id.namaTopikCard)
        val jumlahSoal: TextView = view.findViewById(R.id.detailJumlahSoal)
        val durasi: TextView = view.findViewById(R.id.detailDurasi)
        val tanggalJam: TextView = view.findViewById(R.id.detailTanggalJam)
        val menjawab: TextView = view.findViewById(R.id.detailJumlahMenjawab)

        init {
            view.setOnClickListener {
                val topik = list[adapterPosition]
                onClick(topik)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topik_card_nilaisiswa, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topik = list[position]

        holder.namaTopik.text = topik.namaTopik
        holder.jumlahSoal.text = "Jumlah Soal: ${topik.jumlahSoal}"
        holder.durasi.text = "Durasi: ${topik.durasi} menit"
        holder.tanggalJam.text = "Pelaksanaan: ${topik.tanggal} ${topik.jam ?: ""}"

        val total = topik.totalPeserta ?: 0
        val sudah = topik.jumlahMenjawab ?: 0
        holder.menjawab.text = "Sudah menjawab: $sudah dari $total siswa"
    }

    override fun getItemCount(): Int = list.size
}
