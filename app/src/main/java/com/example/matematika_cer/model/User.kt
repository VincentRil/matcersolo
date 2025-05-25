package com.example.matematika_cer.model

data class User(
    val id: Int? = null,
    val namaLengkap: String?,
    val username: String,
    val password: String,
    val role: String?,
    val kelas: String?
)
