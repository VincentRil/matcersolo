package com.example.matematika_cer.model

data class SoalRequest(
    val pertanyaan: String,
    val pilihanA: String,
    val pilihanB: String,
    val pilihanC: String,
    val pilihanD: String,
    val jawabanBenar: String,
    val gambar: String? = null // bisa null jika tidak ada gambar
)
