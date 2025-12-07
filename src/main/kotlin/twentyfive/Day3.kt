package twentyfive

import Day

class Day3: Day(fileName = "2025/day3", isResource = true) {

    override fun processInput(lines: Int?) {
        var sum = 0L
        // PART ONE
//        input.forEachIndexed { i, bank ->
//            var currLargest = '0'
//            var next = '0'
//            var first = -1
//            var second = -1
//            bank.forEachIndexed { index, battery ->
//                if (battery > currLargest) {
//                    if (index < bank.length - 1) {
//                        currLargest = battery
//                        next = '0'
//                        first = index
//                    } else {
//                        next = battery
//                        second = index
//                    }
//                } else if (battery > next) {
//                    next = battery
//                    second = index
//                }
//            }
//
//            println("$i:\t$bank\t->\t$currLargest$next")
//            println("\t\t${bank.substring(0 until first)}*${bank[first]}*${bank.substring(first + 1 until second)}*${bank[second]}*${bank.substring(second + 1)}")
//            sum += "$currLargest$next".toLong()
//        }

        // PART TWO
        val numValues = 12

        input.forEachIndexed { i, bank ->
            val biggestValues: IntArray = IntArray(12) { 0 }

            var numFound = 0
            var currLargestIndex = 0

            while (numFound < numValues) {
                for (index in currLargestIndex ..bank.length - (numValues - numFound)) {
                    val battery = bank[index]

                    if (battery > bank[currLargestIndex]) {
                        currLargestIndex = index
                    }
                }
                biggestValues[numFound] = currLargestIndex
                currLargestIndex++
                numFound++
            }

            val currSum = biggestValues.map { bank[it] }.joinToString("").toLong()
            sum += currSum
        }

        println("Sum = $sum")
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day3().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}