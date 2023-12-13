package twentythree

import Day
import java.util.*

class Day1: Day("2023/day1.txt", true) {
    private val numberMap = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9,
    )

    private fun convertToInt(spelledNumber: String?): Int? {
        return if (spelledNumber.isNullOrEmpty()) {
            null
        } else {
            numberMap[spelledNumber.lowercase(Locale.getDefault())]
        }
    }

    override fun processInput(lines: Int?) {
        val partTwo = true

        val pairRegex = when {
            partTwo ->  """^\S*?(one|two|three|four|five|six|seven|eight|nine|ten|\d)\S*(one|two|three|four|five|six|seven|eight|nine|ten|\d)\S*?$""".toRegex()
            else -> """^\D*(\d)\S*(\d)\D*$""".toRegex()
        }
        val regex = when {
            partTwo ->  """^\S*?(one|two|three|four|five|six|seven|eight|nine|ten|\d)\S*?$""".toRegex()
            else -> """^\D*(\d)\D*$""".toRegex()
        }
        val values = mutableListOf<Int>()

        for (line in input) {
            val pairMatch = pairRegex.find(line)
            if (pairMatch != null) {
                val first = convertToInt(pairMatch.groups[1]?.value)
                val second = convertToInt(pairMatch.groups[2]?.value)
                if (first != null && second != null) {
                    val calibrationValue = "$first$second".toInt()
                    values.add(calibrationValue)
                    println("$calibrationValue")
                }
            } else {
                val singleMatch = regex.find(line)
                if (singleMatch != null) {
                    val first = convertToInt(singleMatch.groups[1]?.value)
                    if (first != null) {
                        val calibrationValue = "$first$first".toInt()
                        values.add(calibrationValue)
                        println("$calibrationValue")
                    }
                } else {
                    println("No Match for line: $line")
                }
            }
        }

        println("Sum: ${values.sum()}")
    }
}

fun main(args: Array<String>) {
    Day1().processInput()
}