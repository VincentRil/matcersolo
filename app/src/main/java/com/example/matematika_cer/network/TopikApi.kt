package com.example.matematika_cer.network

import com.example.matematika_cer.model.KirimTopikRequest
import com.example.matematika_cer.model.Topik
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TopikApi {

    @POST("topik/buat")
    fun buatTopik(@Body topik: Topik): Call<Topik>

    @POST("topik/kirim")
    fun kirimTopik(@Body request: KirimTopikRequest): Call<String>
}
