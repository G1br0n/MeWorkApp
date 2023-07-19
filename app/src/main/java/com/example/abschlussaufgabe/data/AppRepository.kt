package com.example.abschlussaufgabe.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussaufgabe.data.model.MaterialModel
import com.example.abschlussaufgabe.data.model.UserDataModel
import java.time.LocalDate
import java.util.Date

class AppRepository {

    //todo: UserList
    private val _user = MutableLiveData<List<UserDataModel>>()
    val user: LiveData<List<UserDataModel>>
        get() = _user

    private val _material = MutableLiveData<List<MaterialModel>>()
    val material: LiveData<List<MaterialModel>>
        get() = _material



    //todo: init block !!!!
    init {
        loadUser()
        loadMaterial()
    }

    private fun loadUser(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _user.value = listOf(
                UserDataModel(1001,"Eugen","Lange","12.08.1989","Gibron", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(
                    LocalDate.of(2023,5,22),LocalDate.of(2023,5,22),LocalDate.of(2023,1,22),LocalDate.of(2023,2,22),LocalDate.of(2023,3,22),LocalDate.of(2023,4,22)),102030,152535),
                UserDataModel(1002,"Lucas","Hard","21.06.1986","Biebr", "password", listOf("SIPO","BüP","SAS","BE","HIB"),listOf(
                    LocalDate.of(2023,5,22),LocalDate.of(2023,5,22),LocalDate.of(2023,1,22),LocalDate.of(2023,1,11),LocalDate.of(2023,1,11),LocalDate.of(2023,1,11)),112030,162535),

                // UserDataModel(1003,"Anita","Koch","15.04.1980","Ludwigshafen", "password", listOf("SAKRA","BüP","SAS","BE","HIB"),listOf(),122030,172535),
                // UserDataModel(1004,"Michael","Schmidt","27.11.1990","Heidelberg", "password", listOf("SIPO","BüP","SAS","BE","HIB"),listOf(),132030,182535),
                // UserDataModel(1005,"Sabine","Bauer","31.01.1979","Mainz", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(),142030,192535),
                // UserDataModel(1006,"Max","Müller","03.07.1982","Mannheim", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(),152030,202535),
                // UserDataModel(1007,"Linda","Weber","06.10.1988","Wiesbaden", "password", listOf("SIPO","BüP","SAS","BE","HIB"),listOf(),162030,212535),
                // UserDataModel(1008,"Felix","Schneider","23.03.1985","Karlsruhe", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(),172030,222535),
                // UserDataModel(1009,"Monika","Meyer","18.02.1991","Kaiserslautern", "password", listOf("SIPO","BüP","SAS","BE","HIB"),listOf(),182030,232535),
                // UserDataModel(1010,"Leon","Wolf","05.05.1993","Saarbrücken", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),listOf(),192030,242535),
            )
        }
    }


    fun loadMaterial() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _material.value = listOf(
                MaterialModel(1, "sdgf", LocalDate.of(1991, 12, 12)),
                MaterialModel(2, "sdg234234234231231231", LocalDate.of(1991, 12, 12)),
                MaterialModel(3, "sdg234234234234234f", LocalDate.of(1991, 12, 12)),
                MaterialModel(4, "sdg21231234234234234234f", LocalDate.of(1991, 12, 12)),
                MaterialModel(5, "sdg23424234f", LocalDate.of(1991, 12, 12)),
                MaterialModel(6, "sdg2334f", LocalDate.of(1991, 12, 12)),
            )
        }
    }
}