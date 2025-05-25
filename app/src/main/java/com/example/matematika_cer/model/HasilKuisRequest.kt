package com.example.matematika_cer.model

data class HasilKuisRequest(
    val user_id: Int,
    val nama_siswa: String,
    val kelas: String,
    val topik: Int,
    val nilai: Int,
    val jawaban_benar: Int,
    val jumlah_soal: Int,
    val waktu_submit: String
)
