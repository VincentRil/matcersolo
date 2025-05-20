package com.example.matematika_cer.model

import com.example.matematika_cer.siswa.SoalModel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopikModel(
    val id: Int,
    var namaTopik: String,
    val deskripsiTopik: String,
    val durasi: String,
    var jumlahSoal: Int,
    val tanggal: String,
    val isAktif: Boolean? = null,
    val jumlahMenjawab: Int? = null,
    val totalPeserta: Int? = null,
    val soalList: MutableList<SoalModel> = mutableListOf()
) : Parcelable
