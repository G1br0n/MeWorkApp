package com.example.abschlussaufgabe.data.model

import java.time.LocalDate
import java.time.LocalTime

data class UserWorkTimeModel(
    val userId: Int,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
    )
