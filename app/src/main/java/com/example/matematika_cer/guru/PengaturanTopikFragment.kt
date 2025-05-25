package com.example.matematika_cer.guru

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.model.TopikBesertaSoalRequest // harus dari model
import com.example.matematika_cer.model.toSoalRequest           // extension function model
import com.example.matematika_cer.viewmodel.SharedTopikViewModel
import java.util.*
import java.text.SimpleDateFormat

class PengaturanTopikFragment : Fragment() {

    private val topikViewModel: SharedTopikViewModel by activityViewModels()

    private lateinit var spinner: Spinner
    private lateinit var tvNamaTopik: TextView
    private lateinit var etDeskripsi: EditText
    private lateinit var etJumlahSoal: EditText
    private lateinit var etNamaPembuat: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etTanggalSelesai: EditText
    private lateinit var etJam: EditText
    private lateinit var etDurasi: EditText
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
        etDeskripsi = view.findViewById(R.id.deskripsiTopikPengaturan)
        etJumlahSoal = view.findViewById(R.id.etJumlahSoalPengaturan)
        etNamaPembuat = view.findViewById(R.id.etNamaPembuatPengaturan)
        etTanggal = view.findViewById(R.id.etTanggalPengaturan)
        etTanggalSelesai = view.findViewById(R.id.etTanggalselesaiPengaturan)
        etJam = view.findViewById(R.id.etJamPengaturan)
        etDurasi = view.findViewById(R.id.etDurasiPengaturan)
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
            if (topikDipilih == null) {
                Toast.makeText(requireContext(), "Pilih topik terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil dan trim semua input
            val deskripsi = etDeskripsi.text.toString().trim()
            val jumlahSoal = etJumlahSoal.text.toString().trim()
            val durasi = etDurasi.text.toString().trim()
            val pembuat = etNamaPembuat.text.toString().trim()
            val tanggal = etTanggal.text.toString().trim()
            val tanggalSelesai = etTanggalSelesai.text.toString().trim()
            val jam = etJam.text.toString().trim()

            // Validasi: Tidak boleh kosong
            if (deskripsi.isEmpty() || jumlahSoal.isEmpty() || durasi.isEmpty() ||
                pembuat.isEmpty() || tanggal.isEmpty() || tanggalSelesai.isEmpty() || jam.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi tanggal selesai >= tanggal mulai
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val dateMulai = formatter.parse(tanggal)
            val dateSelesai = formatter.parse(tanggalSelesai)
            if (dateSelesai.before(dateMulai)) {
                Toast.makeText(requireContext(), "Tanggal selesai tidak boleh sebelum tanggal mulai!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Parsing integer
            val jumlahSoalInt = jumlahSoal.toIntOrNull() ?: 0
            val durasiInt = durasi.toIntOrNull() ?: 0

            // Ambil list soal dari TopikModel dan mapping ke SoalRequest
            val soalList = topikDipilih!!.soalList.map { it.toSoalRequest() }

            // Ganti sesuai kelas user/guru jika ada spinner/field kelas
            val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val kelas = sharedPref.getString("kelas", "") ?: ""

            val nilaiPerSoal = 10

            val topikRequest = TopikBesertaSoalRequest(
                namaTopik = topikDipilih!!.namaTopik.trim(),
                deskripsiTopik = deskripsi,
                jumlahSoal = jumlahSoalInt,
                durasiMenit = durasiInt,
                tanggalMulai = tanggal,
                jamPelaksanaan = jam,
                tanggalSelesai = tanggalSelesai,
                nilaiPerSoal = nilaiPerSoal,
                pembuat = pembuat,
                kelas = kelas,
                soalList = soalList
            )

            // Kirim ke backend, dan reset input jika sukses
            topikViewModel.kirimTopikKeBackend(topikRequest) { sukses ->
                if (sukses) {
                    // Reset form input
                    etDeskripsi.setText("")
                    etJumlahSoal.setText("")
                    etNamaPembuat.setText("")
                    etTanggal.setText("")
                    etTanggalSelesai.setText("")
                    etJam.setText("")
                    etDurasi.setText("")
                    spinner.setSelection(0)

                    Toast.makeText(requireContext(), "Topik & soal BERHASIL dikirim!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Gagal kirim topik & soal!", Toast.LENGTH_SHORT).show()
                }
            }

            Toast.makeText(requireContext(), "Topik & soal sedang dikirim...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTanggalDanJamPicker() {
        val calendar = Calendar.getInstance()
        etTanggal.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val tanggal = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                etTanggal.setText(tanggal)
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = calendar.timeInMillis
            datePicker.show()
        }
        etTanggalSelesai.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val tanggal = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                etTanggalSelesai.setText(tanggal)
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = calendar.timeInMillis
            datePicker.show()
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
        etDeskripsi.setText(topik.deskripsiTopik)
        etJumlahSoal.setText(topik.jumlahSoal.toString())
        etDurasi.setText(topik.durasiMenit.toString())
        etNamaPembuat.setText("")
        etTanggal.setText("")
        etTanggalSelesai.setText("")
        etJam.setText("")
    }
}
