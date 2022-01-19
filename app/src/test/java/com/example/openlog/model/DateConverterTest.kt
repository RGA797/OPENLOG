package com.example.openlog.model

import org.junit.Assert.*

import org.junit.Test
import java.util.*

class DateConverterTest {

    val DateConverter = DateConverter()

    //if date in middle of range values, should return true
    @Test
    fun dateInRangeForMiddlingValues() {
        val dateQuery: String = Date(2022, 0, 12, 2, 1).toString()
        val dateRangeStart: Date = Date(2022, 0, 12, 1, 1)
        val dateRangeEnd: Date = Date(2022, 0, 12, 3, 1)
        assertEquals(true, DateConverter.dateInRange(dateQuery, dateRangeStart, dateRangeEnd))
    }

    //if date is in exact range, should return false
    @Test
    fun dateInRangeForExactValues() {
        val dateQuery: String = Date(2022, 0, 12, 1, 1).toString()
        val dateRangeStart: Date = Date(2022, 0, 12, 1, 1)
        val dateRangeEnd: Date = Date(2022, 0, 12, 1, 1)
        assertEquals(false, DateConverter.dateInRange(dateQuery, dateRangeStart, dateRangeEnd))
    }

    //if given date is out of range, should return false
    @Test
    fun dateInRangeForOutOfBoundsValues() {
        val dateQuery: String = Date(2022, 0, 12, 2, 1).toString()
        val dateRangeStart = Date(2022, 0, 12, 3, 1)
        val dateRangeEnd = Date(2022, 0, 12, 4, 1)
        assertEquals(false, DateConverter.dateInRange(dateQuery, dateRangeStart, dateRangeEnd))
    }

    //tests wether or not a date can be converted back and forth between being a string and Java.Util.Date
    @Test
    fun convertStringToDate() {
        val date = Date(2022, 0, 12, 1, 1)
        val dateString = date.toString()
        assertEquals(dateString, DateConverter.convertStringToDate(dateString).toString())
    }
}