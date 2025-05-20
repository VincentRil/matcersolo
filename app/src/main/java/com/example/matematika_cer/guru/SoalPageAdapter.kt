package com.example.matematika_cer.guru

import android.app.AlertDialog
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.siswa.SoalModel

class SoalPageAdapter(
    private val list: List<SoalModel>,
    private val onSoalClick: (SoalModel) -> Unit = {},
    private val onHapusClick: ((SoalModel) -> Unit)? = null
) : RecyclerView.Adapter<SoalPageAdapter.SoalViewHolder>() {

    inner class SoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teksPertanyaan: TextView = itemView.findViewById(R.id.teksPertanyaan)
        val opsiA: TextView = itemView.findViewById(R.id.opsiA)
        val opsiB: TextView = itemView.findViewById(R.id.opsiB)
        val opsiC: TextView = itemView.findViewById(R.id.opsiC)
        val opsiD: TextView = itemView.findViewById(R.id.opsiD)
        val jawabanBenar: TextView = itemView.findViewById(R.id.jawabanBenar)
        val gambarSoal: ImageView = itemView.findViewById(R.id.gambarSoal)
        val btnHapus: ImageButton = itemView.findViewById(R.id.btnHapusSoal)

        init {
            itemView.setOnClickListener {
                val soal = list[adapterPosition]
                onSoalClick(soal)
            }

            btnHapus.setOnClickListener {
                val soal = list[adapterPosition]
                val context = itemView.context

                AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus soal ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        onHapusClick?.invoke(soal)
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_soal_perhalaman, parent, false)
        return SoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoalViewHolder, position: Int) {
        val soal = list[position]
        holder.teksPertanyaan.text = soal.pertanyaan
        holder.opsiA.text = "A. ${soal.pilihanA}"
        holder.opsiB.text = "B. ${soal.pilihanB}"
        holder.opsiC.text = "C. ${soal.pilihanC}"
        holder.opsiD.text = "D. ${soal.pilihanD}"
        holder.jawabanBenar.text = "Jawaban Benar: ${soal.jawabanBenar}"

        if (!soal.gambar.isNullOrEmpty()) {
            holder.gambarSoal.visibility = View.VISIBLE
            holder.gambarSoal.setImageURI(Uri.parse(soal.gambar))
        } else {
            holder.gambarSoal.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = list.size
}
