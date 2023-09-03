package com.example.abschlussaufgabe.viewmodel

import RailStationApi
import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.media.MediaPlayer
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.ApiRepository
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.data.local.StorageMaterialDatabase
import com.example.abschlussaufgabe.data.local.getStorageMaterialDatabase
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.example.abschlussaufgabe.data.model.WorkRunModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

/**
 * Haupt-ViewModel der App, verantwortlich für die Interaktion zwischen der Benutzeroberfläche und den Datenquellen.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Instanz der Materialdatenbank.
    var storageMaterialDatabase: StorageMaterialDatabase = getStorageMaterialDatabase(application)

    // Haupt-Repository für Datenbankinteraktionen.
    private val repository = AppRepository(storageMaterialDatabase)

    // Repository für den Umgang mit API-Aufrufen.
    private val apiRepository = ApiRepository(RailStationApi)

    // Dienst zur Standortbestimmung.
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Grundliste der Lagermaterialien.
    private val storageMaterialList = repository.storageMaterialGroundList

    // LiveData für Fotos von der API.
    val bfPhotoList = apiRepository.bfPhotoList

    // Aktuelle Benutzerdaten.
    var userData: UserTestDataModel = repository.user

    // LiveData-Liste für die Speicherung von Materialdaten.
    private var _storageMaterialDataList = MutableLiveData<List<StorageMaterialModel>>()
    private val storageMaterialDataList: LiveData<List<StorageMaterialModel>>
        get() = _storageMaterialDataList

    // LiveData-Listen für Benutzer-Materialien.
    private val _userMaterialList = MutableLiveData<List<StorageMaterialModel>>()
    val userMaterialList: LiveData<List<StorageMaterialModel>>
        get() = _userMaterialList
    private val _userPsaMaterialList = MutableLiveData<List<StorageMaterialModel>>()
    val userPsaMaterialList: LiveData<List<StorageMaterialModel>>
        get() = _userPsaMaterialList

    // LiveData für GPS-Informationen.
    private var _gpsLiveData = MutableLiveData<WorkRunModel>()
    val gpsLiveData: LiveData<WorkRunModel>
        get() = _gpsLiveData

    init {
        // Initialisierung der Daten beim Start des ViewModels.
        viewModelScope.launch {
            FireStoreViewModel().currentUserStore
            repository.insertAll(storageMaterialList.value!!)
            _storageMaterialDataList.value = repository.getAllStorageMaterialFromDataBank()
            loadUserMaterialList()
            loadBfPhotoList()
        }
    }

    /**
     * Holt eine Liste von Fotos aus der API.
     */
    fun loadBfPhotoList() {
        viewModelScope.launch {
            apiRepository.getBfPhotoList()
        }
    }

    /**
     * Lädt Benutzermaterialien und sortiert sie basierend auf Benutzer-IDs und Material-IDs.
     * Es werden zwei separate Listen erstellt:
     * - Eine Liste für PSA-Materialien (Persönliche Schutzausrüstung oder ähnliches, angenommen aufgrund der Material-ID-Prüfung)
     * - Eine allgemeine Materialliste.
     *
     * Die erstellten Listen werden in LiveData verpackt, um von der Benutzeroberfläche beobachtet zu werden.
     */
    fun loadUserMaterialList() {
        viewModelScope.launch {

            // Erstelle leere Listen für die Materialien.
            val materialList = mutableListOf<StorageMaterialModel>()
            val psaMaterialList = mutableListOf<StorageMaterialModel>()

            // Prüfen, ob es Materialdaten in _storageMaterialDataList gibt.
            _storageMaterialDataList.value?.let { materials ->

                // Logge die Anzahl der Materialien für Debugging-Zwecke.
                Log.e("Log4", materials.size.toString())

                for (material in materials) {

                    // Überprüfe und sortiere Materialien basierend auf ihrer locationId und materialId.
                    when {
                        material.locationId == userData.userUid && material.materialId > 100000 ->
                            psaMaterialList.add(material)

                        material.locationId == userData.userUid && material.materialId < 100000 ->
                            materialList.add(material)
                    }
                }
            }

            // Verpacke die Listen in LiveData, damit die UI sie beobachten kann.
            _userPsaMaterialList.value = psaMaterialList
            _userMaterialList.value = materialList

        }
    }

    //mit der funktion überschreibe ich die daten in storage_material_table RoomData
    fun updateMaterialLocation(id: Int, newLocationId: String) {
        viewModelScope.launch {
            repository.updateStorageMaterial(id, newLocationId)
            _storageMaterialDataList.value = repository.getAllStorageMaterialFromDataBank()
            loadUserMaterialList()
        }
    }


    /**
     * Überprüft, ob eine bestimmte Material-ID in der storageMaterialDataList vorhanden ist.
     *
     * @param id Die zu überprüfende Material-ID.
     * @throws Exception Wenn die Material-ID nicht in der Liste gefunden wird.
     */
    fun checkMaterialId(id: Int) {

        // Liste zur temporären Speicherung von Materialien, die die gesuchte ID haben.
        val checkIdList = mutableListOf<StorageMaterialModel>()

        // Durchlaufe alle Materialien in storageMaterialDataList.
        for (material in storageMaterialDataList.value!!) {

            // Wenn die ID des aktuellen Materials der gesuchten ID entspricht,
            // füge das Material zur checkIdList hinzu.
            if (material.materialId == id) {
                checkIdList.add(material)
            }
        }

        // Wenn die checkIdList leer ist, bedeutet dies, dass kein Material mit der gesuchten ID gefunden wurde.
        // In diesem Fall wird eine Ausnahme ausgelöst.
        if (checkIdList.isEmpty()) {
            throw Exception("ID befindet sich nicht in der liste")
        }
    }


    // Globale Variable zum Halten des MediaPlayer-Objekts, das zur Wiedergabe von Tönen verwendet wird.
    private lateinit var mediaPlayer: MediaPlayer

    /**
     * Spielt den QR-Sound ab.
     * @param context Kontext, der benötigt wird, um auf die Ressourcen zuzugreifen und den MediaPlayer zu initialisieren.
     */
    fun playQrSound(context: Context) {
        // Erstellt und initialisiert den MediaPlayer mit dem QR-Sound.
        mediaPlayer = MediaPlayer.create(context, R.raw.qr_sound)

        // Setzt die Lautstärke für den MediaPlayer.
        mediaPlayer.setVolume(1f, 1f)

        // Startet die Wiedergabe.
        mediaPlayer.start()
    }

    /**
     * Spielt den Klick-Sound ab.
     * @param context Kontext, der benötigt wird, um auf die Ressourcen zuzugreifen und den MediaPlayer zu initialisieren.
     */
    fun playClickSound(context: Context) {
        // Erstellt und initialisiert den MediaPlayer mit dem Klick-Sound.
        mediaPlayer = MediaPlayer.create(context, R.raw.click_sound)

        // Setzt die Lautstärke für den MediaPlayer.
        mediaPlayer.setVolume(1f, 1f)

        // Startet die Wiedergabe.
        mediaPlayer.start()
    }

    /**
     * Spielt den Action-Sound ab.
     * @param context Kontext, der benötigt wird, um auf die Ressourcen zuzugreifen und den MediaPlayer zu initialisieren.
     */
    fun playActionSound(context: Context) {
        // Erstellt und initialisiert den MediaPlayer mit dem Action-Sound.
        mediaPlayer = MediaPlayer.create(context, R.raw.action_sound_1)

        // Setzt die Lautstärke für den MediaPlayer.
        mediaPlayer.setVolume(1f, 1f)

        // Startet die Wiedergabe.
        mediaPlayer.start()
    }

    /**
     * Spielt den Locked-Sound ab.
     * @param context Kontext, der benötigt wird, um auf die Ressourcen zuzugreifen und den MediaPlayer zu initialisieren.
     */
    fun playLockedSound(context: Context) {
        // Erstellt und initialisiert den MediaPlayer mit dem Locked-Sound.
        mediaPlayer = MediaPlayer.create(context, R.raw.locked_sound)

        // Setzt die Lautstärke für den MediaPlayer.
        mediaPlayer.setVolume(1f, 1f)

        // Startet die Wiedergabe.
        mediaPlayer.start()
    }

    /**
     * Spielt den Login-Sound ab.
     * @param context Kontext, der benötigt wird, um auf die Ressourcen zuzugreifen und den MediaPlayer zu initialisieren.
     */
    fun playLogInSound(context: Context) {
        // Erstellt und initialisiert den MediaPlayer mit dem Login-Sound.
        mediaPlayer = MediaPlayer.create(context, R.raw.log_in_sound)

        // Setzt die Lautstärke für den MediaPlayer.
        mediaPlayer.setVolume(1f, 1f)

        // Startet die Wiedergabe.
        mediaPlayer.start()
    }


    /**
     * Holt den letzten bekannten GPS-Standort des Geräts. Wenn die Standortberechtigung nicht gewährt wurde,
     * wird diese beim Benutzer angefordert.
     *
     * @param activity Die FragmentActivity-Instanz, in der diese Funktion aufgerufen wird. Wird verwendet,
     *                 um auf Ressourcen und Dienste zuzugreifen.
     * @param onLocationReceived Ein Callback, der aufgerufen wird, sobald der Standort erfolgreich abgerufen wurde.
     *                           Es gibt eine Liste zurück mit zwei Double-Werten: Breitengrad und Längengrad.
     */
    fun getGPSLocation(activity: FragmentActivity, onLocationReceived: (List<Double>) -> Unit) {

        // Konstante für die Anforderung der Standortberechtigung
        val REQUEST_LOCATION_PERMISSION_CODE = 0

        // Instanz des FusedLocationProviderClient, der verwendet wird, um auf den letzten bekannten Standort zuzugreifen
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        // Überprüfung, ob die Standortberechtigung bereits gewährt wurde
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Wenn die Berechtigung gewährt wurde, holen Sie den letzten bekannten Standort
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
                location?.let {
                    // Extrahiert Breitengrad und Längengrad aus dem Standortobjekt
                    val latitude = it.latitude
                    val longitude = it.longitude

                    // Ruft den Callback mit den ermittelten Koordinaten auf
                    onLocationReceived(listOf(latitude, longitude))
                }
            }
        } else {
            // Wenn die Berechtigung nicht gewährt wurde, fordern Sie sie vom Benutzer an
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_CODE
            )
        }
    }

}