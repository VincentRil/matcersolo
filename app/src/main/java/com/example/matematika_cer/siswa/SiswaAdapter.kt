package com.example.matematika_cer.siswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R

class SiswaAdapter(private var daftarSiswa: List<SiswaModel>) :
    RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder>() {

    // ViewHolder untuk menghubungkan layout item_siswa.xml ke kode
    inner class SiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomorText: TextView = itemView.findViewById(R.id.textNomorSiswa)
        val namaText: TextView = itemView.findViewById(R.id.textNamaSiswa)
        val iconDetail: ImageView = itemView.findViewById(R.id.iconDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa, parent, false)
        return SiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        val siswa = daftarSiswa[position]
        holder.nomorText.text = siswa.nomor
        holder.namaText.text = siswa.nama

        holder.iconDetail.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Detail: ${siswa.nama}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = daftarSiswa.size

    // ✅ Tambahkan fungsi updateData agar adapter bisa di-filter
    fun updateData(newList: List<SiswaModel>) {
        daftarSiswa = newList
        notifyDataSetChanged()
    }
}