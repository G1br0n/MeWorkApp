package com.example.abschlussaufgabe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.data.local.UserMaterialDatabase
import com.example.abschlussaufgabe.data.local.getUserMaterialDatabase
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.data.model.UserMaterialModel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var  userMaterialDatabase: UserMaterialDatabase = getUserMaterialDatabase(application)
    private val repository = AppRepository(userMaterialDatabase)


    val userList = repository.user
    val materialList = repository.material
    val userMaterialList = repository.userMaterialList

    lateinit var userData: UserDataModel

    fun insertUserMaterial(materialList: UserMaterialModel) {
        viewModelScope.launch {
            repository.insert(materialList)
        }
    }
}