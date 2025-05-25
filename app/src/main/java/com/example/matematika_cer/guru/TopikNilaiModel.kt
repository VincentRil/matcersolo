package com.example.matematika_cer.guru

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopikNilaiModel(
    val id: Long,
    val namaTopik: String,
    val jumlahSoal: Int,
    val durasiMenit: Int,
    val tanggalMulai: String,
    val jamPelaksanaan: String?,
    val totalPeserta: Int,
    val jumlahMenjawab: Int
) : Parcelable
