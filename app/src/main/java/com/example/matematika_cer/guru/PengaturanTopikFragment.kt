package com.example.matematika_cer.guru

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.viewmodel.SharedTopikViewModel
import java.util.*

class PengaturanTopikFragment : Fragment() {

    private val topikViewModel: SharedTopikViewModel by activityViewModels()

    private lateinit var spinner: Spinner
    private lateinit var tvNamaTopik: TextView
    private lateinit var etJumlahSoal: EditText
    private lateinit var etNamaPembuat: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etJam: EditText
    private lateinit var etDurasi: EditText
    private lateinit var etNilaiPerSoal: EditText
    private lateinit var cbSoalAcak: CheckBox
    private lateinit var tombolKirim: Button

    private var topikDipilih: TopikModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pengaturan_topik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner = view.findViewById(R.id.spinnerPengaturan)
        tvNamaTopik = view.findViewById(R.id.tvNamaTopikPengaturan)
        etJumlahSoal = view.findViewById(R.id.etJumlahSoalPengaturan)
        etNamaPembuat = view.findViewById(R.id.etNamaPembuatPengaturan)
        etTanggal = view.findViewById(R.id.etTanggalPengaturan)
        etJam = view.findViewById(R.id.etJamPengaturan)
        etDurasi = view.findViewById(R.id.etDurasiPengaturan)
        etNilaiPerSoal = view.findViewById(R.id.etNilaiSoalPengaturan)
        cbSoalAcak = view.findViewById(R.id.cbSoalAcakPengaturan)
        tombolKirim = view.findViewById(R.id.tombol_kirim)

        setupTanggalDanJamPicker()

        val daftarTopik = topikViewModel.daftarTopikSementara

        if (daftarTopik.isEmpty()) {
            Toast.makeText(requireContext(), "Belum ada topik dibuat", Toast.LENGTH_SHORT).show()
            return
        }

        val namaTopikList = daftarTopik.map { it.namaTopik }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namaTopikList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                topikDipilih = daftarTopik[position]
                tampilkanDataTopik(topikDipilih!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        tombolKirim.setOnClickListener {
            if (topikDipilih != null) {
                val durasi = etDurasi.text.toString()
                val pembuat = etNamaPembuat.text.toString()
                val tanggal = etTanggal.text.toString()
                val jam = etJam.text.toString()
                val acak = cbSoalAcak.isChecked

                Toast.makeText(requireContext(), "Pengaturan disiapkan untuk topik ${topikDipilih!!.namaTopik}", Toast.LENGTH_SHORT).show()


            }
        }
    }

    private fun setupTanggalDanJamPicker() {
        val calendar = Calendar.getInstance()

        etTanggal.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val tanggal = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                etTanggal.setText(tanggal)
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etJam.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                val jam = String.format("%02d:%02d", hourOfDay, minute)
                etJam.setText(jam)
            },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun tampilkanDataTopik(topik: TopikModel) {
        tvNamaTopik.text = topik.namaTopik
        etJumlahSoal.setText(topik.jumlahSoal.toString())
        etDurasi.setText(topik.durasi.filter { it.isDigit() })
        etNamaPembuat.setText("")
        etTanggal.setText("")
        etJam.setText("")
        etNilaiPerSoal.setText("")
        cbSoalAcak.isChecked = false
    }
}
