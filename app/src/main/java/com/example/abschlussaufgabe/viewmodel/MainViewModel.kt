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

    val storageMaterialDataList: LiveData<List<StorageMaterialModel>>
        get() = _storageMaterialDataList
    private var _storageMaterialDataList = MutableLiveData<List<StorageMaterialModel>>()


    //User material liste aus dem lagerbestand DAO
   val userMaterialList: LiveData<List<StorageMaterialModel>>
       get() = _userMaterialList
   private val _userMaterialList = MutableLiveData<List<StorageMaterialModel>>()

    val userPsaMaterialList: LiveData<List<StorageMaterialModel>>
        get() = _userPsaMaterialList
    private val _userPsaMaterialList = MutableLiveData<List<StorageMaterialModel>>()

    init {

        viewModelScope.launch {
            repository.insertAll(storageMaterialList.value!!)
            _storageMaterialDataList.value = repository.getAllStorageMaterialFromDataBank()
            loadUserMaterialList()
        }

    }


    fun loadUserMaterialList() {

        viewModelScope.launch {
            val materialList = mutableListOf<StorageMaterialModel>()
            val psaMaterialList = mutableListOf<StorageMaterialModel>()

            _storageMaterialDataList.value?.let { materials ->
                for (material in materials) {
                    when {
                        material.locationId == userData.userId && material.materialId > 100000 ->
                            psaMaterialList.add(material)

                        material.locationId == userData.userId && material.materialId < 100000 ->
                            materialList.add(material)

                    }
                }
            }

            _userPsaMaterialList.value = psaMaterialList
            _userMaterialList.value = materialList

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