// DaftarNilaiFragment.kt
package com.example.matematika_cer.guru

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel

class DaftarNilaiFragment : Fragment() {

    private lateinit var topik: TopikModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daftar_nilai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topik = arguments?.getParcelable("topik") ?: return
        recyclerView = view.findViewById(R.id.recyclerViewNilai)
        tvInfo = view.findViewById(R.id.tvInfoNilai)

        val nilaiList = generateDummyNilai(topik)
        val sudah = nilaiList.size
        val belum = (topik.totalPeserta ?: 0) - sudah

        tvInfo.text = "Sudah menjawab: $sudah, Belum menjawab: $belum"

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = NilaiSiswaAdapter(nilaiList)
    }

    private fun generateDummyNilai(topik: TopikModel): List<NilaiSiswaModel> {
        // Simulasi dummy nilai untuk topik tertentu
        return listOf(
            NilaiSiswaModel("Andi", 80),
            NilaiSiswaModel("Budi", 90),
            NilaiSiswaModel("Citra", 75)
        )
    }
}
