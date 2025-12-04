package twentyfive

import Day
import kotlin.math.abs

class Day1: Day(fileName = "2025/day1", isResource = true) {

    val inputPattern = """^([LR])(\d+)""".toRegex()

    override fun processInput(lines: Int?) {
        val values: MutableList<Int> = mutableListOf()

        for (line in input) {
            inputPattern.find(line)?.let { matchResult ->
                val first = matchResult.groups[1]?.value?.let {
                    when (it) {
                        "L" -> -1
                        "R" -> 1
                        else -> 0
                    }
                } ?: 0
                val second = matchResult.groups[2]?.value?.toInt()!!

                values.add(first * second)
            }
        }

        var initial = 50
        var numZeros = 0

        /* Part One */
        /*
        for (value in values) {
            initial += value
            initial %= 100

            if(initial < 0) initial += 100

            if (initial == 0) numZeros++
        }
        */

        /* Part Two */
        for (value in values) {
            // value will be negative for L and positive for R
            val original = initial

            // count the full circles
            numZeros += abs(value / 100)

            // now this is just rotating by the remainder
            initial += (value % 100)

            // if it's not within the bounds, then 0 has been crossed or landed on
            if (initial !in 1 until 100) {
                if (!(original == 0 && initial < 0)) {
                    numZeros++
                }
            }

            if (initial < 0) {
                // we're below 0, so we need to adjust back to positive values
                initial += 100
            } else if (initial >= 100) {
                initial %= 100
            }
        }
        println("Num Zeros: $numZeros")
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day1().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}