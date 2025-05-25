package com.example.matematika_cer.siswa

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.network.ApiClient

class RiwayatKuisFragment : Fragment() {

    private lateinit var adapter: RiwayatKuisAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private var riwayatList: List<NilaiRiwayatModel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_riwayat_kuis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewRiwayat)
        searchBar = view.findViewById(R.id.searchBarRiwayat)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RiwayatKuisAdapter(riwayatList)
        recyclerView.adapter = adapter

        // Ambil userId dari SharedPreferences (session)
        val prefs = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)
        if (userId != -1) {
            ambilRiwayatKuisDariServer(userId)
        }

        // Fitur pencarian/filter nama topik
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().lowercase()
                val filtered = riwayatList.filter {
                    it.namaTopik?.lowercase()?.contains(keyword) == true
                }
                adapter.filterList(filtered)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Retrofit call untuk ambil riwayat dari backend
    private fun ambilRiwayatKuisDariServer(userId: Int) {
        val api = ApiClient.getApiService()
        api.getRiwayatNilai(userId).enqueue(object : retrofit2.Callback<List<NilaiRiwayatModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<NilaiRiwayatModel>>,
                response: retrofit2.Response<List<NilaiRiwayatModel>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val riwayat = response.body()!!
                    riwayatList = riwayat
                    adapter.filterList(riwayat)
                } else {
                    Log.e("RiwayatKuisFragment", "Gagal ambil data: code=${response.code()} body=${response.errorBody()?.string()}")
                    // Optional: tampilkan pesan error ke user (misal pakai Toast)
                }
            }

            override fun onFailure(
                call: retrofit2.Call<List<NilaiRiwayatModel>>,
                t: Throwable
            ) {
                Log.e("RiwayatKuisFragment", "onFailure: ${t.message}", t)
                // Optional: tampilkan pesan error ke user
            }
        })
    }
}
