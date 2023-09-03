// Import-Anweisungen für die benötigten Klassen und Module
import com.example.abschlussaufgabe.data.model.RailStationsPhotoModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Die Basis-URL für die API-Anfragen
const val BASE_URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/api.railway-stations.org/"

// Ein Moshi-Objekt wird erstellt, um JSON-Daten zu serialisieren und deserialisieren.
private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()) // Fügt den KotlinJsonAdapterFactory hinzu
    .build()

// Ein Retrofit-Objekt wird erstellt, um Netzwerkanfragen durchzuführen.
private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi)) // Verwendet Moshi zum Konvertieren von JSON
    .baseUrl(BASE_URL) // Basis-URL für API-Anfragen
    .build()

// Das ApiService-Interface definiert die verfügbaren API-Endpunkte.
interface ApiService {

    // Die Annotation @GET gibt den Endpunkt an und die Funktion gibt den erwarteten Datentyp zurück.
    @GET("stations")
    suspend fun getStationList(): List<RailStationsPhotoModel>
}

// Das RailStationApi-Objekt bietet eine Singleton-Instanz des ApiService.
object RailStationApi {
    // Lazy wird verwendet, um sicherzustellen, dass die Initialisierung erst bei Bedarf erfolgt.
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}