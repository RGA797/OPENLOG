package com.example.openlog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.openlog.model.Data
import com.example.openlog.model.InputDTO
import com.example.openlog.model.User
import com.google.firebase.auth.FirebaseUser
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DataViewModel: ViewModel() {
    private var user = User()
    private var data = Data()
    var userDataList = ArrayList<InputDTO>(0)
    var userInputList = ArrayList<InputDTO>(0)
    private val _currentUser = MutableLiveData(user.getFirebaseUser())
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    private val _currentDataList = MutableLiveData(ArrayList<InputDTO>(0))
    val currentDataList: MutableLiveData<ArrayList<InputDTO>>
        get() = _currentDataList

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

    fun saveUserData(firebaseUser: FirebaseUser, koen: String, alder: String) {
        return data.storeUserData(firebaseUser, koen, alder)
    }

    fun changeInputData (firebaseUser: FirebaseUser, type: String) {
        data.changeUserData(firebaseUser,type){
            _currentDataList.value = it as ArrayList<InputDTO>
            userInputList = it
        }
    }

    fun changeUserData (firebaseUser: FirebaseUser) {
        data.changeUserData(firebaseUser,"køn"){
            _currentDataList.value = it as ArrayList<InputDTO>
            userDataList = it
        }
    }

    //fun sortDatalistForDateRange(startDate: Date, endDate: Date){
        //val i = 0
        //while (i< userInputList.size && userInputList.size != 0){
        //    if ((userInputList[i].secondInput.split(" "))[0] )
      //  }
    //}

    fun getUserData(): ArrayList<InputDTO>? {
        return currentDataList.value
    }

}