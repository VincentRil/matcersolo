package com.example.matematika_cer.guru

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.viewmodel.SharedTopikViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BuatTopikdanSoalFragment : Fragment() {

    private lateinit var etNamaTopik: EditText
    private lateinit var etDeskripsiTopik: EditText
    private lateinit var etJumlahSoal: EditText
    private lateinit var etDurasi: EditText
    private lateinit var btnKonfirmasi: Button

    private val topikViewModel: SharedTopikViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buat_topikdan_soal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                if (nama.isNotEmpty() && deskripsi.isNotEmpty() && jumlah in 1..20 && durasi.isNotEmpty()) {
                    val tanggalSekarang = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())

                    val topikBaru = TopikModel(
                        namaTopik = nama,
                        deskripsiTopik = deskripsi,
                        durasi = "$durasi menit",
                        jumlahSoal = jumlah,
                        tanggal = tanggalSekarang,
                        isAktif = false
                    )

                    topikViewModel.daftarTopikSementara.add(topikBaru)

                    val bundle = Bundle().apply {
                        putParcelable("topik", topikBaru)
                    }

                    Toast.makeText(requireContext(), "Topik disimpan sementara", Toast.LENGTH_SHORT).show()

                    // Navigasi ke BuatSoalFragment dengan Parcelable
                    findNavController().navigate(
                        R.id.action_tambahTopikSoalFragment_to_buatSoalFragment,
                        bundle
                    )
                } else {
                    Toast.makeText(requireContext(), "Isi semua data dengan benar", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Terjadi kesalahan: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}
