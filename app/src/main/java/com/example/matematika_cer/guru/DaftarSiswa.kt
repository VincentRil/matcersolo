package com.example.matematika_cer.guru

import android.content.Context
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
import com.example.matematika_cer.network.ApiClient
import com.example.matematika_cer.siswa.SiswaModel
import com.example.matematika_cer.siswa.SiswaAdapter
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarSiswa : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var siswaAdapter: SiswaAdapter
    private var daftarSiswa: List<SiswaModel> = listOf()  // List kosong awal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val kelas = sharedPref.getString("kelas", "Guru")?: "Guru"
        val view = inflater.inflate(R.layout.fragment_daftar_siswa, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewSiswa)

        siswaAdapter = SiswaAdapter(daftarSiswa)
        recyclerView.adapter = siswaAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Ambil kelas dari session atau argument (contoh ambil dari argument)
//        val kelas = arguments?.getString("kelas") ?: "Kelas 1" // fallback

        ambilDaftarSiswaDariServer(kelas)

        // 🔍 Logika Search
        val searchEditText = view.findViewById<TextInputEditText>(R.id.searchSiswa)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString().lowercase()
                val filtered = daftarSiswa.filter {
                    it.namaLengkap.lowercase().contains(keyword) ||
                            it.username.lowercase().contains(keyword)
                }
                siswaAdapter.updateData(filtered)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun ambilDaftarSiswaDariServer(kelas: String) {

        val api = ApiClient.getApiService()
        api.getSiswaByKelas(kelas).enqueue(object : Callback<List<SiswaModel>> {
            override fun onResponse(
                call: Call<List<SiswaModel>>,
                response: Response<List<SiswaModel>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    daftarSiswa = response.body()!!
                    siswaAdapter.updateData(daftarSiswa)
                }
            }
            override fun onFailure(call: Call<List<SiswaModel>>, t: Throwable) {
                // Optional: tampilkan error (Toast/log)
            }
        })
    }
}
