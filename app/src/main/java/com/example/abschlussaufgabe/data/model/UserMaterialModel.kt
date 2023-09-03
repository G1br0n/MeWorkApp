package com.example.abschlussaufgabe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ## Information
 * ### Die Datenklasse `UserMaterialModel` repräsentiert Materialinformationen eines Benutzers.
 *
 * Diese Datenklasse enthält Informationen über ein Material, einschließlich Material-ID, Name und
 * Standort-ID.
 *
 * @property idMaterial Die eindeutige ID des Materials (auto-generiert).
 * @property name Der Name des Materials.
 * @property locationId Die ID des Standorts, an dem sich das Material befindet.
 */
@Entity
data class UserMaterialModel(

    @PrimaryKey(autoGenerate = true)
    val idMaterial: Int = 0,

    val name: String,
    val locationId: Int
)
