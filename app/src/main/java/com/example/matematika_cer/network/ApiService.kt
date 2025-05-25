package com.example.matematika_cer.network


import com.example.matematika_cer.guru.NilaiSiswaModel
import com.example.matematika_cer.guru.TopikNilaiModel
import com.example.matematika_cer.model.TopikBesertaSoalRequest
import com.example.matematika_cer.siswa.NilaiRiwayatModel
import com.example.matematika_cer.siswa.NilaiTerbaruModel
import com.example.matematika_cer.siswa.SiswaModel
import com.example.matematika_cer.siswa.TopikSiswaModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Path


interface ApiService {
    @POST("topik/beserta-soal")
    suspend fun kirimTopikBesertaSoal(
        @Body request: TopikBesertaSoalRequest
    ): Response<Void>

    @GET("topik/siswa")
    fun getDaftarTopik(): retrofit2.Call<List<TopikSiswaModel>>

    @GET("topik/siswa/{userId}/topik-selesai")
    fun getTopikSelesaiSiswa(
        @Path("userId") userId: Int
    ): Call<List<Long>>

    @GET("nilai/siswa/{userId}/terbaru")
    fun getNilaiTerbaru(@Path("userId") userId: Int): Call<NilaiTerbaruModel>

    @GET("nilai/siswa/{userId}/riwayat")
    fun getRiwayatNilai(@Path("userId") userId: Int): Call<List<NilaiRiwayatModel>>

    @GET("siswa/kelas/{kelas}")
    fun getSiswaByKelas(@Path("kelas") kelas: String): Call<List<SiswaModel>>

    @GET("nilai/guru/topik/{topikId}/kelas/{kelas}")
    fun getNilaiSiswaByTopikAndKelas(
        @Path("topikId") topikId: Long,
        @Path("kelas") kelas: String
    ): Call<List<NilaiSiswaModel>>

    @GET("topik/guru/kelas/{kelas}")
    fun getTopikNilaiByKelas(@Path("kelas") kelas: String): Call<List<TopikNilaiModel>>

    @GET("topik/guru/nilai/{topikId}")
    fun getNilaiSiswaByTopik(
        @Path("topikId") topikId: Long
    ): Call<List<NilaiSiswaModel>>


    @GET("topik/guru/topik/{topikId}/kelas/{kelas}/belum")
    fun getSiswaBelumMengerjakan(
        @Path("topikId") topikId: Long,
        @Path("kelas") kelas: String
    ): Call<List<SiswaModel>>



}
