package com.example.matematika_cer.siswa

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.matematika_cer.R

class MulaiSoalSiswaFragment : Fragment() {

    private var namaTopik: String? = null
    private var kelas: String? = null
    private var pembuat: String? = null
    private var jumlahSoal: Int = 0
    private var durasi: String? = null
    private var soalList: ArrayList<SoalModel>? = null

    private var indeksSoal = 0
    private var jumlahBenar = 0
    private lateinit var jawabanUser: MutableList<String?>

    private var timer: CountDownTimer? = null
    private var sisaDetik: Long = 0

    // Binding View (supaya akses view lebih rapi)
    private lateinit var tvNomorSoal: TextView
    private lateinit var tvNilaiSementara: TextView
    private lateinit var tvPertanyaan: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var rbA: RadioButton
    private lateinit var rbB: RadioButton
    private lateinit var rbC: RadioButton
    private lateinit var rbD: RadioButton
    private lateinit var tvTimer: TextView
    private lateinit var btnKembali: Button
    private lateinit var btnLanjut: Button
    private var topikId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topikId = it.getInt("topik_id", -1)
            namaTopik = it.getString("namaTopik")
            kelas = it.getString("kelas")
            pembuat = it.getString("pembuat")
            jumlahSoal = it.getInt("jumlahSoal")
            durasi = it.getString("durasi")
            soalList = it.getParcelableArrayList("listSoal")
        }

        // Disable tombol back selama kuis
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(requireContext(), "Selesaikan kuis sebelum keluar!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mulai_soal_siswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi View terlebih dahulu
        tvNomorSoal = view.findViewById(R.id.teks_nomor_soal)
        tvNilaiSementara = view.findViewById(R.id.teks_nilai_sementara)
        tvPertanyaan = view.findViewById(R.id.teks_pertanyaan)
        radioGroup = view.findViewById(R.id.opsi_jawaban)
        rbA = view.findViewById(R.id.jawaban_a)
        rbB = view.findViewById(R.id.jawaban_b)
        rbC = view.findViewById(R.id.jawaban_c)
        rbD = view.findViewById(R.id.jawaban_d)
        tvTimer = view.findViewById(R.id.teks_timer)
        btnKembali = view.findViewById(R.id.btn_kembali_soal)
        btnLanjut = view.findViewById(R.id.btn_lanjut)

        btnKembali.visibility = View.INVISIBLE // tombol kembali tetap disembunyikan

        val listSoal = soalList ?: arrayListOf()
        if (listSoal.isEmpty()) {
            Toast.makeText(requireContext(), "Soal tidak ditemukan.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        jawabanUser = MutableList(listSoal.size) { null }
        indeksSoal = 0
        jumlahBenar = 0

        tampilkanSoal(listSoal)
        startTimer()

        btnLanjut.setOnClickListener {
            val pilihan = getJawabanDipilih()
            if (pilihan == null) {
                Toast.makeText(requireContext(), "Pilih salah satu jawaban!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            jawabanUser[indeksSoal] = pilihan

            if (indeksSoal < listSoal.size - 1) {
                indeksSoal++
                tampilkanSoal(listSoal)
            } else {
                selesaiKuis()
            }
        }
    }

    private fun tampilkanSoal(listSoal: List<SoalModel>) {
        val soal = listSoal[indeksSoal]
        Log.d("SOAL_LIST", "Tampilkan soal ke-${indeksSoal}: ${soal.pertanyaan}")

        tvNomorSoal.text = "Soal ${indeksSoal + 1}/${listSoal.size}"
        val jumlahBenarSementara = jawabanUser.subList(0, indeksSoal).withIndex().count { (i, jawab) ->
            jawab == listSoal[i].jawabanBenar
        }
        tvNilaiSementara.text = "Nilai: ${(jumlahBenarSementara * 100) / listSoal.size}"

        tvPertanyaan.text = soal.pertanyaan

        radioGroup.clearCheck()
        rbA.text = "A. ${soal.pilihanA}"
        rbB.text = "B. ${soal.pilihanB}"
        rbC.text = "C. ${soal.pilihanC}"
        rbD.text = "D. ${soal.pilihanD}"

        // Restore pilihan user jika ada
        when (jawabanUser[indeksSoal]) {
            "A" -> rbA.isChecked = true
            "B" -> rbB.isChecked = true
            "C" -> rbC.isChecked = true
            "D" -> rbD.isChecked = true
        }
    }

    private fun getJawabanDipilih(): String? {
        return when (radioGroup.checkedRadioButtonId) {
            R.id.jawaban_a -> "A"
            R.id.jawaban_b -> "B"
            R.id.jawaban_c -> "C"
            R.id.jawaban_d -> "D"
            else -> null
        }
    }

    private fun startTimer() {
        val totalMenit = durasi?.toIntOrNull() ?: 5
        sisaDetik = totalMenit * 60L

        timer = object : CountDownTimer(sisaDetik * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val detik = (millisUntilFinished / 1000) % 60
                val menit = (millisUntilFinished / 1000) / 60
                tvTimer.text = String.format("Sisa Waktu: %02d:%02d", menit, detik)
            }

            override fun onFinish() {
                Toast.makeText(requireContext(), "Waktu habis! Kuis disubmit otomatis.", Toast.LENGTH_SHORT).show()
                selesaiKuis()
            }
        }.start()
    }

    private fun selesaiKuis() {
        timer?.cancel()

        val listSoal = soalList ?: arrayListOf()
        for (i in jawabanUser.indices) {
            if (jawabanUser[i] == null) jawabanUser[i] = "-"
        }
        jumlahBenar = jawabanUser.withIndex().count { (i, jawab) ->
            jawab == listSoal[i].jawabanBenar
        }
        val nilai = (jumlahBenar * 100) / listSoal.size

        val bundle = Bundle().apply {
            putInt("topik_id", topikId)
            putString("namaTopik", namaTopik)
            putString("kelas", kelas)
            putInt("nilai", nilai)
            putInt("jawaban_benar", jumlahBenar)
            putInt("jumlah_soal", listSoal.size)
        }
        findNavController().navigate(
            R.id.action_mulaiSoalSiswaFragment_to_hasilKuisSiswaFragment,
            bundle,
            NavOptions.Builder()
                .setPopUpTo(R.id.berandaSiswaFragment, false) // hapus stack sampai beranda
                .build()
        )
    }

    override fun onDestroyView() {
        timer?.cancel()
        super.onDestroyView()
    }
}
