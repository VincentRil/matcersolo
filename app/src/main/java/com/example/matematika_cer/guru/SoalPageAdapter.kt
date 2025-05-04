package com.example.matematika_cer.guru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.siswa.SoalModel

class SoalPageAdapter(private val list: List<SoalModel>) : RecyclerView.Adapter<SoalPageAdapter.SoalViewHolder>() {

    inner class SoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teksSoal: TextView = itemView.findViewById(R.id.teksSoal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_soal_perhalaman, parent, false)
        return SoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoalViewHolder, position: Int) {
        val soal = list[position]
        holder.teksSoal.text = soal.pertanyaan
    }

    override fun getItemCount(): Int = list.size
}
