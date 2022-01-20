package com.example.openlog.model

const val GOLDEN = 4293507916.toInt()
const val BLUE = 4280062934.toInt()
const val RED = 4293483617.toInt()

class LineData(type: Int) {
    var color: Int = 0
    var title: String = ""

    init {
        when(type) {
            0 -> {
                color = GOLDEN
                title = "kulhydrater"
            }
            1 -> {
                color = BLUE
                title = "insulin"
            }
            2 -> {
                color = RED
                title = "blodsukker"
            }
        }
    }
}