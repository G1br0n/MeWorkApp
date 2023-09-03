package com.example.abschlussaufgabe.data

// Externe Abhängigkeiten, die in dieser Klasse verwendet werden.
import RailStationApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussaufgabe.data.model.RailStationsPhotoModel

/**
 * ## Information
 * `ApiRepository` - Klasse, die Dateninteraktionen im Zusammenhang mit RailStationApi verwaltet.
 * ###
 * ## Funktionen
 * - [getBfPhotoList]: Verwendet den RailStationApi Service, um eine Liste von Bahnhofsfotos abzurufen und speichert ein zufälliges Foto davon.
 *
 * @param api Eine Instanz von RailStationApi, die Methoden zum Zugriff auf Bahnhof-bezogene APIs bereitstellt.
 */
class ApiRepository(private val api: RailStationApi) {

    /**
     * ## Information
     * ### Eine private Mutable LiveData, die ein Modell von RailStationsPhotoModel speichert.
     * ### Sie wird intern innerhalb der Klasse verwendet, um Daten zu aktualisieren.
     */
    private var _bfPhotoList = MutableLiveData<RailStationsPhotoModel>()

    /**
     * ## Information
     * ### Eine öffentliche LiveData, die Daten vom Typ RailStationsPhotoModel bereitstellt.
     * ### Externe Klassen können diese LiveData beobachten, um auf Datenänderungen zu reagieren.
     */
    val bfPhotoList: LiveData<RailStationsPhotoModel>
        get() = _bfPhotoList

    /**
     * ## Information
     * ### Diese Funktion verwendet den RailStationApi Service, um eine Liste von Bahnhofsfotos abzurufen.
     * ### Ein zufälliges Foto aus dieser Liste wird anschließend in _bfPhotoList gespeichert.
     * ### Falls während des Abrufvorgangs ein Fehler auftritt, wird ein Fehlerprotokoll generiert.
     */
    suspend fun getBfPhotoList() {
        try {
            // Abruf der Bahnhofsfotoliste vom Service.
            val bfPhotoList = api.retrofitService.getStationList()

            // Ein zufälliges Foto aus der Liste wird in _bfPhotoList gespeichert.
            _bfPhotoList.value = bfPhotoList.random()
        } catch (ex: Exception) {
            // Im Fehlerfall wird eine Fehlermeldung protokolliert.
            // Log.e("Api Repository", "ApiRepository: ${ex.message}")
        }
    }
}
