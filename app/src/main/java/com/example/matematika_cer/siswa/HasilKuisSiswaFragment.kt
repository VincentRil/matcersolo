package com.example.matematika_cer.siswa

import HasilKuisApi
import RiwayatKuisModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsAnimation
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.matematika_cer.R
import com.example.matematika_cer.network.ApiClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.example.matematika_cer.model.HasilKuisRequest
import okhttp3.ResponseBody
import retrofit2.Callback

class HasilKuisSiswaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_hasil_kuis_siswa, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val nama = prefs.getString("nama", "Siswa") ?: "Siswa"
        val kelas = arguments?.getString("kelas") ?: "-"
        val nilai = arguments?.getInt("nilai") ?: 0
        val jumlahBenar = arguments?.getInt("jawaban_benar") ?: 0
        val jumlahSoal = arguments?.getInt("jumlah_soal") ?: 0
        val topikId = arguments?.getInt("topik_id") ?: -1
        val idUser = prefs.getInt("user_id", -1)

        if (idUser != -1) {
            kirimHasilKuisKeServer(idUser, nama, kelas, topikId, nilai, jumlahBenar, jumlahSoal)
        }

        view.findViewById<TextView>(R.id.text_nama_siswa).text = nama
        view.findViewById<TextView>(R.id.text_kelas).text = kelas
        view.findViewById<TextView>(R.id.text_nilai_akhir).text = nilai.toString()
        view.findViewById<TextView>(R.id.text_jawaban_benar).text = "$jumlahBenar dari $jumlahSoal"

        // Tombol kembali ke beranda
        view.findViewById<Button>(R.id.btn_kembali_beranda).setOnClickListener {
            findNavController().popBackStack(R.id.berandaSiswaFragment, false)
        }
    }

    private fun kirimHasilKuisKeServer(
        userId: Int, nama: String, kelas: String,
        topik: Int, nilai: Int, jawabanBenar: Int, jumlahSoal: Int
    ) {
        val waktuSubmit = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
        val request = HasilKuisRequest(
            user_id = userId,
            nama_siswa = nama,
            kelas = kelas,
            topik = topik,
            nilai = nilai,
            jawaban_benar = jawabanBenar,
            jumlah_soal = jumlahSoal,
            waktu_submit = waktuSubmit
        )
        val api = ApiClient.getService().create(HasilKuisApi::class.java)
        api.submitHasilKuis(request).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                // Sukses/Kegagalan bisa dihandle di sini
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle error
            }
        })
    }
}

