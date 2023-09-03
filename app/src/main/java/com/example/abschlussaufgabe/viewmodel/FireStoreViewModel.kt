package com.example.abschlussaufgabe.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val TAG = "fireStoreTest"

/**
 * ## Information
 * ViewModel-Klasse zur Interaktion mit Firebase Firestore.
 * Diese Klasse dient als Schnittstelle für alle Firestore-spezifischen Vorgänge.
 *
 * ###
 * ## Funktionen
 * - [addNewUserWorkTimeListStore] - Neue Arbeitszeitliste für den Benutzer im Firestore erstellen.
 * - [addNewUserDataStore] - Neue Benutzerdaten im Firestore speichern.
 * - [saveUserDataStore] - Aktualisierte Benutzerdaten im Firestore speichern.
 * - [saveUserWorkTimeLogStore] - Arbeitszeitliste eines Benutzers im Firestore speichern.
 * - [getUserDataStore] - Benutzerdaten eines bestimmten Benutzers aus dem Firestore abrufen.
 * - [getWorkTimeListStore] - Arbeitszeitliste eines bestimmten Benutzers aus dem Firestore abrufen.
 *
 */
class FireStoreViewModel : ViewModel() {

    // Initialisierung von Benutzerdatenmodell
    var userData: UserTestDataModel = UserTestDataModel(
        "",
        "",
        "",
        "",
        "",
        0,
        mapOf<String, String>(),
        false,
        false,
        mutableMapOf<String, String>()
    )

    // MutableLiveData für die Arbeitszeitliste des Benutzers
    var _currentTimWorkList = MutableLiveData<MutableList<String>>()
    val currentTimWorkList: LiveData<MutableList<String>>
        get() = _currentTimWorkList

    // MutableLiveData zur Speicherung und Überwachung des aktuellen Benutzers
    var _currentUserStore = MutableLiveData<UserTestDataModel>()
    val currentUserStore: LiveData<UserTestDataModel>
        get() = _currentUserStore

    // Firebase Firestore Instanz erhalten
    private val db = Firebase.firestore

    /**
     * Funktion, um eine neue Arbeitszeitliste für den Benutzer im Firestore zu erstellen.
     *
     * @param newUserUid Benutzer-UID.
     * @param list Arbeitszeitliste (Standardwert ist eine leere Liste).
     */
    fun addNewUserWorkTimeListStore(newUserUid: String, list: List<String> = listOf()) {
        db.collection("workTimeLog").document(newUserUid).set(
            "userWorkTimeLogList" to list
        )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                throw Exception("Error writing document")
            }
    }

    /**
     * Funktion, um neue Benutzerdaten im Firestore zu speichern.
     *
     * @param userUid Benutzer-UID.
     * @param email E-Mail des Benutzers.
     * @param password Passwort des Benutzers.
     * @param firstName Vorname des Benutzers.
     * @param lastName Nachname des Benutzers.
     * @param baNumber BA-Nummer des Benutzers.
     * @param userQualification Qualifikation des Benutzers.
     */
    fun addNewUserDataStore(
        userUid: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        baNumber: Int = 0,
        userQualification: Map<String, String>
    ) {
        val userData = mapOf(
            "userUid" to userUid,
            "email" to email,
            "password" to password,
            "firstName" to firstName,
            "lastName" to lastName,
            "baNumber" to baNumber,
            "userQualification" to userQualification,
            "carStatus" to false,
            "timerStatus" to false,
            "timerMap" to mapOf(
                "startYear" to "0",
                "startMonth" to "0",
                "startDay" to "0",
                "startHour" to "0",
                "startMin" to "0",
                "startSek" to "0",
                "latitude" to "0",
                "longitude" to "0",
                "sap" to "0",
                "position" to "null"
            )
        )
        db.collection("users").document(userUid).set(userData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                throw Exception("Error writing document")
            }
    }

    /**
     * Funktion, um aktualisierte Benutzerdaten im Firestore zu speichern.
     *
     * @param userData Benutzerdatenmodell.
     */
    fun saveUserDataStore(userData: UserTestDataModel) {
        val userDataStore = mapOf(
            "userUid" to userData.userUid,
            "email" to userData.email,
            "password" to userData.password,
            "firstName" to userData.firstName,
            "lastName" to userData.lastName,
            "baNumber" to userData.baNumber,
            "userQualification" to userData.userQualification,
            "carStatus" to userData.carStatus,
            "timerStatus" to userData.timerStatus,
            "timerMap" to userData.timerMap
        )
        db.collection("users").document(userData.userUid).set(userDataStore)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                throw Exception("Error writing document")
            }
    }

    /**
     * Funktion, um die Arbeitszeitliste eines Benutzers im Firestore zu speichern.
     *
     * @param workTimeLogList Arbeitszeitliste.
     */
    fun saveUserWorkTimeLogStore(workTimeLogList: List<String>) {
        db.collection("workTimeLog").document(userData.userUid).set(
            "userWorkTimeLogList" to workTimeLogList
        )
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                throw Exception("Error writing document")
            }
    }

    /**
     * Funktion, um die Benutzerdaten eines bestimmten Benutzers aus dem Firestore abzurufen.
     *
     * @param userUid Benutzer-UID.
     */
    fun getUserDataStore(userUid: String) {
        try {
            val docRef = db.collection("users").document(userUid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        _currentUserStore.value = UserTestDataModel(
                            document.data?.get("userUid").toString(),
                            document.data?.get("email").toString(),
                            document.data?.get("password").toString(),
                            document.data?.get("firstName").toString(),
                            document.data?.get("lastName").toString(),
                            (document.data?.get("baNumber") as Long?)?.toInt()!!,
                            document.data?.get("userQualification") as Map<String, String>,
                            document.data?.get("carStatus").toString().toBoolean(),
                            document.data?.get("timerStatus").toString().toBoolean(),
                            document.data?.get("timerMap") as MutableMap<String, String>
                        )
                        Log.d(TAG, _currentUserStore.value.toString())
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
        }
    }

    /**
     * Funktion, um die Arbeitszeitliste eines bestimmten Benutzers aus dem Firestore abzurufen.
     *
     * @param userUid Benutzer-UID.
     */
    fun getWorkTimeListStore(userUid: String) {
        try {
            val docRef = db.collection("workTimeLog").document(userUid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        val workTimeList = document.data!!["second"] as MutableList<String>
                        _currentTimWorkList.value = workTimeList.toMutableList()
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        } catch (ex: Exception) {
            Log.e(TAG, ex.message!!)
        }
    }
}
