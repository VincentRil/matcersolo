package com.example.matematika_cer.siswa

import com.google.gson.annotations.SerializedName

data class TopikSiswaModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("namaTopik")
    val namaTopik: String,

    @SerializedName("deskripsiTopik")
    val deskripsiTopik: String?,

    @SerializedName("kelas")
    val kelas: String?,

    @SerializedName("pembuat")
    val pembuat: String?,

    @SerializedName("jumlahSoal")
    val jumlahSoal: String?,

    @SerializedName("durasiMenit")
    val durasiMenit: String?,   // Dari backend bentuknya String

    @SerializedName("jamPelaksanaan")
    val jamPelaksanaan: String? = null,

    @SerializedName("tanggalMulai")
    val tanggalMulai: String? = null,

    @SerializedName("tanggalSelesai")
    val tanggalSelesai: String? = null,

    @SerializedName("soalList")
    val soalList: List<SoalModel>? = null

)
