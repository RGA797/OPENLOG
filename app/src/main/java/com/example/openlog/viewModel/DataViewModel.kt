
package com.example.openlog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.openlog.model.*
import com.google.firebase.auth.FirebaseUser
import java.util.*
import kotlin.collections.ArrayList

//dataViewModel follows MVVM structure.
//used to fetch and save/change values in model (both local and firebase database)
class DataViewModel: ViewModel() {
    //local user and data model objects
    private var user = User()
    private var data = Data()
    private var graphOptionData = GraphOptionData()
    //these are the two lists holding fetched database values, as lists of inputDTO's.

    //userDataList holds user data made when creating a user (gender and age)
    var userDataList = ArrayList<InputDTO>(0)

    //userInputList holds a list of list of user input made when logged in (in order, insulin, blodsukker, kulhydrat values, with dates)
    var userInputList = arrayOfNulls<ArrayList<InputDTO>>(3)

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
    fun saveInput(): Boolean {
        return data.storeInput(getCurrentFirebaseUser()!!)
    }

    //saves gender/age/name to database
    fun saveUserData(koen: String, alder: String, navn: String) {
        return data.storeUserData(getCurrentFirebaseUser()!!, koen, alder, navn)
    }

    //updates userDataList to hold all values of given type and date range. the values updated depend on categories booleans in model
    fun updateInputData () {

        val firebaseUser = getCurrentFirebaseUser()!!
        val startDate = graphOptionData.getSelectedDates()[0]
        val endDate = graphOptionData.getSelectedDates()[1]

        if (startDate != null && endDate != null) {
            //if kulhydrat == true
            if (graphOptionData.categorySelected(CARB)) {
                data.updateUserData(firebaseUser, "kulhydrat", startDate, endDate) {
                    _currentDataList.value = it as ArrayList<InputDTO>
                    userInputList[CARB] = it
                }
            }

            //if insulin == true (set true temporarily for testing)
            if (graphOptionData.categorySelected(INSULIN)) {
                data.updateUserData(firebaseUser, "insulin", startDate, endDate) {
                    _currentDataList.value = it as ArrayList<InputDTO>
                    userInputList[INSULIN] = it
                }
            }

            //if blodsukker == true
            if (graphOptionData.categorySelected(BLOODSUGAR)) {
                data.updateUserData(firebaseUser, "blodsukker", startDate, endDate) {
                    _currentDataList.value = it as ArrayList<InputDTO>
                    userInputList[BLOODSUGAR] = it
                }
            }
            // TODO : find better way to prevent attempting to use date before it has been fetched
            Thread.sleep(250)
        }
        graphOptionData.resetDatesAndCategories()
    }

    //updates user data list to hold gender/age for given firebaseuser
    fun updateUserData () {
        data.updateUserData(getCurrentFirebaseUser()!!,"køn", null, null){
            _currentDataList.value = it as ArrayList<InputDTO>
            userDataList = it
        }
    }

    //returns userDataList
    fun getDataList(): ArrayList<InputDTO> {
        return userDataList
    }

    //flips boolean value for category in bool array indicating if it is to be displayed on graph or not
    fun flipCategory(index: Int) {
        graphOptionData.flipCategory(index)
    }

    //checks if category in given index is selected by user
    fun categorySelected(index : Int): Boolean {
        return graphOptionData.categorySelected(index)
    }

    fun setDateSelected(date: Date) {
        graphOptionData.setDateSelected(date)
    }

    fun getInputList(): Array<ArrayList<InputDTO>?> {
        return userInputList
    }
}