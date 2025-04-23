package com.example.matematika_cer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class DetailTopikSiswaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_topik_siswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari Bundle
        val namaTopik = arguments?.getString("namaTopik") ?: "-"
        val deskripsi = arguments?.getString("deskripsi") ?: "-"
        val kelas = arguments?.getString("kelas") ?: "-"
        val pembuat = arguments?.getString("pembuat") ?: "-"
        val jumlahSoal = arguments?.getInt("jumlahSoal") ?: 0
        val durasi = arguments?.getString("durasi") ?: "-"

        // Set teks ke TextView
        view.findViewById<TextView>(R.id.text_judul_topik).text = namaTopik
        view.findViewById<TextView>(R.id.text_kelas).text = "Kelas: $kelas"
        view.findViewById<TextView>(R.id.text_pembuat).text = "Dibuat oleh: $pembuat"
        view.findViewById<TextView>(R.id.text_jumlah_soal).text = "Jumlah soal: $jumlahSoal"
        view.findViewById<TextView>(R.id.text_durasi).text = "Durasi: $durasi"

        // Tombol Mulai
        view.findViewById<Button>(R.id.btn_mulai).setOnClickListener {
            val bundle = Bundle().apply {
                putString("namaTopik", namaTopik)
                // tambahkan data tambahan kalau diperlukan di soal nanti
            }
            findNavController().navigate(R.id.action_detailTopikSiswaFragment_to_soalSiswaFragment, bundle)
        }

        // Tombol Kembali
        view.findViewById<Button>(R.id.btn_kembali).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
