package com.example.openlog.model

class InputDTO(var time: String, var value: String) {
    fun getInputTime(): String{
        return time
    }
    fun getInputValue(): String {
        return value
    }
}