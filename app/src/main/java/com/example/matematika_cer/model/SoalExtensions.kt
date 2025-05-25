package com.example.matematika_cer.model

import com.example.matematika_cer.model.SoalRequest
import com.example.matematika_cer.siswa.SoalModel

fun SoalModel.toSoalRequest(): SoalRequest {
    return SoalRequest(
        pertanyaan = this.pertanyaan.trim(),
        pilihanA = this.pilihanA.trim(),
        pilihanB = this.pilihanB.trim(),
        pilihanC = this.pilihanC.trim(),
        pilihanD = this.pilihanD.trim(),
        jawabanBenar = this.jawabanBenar.trim(),
        gambar = this.gambar?.trim() // null jika tidak ada gambar
    )
}
