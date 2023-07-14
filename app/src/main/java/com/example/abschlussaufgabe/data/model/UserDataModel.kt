package com.example.abschlussaufgabe.data.model


import java.time.LocalDate

data class UserDataModel(
    val userId: Int,
    val userFirstName: String,
    val userLastName: String,
    val userBirthDate: String,

    val userLogIn: String,
    val userPassword: String,

    val userQualification: List<String>,
    val userQualificationFit: List<LocalDate>,
    
    val userBaNumber: Int,
    val userBupNumber: Int,

)
