package com.example.matematika_cer.siswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R

data class RiwayatKuisModel(
    val namaTopik: String,
    val tanggal: String,
    val nilai: Int,
    val jumlahBenar: Int,
    val jumlahSoal: Int
)

class RiwayatKuisAdapter(
    private var list: List<RiwayatKuisModel>
) : RecyclerView.Adapter<RiwayatKuisAdapter.RiwayatViewHolder>() {

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomor = itemView.findViewById<TextView>(R.id.text_nomor_riwayat)
        val judul = itemView.findViewById<TextView>(R.id.text_judul_riwayat)
        val tanggal = itemView.findViewById<TextView>(R.id.text_tanggal_riwayat)
        val nilai = itemView.findViewById<TextView>(R.id.text_nilai_riwayat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat_kuis, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val item = list[position]
        holder.nomor.text = "${position + 1}."
        holder.judul.text = item.namaTopik
        holder.tanggal.text = item.tanggal
        holder.nilai.text = "Nilai: ${item.nilai}\\100"
    }

    override fun getItemCount(): Int = list.size

    fun filterList(filtered: List<RiwayatKuisModel>) {
        list = filtered
        notifyDataSetChanged()
    }
}
