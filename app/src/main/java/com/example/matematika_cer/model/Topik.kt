package com.example.matematika_cer.model

import java.io.Serializable

data class Topik(
    val id: Long? = null,
    val namaTopik: String,
    val deskripsiTopik: String,
    val jumlahSoal: String,
    val durasiMenit: String
) : Serializable
