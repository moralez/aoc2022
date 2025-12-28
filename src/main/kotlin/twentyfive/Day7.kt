package twentyfive

import Day

class Day7 : Day(fileName = "2025/day7", isResource = true) {

    companion object {
        private const val START = 'S'
        private const val SPLITTER = '^'
        private const val BEAM = '|'
        private const val SPACE = '.'
    }

    override fun processInput(lines: Int?) {
        val beamMap: MutableMap<Int, List<Int>> = mutableMapOf()
        var splits = 0
        input.forEachIndexed { lineNumber, line ->
            if (lineNumber == 0) {
                val start = line.indexOf(START)
                beamMap[lineNumber] = listOf(start)
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
            }
        }

        beamMap.forEach {
            println(it)
        }
        println("Splits = $splits")
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day7().processInput()

    val endTime = System.nanoTime()
    val duration = (endTime - startTime) / 1_000_000

    println("Execution time: $duration ms")
}