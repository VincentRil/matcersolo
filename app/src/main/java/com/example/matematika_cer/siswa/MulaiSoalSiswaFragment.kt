package com.example.matematika_cer.siswa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
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

        // Sembunyikan tombol kembali di UI (atau disable)
        view.findViewById<Button>(R.id.btn_kembali_soal).visibility = View.INVISIBLE

        val listSoal = soalList ?: arrayListOf()
        if (listSoal.isEmpty()) {
            Toast.makeText(requireContext(), "Soal tidak ditemukan.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        jawabanUser = MutableList(listSoal.size) { null }
        indeksSoal = 0
        jumlahBenar = 0

        tampilkanSoal(view, listSoal)

        view.findViewById<Button>(R.id.btn_lanjut).setOnClickListener {
            val pilihan = getJawabanDipilih(view)
            if (pilihan == null) {
                Toast.makeText(requireContext(), "Pilih salah satu jawaban!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            jawabanUser[indeksSoal] = pilihan

            if (indeksSoal < listSoal.size - 1) {
                indeksSoal++
                tampilkanSoal(view, listSoal)
            } else {
                // Hitung jumlah benar
                jumlahBenar = jawabanUser.withIndex().count { (i, jawab) ->
                    jawab == listSoal[i].jawabanBenar
                }
                val nilai = (jumlahBenar * 100) / listSoal.size

                // Kirim data ke hasil
                val bundle = Bundle().apply {
                    putString("namaTopik", namaTopik)
                    putString("kelas", kelas)
                    putInt("nilai", nilai)
                    putInt("jawaban_benar", jumlahBenar)
                    putInt("jumlah_soal", listSoal.size)
                }
                findNavController().navigate(R.id.action_mulaiSoalSiswaFragment_to_hasilKuisSiswaFragment, bundle)
            }
        }
    }

    private fun tampilkanSoal(view: View, listSoal: List<SoalModel>) {
        val soal = listSoal[indeksSoal]
        view.findViewById<TextView>(R.id.teks_nomor_soal).text = "Soal ${indeksSoal + 1}/${listSoal.size}"
        // Update nilai sementara (jawaban benar sampai soal sekarang)
        val jumlahBenarSementara = jawabanUser.subList(0, indeksSoal).withIndex().count { (i, jawab) ->
            jawab == listSoal[i].jawabanBenar
        }
        view.findViewById<TextView>(R.id.teks_nilai_sementara).text =
            "Nilai: ${(jumlahBenarSementara * 100) / listSoal.size}"

        view.findViewById<TextView>(R.id.teks_pertanyaan).text = soal.pertanyaan

        val rbA = view.findViewById<RadioButton>(R.id.jawaban_a)
        val rbB = view.findViewById<RadioButton>(R.id.jawaban_b)
        val rbC = view.findViewById<RadioButton>(R.id.jawaban_c)
        val rbD = view.findViewById<RadioButton>(R.id.jawaban_d)
        val radioGroup = view.findViewById<RadioGroup>(R.id.opsi_jawaban)
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
            else -> { /* no-op */ }
        }
    }

    private fun getJawabanDipilih(view: View): String? {
        val id = view.findViewById<RadioGroup>(R.id.opsi_jawaban).checkedRadioButtonId
        return when (id) {
            R.id.jawaban_a -> "A"
            R.id.jawaban_b -> "B"
            R.id.jawaban_c -> "C"
            R.id.jawaban_d -> "D"
            else -> null
        }
    }
}
