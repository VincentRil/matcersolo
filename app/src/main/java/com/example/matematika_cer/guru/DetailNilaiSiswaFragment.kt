package com.example.matematika_cer.guru

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.siswa.NilaiPerSiswaAdapter
import com.example.matematika_cer.siswa.NilaiPerSiswaModel

class DetailNilaiSiswaFragment : Fragment() {

    private lateinit var topik: String
    private lateinit var tanggal: String
    private lateinit var adapter: NilaiPerSiswaAdapter
    private lateinit var nilaiList: List<NilaiPerSiswaModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_nilai_siswa, container, false)

        // Ambil data dari arguments
        topik = arguments?.getString("topik") ?: "-"
        tanggal = arguments?.getString("tanggal") ?: "-"

        // Tampilkan topik dan tanggal
        view.findViewById<TextView>(R.id.topikTextView).text = "Nama Topik: $topik"
        view.findViewById<TextView>(R.id.tanggalTextView).text = "Tanggal: $tanggal"

        // Dummy data nilai siswa
        nilaiList = listOf(
            NilaiPerSiswaModel("23456", "Rosalia Span", 90),
            NilaiPerSiswaModel("21234", "Harli", 70)
        )

        // Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvNilaiPerSiswa)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NilaiPerSiswaAdapter(nilaiList)
        recyclerView.adapter = adapter

        // Setup Search EditText
        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = nilaiList.filter {
                    it.nama.contains(s.toString(), ignoreCase = true)
                }
                adapter.updateData(filteredList)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }
}