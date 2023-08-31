package com.example.abschlussaufgabe.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class FireStorageViewModel : ViewModel() {
   var storage = Firebase.storage

}