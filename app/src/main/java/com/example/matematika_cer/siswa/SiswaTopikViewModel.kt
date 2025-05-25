package com.example.matematika_cer.siswa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matematika_cer.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SiswaTopikViewModel : ViewModel() {

    // LiveData daftar topik yang diambil dari backend
    private val _daftarTopikLiveData = MutableLiveData<List<TopikSiswaModel>>()
    val daftarTopikLiveData: LiveData<List<TopikSiswaModel>> get() = _daftarTopikLiveData

    // Ambil data topik dari backend (API)
    fun ambilDaftarTopik() {
        val apiService = ApiClient.getApiService()
        apiService.getDaftarTopik().enqueue(object : Callback<List<TopikSiswaModel>> {
            override fun onResponse(
                call: Call<List<TopikSiswaModel>>,
                response: Response<List<TopikSiswaModel>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    Log.d("SiswaTopikViewModel", "SUKSES ambil data topik: $data")
                    _daftarTopikLiveData.value = data
                } else {
                    // Tampilkan errorBody
                    val errorBody = response.errorBody()?.string()
                    Log.e(
                        "SiswaTopikViewModel",
                        "RESPONSE ERROR: code=${response.code()}, message=${response.message()}, errorBody=$errorBody"
                    )
                    _daftarTopikLiveData.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<TopikSiswaModel>>, t: Throwable) {
                Log.e("SiswaTopikViewModel", "GAGAL koneksi API: ${t.message}", t)
                _daftarTopikLiveData.value = emptyList()
            }
        })
    }

    private val _topikSelesaiLiveData = MutableLiveData<List<Long>>()
    val topikSelesaiLiveData: LiveData<List<Long>> get() = _topikSelesaiLiveData

    fun ambilTopikSelesaiSiswa(userId: Int) {
        val apiService = ApiClient.getApiService()
        apiService.getTopikSelesaiSiswa(userId).enqueue(object : Callback<List<Long>> {
            override fun onResponse(
                call: Call<List<Long>>,
                response: Response<List<Long>>
            ) {
                if (response.isSuccessful) {
                    _topikSelesaiLiveData.value = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<Long>>, t: Throwable) {
                _topikSelesaiLiveData.value = emptyList()
            }
        })
    }

}
