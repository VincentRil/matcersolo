package com.example.matematika_cer.guru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel

class BankSoalAdapter(
    private var listTopik: List<TopikModel>,
    private val onTopikClick: (TopikModel) -> Unit
) : RecyclerView.Adapter<BankSoalAdapter.TopikViewHolder>() {

    inner class TopikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTopik: TextView = itemView.findViewById(R.id.tvTopik)
        val tvJumlahSoal: TextView = itemView.findViewById(R.id.tvJumlahSoal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopikViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bank_soal, parent, false)
        return TopikViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopikViewHolder, position: Int) {
        val data = listTopik[position]
        holder.tvTopik.text = data.namaTopik
        holder.tvJumlahSoal.text = "Jumlah soal: ${data.jumlahSoal}"

        holder.itemView.setOnClickListener {
            onTopikClick(data)
        }
    }

    override fun getItemCount(): Int = listTopik.size

    fun updateData(newList: List<TopikModel>) {
        listTopik = newList
        notifyDataSetChanged()
    }
}
