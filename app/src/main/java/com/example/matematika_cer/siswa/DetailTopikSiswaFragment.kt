package com.example.matematika_cer.siswa

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.matematika_cer.R
import java.text.SimpleDateFormat
import java.util.*

class DetailTopikSiswaFragment : Fragment() {

    private var namaTopik: String? = null
    private var kelas: String? = null
    private var pembuat: String? = null
    private var jumlahSoal: String? = null
    private var durasi: String? = null
    private var deskripsi: String? = null
    private var tanggalMulai: String? = null
    private var tanggalSelesai: String? = null
    private var jamPelaksanaan: String? = null
    private var soalList: ArrayList<SoalModel>? = null // Pastikan implement Parcelable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            namaTopik = it.getString("namaTopik")
            kelas = it.getString("kelas")
            pembuat = it.getString("pembuat")
            jumlahSoal = it.getString("jumlahSoal")
            durasi = it.getString("durasiMenit")
            deskripsi = it.getString("deskripsi")
            tanggalMulai = it.getString("tanggalMulai")
            tanggalSelesai = it.getString("tanggalSelesai")
            jamPelaksanaan = it.getString("jamPelaksanaan")
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

        // ============== DUMMY DATA (hapus jika sudah dari database) ==============
//        if (soalList == null || soalList!!.isEmpty()) {
//            soalList = arrayListOf(
//                SoalModel(
//                    id = "1",
//                    pertanyaan = "2 + 2 = ...",
//                    pilihanA = "2",
//                    pilihanB = "4",
//                    pilihanC = "3",
//                    pilihanD = "5",
//                    jawabanBenar = "B"
//                )
//            )
//            if (namaTopik == null) namaTopik = "Matematika Dasar"
//            if (kelas == null) kelas = "4"
//            if (pembuat == null) pembuat = "Ibu Sali"
//            if (durasi == null) durasi = "05"
//            if (deskripsi == null) deskripsi = "Deskripsi belum tersedia"
//            jumlahSoal = soalList!!.size
//            if (tanggalMulai == null) tanggalMulai = "25-05-2025"
//            if (tanggalSelesai == null) tanggalSelesai = "31-05-2025"
//            if (jamPelaksanaan == null) jamPelaksanaan = "08:00"
//        }
        // ========================================================================

        // Set text ke view
        view.findViewById<TextView>(R.id.text_judul_topik).text = namaTopik ?: "-"
        view.findViewById<TextView>(R.id.text_kelas).text = "Kelas: ${kelas ?: "-"}"
        view.findViewById<TextView>(R.id.text_pembuat).text = "Dibuat oleh: ${pembuat ?: "-"}"
        view.findViewById<TextView>(R.id.text_jumlah_soal).text = "Jumlah soal: $jumlahSoal"
        view.findViewById<TextView>(R.id.text_durasi).text = "Durasi: ${durasi ?: "-"} menit"

        // Tambahan data detail
        view.findViewById<TextView>(R.id.text_tanggal_mulai)?.text = "Tanggal Mulai: ${tanggalMulai ?: "-"}"
        view.findViewById<TextView>(R.id.text_tanggal_selesai)?.text = "Tanggal Selesai: ${tanggalSelesai ?: "-"}"
        view.findViewById<TextView>(R.id.text_jam_pelaksanaan)?.text = "Jam Pelaksanaan: ${jamPelaksanaan ?: "-"}"
        view.findViewById<TextView>(R.id.deskripsiTopik)?.text = deskripsi ?: "-"

        val btnMulai = view.findViewById<Button>(R.id.btn_mulai)
        val btnKembali = view.findViewById<Button>(R.id.btn_kembali)
        val topikId = arguments?.getInt("topik_id") ?: -1


        // Cek waktu untuk enable/disable tombol mulai
        val kuisBisaDimulai = isKuisBisaDimulai(tanggalMulai, tanggalSelesai, jamPelaksanaan)
        btnMulai.isEnabled = kuisBisaDimulai
        btnMulai.alpha = if (kuisBisaDimulai) 1f else 0.5f

        btnMulai.setOnClickListener {
            if (!isKuisBisaDimulai(tanggalMulai, tanggalSelesai, jamPelaksanaan)) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Belum Bisa/Melewati Waktu Kuis")
                    .setMessage("Kuis hanya dapat dikerjakan pada waktu yang sudah ditentukan.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Konfirmasi")
                    .setMessage("Yakin ingin mulai kuis? Setelah dimulai, waktu akan berjalan dan kamu tidak bisa kembali sebelum selesai.")
                    .setPositiveButton("Mulai") { _, _ ->
                        val bundle = Bundle().apply {
                            putInt("topik_id", topikId)
                            putString("namaTopik", namaTopik)
                            putString("kelas", kelas)
                            putString("pembuat", pembuat)
                            putString("jumlahSoal", jumlahSoal)
                            putString("durasi", durasi)
                            putParcelableArrayList("listSoal", soalList)
                            putString("tanggalMulai", tanggalMulai)
                            putString("tanggalSelesai", tanggalSelesai)
                            putString("jamPelaksanaan", jamPelaksanaan)
                            putString("deskripsi", deskripsi)
                        }
                        findNavController().navigate(
                            R.id.action_detailTopikSiswaFragment_to_mulaiSoalSiswaFragment,
                            bundle
                        )
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        }

        btnKembali.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun isKuisBisaDimulai(
        tanggalMulai: String?,
        tanggalSelesai: String?,
        jamPelaksanaan: String?
    ): Boolean {
        if (tanggalMulai == null || tanggalSelesai == null) return false
        try {
            // Sesuaikan format backend
            val dateFormatMulai = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            val dateFormatSelesai = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            val now = Calendar.getInstance().time

            val mulaiStr = if (jamPelaksanaan != null)
                "$tanggalMulai $jamPelaksanaan"
            else
                "$tanggalMulai 00:00"
            val selesaiStr = "$tanggalSelesai 23:59"

            val mulai = dateFormatMulai.parse(mulaiStr)
            val selesai = dateFormatSelesai.parse(selesaiStr)
            Log.d("WAKTU_KUIS", "Mulai: $mulaiStr, Selesai: $selesaiStr, Sekarang: $now")

            return (mulai != null && selesai != null && now.after(mulai) && now.before(selesai))
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
