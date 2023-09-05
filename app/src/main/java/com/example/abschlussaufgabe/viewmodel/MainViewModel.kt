package com.example.abschlussaufgabe.viewmodel

import RailStationApi
import android.Manifest
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
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
import java.util.Calendar

/**
 * ## Information
 * - Haupt-ViewModel der App: Verantwortlich für die Interaktion zwischen der Benutzeroberfläche und den Datenquellen.
 *
 * ## Funktionen
 * - [loadBfPhotoList]: Holt eine Liste von Fotos aus der API.
 * - [loadUserMaterialList]: Lädt Benutzermaterialien und sortiert sie.
 * - [updateMaterialLocation]: Aktualisiert den Ort des Materials in der Room-Datenbank.
 * - [checkMaterialId]): Überprüft das Vorhandensein einer Material-ID in der Liste.
 * - [playQrSound]: Spielt den QR-Sound ab.
 * - [playClickSound]: Spielt den Klick-Sound ab.
 * - [playActionSound]: Spielt den Action-Sound ab.
 * - [playLockedSound]: Spielt den Locked-Sound ab.
 * - [playLogInSound]: Spielt den Login-Sound ab.
 * - [showDatePicker]: Zeigt einen DatePickerDialog.
 * - [getGPSLocation]: Holt den letzten bekannten GPS-Standort des Geräts.
 * - [isValidEmail]: Überprüft, ob ein String ein gültiges E-Mail-Format hat.
 * - [isValidPassword]: Überprüft die Anforderungen eines Passworts.
 * - [internetCheck]: Überprüft, ob eine Internetverbindung vorhanden ist.
 * - [isInternetAvailable]: Bestimmt, ob eine aktive Internetverbindung besteht.
 */

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Globale Variable zum Halten des MediaPlayer-Objekts, das zur Wiedergabe von Tönen verwendet wird.
    private lateinit var mediaPlayer: MediaPlayer

    //Private variable für qr Scaner
    lateinit var codeScanner: CodeScanner

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
     * ## Information
     * Holt eine Liste von Fotos aus der API.
     */
    fun loadBfPhotoList() {
        viewModelScope.launch {
            apiRepository.getBfPhotoList()
        }
    }

    /**
     * ## Information
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

    /**
     * ## Information
     * ### Aktualisiert die Lokation eines bestimmten Lagermaterials und lädt anschließend die Liste der Lagermaterialien neu.
     *
     * Diese Methode nimmt die ID eines Lagermaterials und eine neue Lokations-ID als Parameter.
     * Sie ruft die `updateStorageMaterial` Methode des zugehörigen Repositories auf, um die Lokation des
     * Lagermaterials in der Datenbank zu aktualisieren. Nach der Aktualisierung wird die komplette Liste
     * der Lagermaterialien neu geladen und die Benutzer-Materialliste durch die Methode `loadUserMaterialList` aktualisiert.
     *
     * @param id Die ID des zu aktualisierenden Lagermaterials.
     * @param newLocationId Die neue Lokations-ID, die dem Lagermaterial zugewiesen werden soll.
     */
    fun updateMaterialLocation(id: Int, newLocationId: String) {
        viewModelScope.launch {
            repository.updateStorageMaterial(id, newLocationId)
            _storageMaterialDataList.value = repository.getAllStorageMaterialFromDataBank()
            loadUserMaterialList()
        }
    }

    /**
     * ## Information
     * ### Überprüft, ob ein Lagermaterial mit einer bestimmten ID in der `storageMaterialDataList` existiert.
     *
     * Diese Methode durchläuft alle Materialien in `storageMaterialDataList` und sucht nach einem
     * Lagermaterial mit einer bestimmten ID. Wenn ein solches Material gefunden wird, wird es zu einer
     * temporären Liste `checkIdList` hinzugefügt. Am Ende der Überprüfung wird überprüft, ob die `checkIdList`
     * leer ist. Wenn sie leer ist, wird eine Ausnahme geworfen, um anzuzeigen, dass kein Material mit der
     * gesuchten ID gefunden wurde.
     *
     * @param id Die zu überprüfende ID des Lagermaterials.
     * @throws Exception Wenn kein Material mit der angegebenen ID gefunden wird.
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

    /**
     * ## Information
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
     * ## Information
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
     * ## Information
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
     * ## Information
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
     * ## Information
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
     * ## Information
     * ### Zeigt einen DatePickerDialog an und setzt das ausgewählte Datum als Text in den übergebenen TextView.
     *
     * Diese Methode initialisiert und zeigt einen DatePickerDialog mit dem aktuellen Datum als Standardwert an.
     * Nachdem der Benutzer ein Datum ausgewählt hat, wird dieses Datum im Format "Tag.Monat.Jahr" in den
     * übergebenen TextView eingefügt.
     *
     * @param textView Der TextView, in den das ausgewählte Datum eingefügt werden soll.
     * @param context Der Kontext, in dem der DatePickerDialog angezeigt wird (üblicherweise die Aktivität oder der Fragment-Kontext).
     */
    fun showDatePicker(textView: TextView, context: Context) {
        val calendar = Calendar.getInstance()

        // Aktuelles Datum holen
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Erstellen und Anzeigen eines DatePickerDialogs
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.MyDatePickerDialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
                textView.text = selectedDate
            },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    /**
     * ## Information
     * ### Ermittelt den letzten bekannten GPS-Standort des Geräts und ruft einen Callback mit diesen Informationen auf.
     *
     * Die Methode versucht, den letzten bekannten Standort mithilfe des FusedLocationProviderClients zu erhalten.
     * Wenn die Standortberechtigung nicht gewährt wurde, wird sie beim Benutzer angefordert.
     * Nachdem der Standort erfolgreich abgerufen wurde, wird der bereitgestellte Callback `onLocationReceived` mit
     * den GPS-Koordinaten (Breitengrad und Längengrad) aufgerufen.
     *
     * @param activity Die FragmentActivity, von der aus diese Methode aufgerufen wird und die für den Kontext und die
     *                 Berechtigungsanfrage benötigt wird.
     * @param onLocationReceived Ein Callback, der aufgerufen wird, sobald die GPS-Position erfolgreich ermittelt wurde.
     *                           Er gibt eine Liste mit zwei Werten zurück: [Breitengrad, Längengrad].
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

    /**
     * ## Informationen
     * ### Überprüft, ob der gegebene String ein gültiges E-Mail-Format hat.
     *
     * Ein gültiges E-Mail-Format besteht aus:
     * 1. Ein oder mehrere Zeichen vor dem @-Zeichen, die Buchstaben, Zahlen, Punkte, Unterstriche, Prozent- oder Minuszeichen sein können.
     * 2. Ein @-Zeichen.
     * 3. Ein oder mehrere Zeichen nach dem @-Zeichen, die Buchstaben, Zahlen oder Punkte sein können.
     * 4. Ein Punkt.
     * 5. Zwei bis vier Zeichen am Ende, die den Domain-Teil darstellen.
     *
     * @param email Der zu überprüfende String.
     * @throws Exception Wenn der String nicht dem E-Mail-Format entspricht.
     */
    fun isValidEmail(email: String) {
        val emailRegex = "[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}".toRegex()
        if (!emailRegex.matches(email)) {
            throw Exception("Eingabe muss E-mail sein")
        }
    }

    /**
     * ## Information
     * ### Überprüft, ob das gegebene Passwort den Anforderungen entspricht.
     *
     * Die Anforderungen für das Passwort sind:
     * 1. Es muss mindestens 6 Zeichen lang sein.
     *
     * Diese Funktion kann erweitert werden, um zusätzliche Anforderungen, wie z.B. das Vorhandensein von Großbuchstaben, Zahlen oder Sonderzeichen zu überprüfen.
     *
     * @param password Das zu überprüfende Passwort.
     * @throws Exception Wenn das Passwort nicht den Anforderungen entspricht.
     */
    fun isValidPassword(password: String) {
        if (password.length < 6) {
            throw Exception("Passwort muss mindestens 6 Zeichen haben.")
        }
    }


    /**
     * ## Information
     * ### Diese Funktion prüft, ob eine Internetverbindung verfügbar ist.
     * Wenn keine Internetverbindung vorhanden ist, wird eine Exception ausgelöst.
     *
     * @param context Kontext, um Zugriff auf das System Connectivity Service zu erhalten.
     * @throws Exception wenn keine Internetverbindung vorhanden ist.
     */
    fun internetCheck(context: Context){
        if(!isInternetAvailable(context)){
            throw Exception("Kein Internet verbindung!")
        }
    }

    /**
     * ## Information
     * Überprüft, ob eine aktive Internetverbindung vorhanden ist.
     *
     * @param context Kontext, um Zugriff auf das System Connectivity Service zu erhalten.
     * @return `true`, wenn eine aktive Internetverbindung vorhanden ist, sonst `false`.
     *
     */
    private fun isInternetAvailable(context: Context): Boolean {
        // Hole sich den ConnectivityManager aus dem Kontext, um den Netzwerkstatus abzufragen.
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Überprüfe das aktive Netzwerk; wenn keines aktiv ist, geben Sie `false` zurück.
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        // Ermittel die Fähigkeiten des aktiven Netzwerks. Wenn keine Fähigkeiten ermittelt werden können, geben Sie `false` zurück.
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        // Überprüfen Sie, ob das aktive Netzwerk über eine der folgenden Verbindungsarten verfügt:
        return when {
            // Überprüfe, ob das Netzwerk über eine WLAN-Verbindung verfügt.
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            // Überprüfe, ob das Netzwerk über eine Mobilfunkverbindung verfügt.
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            // Überprüfe, ob das Netzwerk über eine Ethernet-Verbindung verfügt.
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

            // Wenn keine der obigen Verbindungen vorhanden ist, gebe `false` zurück.
            else -> false
        }
    }


    /**
     * ## Information
     * ### Initialisiert und startet den QR-Code-Scanner, um eine Material-ID zu scannen.
     *
     * Diese Funktion setzt den QR-Code-Scanner auf das bereitgestellte [view]-Objekt
     * und startet den Scanvorgang. Bei einem erfolgreichen Scan wird versucht,
     * die gescannte ID als Ganzzahl zu interpretieren und der bereitgestellte
     * [textView] aktualisiert. Wenn der Scan fehlschlägt oder der gescannte
     * Text nicht in eine Ganzzahl konvertiert werden kann, wird ein Fehlersound
     * abgespielt und ein Toast-Nachricht angezeigt.
     *
     * @param view Das View-Objekt, das den Scanner enthält.
     * @param activity Die zugehörige [FragmentActivity], in der diese Funktion aufgerufen wird.
     * @param context Der Kontext, der für verschiedene UI-Operationen verwendet wird.
     * @param textView Das TextView-Objekt, das aktualisiert wird, wenn ein gültiger Scan erkannt wird.
     *
     * @return Die gescannte Material-ID als Zeichenkette, oder "0" wenn der Scan fehlschlägt.
     */
    fun getQrCodeScan (view: View,activity: FragmentActivity, context: Context, textView: TextView): String{

        // Findet das CodeScannerView-Element in der bereitgestellten View
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)

        // Initialwert der gescannten ID
        var id = 0

        // Erstellt ein neues CodeScanner-Objekt mit der angegebenen Activity und dem scannerView
        codeScanner = CodeScanner(activity, scannerView)

        // Definiert, was passieren soll, sobald ein QR-Code erkannt wurde
        codeScanner.decodeCallback = DecodeCallback {
            // Führt den folgenden Code im Haupt-Thread aus, um UI-Operationen sicher durchzuführen
            activity.runOnUiThread {
                try {
                    // Versucht, den gescannten Text in eine Integer-Zahl zu konvertieren
                    val materialId = it.text.toInt()

                    // Spielt den QR-Code-Scan-Sound ab
                    playQrSound(context)

                    // Speichert die gescannte ID
                    id = materialId

                    // Aktualisiert den übergebenen TextView mit der gescannten ID
                    textView.text = Editable.Factory.getInstance().newEditable(materialId.toString())
                } catch (ex: Exception) {
                    // Wenn der gescannte Text keine gültige Zahl ist oder ein anderer Fehler auftritt, wird dieser Block ausgeführt
                    playLockedSound(context)
                    Toast.makeText(activity, "Die Id darf nur aus zahlen bestähen", Toast.LENGTH_LONG).show()
                }
            }
        }
        // Gibt die gescannte ID als String zurück, oder "0" wenn der Scan nicht erfolgreich war
        return id.toString()
    }


    /**
     * ## Information
     * ### Konfiguriert das Verhalten einer CheckBox in Bezug auf einen zugehörigen TextView.
     * Wenn die CheckBox aktiviert ist, wird der TextView sichtbar und sein Text wird zu
     * [userQualification] hinzugefügt. Wenn die CheckBox deaktiviert ist, wird der TextView
     * unsichtbar und der entsprechende Eintrag wird aus [userQualification] entfernt.
     *
     * @param checkBox Die zu konfigurierende CheckBox.
     * @param textView Der zugehörige TextView, dessen Sichtbarkeit und Textinhalt
     *                 basierend auf dem Zustand der CheckBox geändert wird.
     */
    fun setupCheckbox(checkBox: CheckBox, textView: TextView, userQualification: MutableMap<String, String>) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                textView.visibility = View.VISIBLE
                userQualification[checkBox.text.toString()] = textView.text.toString()
            } else {
                textView.visibility = View.GONE
                userQualification.remove(checkBox.text.toString())
            }
        }
    }

}