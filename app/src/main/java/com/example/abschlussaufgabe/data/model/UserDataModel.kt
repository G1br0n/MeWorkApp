package com.example.abschlussaufgabe.data.model

data class UserDataModel(
    val userId: Int,
    val userFirstName: String,
    val userLastName: String,
    val userBirthDate: String,

    val userLogIn: String,
    val userPassword: String,

    val userQualification: List<String>
)
