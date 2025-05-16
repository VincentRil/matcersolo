package com.example.matematika_cer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.siswa.SoalModel

class SharedTopikViewModel : ViewModel() {

    // ⛔ Jangan langsung public, supaya tidak disimpan sebagai referensi luar
    private val _daftarTopikSementara = mutableListOf<TopikModel>()

    // ✅ Public hanya salinan (read-only)
    val daftarTopikSementara: List<TopikModel>
        get() = _daftarTopikSementara.toList()

    // ✅ Tambah topik
    fun tambahTopik(topik: TopikModel) {
        _daftarTopikSementara.add(topik)
    }

    // ✅ Hapus topik
    fun hapusTopik(topik: TopikModel) {
        _daftarTopikSementara.removeAll { it.namaTopik == topik.namaTopik }
    }

    // ✅ Ganti topik lama dengan topik baru (berdasarkan namaTopik)
    fun updateTopikLama(digantiDengan: TopikModel) {
        val index = _daftarTopikSementara.indexOfFirst { it.namaTopik == digantiDengan.namaTopik }
        if (index != -1) {
            _daftarTopikSementara[index] = digantiDengan
        }
    }

    // ✅ Ambil satu topik berdasarkan nama
    fun getTopikByNama(nama: String): TopikModel? {
        return _daftarTopikSementara.find { it.namaTopik == nama }
    }

    // ✅ Update soal dalam topik
    fun updateSoalDalamTopik(namaTopik: String, soalBaru: List<SoalModel>) {
        val topik = getTopikByNama(namaTopik)
        if (topik != null) {
            topik.soalList.clear()
            topik.soalList.addAll(soalBaru)
            topik.jumlahSoal = topik.soalList.size
        }
    }

    // ✅ Sinkron jumlah soal untuk semua topik
    fun sinkronSemuaJumlahSoal() {
        _daftarTopikSementara.forEach {
            it.jumlahSoal = it.soalList.size
        }
    }

    // ✅ Cek apakah topik sudah ada
    fun isTopikSudahAda(nama: String): Boolean {
        return _daftarTopikSementara.any { it.namaTopik.equals(nama, ignoreCase = true) }
    }
}
