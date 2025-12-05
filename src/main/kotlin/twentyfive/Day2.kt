package twentyfive

import Day

class Day2: Day(fileName = "2025/day2", isResource = true) {

    override fun processInput(lines: Int?) {
        val ranges: List<LongRange> = input
            .filter { it.isNotEmpty() }
            .flatMap { line ->
                line.split(",")
                    .map { ranges ->
                        val range = ranges.split("-")
                        range[0].toLong()..range[1].toLong()
                    }
                }

        var sum = 0L
        ranges.forEach { range ->
            for (value in range) {
                val currValueString = value.toString()
                for (j in 0 until currValueString.length / 2) {
                    val replaceString = currValueString.take(j + 1)
                    val newString = currValueString.replace(replaceString, "")
                    if (newString.isEmpty()) {
                        sum += value
                        break
                    }
                }
            }
        }

        println("Sum: $sum")
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day2().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}