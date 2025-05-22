package com.example.matematika_cer.siswa

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.matematika_cer.R

class DetailTopikSiswaFragment : Fragment() {

    private var namaTopik: String? = null
    private var kelas: String? = null
    private var pembuat: String? = null
    private var jumlahSoal: Int = 0
    private var durasi: String? = null
    private var soalList: ArrayList<SoalModel>? = null // Harus implement Parcelable

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_topik_siswa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ===== DUMMY DATA (hapus/replace dengan database saat sudah real) =====
        if (soalList == null || soalList!!.isEmpty()) {
            soalList = arrayListOf(
                SoalModel(
                    id = "1",
                    pertanyaan = "2 + 2 = ...",
                    pilihanA = "2",
                    pilihanB = "4",
                    pilihanC = "3",
                    pilihanD = "5",
                    jawabanBenar = "B"
                ),
                SoalModel(
                    id = "2",
                    pertanyaan = "5 - 3 = ...",
                    pilihanA = "2",
                    pilihanB = "3",
                    pilihanC = "4",
                    pilihanD = "1",
                    jawabanBenar = "A"
                ),
                SoalModel(
                    id = "3",
                    pertanyaan = "3 x 2 = ...",
                    pilihanA = "8",
                    pilihanB = "5",
                    pilihanC = "6",
                    pilihanD = "7",
                    jawabanBenar = "C"
                )
            )
            // Dummy juga untuk topik, kelas, dsb jika masih kosong
            if (namaTopik == null) namaTopik = "Matematika Dasar"
            if (kelas == null) kelas = "4"
            if (pembuat == null) pembuat = "Ibu Sali"
            if (durasi == null) durasi = "05:00"
            jumlahSoal = soalList!!.size
        }
        // ======================================================================

        // Set text ke view
        view.findViewById<TextView>(R.id.text_judul_topik).text = namaTopik ?: "-"
        view.findViewById<TextView>(R.id.text_kelas).text = "Kelas: ${kelas ?: "-"}"
        view.findViewById<TextView>(R.id.text_pembuat).text = "Dibuat oleh: ${pembuat ?: "-"}"
        view.findViewById<TextView>(R.id.text_jumlah_soal).text = "Jumlah soal: $jumlahSoal"
        view.findViewById<TextView>(R.id.text_durasi).text = "Durasi: ${durasi ?: "-"}"

        // Tombol Mulai — dengan dialog konfirmasi!
        view.findViewById<Button>(R.id.btn_mulai).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Yakin ingin mulai kuis? Setelah dimulai, waktu akan berjalan dan kamu tidak bisa kembali sebelum selesai.")
                .setPositiveButton("Mulai") { _, _ ->
                    val bundle = Bundle().apply {
                        putString("namaTopik", namaTopik)
                        putString("kelas", kelas)
                        putString("pembuat", pembuat)
                        putInt("jumlahSoal", jumlahSoal)
                        putString("durasi", durasi)
                        putParcelableArrayList("listSoal", soalList)
                    }
                    findNavController().navigate(
                        R.id.action_detailTopikSiswaFragment_to_mulaiSoalSiswaFragment,
                        bundle
                    )
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        // Tombol Kembali
        view.findViewById<Button>(R.id.btn_kembali).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
