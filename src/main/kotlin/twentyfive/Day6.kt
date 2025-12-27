package twentyfive

import Day

class Day6: Day(fileName = "2025/day6", isResource = true) {

    override fun processInput(lines: Int?) {

        val operands: MutableMap<Int, List<Long>> = mutableMapOf()
        val operators: MutableList<String> = mutableListOf()
        input.forEachIndexed { lineNumber, line ->
            if (lineNumber < input.lastIndex) {
                operands[lineNumber] = line.split(" ")
                    .filter { it.isNotBlank() }
                    .map { it.trim() }
                    .mapNotNull { it.toLongOrNull() }
            } else {
                operators.addAll(
                    line.split(" ")
                        .filter { it.isNotBlank() }
                        .map { it.trim() }
                )
            }
        }

        operands.forEach { (lineNumber, operand) ->
            println("$lineNumber: $operand")
        }

        println("$operators")

        var total = 0L
        for (i in operators.indices) {
            val currentOperands = operands.map { it.value[i] }
            val operator = operators[i]

            val lineResult = when (operator) {
                "+" -> currentOperands.reduce { acc, value -> acc + value }
                "-" -> currentOperands.reduce { acc, value -> acc - value }
                "*" -> currentOperands.reduce { acc, value -> acc * value }
                "/" -> currentOperands.reduce { acc, value -> acc / value }
                else -> 0
            }

            println("Line Result: $lineResult")
            total += lineResult
        }

        println("Total: $total")
    }
}

// 5524274308182

fun main() {
    val startTime = System.nanoTime()

    Day6().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}