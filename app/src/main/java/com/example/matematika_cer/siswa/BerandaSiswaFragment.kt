package com.example.matematika_cer.siswa

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.matematika_cer.network.ApiClient
import retrofit2.Response

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.loginregis.LoginActivity
import retrofit2.Call
import retrofit2.Callback

class BerandaSiswaFragment : Fragment() {

    private lateinit var topikAdapter: TopikSiswaAdapter
    private var filteredTopikList: List<TopikSiswaModel> = emptyList()
    private val topikViewModel: SiswaTopikViewModel by viewModels()

    private var semuaTopik: List<TopikSiswaModel> = emptyList()
    private var doneTopikIds: List<Long> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_beranda_siswa, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logoutBtn = view.findViewById<View>(R.id.logoutbtnsiswa)
        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        tampilkanIdentitasSiswa(view)
        tampilkanNilaiTerbaru(view)
        setupRecyclerView(view)

        val idUser = sharedPref.getInt("user_id", -1)
        if (idUser != -1) {
            topikViewModel.ambilTopikSelesaiSiswa(idUser)
        }
        topikViewModel.ambilDaftarTopik()

        // Observe daftarTopik & topikSelesai secara terpisah, lalu filter jika dua-duanya sudah ada
        topikViewModel.daftarTopikLiveData.observe(viewLifecycleOwner) { semuaTopikBaru ->
            semuaTopik = semuaTopikBaru
            updateFilteredTopik()
        }
        topikViewModel.topikSelesaiLiveData.observe(viewLifecycleOwner) { doneIds ->
            doneTopikIds = doneIds
            updateFilteredTopik()
        }

        // --- Logout logic ---
        logoutBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout dan kembali ke halaman login?")
                .setPositiveButton("Ya") { dialog, _ ->
                    sharedPref.edit().clear().apply()
                    Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }, 1000)
                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun updateFilteredTopik() {
        val prefs = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val kelasSiswa = prefs.getString("kelas", "") ?: ""

        filteredTopikList = semuaTopik.filter {
            it.kelas?.trim()?.equals(kelasSiswa.trim(), ignoreCase = true) == true &&
                    !doneTopikIds.contains(it.id.toLong())
        }
        topikAdapter.filterList(filteredTopikList)
        Log.d("DEBUG_TOPIK", "filteredTopikList.size = ${filteredTopikList.size}")
    }

    private fun tampilkanIdentitasSiswa(view: View) {
        val prefs = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val nama = prefs.getString("nama", "Siswa") ?: "Siswa"
        val kelas = prefs.getString("kelas", "-") ?: "-"
        view.findViewById<TextView>(R.id.nama_siswa).text = "Halo $nama!"
        view.findViewById<TextView>(R.id.kelas_siswa).text = "$kelas"
    }

    private fun tampilkanNilaiTerbaru(view: View) {
        val prefs = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)
        val nilaiTextView = view.findViewById<TextView>(R.id.text_nilai)      // Ganti id sesuai layout kamu
        val topikTextView = view.findViewById<TextView>(R.id.text_topik_nilai) // Ganti id sesuai layout kamu

        if (userId == -1) {
            topikTextView.text = "Belum ada"
            nilaiTextView.text = "Nilai : 0"
            return
        }

        val api = ApiClient.getApiService()
        api.getNilaiTerbaru(userId).enqueue(object : retrofit2.Callback<NilaiTerbaruModel> {
            override fun onResponse(
                call: retrofit2.Call<NilaiTerbaruModel>,
                response: retrofit2.Response<NilaiTerbaruModel>
            ) {
                Log.d("NilaiTerbaru", "HTTP Code: ${response.code()}, body: ${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val nilai = response.body()!!
                    val namaTopik = nilai.namaTopik ?: "-"
                    val skor = nilai.skor ?: 0
                    topikTextView.text = namaTopik
                    nilaiTextView.text = "Nilai : $skor"
                    Log.d("NilaiTerbaru", "Tampil: $namaTopik : $skor")
                } else {
                    Log.e("NilaiTerbaru", "Gagal: code=${response.code()} error=${response.errorBody()?.string()}")
                    topikTextView.text = "Belum ada"
                    nilaiTextView.text = "Nilai : 0"
                }
            }
            override fun onFailure(call: retrofit2.Call<NilaiTerbaruModel>, t: Throwable) {
                Log.e("NilaiTerbaru", "onFailure: ${t.message}", t)
                topikTextView.text = "Belum ada"
                nilaiTextView.text = "Nilai : 0"
            }
        })

    }




    private fun setupRecyclerView(view: View) {
        topikAdapter = TopikSiswaAdapter(emptyList()) { topik ->
            val bundle = Bundle().apply {
                putInt("topik_id", topik.id)
                putString("namaTopik", topik.namaTopik)
                putString("deskripsiTopik", topik.deskripsiTopik)
                putString("kelas", topik.kelas)
                putString("pembuat", topik.pembuat)
                putString("jumlahSoal", topik.jumlahSoal)
                putString("durasiMenit", topik.durasiMenit)
                putString("jamPelaksanaan", topik.jamPelaksanaan)
                putString("tanggalMulai", topik.tanggalMulai)
                putString("tanggalSelesai", topik.tanggalSelesai)
                putParcelableArrayList("listSoal", ArrayList(topik.soalList ?: listOf()))
            }
            findNavController().navigate(
                R.id.action_berandaSiswaFragment_to_detailTopikSiswaFragment,
                bundle
            )
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvTopik)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = topikAdapter

        val searchBar = view.findViewById<EditText>(R.id.searchBar)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().lowercase()
                val filtered = filteredTopikList.filter {
                    it.namaTopik.lowercase().contains(keyword)
                }
                topikAdapter.filterList(filtered)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
