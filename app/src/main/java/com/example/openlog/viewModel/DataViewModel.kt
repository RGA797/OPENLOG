
package com.example.openlog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.openlog.model.*
import com.google.firebase.auth.FirebaseUser
import com.jjoe64.graphview.GraphView
import java.util.*
import kotlin.collections.ArrayList

//dataViewModel follows MVVM structure.
//used to fetch and save/change values in model (both local and firebase database)
class DataViewModel: ViewModel() {
    //local user and data model objects
    private var user = User()
    private var data = Data()
    private var graphOptionData = GraphOptionData()
    private var lineData = arrayOfNulls<LineData>(3)
    private var graphData = GraphData()
    //these are the two lists holding fetched database values, as lists of inputDTO's.

    //userDataList holds user data made when creating a user (gender and age)
    var userDataList = ArrayList<DataDTO>(0)

    //userInputList holds a list of list of user input made when logged in (in order, insulin, blodsukker, kulhydrat values, with dates)
    var userInputList = arrayOfNulls<ArrayList<DataDTO>>(3)

    //currentUser livedata. not used atm.
    private val _currentUser = MutableLiveData(user.getFirebaseUser())
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    private val _currentUsername = MutableLiveData(user.getUsername())
    val currentUsername: LiveData<String?>
        get() = _currentUsername

    private val _currentAge = MutableLiveData(user.getAge())
    val currentAge: LiveData<String?>
        get() = _currentAge

    private val _currentGender = MutableLiveData(user.getGender())
    val currentGender: LiveData<String?>
        get() = _currentGender

    //currentDataList livedata. not used atm.
    private val _currentInputList = MutableLiveData(ArrayList<DataDTO>(0))
    val currentDataList: MutableLiveData<ArrayList<DataDTO>>
        get() = _currentInputList

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

    fun setGraph(graph: GraphView){
        graphData.setGraphData(graph)
    }

    fun getGraph(): GraphView? {
        return graphData.getGraphData()
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

    //updates userInputList to hold all values of given type and date range. the values updated depend on categories booleans in model
    fun updateInputData () {
        userInputList = arrayOfNulls(3)
        val firebaseUser = getCurrentFirebaseUser()!!
        val startDate = graphOptionData.getSelectedDates()[0]
        val endDate = graphOptionData.getSelectedDates()[1]

        resetUseInputList()

        if (startDate != null && endDate != null) {
            //if kulhydrat == true
            if (graphOptionData.categorySelected(CARB)) {
                data.updateUserData(firebaseUser, "kulhydrat", startDate, endDate) {
                    userInputList[CARB] = it as ArrayList<DataDTO>
                    lineData[CARB] = LineData(CARB)
                }
            }

            //if insulin == true (set true temporarily for testing)
            if (graphOptionData.categorySelected(INSULIN)) {
                data.updateUserData(firebaseUser, "insulin", startDate, endDate) {
                    userInputList[INSULIN] = it as ArrayList<DataDTO>
                    lineData[INSULIN] = LineData(INSULIN)

                }
            }

            //if blodsukker == true
            if (graphOptionData.categorySelected(BLOODSUGAR)) {
                data.updateUserData(firebaseUser, "blodsukker", startDate, endDate) {
                    userInputList[BLOODSUGAR] = it as ArrayList<DataDTO>
                    lineData[BLOODSUGAR] = LineData(BLOODSUGAR)
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
            resetUserData()
            userDataList = it as ArrayList<DataDTO>
            if (userDataList.size != 0) {
                user.setUsername(userDataList.get(1).getInputOneAsString())
                _currentUsername.value = user.getUsername()
                user.setGender(userDataList.get(0).getInputOneAsString())
                _currentGender.value = user.getGender()
                user.setAge(userDataList.get(0).getInputTwoAsString())
                _currentAge.value = user.getAge()
            }
        }
    }

    //resets current username gender and age to empty strings
    fun resetUserData(){
        _currentUsername.value = ""
        _currentGender.value = ""
        _currentAge.value = ""
    }

    private fun resetUseInputList() {
        for ((index, item) in userInputList.withIndex()) {
            userInputList[index] = null
        }
    }

    //returns userDataList
    fun getDataList(): ArrayList<DataDTO> {
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

    //sets the selected date
    fun setDateSelected(date: Date) {
        graphOptionData.setDateSelected(date)
    }

    //returns userinputlist
    fun getInputList(): Array<ArrayList<DataDTO>?> {
    fun getCopySelectedDates(): Array<Date?> {
        return graphOptionData.getCopySelectedDates()
    }

    fun getInputList(): Array<ArrayList<InputDTO>?> {
        return userInputList
    }

    fun getLineData(): Array<LineData?> {
        return lineData
    }
}