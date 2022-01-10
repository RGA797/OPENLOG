package com.example.openlog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.openlog.model.Data
import com.example.openlog.model.InputDTO
import com.example.openlog.model.User
import com.google.firebase.auth.FirebaseUser
import java.util.*
import kotlin.collections.ArrayList

//dataViewModel follows MVVM structure.
//used to fetch and save/change values in model (both local and firebase database)
class DataViewModel: ViewModel() {
    //local user and data model objects
    private var user = User()
    private var data = Data()

    //these are the two lists holding fetched database values, as lists of inputDTO's.

    //userDataList holds user data made when creating a user (gender and age)
    var userDataList = ArrayList<InputDTO>(0)

    //userInputList holds user input made when logged in (blodsukker, insulin, kulhydrat values with dates)
    var userInputList = ArrayList<InputDTO>(0)

    //currentUser livedata. not used atm.
    private val _currentUser = MutableLiveData(user.getFirebaseUser())
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    //currentDataList livedata. not used atm.
    private val _currentDataList = MutableLiveData(ArrayList<InputDTO>(0))
    val currentDataList: MutableLiveData<ArrayList<InputDTO>>
        get() = _currentDataList

    //currentEmail livedata. used for greeting in viewbinding for forside fragment
    private val _currentEmail = MutableLiveData(user.getEmail())
    val currentEmail: LiveData<String>
        get() = _currentEmail

    //currentInput livedata. used in forside fragment to display input before saving it.
    private val _currentInput = MutableLiveData(data.getDataInput())
    val currentInput: LiveData<String?>
        get() = _currentInput

    //updates current. used upon login.
    fun changeUser(firebaseUser: FirebaseUser, email: String){
        user.setFirebaseUser(firebaseUser)
        user.setEmail(email)
        _currentEmail.value =user.getEmail()
        _currentUser.value = user.getFirebaseUser()
    }

    //changes the current input. used to store input of speech recognition. does not save in database
    fun changeInput(input: String){
        data.changeInput(input)
        _currentInput.value = data.getDataInput()
    }

    //returns current firebaseuser
    fun getCurrentFirebaseUser(): FirebaseUser?{
        return user.getFirebaseUser()
    }

    //saves current input to database
    fun saveInput(firebaseUser: FirebaseUser): Boolean {
        return data.storeInput(firebaseUser)
    }

    //saves gender/age/name to database
    fun saveUserData(firebaseUser: FirebaseUser, koen: String, alder: String, navn: String) {
        return data.storeUserData(firebaseUser, koen, alder, navn)
    }

    //updates userDataList to hold all values of given type and date range
    fun updateInputData (firebaseUser: FirebaseUser, type: String, startDate: Date, endDate: Date) {
        //val date = Calendar.getInstance()

        //date.set(2020, 2, 3,3 , 3, 3)
        //val startDate = date.time

        //date.set(2022, 0, 6,3 , 3, 3)
        //val endDate = date.time
        data.updateUserData(firebaseUser,type, startDate, endDate){
            _currentDataList.value = it as ArrayList<InputDTO>
            userInputList = it
        }
    }

    //updates user data list to hold gender/age for given firebaseuser
    fun updateUserData (firebaseUser: FirebaseUser) {
        data.updateUserData(firebaseUser,"køn", null, null){
            _currentDataList.value = it as ArrayList<InputDTO>
            userDataList = it
        }
    }

    //returns userDataList
    fun getDataList(): ArrayList<InputDTO> {
        return userDataList
    }

}