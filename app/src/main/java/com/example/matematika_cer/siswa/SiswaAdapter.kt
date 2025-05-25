package com.example.matematika_cer.siswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R



class SiswaAdapter(private var daftarSiswa: List<SiswaModel>) :
    RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder>() {

    inner class SiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idText: TextView = itemView.findViewById(R.id.textIdSiswa)
        val usernameText: TextView = itemView.findViewById(R.id.textUsernameSiswa)
        val password: TextView = itemView.findViewById(R.id.textpassword)
        val namaText: TextView = itemView.findViewById(R.id.textNamaSiswa)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_siswa, parent, false)
        return SiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        val siswa = daftarSiswa[position]
        holder.idText.text = siswa.id.toString()
        holder.usernameText.text = siswa.username
        holder.namaText.text = siswa.namaLengkap
        holder.password.text = siswa.password
    }

    override fun getItemCount(): Int = daftarSiswa.size

    fun updateData(newList: List<SiswaModel>) {
        daftarSiswa = newList
        notifyDataSetChanged()
    }
}
