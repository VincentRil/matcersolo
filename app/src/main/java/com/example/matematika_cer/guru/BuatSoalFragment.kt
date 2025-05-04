package com.example.matematika_cer.guru

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.siswa.SoalModel
import com.example.matematika_cer.viewmodel.SharedTopikViewModel
import java.util.UUID

class BuatSoalFragment : Fragment() {

    private lateinit var etPertanyaan: EditText
    private lateinit var etA: EditText
    private lateinit var etB: EditText
    private lateinit var etC: EditText
    private lateinit var etD: EditText
    private lateinit var rgJawaban: RadioGroup
    private lateinit var tvSoalKe: TextView
    private lateinit var btnSimpan: Button
    private lateinit var btnKembali: Button
    private lateinit var imgSoal: ImageView

    private var uriGambar: Uri? = null
    private lateinit var topik: TopikModel
    private val topikViewModel: SharedTopikViewModel by activityViewModels()

    private var jumlahSoal = 0
    private var indeksSoal = 1
    private val listSoalBaru = mutableListOf<SoalModel>()

    private val launcherGaleri = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uriGambar = it
            imgSoal.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buat_soal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etPertanyaan = view.findViewById(R.id.etPertanyaan)
        etA = view.findViewById(R.id.etA)
        etB = view.findViewById(R.id.etB)
        etC = view.findViewById(R.id.etC)
        etD = view.findViewById(R.id.etD)
        rgJawaban = view.findViewById(R.id.rgJawaban)
        tvSoalKe = view.findViewById(R.id.tvSoalKe)
        btnSimpan = view.findViewById(R.id.btnSoalBerikut)
        btnKembali = view.findViewById(R.id.btnKembali)
        imgSoal = view.findViewById(R.id.imgSoal)

        imgSoal.setOnClickListener {
            launcherGaleri.launch("image/*")
        }

        topik = arguments?.getParcelable("topik") ?: run {
            Toast.makeText(requireContext(), "Data topik tidak ditemukan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val mode = arguments?.getString("mode")
        val tambahan = arguments?.getInt("tambahan", 0) ?: 0

        jumlahSoal = when (mode) {
            "EDIT" -> tambahan
            else -> topik.jumlahSoal
        }

        updateJudulSoal()
        isiFormJikaAda()

        btnSimpan.setOnClickListener {
            if (validInput()) {
                if (indeksSoal <= listSoalBaru.size) {
                    listSoalBaru[indeksSoal - 1] = buatSoal()
                } else {
                    listSoalBaru.add(buatSoal())
                }

                if (indeksSoal < jumlahSoal) {
                    indeksSoal++
                    updateJudulSoal()
                    isiFormJikaAda()
                } else {
                    if (mode == "EDIT") {
                        topik.soalList.addAll(listSoalBaru)
                    } else {
                        topik.soalList.clear()
                        topik.soalList.addAll(listSoalBaru)
                    }
                    topik.jumlahSoal = topik.soalList.size
                    topikViewModel.updateTopikLama(topik)
                    Toast.makeText(requireContext(), "Semua soal berhasil disimpan", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            } else {
                Toast.makeText(requireContext(), "Lengkapi semua isian terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        btnKembali.setOnClickListener {
            if (indeksSoal > 1) {
                if (validInput()) {
                    if (indeksSoal <= listSoalBaru.size) {
                        listSoalBaru[indeksSoal - 1] = buatSoal()
                    } else {
                        listSoalBaru.add(buatSoal())
                    }
                }
                indeksSoal--
                updateJudulSoal()
                isiFormJikaAda()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Batalkan?")
                    .setMessage("Soal belum disimpan. Yakin ingin kembali?")
                    .setPositiveButton("Ya") { _, _ ->
                        findNavController().popBackStack()
                    }
                    .setNegativeButton("Tidak", null)
                    .show()
            }
        }
    }

    private fun updateJudulSoal() {
        tvSoalKe.text = "Soal $indeksSoal dari $jumlahSoal"
        btnSimpan.text = if (indeksSoal == jumlahSoal) "Simpan Semua" else "Soal Berikut"
    }

    private fun validInput(): Boolean {
        return etPertanyaan.text!!.isNotBlank() &&
                etA.text!!.isNotBlank() &&
                etB.text!!.isNotBlank() &&
                etC.text!!.isNotBlank() &&
                etD.text!!.isNotBlank() &&
                rgJawaban.checkedRadioButtonId != -1
    }

    private fun buatSoal(): SoalModel {
        val jawabanBenar = when (rgJawaban.checkedRadioButtonId) {
            R.id.rbA -> "A"
            R.id.rbB -> "B"
            R.id.rbC -> "C"
            R.id.rbD -> "D"
            else -> ""
        }

        return SoalModel(
            id = UUID.randomUUID().toString(),
            pertanyaan = etPertanyaan.text.toString(),
            pilihanA = etA.text.toString(),
            pilihanB = etB.text.toString(),
            pilihanC = etC.text.toString(),
            pilihanD = etD.text.toString(),
            jawabanBenar = jawabanBenar,
            gambar = uriGambar?.toString()
        )
    }

    private fun isiFormDariSoal(soal: SoalModel) {
        etPertanyaan.setText(soal.pertanyaan)
        etA.setText(soal.pilihanA)
        etB.setText(soal.pilihanB)
        etC.setText(soal.pilihanC)
        etD.setText(soal.pilihanD)

        when (soal.jawabanBenar) {
            "A" -> rgJawaban.check(R.id.rbA)
            "B" -> rgJawaban.check(R.id.rbB)
            "C" -> rgJawaban.check(R.id.rbC)
            "D" -> rgJawaban.check(R.id.rbD)
        }

        soal.gambar?.let {
            imgSoal.setImageURI(Uri.parse(it))
            uriGambar = Uri.parse(it)
        }
    }

    private fun isiFormJikaAda() {
        if (indeksSoal <= listSoalBaru.size) {
            isiFormDariSoal(listSoalBaru[indeksSoal - 1])
        } else {
            resetForm()
        }
    }

    private fun resetForm() {
        etPertanyaan.setText("")
        etA.setText("")
        etB.setText("")
        etC.setText("")
        etD.setText("")
        rgJawaban.clearCheck()
        imgSoal.setImageResource(R.drawable.ic_image_placeholder)
        uriGambar = null
    }
}
