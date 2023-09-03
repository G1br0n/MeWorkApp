package com.example.abschlussaufgabe.viewmodel

// Importieren Sie die erforderlichen Android- und Firebase-Bibliotheken
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * ## Information
 * ### `FireBaseAuthViewModel`: Eine ViewModel-Klasse für die Firebase-Authentifizierung.
 * Diese Klasse verwaltet die Firebase-Authentifizierungsoperationen wie Registrierung, Anmeldung und Abmeldung.
 * ###
 * ## Funktionen
 * - [checkedLogin] - Variable, um den Login-Status zu überprüfen.
 * - [firebaseAuth] - Instanz von FirebaseAuth.
 * - [_currentUserBase] - MutableLiveData, das den aktuellen Firebase-Benutzer speichert.
 * - [currentUserBase] - LiveData des aktuellen Firebase-Benutzers.
 * - [register] - Funktion zur Registrierung eines neuen Benutzers mit Firebase Authentication.
 * - [login] - Funktion zum Einloggen eines Benutzers mit Firebase Authentication.
 * - [logout] - Funktion zum Abmelden des aktuellen Benutzers von Firebase Authentication.
 */
class FireBaseAuthViewModel : ViewModel() {

    // Variable, um den Login-Status zu überprüfen
    var checkedLogin = false

    // Instanz von FirebaseAuth erhalten
    private val firebaseAuth = FirebaseAuth.getInstance()

    // MutableLiveData, das den aktuellen Firebase-Benutzer speichert
    private val _currentUserBase = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUserBase: LiveData<FirebaseUser?>
        get() = _currentUserBase

    /**
     * ## Information
     * Funktion zur Registrierung eines neuen Benutzers mit Firebase Authentication.
     *
     * @param context Kontext für die Erstellung von Toast-Meldungen.
     * @param email E-Mail-Adresse des Benutzers.
     * @param password Passwort des Benutzers.
     * @param firstName Vorname des Benutzers.
     * @param lastName Nachname des Benutzers.
     * @param baNumber BA-Nummer des Benutzers (Standardwert ist 0).
     * @param userQualification Qualifikation des Benutzers.
     */
    fun register(
        context: Context,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        baNumber: Int = 0,
        userQualification: Map<String, String>
    ) {

        // Versuch, den Benutzer mit Firebase zu registrieren
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    // Bei erfolgreicher Registrierung neue Benutzerdaten in Firestore speichern
                    FireStoreViewModel().addNewUserDataStore(
                        authResult.result.user!!.uid,
                        email,
                        password,
                        firstName,
                        lastName,
                        baNumber,
                        userQualification
                    )

                    // Neue Arbeitzeittabelle für den Benutzer in Firestore hinzufügen
                    FireStoreViewModel().addNewUserWorkTimeListStore(
                        authResult.result.user!!.uid
                    )
                } else {
                    // Bei Fehler Log und Toast mit Fehlermeldung anzeigen
                    Log.e("ERROR", "${authResult.exception}")
                    Toast.makeText(context, "${authResult.exception}", Toast.LENGTH_LONG).show()
                }
            }
    }

    /**
     * ## Information
     * Funktion zum Einloggen eines Benutzers mit Firebase Authentication.
     *
     * @param email E-Mail-Adresse des Benutzers.
     * @param password Passwort des Benutzers.
     * @return Boolean, der den Login-Status angibt.
     */
    fun login(email: String, password: String): Boolean {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                _currentUserBase.value = firebaseAuth.currentUser
                checkedLogin = authResult.isSuccessful

                // Log-Eintrag je nach Ergebnis des Login-Vorgangs
                if (authResult.isSuccessful) {
                    Log.e("loginTrue", "${authResult.isSuccessful}")
                } else {
                    Log.e("loginFalse", "${authResult.isSuccessful}")
                }
            }
        return checkedLogin
    }

    /**
     * ## Information
     * Funktion zum Abmelden des aktuellen Benutzers von Firebase Authentication.
     */
    fun logout() {
        firebaseAuth.signOut()
        _currentUserBase.value = null
        FireStoreViewModel()._currentUserStore.value = null
    }
}
