package com.example.openlog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class DataViewModel: ViewModel() {

    private var user = User()
    private val _currentUser = MutableLiveData(user.getFirebaseUser())
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser


    fun changeUser(firebaseUser: FirebaseUser){
        user.setFirebaseUser(firebaseUser)
        _currentUser.value = user.getFirebaseUser()
    }

    fun getFirebaseUser(): FirebaseUser?{
        return user.getFirebaseUser()
    }

}