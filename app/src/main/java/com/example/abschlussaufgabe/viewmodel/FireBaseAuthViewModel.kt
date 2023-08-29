package com.example.abschlussaufgabe.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FireBaseAuthViewModel : ViewModel() {


    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _currentUserBase = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUserBase: LiveData<FirebaseUser?>
        get() = _currentUserBase


    fun register(
        context: Context,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        baNumber: Int = 0,
        userQualification: Map<String, String>,
    ) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {

                    FireStoreViewModel().addNewUserDataStore(
                        authResult.result.user!!.uid,
                        email,
                        password,
                        firstName,
                        lastName,
                        baNumber,
                        userQualification
                    )


                } else {
                    Log.e("ERROR", "${authResult.exception}")
                    Toast.makeText(context, "${authResult.exception}", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }


    fun login(email: String, password: String): Boolean {
        var checkedLogin = false

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    _currentUserBase.value = firebaseAuth.currentUser
                    checkedLogin = true
                } else {
                    checkedLogin = false
                    Log.e("ERROR", "${authResult.exception}")
                }

            }
        return checkedLogin
    }


    fun logout() {
        firebaseAuth.signOut()
        _currentUserBase.value = null
        FireStoreViewModel()._currentUserStore.value = null
    }


}