import com.example.abschlussaufgabe.data.model.RailStationsPhotoModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET




const val BASE_URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/api.railway-stations.org/"

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    @GET("stations")
    suspend fun getStationList(): RailStationsPhotoModel

}

object RailStationApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}


