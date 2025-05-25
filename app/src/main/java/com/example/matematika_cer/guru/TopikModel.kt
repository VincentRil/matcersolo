package com.example.matematika_cer.model

import com.example.matematika_cer.siswa.SoalModel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopikModel(
    val id: Int,
    var namaTopik: String,
    val deskripsiTopik: String,
    var jumlahSoal: Int,
    val durasiMenit: Int,
    val tanggalMulai: String,
    val tanggalSelesai: String,
    val jamPelaksanaan: String? = null,
    val pembuat: String = "",
    val kelas: String = "",
    val nilaiPerSoal: Int = 10,
    val soalList: MutableList<SoalModel> = mutableListOf(),
    val totalPeserta: Int = 0,
    val jumlahMenjawab: Int = 0 // <--- TAMBAHKAN INI kalau belum ada!
) : Parcelable

