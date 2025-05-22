package com.example.matematika_cer.siswa

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.matematika_cer.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class HasilKuisSiswaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hasil_kuis_siswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari arguments
        val namaSiswa = arguments?.getString("namaSiswa") ?: "Siswa"
        val kelas = arguments?.getString("kelas") ?: "-"
        val nilai = arguments?.getInt("nilai") ?: 0
        val jumlahBenar = arguments?.getInt("jawaban_benar") ?: 0
        val jumlahSoal = arguments?.getInt("jumlah_soal") ?: 0
        val namaTopik = arguments?.getString("namaTopik") ?: "-"

        // Set text ke tampilan
        view.findViewById<TextView>(R.id.text_nama_siswa).text = namaSiswa
        view.findViewById<TextView>(R.id.text_kelas).text = "Kelas $kelas"
        view.findViewById<TextView>(R.id.text_nilai_akhir).text = nilai.toString()
        view.findViewById<TextView>(R.id.text_jawaban_benar).text = "$jumlahBenar dari $jumlahSoal"

        // Simpan ke riwayat setiap fragment ini dibuka
        simpanRiwayatKuis(
            context = requireContext(),
            namaTopik = namaTopik,
            nilai = nilai,
            jumlahBenar = jumlahBenar,
            jumlahSoal = jumlahSoal
        )

        // Tombol kembali ke beranda
        view.findViewById<Button>(R.id.btn_kembali_beranda).setOnClickListener {
            // Kembali ke fragment beranda siswa
            findNavController().popBackStack(R.id.berandaSiswaFragment, false)
        }
    }

    // Fungsi untuk simpan riwayat ke SharedPreferences
    private fun simpanRiwayatKuis(
        context: Context,
        namaTopik: String,
        nilai: Int,
        jumlahBenar: Int,
        jumlahSoal: Int
    ) {
        val prefs = context.getSharedPreferences("riwayat_kuis", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("list_riwayat", "[]")
        val type = object : TypeToken<ArrayList<RiwayatKuisModel>>() {}.type
        val riwayatList: ArrayList<RiwayatKuisModel> = gson.fromJson(json, type)

        // Tanggal format Indonesia
        val tanggal = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
        val riwayat = RiwayatKuisModel(
            namaTopik = namaTopik,
            tanggal = tanggal,
            nilai = nilai,
            jumlahBenar = jumlahBenar,
            jumlahSoal = jumlahSoal
        )
        // Hindari double insert (cek hanya jika nilai dan namaTopik sama persis terakhir)
        if (riwayatList.isEmpty() || riwayatList[0] != riwayat) {
            riwayatList.add(0, riwayat) // Masukkan ke paling atas
            val newJson = gson.toJson(riwayatList)
            prefs.edit().putString("list_riwayat", newJson).apply()
        }
    }
}
