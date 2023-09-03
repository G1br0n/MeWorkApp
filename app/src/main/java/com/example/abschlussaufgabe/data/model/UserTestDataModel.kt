package com.example.abschlussaufgabe.data.model

/**
 * Die Datenklasse `UserTestDataModel` repräsentiert Testbenutzerdaten.
 *
 * Diese Datenklasse enthält Informationen über Testbenutzer, einschließlich Benutzer-ID, E-Mail,
 * Passwort, Vorname, Nachname, BA-Nummer, Benutzerqualifikationen, Auto-Status, Timer-Status und Timer-Mapping.
 *
 * @property userUid Die eindeutige ID des Benutzers.
 * @property email Die E-Mail-Adresse des Benutzers.
 * @property password Das Passwort des Benutzers.
 * @property firstName Der Vorname des Benutzers.
 * @property lastName Der Nachname des Benutzers.
 * @property baNumber Die BA-Nummer des Benutzers (Standardwert: 0).
 * @property userQualification Eine Map, die die Qualifikationen des Benutzers enthält.
 * @property carStatus Der Status des Autos des Benutzers (true, wenn verfügbar; false, wenn nicht verfügbar).
 * @property timerStatus Der Timer-Status des Benutzers (true, wenn aktiviert; false, wenn deaktiviert).
 * @property timerMap Eine MutableMap, die Timer-Informationen des Benutzers enthält.
 */
data class UserTestDataModel(
    var userUid: String,
    var email: String,
    var password: String,
    var firstName: String,
    var lastName: String,
    var baNumber: Int = 0,
    var userQualification: Map<String, String>,
    var carStatus: Boolean,
    var timerStatus: Boolean,
    var timerMap: MutableMap<String, String>
)