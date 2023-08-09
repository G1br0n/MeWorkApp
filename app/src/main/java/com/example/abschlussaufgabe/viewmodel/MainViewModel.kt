package com.example.abschlussaufgabe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.data.local.StorageMaterialDatabase
import com.example.abschlussaufgabe.data.local.UserMaterialDatabase
import com.example.abschlussaufgabe.data.local.getStorageMaterialDatabase
import com.example.abschlussaufgabe.data.local.getUserMaterialDatabase
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.data.model.UserMaterialModel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var  userMaterialDatabase: UserMaterialDatabase = getUserMaterialDatabase(application)
    private var storageMaterialDatabase: StorageMaterialDatabase = getStorageMaterialDatabase(application)

    private val repository = AppRepository(storageMaterialDatabase)


    val userList = repository.user
    val materialList = repository.material
    //val userMaterialList = repository.userMaterialList
    val storageMaterialList = repository.storageMaterialList

    lateinit var userData: UserDataModel

    init {

        insertUserMaterial()

    }
    private fun insertUserMaterial() {
        viewModelScope.launch {
            repository.insertAll(storageMaterialList.value!!)
        }
    }
}