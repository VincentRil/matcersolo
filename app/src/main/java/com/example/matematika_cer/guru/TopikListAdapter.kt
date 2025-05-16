package com.example.matematika_cer.guru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel

class TopikListAdapter(
    private var list: List<TopikModel>,
    private val onEditClick: (TopikModel) -> Unit,
    private val onDeleteClick: (TopikModel) -> Unit
) : RecyclerView.Adapter<TopikListAdapter.TopikViewHolder>() {

    inner class TopikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama: TextView = itemView.findViewById(R.id.nama_topik)
        val editIcon: ImageView = itemView.findViewById(R.id.edit_soal_ic)
        val deleteIcon: ImageView = itemView.findViewById(R.id.hapus_topik_ic)

        init {
            editIcon.setOnClickListener {
                val topik = list[adapterPosition]
                onEditClick(topik)
            }

            deleteIcon.setOnClickListener {
                val topik = list[adapterPosition]
                onDeleteClick(topik)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopikViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topik_card, parent, false)
        return TopikViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopikViewHolder, position: Int) {
        val topik = list[position]
        holder.nama.text = topik.namaTopik
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<TopikModel>) {
        list = newList
        notifyDataSetChanged()
    }
}
