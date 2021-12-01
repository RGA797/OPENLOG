package com.example.openlog.model

class Data {
    var input: String? = null
    var new: Boolean = false

    fun changeInput(input: String){
        this.input = input
        new = true
    }

    fun changeNew(new: Boolean){
        this.new = new
    }

    fun getDataInput(): String? {
        return input
    }

    fun isNew(): Boolean {
        return new
    }
}