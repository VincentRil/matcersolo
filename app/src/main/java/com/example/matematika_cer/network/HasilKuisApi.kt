import com.example.matematika_cer.model.HasilKuisRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HasilKuisApi {
    @POST("/api/hasilkuis")
    fun submitHasilKuis(@Body hasilKuis: HasilKuisRequest): Call<Void>
}
