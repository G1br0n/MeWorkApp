package com.example.abschlussaufgabe.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FireBaseAuthViewModel: ViewModel() {


        private val firebaseAuth = FirebaseAuth.getInstance()

        private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
        val currentUser: LiveData<FirebaseUser?>
            get() = _currentUser



        fun register(email: String, password: String) {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    login(email, password)
                } else {
                    Log.e("ERROR", "${authResult.exception}")
                }
            }

        }


        fun login(email: String, password: String){


            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    _currentUser.value = firebaseAuth.currentUser

                } else {

                    Log.e("ERROR", "${authResult.exception}")
                }

            }

        }


        fun logout() {
            firebaseAuth.signOut()
            _currentUser.value = firebaseAuth.currentUser
        }


}