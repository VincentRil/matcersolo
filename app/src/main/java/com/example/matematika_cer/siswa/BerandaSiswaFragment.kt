package com.example.matematika_cer.siswa

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.loginregis.LoginActivity

class BerandaSiswaFragment : Fragment() {

    private lateinit var topikAdapter: TopikSiswaAdapter
    private lateinit var topikList: List<TopikSiswaModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_beranda_siswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logoutBtn = view.findViewById<View>(R.id.logoutbtnsiswa)
        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        tampilkanIdentitasSiswa(view)
        tampilkanNilaiTerbaru(view)
        setupDaftarTopik(view)

        logoutBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout dan kembali ke halaman login?")
                .setPositiveButton("Ya") { dialog, _ ->
                    // Hapus sesi
                    sharedPref.edit().clear().apply()

                    Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }, 1000)

                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun tampilkanIdentitasSiswa(view: View) {
        val prefs = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val nama = prefs.getString("nama", "Siswa") ?: "Siswa"
        val kelas = prefs.getString("kelas", "-") ?: "-"

        view.findViewById<TextView>(R.id.nama_siswa).text = "Halo $nama!"
        view.findViewById<TextView>(R.id.kelas_siswa).text = "$kelas"
    }

    private fun tampilkanNilaiTerbaru(view: View) {
        val prefs = requireContext().getSharedPreferences("nilai_pref", Context.MODE_PRIVATE)
        val topik = prefs.getString("topik_terakhir", "Belum ada") ?: "Belum ada"
        val nilai = prefs.getInt("nilai_terakhir", 0)
        val total = prefs.getInt("total_soal", 100)

        view.findViewById<TextView>(R.id.text_topik_nilai).text = topik
        view.findViewById<TextView>(R.id.text_nilai).text = "Nilai : $nilai/$total"
    }

    private fun setupDaftarTopik(view: View) {
        // Dummy data
        topikList = listOf(
            TopikSiswaModel(1, "Penjumlahan dan Pengurangan", "Topik dasar aritmetika", "4", "Ibu Sali", 10, "05:00"),
            TopikSiswaModel(2, "Perkalian dan Pembagian", "Latihan dasar perkalian", "4", "Ibu Rani", 8, "04:00"),
            TopikSiswaModel(3, "Pecahan", "Memahami pecahan dalam matematika", "4", "Pak Budi", 12, "06:00"),
            TopikSiswaModel(4, "Pengukuran", "Satuan panjang dan berat", "4", "Ibu Lilis", 9, "05:00")
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvTopik)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        topikAdapter = TopikSiswaAdapter(topikList) { topik ->
            val bundle = Bundle().apply {
                putString("namaTopik", topik.namaTopik)
                putString("deskripsi", topik.deskripsi)
                putString("kelas", topik.kelas)
                putString("pembuat", topik.pembuat)
                putInt("jumlahSoal", topik.jumlahSoal)
                putString("durasi", topik.durasi)
            }
            findNavController().navigate(
                R.id.action_berandaSiswaFragment_to_detailTopikSiswaFragment,
                bundle
            )
        }
        recyclerView.adapter = topikAdapter

        val searchBar = view.findViewById<EditText>(R.id.searchBar)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().lowercase()
                val filtered = topikList.filter {
                    it.namaTopik.lowercase().contains(keyword)
                }
                topikAdapter.filterList(filtered)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


}
