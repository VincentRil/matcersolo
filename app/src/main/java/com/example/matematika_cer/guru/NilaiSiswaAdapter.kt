package com.example.matematika_cer.guru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R

class NilaiSiswaAdapter(
    private val list: List<NilaiSiswaModel>
) : RecyclerView.Adapter<NilaiSiswaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tvNamaSiswa)
        val nilai: TextView = view.findViewById(R.id.tvNilaiSiswa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nilai_siswa, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val siswa = list[position]
        holder.nama.text = siswa.namaSiswa
        holder.nilai.text = "${siswa.nilai}"
    }

    override fun getItemCount(): Int = list.size
}
