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
            for (i in range) {
                val currString = i.toString()
                if (currString.length % 2 != 0) {
                    continue
                }

                val parts = currString.chunked(currString.length / 2)
                val firstHalf = parts[0]
                val secondHalf = parts[1]

                if (firstHalf == secondHalf) {
                    sum += i
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