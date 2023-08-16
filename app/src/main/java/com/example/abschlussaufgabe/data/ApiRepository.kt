package com.example.abschlussaufgabe.data

import RailStationApi
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussaufgabe.data.model.RailStationsPhotoModel

class ApiRepository(private val api: RailStationApi) {


    private var _bfPhotoList = MutableLiveData<RailStationsPhotoModel>()
    val bfPhotoList: LiveData<RailStationsPhotoModel>
        get() = _bfPhotoList


        suspend fun getBfPhotoList(){
            try {
                val bfPhotoList = api.retrofitService.getStationList()

                _bfPhotoList.value = bfPhotoList.random()

                Log.e("Test","ApiRepository: ${_bfPhotoList.value}")

            } catch (ex:Exception){
                Log.e("Test","ApiRepository: ${ex.message}")
            }
        }

}