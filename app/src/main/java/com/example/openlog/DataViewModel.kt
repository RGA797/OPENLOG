package com.example.openlog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    fun getInput(): String? {
        return data.getDataInput()
    }

    fun isNewInput(): Boolean{
        return data.isNew()
    }

    fun changeNew(new: Boolean){
        data.changeNew(new)
    }


}