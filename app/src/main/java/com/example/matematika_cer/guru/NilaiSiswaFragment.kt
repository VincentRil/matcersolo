package com.example.matematika_cer.guru

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.matematika_cer.guru.TopikNilaiModel

class NilaiSiswaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TopikNilaiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_nilai_siswa, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerViewTopikNilai)
        adapter = TopikNilaiAdapter(emptyList()) { topik ->
            val bundle = Bundle().apply {
                putParcelable("topik", topik)
            }
            findNavController().navigate(R.id.nilaisiswa_ke_daftarnilai, bundle)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // --- Ambil kelas dari session guru atau hardcode sementara ---
        val kelasGuru = "Kelas 1" // Sesuaikan, atau ambil dari login/profile
        ambilDaftarTopik(kelasGuru)
    }

    private fun ambilDaftarTopik(kelas: String) {
        val api = ApiClient.getApiService()
        api.getTopikNilaiByKelas(kelas).enqueue(object : Callback<List<TopikNilaiModel>> {
            override fun onResponse(
                call: Call<List<TopikNilaiModel>>,
                response: Response<List<TopikNilaiModel>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val listTopik = response.body()!!
                    adapter.updateData(listTopik)
                }
            }

            override fun onFailure(call: Call<List<TopikNilaiModel>>, t: Throwable) {
                // Tampilkan pesan error ke user, misal Toast
            }
        })
    }
}
