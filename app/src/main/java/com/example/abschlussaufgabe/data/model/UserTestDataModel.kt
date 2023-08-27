package com.example.abschlussaufgabe.data.model

import java.sql.Timestamp

data class UserTestDataModel(
    var userUid: String,
    var email: String,
    var password: String,
    var firstName: String,
    var lastName: String,
    var baNumber:Int = 0,
    var userQualification: Map<String,String>,

    var carStatus: Boolean,

    var timerStatus: Boolean,
    var timerMap: MutableMap<String,String>

)

