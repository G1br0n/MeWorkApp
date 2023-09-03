package com.example.abschlussaufgabe.data.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * Die Datenklasse `UserWorkTimeModel` repräsentiert die Arbeitszeit eines Benutzers.
 *
 * @property userId Die ID des Benutzers.
 * @property date Das Datum, an dem die Arbeitszeit erfasst wurde (LocalDate).
 * @property startTime Die Startzeit der Arbeitszeit (LocalTime).
 * @property endTime Die Endzeit der Arbeitszeit (LocalTime).
 */
data class UserWorkTimeModel(
    val userId: Int,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)
