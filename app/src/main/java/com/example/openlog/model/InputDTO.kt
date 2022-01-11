package com.example.openlog.model

import java.util.*

//This is a class meant to consist of the two key value pairs, fetched from a firebase database.
class InputDTO(var firstInput: String, var secondInput: String) {
    val dateConverter = DateConverter()

    //gets the first value (ex, a number for age, insulin, kulhydrat, blodsukker etc.)
    fun getInputOneAsString(): String{
        return firstInput
    }

    //gets the second value (ex date or gender.)
    fun getInputTwoAsString(): String {
        return secondInput
    }

    fun getInputTwoAsDate(): Date{
        val date = dateConverter.convertStringToDate(getInputTwoAsString())
        return date
        }
    }
