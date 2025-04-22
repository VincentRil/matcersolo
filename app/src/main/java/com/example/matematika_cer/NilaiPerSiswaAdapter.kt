package com.example.matematika_cer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.NilaiPerSiswaModel

class NilaiPerSiswaAdapter(private var data: List<NilaiPerSiswaModel>) :
    RecyclerView.Adapter<NilaiPerSiswaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nis: TextView = view.findViewById(R.id.tvNis)
        val nama: TextView = view.findViewById(R.id.tvNama)
        val nilai: TextView = view.findViewById(R.id.tvNilai)
        val detail: ImageView = view.findViewById(R.id.ivDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nilai_per_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val siswa = data[position]
        holder.nis.text = siswa.nis
        holder.nama.text = siswa.nama
        holder.nilai.text = "${siswa.nilai}/100"
        // Optional: tambahkan aksi klik detail jika perlu
    }

    override fun getItemCount() = data.size

    // ✅ Fungsi tambahan untuk update data saat search
    fun updateData(newData: List<NilaiPerSiswaModel>) {
        data = newData
        notifyDataSetChanged()
    }
}
