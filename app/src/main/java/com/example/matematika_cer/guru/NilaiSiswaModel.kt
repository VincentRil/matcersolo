package com.example.matematika_cer.guru

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NilaiSiswaModel(
    val namaSiswa: String,
    val nilai: Int
) : Parcelable
