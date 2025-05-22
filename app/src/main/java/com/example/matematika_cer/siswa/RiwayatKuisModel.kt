import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RiwayatKuisModel(
    val namaTopik: String,
    val tanggal: String,         // simpan waktu main kuis
    val nilai: Int,
    val jumlahBenar: Int,
    val jumlahSoal: Int
) : Parcelable
