package com.example.openlog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.openlog.model.Data
import com.example.openlog.model.User
import com.google.firebase.auth.FirebaseUser

class DataViewModel: ViewModel() {

    private var user = User()
    private var data = Data()
    private val _currentUser = MutableLiveData(user.getFirebaseUser())
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    private val _currentEmail = MutableLiveData(user.getEmail())
    val currentEmail: LiveData<String>
        get() = _currentEmail

    private val _currentInput = MutableLiveData(data.getDataInput())
    val currentInput: LiveData<String?>
        get() = _currentInput

    fun changeUser(firebaseUser: FirebaseUser, email: String){
        user.setFirebaseUser(firebaseUser)
        user.setEmail(email)
        _currentEmail.value =user.getEmail()
        _currentUser.value = user.getFirebaseUser()
    }

    fun changeInput(input: String){
        data.changeInput(input)
        _currentInput.value = data.getDataInput()
    }

    fun getCurrentFirebaseUser(): FirebaseUser?{
        return user.getFirebaseUser()
    }

    fun saveInput(firebaseUser: FirebaseUser): Boolean {
        return data.storeInput(firebaseUser)
    }



}