package com.example.matematika_cer.model

data class TopikBesertaSoalRequest(

    val namaTopik: String,
    val deskripsiTopik: String,
    val jumlahSoal: Int,
    val durasiMenit: Int,
    val tanggalMulai: String,
    val jamPelaksanaan: String,
    val tanggalSelesai: String,
    val nilaiPerSoal: Int,
    val pembuat: String,
    val kelas: String,
    val soalList: List<SoalRequest>
)

