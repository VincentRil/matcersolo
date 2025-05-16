package com.example.matematika_cer.guru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel

class TopikGridAdapter(
    private val list: List<TopikModel>,
    private val onEditClick: (TopikModel) -> Unit,
    private val onDeleteClick: (TopikModel) -> Unit
) : RecyclerView.Adapter<TopikGridAdapter.TopikViewHolder>() {

    inner class TopikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.card_topik)
        val namaTopik: TextView = itemView.findViewById(R.id.nama_topik)
        val editIcon: ImageView = itemView.findViewById(R.id.edit_soal_ic)
        val deleteIcon: ImageView = itemView.findViewById(R.id.hapus_topik_ic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopikViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topik_card, parent, false)
        return TopikViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopikViewHolder, position: Int) {
        val topik = list[position]
        holder.namaTopik.text = topik.namaTopik

        holder.editIcon.setOnClickListener {
            onEditClick(topik)
        }

        holder.deleteIcon.setOnClickListener {
            onDeleteClick(topik)
        }
    }

    override fun getItemCount(): Int = list.size
}
