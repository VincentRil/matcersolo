package com.example.matematika_cer.guru

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.network.ApiClient
import com.example.matematika_cer.siswa.SiswaModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarNilaiFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NilaiSiswaAdapter
    private var nilaiList: MutableList<NilaiSiswaModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_daftar_nilai, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerViewNilaiSiswa)
        adapter = NilaiSiswaAdapter(nilaiList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val kelasGuru = requireContext()
            .getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            .getString("kelas", "") ?: ""


        val topik = arguments?.getParcelable<TopikNilaiModel>("topik")
        view.findViewById<TextView>(R.id.tvNamaTopik).text = topik?.namaTopik ?: "-"

        topik?.let {
            ambilDaftarNilai(it.id, kelasGuru)
        }
    }

    // Tambahkan parameter kelas di sini
    private fun ambilDaftarNilai(topikId: Long, kelas: String) {
        val api = ApiClient.getApiService()

        // 1. Ambil siswa yang sudah mengerjakan (ada nilai)
        api.getNilaiSiswaByTopikAndKelas(topikId, kelas)
            .enqueue(object : Callback<List<NilaiSiswaModel>> {
                override fun onResponse(
                    call: Call<List<NilaiSiswaModel>>,
                    response: Response<List<NilaiSiswaModel>>
                ) {
                    nilaiList.clear()
                    if (response.isSuccessful && response.body() != null) {
                        nilaiList.addAll(response.body()!!)
                        Log.d("DaftarNilaiFragment", "Jumlah sudah mengerjakan: ${nilaiList.size}")
                    }

                    // 2. Setelah itu, ambil yang belum mengerjakan (PASTIKAN PARAMETERNYA BENAR)
                    api.getSiswaBelumMengerjakan(topikId, kelas)
                        .enqueue(object : Callback<List<SiswaModel>> {
                            override fun onResponse(
                                call: Call<List<SiswaModel>>,
                                response: Response<List<SiswaModel>>
                            ) {
                                if (response.isSuccessful && response.body() != null) {
                                    val belumMengerjakan = response.body()!!
                                    Log.d("DaftarNilaiFragment", "Jumlah belum mengerjakan: ${belumMengerjakan.size}")
                                    for (siswa in belumMengerjakan) {
                                        Log.d("DaftarNilaiFragment", "Siswa belum: ${siswa.namaLengkap}")
                                        nilaiList.add(
                                            NilaiSiswaModel(
                                                namaSiswa = siswa.namaLengkap,
                                                nilai = null,           // Belum mengerjakan: nilai null
                                                waktuSubmit = null      // Belum mengerjakan: waktu null
                                            )
                                        )
                                    }
                                }
                                adapter.updateData(nilaiList)
                            }

                            override fun onFailure(call: Call<List<SiswaModel>>, t: Throwable) {
                                Log.e("DaftarNilaiFragment", "Gagal ambil siswa belum: ${t.message}")
                                adapter.updateData(nilaiList)
                            }
                        })
                }

                override fun onFailure(call: Call<List<NilaiSiswaModel>>, t: Throwable) {
                    Log.e("DaftarNilaiFragment", "Gagal ambil nilai: ${t.message}")
                }
            })
    }
}
