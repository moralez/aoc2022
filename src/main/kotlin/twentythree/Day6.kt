package twentythree

import Day

class Day6 : Day("2023/day6.txt", true) {
    override fun processInput(lines: Int?) {
        val times = mutableListOf<Long>()
        val records = mutableListOf<Long>()
        println("Max Int: ${Int.MAX_VALUE}")
        for (line in input) {
            when {
                line.contains("Time") -> times.addAll(processInputTwo(line))
                line.contains("Distance") -> records.addAll(processInputTwo(line))
            }
        }

        val games = times.zip(records).map { (time, record) -> Game(time, record) }
        val winningTotals = mutableListOf<Long>()
        games.forEach {
            println("$it")
            winningTotals.add(it.winningTimes())
//            winningTotals.add(it.winningTimesOptimized())
        }
        println("Product Sum: ${winningTotals.reduce { acc, i -> acc * i }}")
    }

    data class Game(val time: Long, val record: Long) {
        fun winningTimes(): Long {
            val winning = mutableListOf<Long>()
            for (timeHeld in 1 until time) {
                val totalDistanceTraveled = (time - timeHeld) * timeHeld
                if (totalDistanceTraveled > record) {
                    winning.add(timeHeld)
                }
            }
//            println("Winning: ${winning.size} $winning")
            return winning.size.toLong()
        }

        fun winningTimesOptimized(): Long {
            var minWin = 0L
            var maxWin = 0L
            for (timeHeld in 1 until time) {
                val totalDistanceTraveled = (time - timeHeld) * timeHeld
                if (totalDistanceTraveled > record) {
                    minWin = timeHeld
                    break
                }
            }

            for (timeHeld in time downTo 1) {
                val totalDistanceTraveled = (time - timeHeld) * timeHeld
                if (totalDistanceTraveled > record) {
                    maxWin = timeHeld
                    break
                }
            }
//            println("Winning: ${winning.size} $winning")
            return maxWin - minWin
        }
    }

    private fun processInput(input: String): List<Long> =
        input.split(" ").mapNotNull { it.toLongOrNull() }

    private fun processInputTwo(input: String): List<Long> =
        listOf(
            input.split(" ")
                .mapNotNull { it.toIntOrNull() }
                .joinToString("")
                .toLong()
        )
}

fun main(args: Array<String>) {
    val startTime = System.currentTimeMillis()
    Day6().processInput()
    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}