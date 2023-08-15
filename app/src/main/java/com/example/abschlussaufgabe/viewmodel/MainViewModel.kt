package com.example.abschlussaufgabe.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.data.local.StorageMaterialDatabase
import com.example.abschlussaufgabe.data.local.getStorageMaterialDatabase
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.data.model.UserDataModel

import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    var storageMaterialDatabase: StorageMaterialDatabase = getStorageMaterialDatabase(application)
    val repository = AppRepository(storageMaterialDatabase)


    //Userliste
    val userList = repository.userData

    //Einzelne User, wird im LoginFragment deklarirt
    lateinit var userData: UserDataModel

    //Material liste für erst befülüng des datenbank vom lager bestand
    val storageMaterialList = repository.storageMaterialGroundList

   var _storageMaterialDataList = MutableLiveData<List<StorageMaterialModel>>()
    val storageMaterialDataList: LiveData<List<StorageMaterialModel>>
        get() = _storageMaterialDataList


    //User material liste aus dem lagerbestand DAO
   val userMaterialList: LiveData<List<StorageMaterialModel>>
       get() = _userMaterialList
   private val _userMaterialList = MutableLiveData<List<StorageMaterialModel>>()


    init {

        viewModelScope.launch {
            repository.insertAll(storageMaterialList.value!!)
            _storageMaterialDataList.value = repository.getAllStorageMaterialFromDataBank()
            loadUserMaterialList()
        }

    }


    //Hier Lade Ich den datenbanck wen es lehr ist
    private fun insertStorageMaterial() {
        viewModelScope.launch {

        }
    }

    //Hier versuche ich ich die LiveData aus dem datenbank zu fülehen
    private fun getStorageMaterialFromDataBank(){
        viewModelScope.launch {

            //todo: Tester Log
            Log.e("Home","${storageMaterialDataList.value}")
        }
    }

    fun loadUserMaterialList() {

        viewModelScope.launch {
            val materialCheckedList = mutableListOf<StorageMaterialModel>()


            if (!storageMaterialDataList.value.isNullOrEmpty()){
                //TODO: storageMaterialDataList wird nicht gefült
                for (material in _storageMaterialDataList.value!!) {
                    //TODO: 1001 to userDada.userId
                    if (material.locationId == userData.userId) {

                        //todo: Tester Log
                        Log.e("Home","Test loadUserMaterialList")

                        materialCheckedList.add(material)
                    }
                }
                _userMaterialList.value = materialCheckedList
                }



           // _userMaterialList.value = repository.getById(userData.userId).value
        }
    }

  fun updateMaterialLocation(id: Int, newLocationId: Int) {
        viewModelScope.launch {
            repository.updateStorageMaterial(id,newLocationId)
            _storageMaterialDataList.value = repository.getAllStorageMaterialFromDataBank()
            loadUserMaterialList()
        }
    }
}