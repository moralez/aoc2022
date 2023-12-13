package twentythree

import Day

class Day3 : Day("2023/day3.txt", true) {
    override fun processInput(lines: Int?) {
        val pattern = """\d+""".toRegex()
        val symbolPattern = """[^.\d]""".toRegex()
        val readings: MutableList<Reading> = mutableListOf()
        val symbols: MutableList<Symbol> = mutableListOf()

        for ((currentLine, line) in input.withIndex()) {
            pattern.findAll(line).forEach {
                try {
                    readings.add(Reading(it.value.toInt(), it.range, currentLine))
                } catch (_: NumberFormatException) {}
            }
            symbolPattern.findAll(line).forEach {
                try {
                    symbols.add(Symbol(it.value, Pair(it.range.first, currentLine)))
                } catch (_: NumberFormatException) {}
            }
        }

        readings.forEach { reading ->
            symbols.map {
                it.coordinates
            }.firstOrNull {
                reading.validPoints.contains(it)
            }.let { result ->
                if (result != null) {
                    reading.valid = true
                }
            }
        }

        val sum = readings.filter { it.valid }.sumOf { it.value }
        println("Sum: $sum")

        var crazy = 0
        symbols.filter { it.value == "*" }.forEach { symbol ->
            val neighbors = readings.filter { it.validPoints.contains(symbol.coordinates) }
            if (neighbors.size == 2) {
                crazy += (neighbors[0].value * neighbors[1].value)
            }
        }
        println("Crazy: $crazy")
    }

    data class Reading(val value: Int, val range: IntRange, val row: Int) {
        var valid: Boolean = false

        val validPoints = genValidPoints()

        private fun genValidPoints(): List<Pair<Int, Int>> {
            val points = mutableListOf<Pair<Int, Int>>()
            val minX = range.first - 1
            val maxX = range.last + 1
            val minY = row - 1
            val maxY = row + 1

            for (y in minY..maxY) {
                for (x in minX.. maxX) {
                    points.add(Pair(x, y))
                }
            }

            return points
        }
    }

    data class Symbol(val value: String, val coordinates: Pair<Int, Int>) {
        val neighbors: MutableList<Reading> = mutableListOf()
    }
}

fun main(args: Array<String>) {
    Day3().processInput()
}