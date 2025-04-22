import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SoalModel(
    val id: String,                // penting untuk tracking, cocokkan jawaban
    val pertanyaan: String,
    val pilihanA: String,
    val pilihanB: String,
    val pilihanC: String,
    val pilihanD: String,
    val jawabanBenar: String,      // "A", "B", "C", atau "D"
    val gambar: String? = null     // opsional
) : Parcelable
