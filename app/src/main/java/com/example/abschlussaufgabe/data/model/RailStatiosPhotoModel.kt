package com.example.abschlussaufgabe.data.model

/**
* Die Datenklasse `RailStationsPhotoModel` repräsentiert ein Foto einer Bahnhofsstation.
*
* Diese Datenklasse enthält Informationen über den Titel des Fotos und die URL des Fotos.
*
* @property title Der Titel des Fotos.
* @property photoUrl Die URL des Fotos.
*/
data class RailStationsPhotoModel(
    val title:String,
    val photoUrl: String?
)