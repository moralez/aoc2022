package twentyfour

import Day
import util.insertSorted
import kotlin.math.abs

class Day1: Day(fileName = "2024/day1.txt", isResource = true) {
    val inputPattern = """(\d+)\s+(\d+)""".toRegex()

    override fun processInput(lines: Int?) {
        val listOne: MutableList<Int> = mutableListOf()
        val listTwo: MutableList<Int> = mutableListOf()

        for (line in input) {
            inputPattern.find(line)?.let {
                val first = it.groups[1]?.value?.toInt()!!
                val second = it.groups[2]?.value?.toInt()!!

                listOne.insertSorted(first)
                listTwo.insertSorted(second)
            }
        }

        var sum = 0
        for (index in listOne.indices) {
            val first = listOne[index]
            val second = listTwo[index]

            sum += abs(first - second)
        }

        println("Sum $sum")

        var partTwoSum = 0
        listOne.toSet().map { first ->
            partTwoSum += first * listTwo.count { first == it }
        }

        println("Part Two Sum $partTwoSum")
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day1().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}