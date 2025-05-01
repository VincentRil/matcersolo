package com.example.matematika_cer.guru

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.siswa.SiswaModel
import com.example.matematika_cer.siswa.SiswaAdapter
import com.google.android.material.textfield.TextInputEditText

class DaftarSiswa : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var siswaAdapter: SiswaAdapter
    private lateinit var daftarSiswa: List<SiswaModel>  // original list

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daftar_siswa, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewSiswa)

        // Dummy data siswa
        daftarSiswa = listOf(
            SiswaModel("00001", "Rosalia Clara"),
            SiswaModel("69696", "Harli Lilir"),
            SiswaModel("96969", "Brigita Kalalo"),
            SiswaModel("00002", "Dani Surya"),
            SiswaModel("00003", "Yanti Saputri")
        )

        siswaAdapter = SiswaAdapter(daftarSiswa)
        recyclerView.adapter = siswaAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 🔍 Logika Search
        val searchEditText = view.findViewById<TextInputEditText>(R.id.searchSiswa)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = daftarSiswa.filter {
                    it.nama.contains(s.toString(), ignoreCase = true)
                }
                siswaAdapter.updateData(filtered)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }
}