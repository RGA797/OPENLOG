package com.example.openlog.model

class InputDTO(var firstInput: String, var secondInput: String) {
    fun getInputOne(): String{
        return firstInput
    }
    fun getInputTwo(): String {
        return secondInput
    }
}