package com.example.openlog.model

//This is a class meant to consist of the two key value pairs, fetched from a firebase database.
class InputDTO(var firstInput: String, var secondInput: String) {

    //gets the first value (ex, a number for age, insulin, kulhydrat, blodsukker etc.)
    fun getInputOne(): String{
        return firstInput
    }

    //gets the second value (ex date or gender.)
    fun getInputTwo(): String {
        return secondInput
    }
}