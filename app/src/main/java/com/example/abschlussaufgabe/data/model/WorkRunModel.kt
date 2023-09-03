package com.example.abschlussaufgabe.data.model

/**
 * Die Datenklasse `WorkRunModel` repräsentiert Informationen über einen Arbeitslauf.
 *
 * @property latitude Die Breitengradkoordinate des Arbeitslaufs.
 * @property longitude Die Längengradkoordinate des Arbeitslaufs.
 * @property position Die Position des Arbeitslaufs.
 * @property sap Die SAP-Nummer oder Bezeichnung des Arbeitslaufs.
 * @property startYear Das Startjahr des Arbeitslaufs.
 * @property startMonth Der Startmonat des Arbeitslaufs.
 * @property startDay Der Starttag des Arbeitslaufs.
 * @property startHour Die Startstunde des Arbeitslaufs.
 * @property startMin Die Startminute des Arbeitslaufs.
 * @property startSek Die Startsekunde des Arbeitslaufs.
 */
data class WorkRunModel(
    var latitude: String,
    var longitude: String,
    var position: String,
    var sap: String,
    var startYear: Int,
    var startMonth: Int,
    var startDay: Int,
    var startHour: Int,
    var startMin: Int,
    var startSek: Int
)