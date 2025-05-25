package com.example.matematika_cer.siswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R

class TopikSiswaAdapter(
    private var list: List<TopikSiswaModel>,
    private val onItemClick: (TopikSiswaModel) -> Unit
) : RecyclerView.Adapter<TopikSiswaAdapter.TopikViewHolder>() {

    inner class TopikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTopik: TextView = itemView.findViewById(R.id.nama_topik)
        val deskripsiTopik: TextView = itemView.findViewById(R.id.deskripsiTopik)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(list[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopikViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topik_siswa, parent, false)
        return TopikViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopikViewHolder, position: Int) {
        val topik = list[position]
        holder.namaTopik.text = "${position + 1}. ${topik.namaTopik}"   // Bukan id, tapi nomor urut tampil
        holder.deskripsiTopik.text = topik.deskripsiTopik
    }


    override fun getItemCount(): Int = list.size

    fun filterList(newList: List<TopikSiswaModel>) {
        // urutkan dari id terbesar ke terkecil (terbaru di atas)
        list = newList.sortedByDescending { it.id }
        notifyDataSetChanged()
    }

}
