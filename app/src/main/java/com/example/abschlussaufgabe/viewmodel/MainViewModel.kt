package com.example.abschlussaufgabe.viewmodel

import androidx.lifecycle.ViewModel
import com.example.abschlussaufgabe.data.AppRepository

class MainViewModel: ViewModel() {

    private val repository = AppRepository()

    val userList = repository.user
    val materialList = repository.material

}