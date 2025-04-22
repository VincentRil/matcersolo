package com.example.matematika_cer

import TopikModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.*

class BuatTopikdanSoalFragment : Fragment() {

    private lateinit var etNamaTopik: EditText
    private lateinit var etDeskripsiTopik: EditText
    private lateinit var etJumlahSoal: EditText
    private lateinit var etDurasi: EditText
    private lateinit var btnKonfirmasi: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buat_topikdan_soal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi elemen dari layout XML
        etNamaTopik = view.findViewById(R.id.etNamaTopik)
        etDeskripsiTopik = view.findViewById(R.id.etDeskripsiTopik)
        etJumlahSoal = view.findViewById(R.id.etJumlahSoal)
        etDurasi = view.findViewById(R.id.etDurasi)
        btnKonfirmasi = view.findViewById(R.id.btnKonfirmasi)

        btnKonfirmasi.setOnClickListener {
            try {
                val nama = etNamaTopik.text.toString().trim()
                val deskripsi = etDeskripsiTopik.text.toString().trim()
                val jumlah = etJumlahSoal.text.toString().toIntOrNull() ?: 0
                val durasi = etDurasi.text.toString().trim()

                if (nama.isNotEmpty() && jumlah in 1..20 && durasi.isNotEmpty()) {
                    val tanggalSekarang = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())

                    val topikBaru = TopikModel(
                        namaTopik = nama,
                        durasi = "$durasi menit",
                        jumlahSoal = jumlah,
                        tanggal = tanggalSekarang,
                        isAktif = false
                    )

                    val bundle = Bundle()
                    bundle.putParcelable("topik", topikBaru)

                    Toast.makeText(requireContext(), "Navigasi dimulai", Toast.LENGTH_SHORT).show()

                    // Ganti navController aman:
                    val navController = requireActivity()
                        .supportFragmentManager
                        .findFragmentById(R.id.nav_host_fragment)
                        ?.findNavController()

                    navController?.navigate(
                        R.id.action_tambahTopikSoalFragment_to_buatSoalFragment,
                        bundle
                    ) ?: Toast.makeText(requireContext(), "NavController null", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Isi semua data dengan benar", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "ERROR: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

    }
}
