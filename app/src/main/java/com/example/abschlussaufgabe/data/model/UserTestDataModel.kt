package com.example.abschlussaufgabe.data.model

import java.sql.Timestamp

data class UserTestDataModel(
    val userUid: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val baNumber:Int = 0,
    val userQualification: List<String>
)