package com.example.matematika_cer.guru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matematika_cer.R
import com.example.matematika_cer.siswa.SoalModel

class SoalEditAdapter(
    private val list: MutableList<SoalModel>,
    private val onItemClick: (SoalModel) -> Unit,
    private val onItemDelete: (SoalModel) -> Unit
) : RecyclerView.Adapter<SoalEditAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pertanyaan: TextView = view.findViewById(R.id.teksPertanyaan)
        val opsiA: TextView = view.findViewById(R.id.opsiA)
        val opsiB: TextView = view.findViewById(R.id.opsiB)
        val opsiC: TextView = view.findViewById(R.id.opsiC)
        val opsiD: TextView = view.findViewById(R.id.opsiD)
        val jawabanBenar: TextView = view.findViewById(R.id.jawabanBenar)
        val gambarSoal: ImageView = view.findViewById(R.id.gambarSoal)
        val btnHapus: ImageButton = view.findViewById(R.id.btnHapusSoal)

        init {
            view.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onItemClick(list[pos])
                }
            }

            btnHapus.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onItemDelete(list[pos])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_soal_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val soal = list[position]
        holder.pertanyaan.text = soal.pertanyaan
        holder.opsiA.text = "A. ${soal.pilihanA}"
        holder.opsiB.text = "B. ${soal.pilihanB}"
        holder.opsiC.text = "C. ${soal.pilihanC}"
        holder.opsiD.text = "D. ${soal.pilihanD}"
        holder.jawabanBenar.text = "Jawaban Benar: ${soal.jawabanBenar}"

        if (!soal.gambar.isNullOrEmpty()) {
            holder.gambarSoal.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(soal.gambar)
                .into(holder.gambarSoal)
        } else {
            holder.gambarSoal.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<SoalModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
