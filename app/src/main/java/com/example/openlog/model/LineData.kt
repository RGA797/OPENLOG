package com.example.openlog.model

const val YELLOW = 4293507916.toInt()
const val BLUE = 4280062934.toInt()
const val RED = 4293483617.toInt()

class LineData(type: Int) {
    var color: Int = 0
    var unit: String = ""

    init {
        when(type) {
            0 -> {
                color = YELLOW
                unit = "yellow"
            }
            1 -> {
                color = BLUE
                unit = "blue"
            }
            2 -> {
                color = RED
                unit = "red"
            }
        }
    }
}