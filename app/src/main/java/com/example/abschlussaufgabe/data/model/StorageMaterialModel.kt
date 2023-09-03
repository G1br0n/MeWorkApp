package com.example.abschlussaufgabe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Die Datenklasse `StorageMaterialModel` repräsentiert ein Material, das im Lager gespeichert ist.
 *
 * Diese Datenklasse enthält Informationen über ein spezifisches Material, das im Lager vorhanden ist.
 *
 * @property materialId Die eindeutige ID des Materials.
 * @property name Der Name des Materials.
 * @property locationId Die ID des Ortes, an dem sich das Material befindet.
 */
@Entity
data class StorageMaterialModel(

    @PrimaryKey(autoGenerate = true)
    val materialId: Int,

    val name: String,
    var locationId: String
)