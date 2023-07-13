package com.example.abschlussaufgabe.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussaufgabe.data.model.UserDataModel

class AppRepository {

    //todo: UserList
    private val _user = MutableLiveData<List<UserDataModel>>()
    val user: LiveData<List<UserDataModel>>
        get() = _user


    //todo: init block !!!!
    init {
        loadUser()
    }

    fun loadUser(){
        _user.value = listOf(
            UserDataModel(1001,"Eugen","Lange","12.08.1989","Gibron", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),102030,152535),
            UserDataModel(1002,"Lucas","Hard","21.06.1986","Biebr", "password", listOf("SIPO","BüP","SAS","BE","HIB"),112030,162535),
            UserDataModel(1003,"Anita","Koch","15.04.1980","Ludwigshafen", "password", listOf("SAKRA","BüP","SAS","BE","HIB"),122030,172535),
            UserDataModel(1004,"Michael","Schmidt","27.11.1990","Heidelberg", "password", listOf("SIPO","BüP","SAS","BE","HIB"),132030,182535),
            UserDataModel(1005,"Sabine","Bauer","31.01.1979","Mainz", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),142030,192535),
            UserDataModel(1006,"Max","Müller","03.07.1982","Mannheim", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),152030,202535),
            UserDataModel(1007,"Linda","Weber","06.10.1988","Wiesbaden", "password", listOf("SIPO","BüP","SAS","BE","HIB"),162030,212535),
            UserDataModel(1008,"Felix","Schneider","23.03.1985","Karlsruhe", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),172030,222535),
            UserDataModel(1009,"Monika","Meyer","18.02.1991","Kaiserslautern", "password", listOf("SIPO","BüP","SAS","BE","HIB"),182030,232535),
            UserDataModel(1010,"Leon","Wolf","05.05.1993","Saarbrücken", "password", listOf("SIPO","SAKRA","BüP","SAS","BE","HIB"),192030,242535)
        )
    }

}