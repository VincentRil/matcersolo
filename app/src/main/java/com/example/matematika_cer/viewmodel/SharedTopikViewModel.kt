package com.example.matematika_cer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.siswa.SoalModel

class SharedTopikViewModel : ViewModel() {

    val daftarTopikSementara = mutableListOf<TopikModel>()

    // ✅ Hapus topik dari list
    fun hapusTopik(topik: TopikModel) {
        daftarTopikSementara.removeAll { it.namaTopik == topik.namaTopik }
    }

    // ✅ Tambah topik
    fun tambahTopik(topik: TopikModel) {
        daftarTopikSementara.add(topik)
    }

    // ✅ Ganti topik lama dengan topik baru (berdasarkan namaTopik)
    fun updateTopikLama(digantiDengan: TopikModel) {
        val index = daftarTopikSementara.indexOfFirst { it.namaTopik == digantiDengan.namaTopik }
        if (index != -1) {
            daftarTopikSementara[index] = digantiDengan
        }
    }

    // ✅ Ambil satu topik berdasarkan nama
    fun getTopikByNama(nama: String): TopikModel? {
        return daftarTopikSementara.find { it.namaTopik == nama }
    }

    // ✅ Update soal pada satu topik dan sinkron jumlah soal
    fun updateSoalDalamTopik(namaTopik: String, soalBaru: List<SoalModel>) {
        val topik = getTopikByNama(namaTopik)
        if (topik != null) {
            topik.soalList.clear()
            topik.soalList.addAll(soalBaru)
            topik.jumlahSoal = topik.soalList.size // ✅ sinkron jumlah soal
        }
    }

    // ✅ Sinkron jumlah soal untuk semua topik (opsional)
    fun sinkronSemuaJumlahSoal() {
        daftarTopikSementara.forEach {
            it.jumlahSoal = it.soalList.size
        }
    }
}