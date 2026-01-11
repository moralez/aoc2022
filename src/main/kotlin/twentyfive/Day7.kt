package twentyfive

import Day
import java.math.BigInteger

class Day7 : Day(fileName = "2025/day7", isResource = true) {

    companion object {
        private const val START = 'S'
        private const val SPLITTER = '^'
        private const val BEAM = '|'
        private const val SPACE = '.'
    }

    override fun processInput(lines: Int?) {
        val beamMap: MutableMap<Int, List<Int>> = mutableMapOf()
        val beams2: MutableMap<Int, BigInteger> = mutableMapOf()
        var splits = 0
        input.forEachIndexed { lineNumber, line ->
            if (lineNumber == 0) {
                val start = line.indexOf(START)
                beamMap[lineNumber] = listOf(start)
                beams2[start] = BigInteger.ONE
            } else {
                val beams = beamMap[lineNumber - 1]
                val newBeams = mutableSetOf<Int>()
                if (beams != null) {
                    for (beam in beams) {
                        val currTarget = line[beam]
                        when (currTarget) {
                            SPACE -> {
                                newBeams.add(beam)
                            }
                            SPLITTER -> {
                                splits++
                                val leftSplitBeamIndex = beam - 1
                                val rightSplitBeamIndex = beam + 1
                                if (leftSplitBeamIndex >= 0) {
                                    newBeams.add(leftSplitBeamIndex)
                                }
                                if (rightSplitBeamIndex < line.lastIndex) {
                                    newBeams.add(rightSplitBeamIndex)
                                }
                            }
                        }
                    }

                    beamMap[lineNumber] = newBeams.toList()
                }

                val splitterIndices = line.indices.filter { line[it] == SPLITTER }

                for (splitterIndex in splitterIndices) {
                    val currWeight = beams2[splitterIndex]
                    if (currWeight == null || currWeight == BigInteger.ZERO) continue

                    if (splitterIndex > 0) {
                        val existingLeftWeight = beams2[splitterIndex - 1] ?: BigInteger.ZERO
                        beams2[splitterIndex - 1] = currWeight + existingLeftWeight
                    }

                    if (splitterIndex < line.lastIndex) {
                        val existingRightWeight = beams2[splitterIndex + 1] ?: BigInteger.ZERO
                        beams2[splitterIndex + 1] = currWeight + existingRightWeight
                    }
                    beams2[splitterIndex] = BigInteger.ZERO
                }
            }
        }

//        beamMap.forEach {
//            println(it)
//        }

        var huh = BigInteger.ZERO
        beams2.forEach { (_, value) ->
            huh = maxOf(huh, value)
        }
        println("huh = $huh")

        val result = solvePart2(input)
        println("Result $result")
    }

    fun solvePart2(grid: List<String>): BigInteger {
        val rowCount = grid.size
        val colCount = grid[0].length

        val start = grid.indices
            .firstNotNullOf { r ->
                grid[r].indexOf('S').takeIf { it != -1 }?.let { r to it }
            }

        val memo = HashMap<Pair<Int, Int>, BigInteger>()

        fun countTimelinesFrom(position: Pair<Int, Int>): BigInteger =
            memo.getOrPut(position) {
                var (row, col) = position

                while (++row < rowCount) {
                    if (grid[row][col] == '^') {
                        var total = BigInteger.ZERO
                        if (col > 0) total += countTimelinesFrom(row to col - 1)
                        if (col + 1 < colCount) total += countTimelinesFrom(row to col + 1)
                        return@getOrPut total
                    }
                }

                BigInteger.ONE
            }

        return countTimelinesFrom(start)
    }
}

// 2036573051 TOO LOW
// 35078008003485 TOO LOW

fun main() {
    val startTime = System.nanoTime()

    Day7().processInput()

    val endTime = System.nanoTime()
    val duration = (endTime - startTime) / 1_000_000

    println("Execution time: $duration ms")
}