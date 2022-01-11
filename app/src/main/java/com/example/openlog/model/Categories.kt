package com.example.openlog.model

//holds a category array to know which types of input to fetch from database
class Categories {
    private var categories = arrayOf(false, false, false)

    fun getCategories(): Array<Boolean>{
        return categories
    }

    fun setCategories(isInsulin: Boolean, isBlodsukker: Boolean, isKulhydrat: Boolean,){
        categories[0] = isInsulin
        categories[1] = isBlodsukker
        categories[2] = isKulhydrat
    }

}