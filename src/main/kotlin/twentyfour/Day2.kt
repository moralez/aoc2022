package twentyfour

import Day
import util.SortedOrder
import util.getSortedOrder

class Day2: Day(fileName = "2024/day2.txt", isResource = true) {
    override fun processInput(lines: Int?) {
        var validReports = 0
        var validReportsWithDampener = 0
        for (line in input) {
            if (line.isEmpty()) continue

            line.split(" ")
                .map { it.toInt() }
                .let {
                    val report = Report(it)
                    if (report.isSafe()) validReports++
                    if (report.isSafe(applyDampener = true)) validReportsWithDampener++
                }
        }

        println("Valid Reports: $validReports")
        println("Valid Reports w/ Dampener: $validReportsWithDampener")
    }
}

private class Report(val values: List<Int>) {
    val differences: MutableList<Int> = mutableListOf()
    val sortedOrder: SortedOrder = values.getSortedOrder()

    init {
        for (i in 1 until values.size) {
            differences.add(values[i] - values[i - 1])
        }
    }

    fun isSafe(applyDampener: Boolean = false): Boolean {
        val differencesInRange = differences.all { it in 1..3 } || differences.all { it in -3..-1 }

        var dampenerWorks = false
        if (applyDampener && !differencesInRange) {
            val subReports = mutableListOf<Report>()
            for (i in values.indices) {
                subReports.add(
                    Report(
                        mutableListOf<Int>().apply {
                            addAll(values)
                            removeAt(i)
                        }
                    )
                )
            }

            for (subReport in subReports) {
                dampenerWorks = subReport.isSafe()
                if (dampenerWorks) break
            }
        }

        return differencesInRange || dampenerWorks
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day2().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}