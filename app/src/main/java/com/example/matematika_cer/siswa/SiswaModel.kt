package com.example.matematika_cer.siswa

data class SiswaModel(
    val id: Long,
    val namaLengkap: String,
    val kelas: String,
    val username: String, // Tambahkan ini
    val password: String
)
