package com.example.matematika_cer.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matematika_cer.model.TopikBesertaSoalRequest
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.network.ApiClient
import com.example.matematika_cer.network.ApiService
import com.example.matematika_cer.siswa.SoalModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class SharedTopikViewModel : ViewModel() {

    // Private mutable list sebagai sumber data utama
    private var _daftarTopikSementara: MutableList<TopikModel> = mutableListOf()

    // Getter public, hanya bisa dibaca sebagai List<TopikModel>
    val daftarTopikSementara: List<TopikModel>
        get() = _daftarTopikSementara

    // Generate ID baru (otomatis)
    fun generateTopikId(): Int {
        return (_daftarTopikSementara.maxOfOrNull { it.id } ?: 0) + 1
    }

    // Tambah topik ke list
    fun tambahTopik(topik: TopikModel) {
        _daftarTopikSementara.add(topik)
    }

    // Hapus topik dari list (berdasarkan nama)
    fun hapusTopik(topik: TopikModel) {
        _daftarTopikSementara.removeAll { it.namaTopik == topik.namaTopik }
    }

    // Update topik lama (replace by namaTopik)
    fun updateTopikLama(digantiDengan: TopikModel) {
        val index = _daftarTopikSementara.indexOfFirst { it.namaTopik == digantiDengan.namaTopik }
        if (index != -1) {
            _daftarTopikSementara[index] = digantiDengan
        }
    }

    // Simpan ke SharedPreferences (gunakan _daftarTopikSementara)
    fun simpanTopikKePrefs(context: Context) {
        val prefs = context.getSharedPreferences("topik_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(_daftarTopikSementara)
        prefs.edit().putString("list_topik", json).apply()
    }

    // Load dari SharedPreferences (langsung _assign_ ke _daftarTopikSementara)
    fun loadTopikDariPrefs(context: Context) {
        val prefs = context.getSharedPreferences("topik_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("list_topik", "[]")
        val type = object : TypeToken<MutableList<TopikModel>>() {}.type
        _daftarTopikSementara = gson.fromJson(json, type) ?: mutableListOf()
    }

    // Cari satu topik berdasarkan nama
    fun getTopikByNama(nama: String): TopikModel? {
        return _daftarTopikSementara.find { it.namaTopik == nama }
    }

    // Update soal dalam topik tertentu
    fun updateSoalDalamTopik(namaTopik: String, soalBaru: List<SoalModel>) {
        val topik = getTopikByNama(namaTopik)
        if (topik != null) {
            topik.soalList.clear()
            topik.soalList.addAll(soalBaru)
            topik.jumlahSoal = topik.soalList.size
        }
    }

    // Sinkron jumlah soal semua topik
    fun sinkronSemuaJumlahSoal() {
        _daftarTopikSementara.forEach {
            it.jumlahSoal = it.soalList.size
        }
    }

    // Cek apakah nama topik sudah ada (ignore case)
    fun isTopikSudahAda(nama: String): Boolean {
        return _daftarTopikSementara.any { it.namaTopik.equals(nama, ignoreCase = true) }
    }

    // Kirim ke backend (jika diperlukan)
    fun kirimTopikKeBackend(request: TopikBesertaSoalRequest, onResult: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val apiService = ApiClient.getService().create(ApiService::class.java)
                val response = apiService.kirimTopikBesertaSoal(request)
                if (response.isSuccessful) {
                    onResult?.invoke(true)
                    Log.d("SharedTopikViewModel", "Sukses kirim topik ke backend!")
                } else {
                    onResult?.invoke(false)
                    Log.e("SharedTopikViewModel", "Gagal kirim: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult?.invoke(false)
                Log.e("SharedTopikViewModel", "Error kirim topik: ${e.message}")
            }
        }
    }
}
