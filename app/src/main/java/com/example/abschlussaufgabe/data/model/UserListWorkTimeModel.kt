package com.example.abschlussaufgabe.data.model

/**
 * Die Datenklasse `UserListWorkTimeModel` repräsentiert eine Liste von Arbeitszeiten eines Benutzers.
 *
 * Diese Datenklasse enthält eine Liste von `UserWorkTimeModel`-Objekten, die die Arbeitszeiten des
 * Benutzers darstellen.
 *
 * @property ListWorkTime Eine mutable Liste von `UserWorkTimeModel`-Objekten, die die Arbeitszeiten des Benutzers enthalten.
 */
data class UserListWorkTimeModel(
    val ListWorkTime: MutableList<UserWorkTimeModel>
)