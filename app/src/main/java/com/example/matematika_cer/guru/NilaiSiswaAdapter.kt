package com.example.matematika_cer.guru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R

class NilaiSiswaAdapter(private var list: List<NilaiSiswaModel>)
    : RecyclerView.Adapter<NilaiSiswaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaSiswa)
        val tvNilai: TextView = itemView.findViewById(R.id.tvNilai)
        val tvWaktu: TextView = itemView.findViewById(R.id.tvWaktuSubmit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nilai_siswa, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNama.text = item.namaSiswa

        if (item.nilai != null && item.waktuSubmit != null) {
            holder.tvNilai.text = "${item.nilai}"
            holder.tvWaktu.text = "${item.waktuSubmit}"
        } else {
            holder.tvNilai.text = "Belum mengerjakan"
            holder.tvWaktu.text = "-"
        }
    }

    override fun getItemCount() = list.size

    fun updateData(newList: List<NilaiSiswaModel>) {
        list = newList
        notifyDataSetChanged()
    }
}
