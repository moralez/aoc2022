package twentyfour

import Day

class Day3 : Day(fileName = "2024/day3.txt", isResource = true) {
    val inputPattern = """mul\((\d+),(\d+)\)""".toRegex()
    val inputPatternPartTwo = """mul\((\d+),(\d+)\)|do\(\)|don't\(\)""".toRegex()

    private enum class ReadState {
        LOOKING,
        MUL,
        OPEN,
        NUMBER,
        CLOSE
    }

    override fun processInput(lines: Int?) {
        var state = ReadState.LOOKING
        val stack = mutableListOf<Char>()
        val currentNumber = StringBuilder()
        var first: Int? = null
        var second: Int? = null
        val found: MutableList<Pair<Int, Int>> = mutableListOf()
        val found2: MutableList<Pair<Int, Int>> = mutableListOf()
        var inputsEnabled = true

        for (line in input) {
            val searchResults = inputPatternPartTwo.findAll(line)
            for (match in searchResults) {
                println(match.groups[0])
                if (match.groups[0]?.value.equals("do()")) {
                    inputsEnabled = true
                } else if (match.groups[0]?.value.equals("don't()")) {
                    inputsEnabled = false
                } else {
                    if (inputsEnabled) {
                        found2.add(Pair(match.groups[1]!!.value.toInt(), match.groups[2]!!.value.toInt()))
                    }
                }
            }
        }

//        for (c in input.first()) {
//            when (c) {
//                'm' -> {
//                    state = ReadState.MUL
//                    stack.clear()
//                    stack.add(c)
//                }
//                'u' -> {
//                    if (
//                        (state != ReadState.MUL) ||
//                        (stack.last() != 'm')
//                    ) {
//                        state = ReadState.LOOKING
//                        stack.clear()
//                    } else {
//                        stack.add(c)
//                    }
//                }
//                'l' -> {
//                    if (
//                        (state != ReadState.MUL) ||
//                        (stack.last() != 'u')
//                    ) {
//                        state = ReadState.LOOKING
//                        stack.clear()
//                    } else {
//                        stack.add(c)
//                    }
//                }
//                '(' -> {
//                    if (
//                        (state != ReadState.MUL) ||
//                        (stack.last() != 'l')
//                    ) {
//                        state = ReadState.LOOKING
//                        stack.clear()
//                    } else {
//                        state = ReadState.OPEN
//                        stack.add(c)
//                    }
//                }
//                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
//                    if (
//                        state == ReadState.OPEN || state == ReadState.NUMBER
//                    ) {
//                        state = ReadState.NUMBER
//                        stack.add(c)
//                        currentNumber.append(c)
//                    } else {
//                        state = ReadState.LOOKING
//                        stack.clear()
//                    }
//                }
//                ',' -> {
//                    if (state != ReadState.NUMBER) {
//                        state = ReadState.LOOKING
//                        stack.clear()
//                    } else {
//                        if (currentNumber.isNotEmpty()) {
//                            first = currentNumber.toString().toInt()
//                            currentNumber.clear()
//                        } else {
//                            state = ReadState.LOOKING
//                            stack.clear()
//                        }
//                    }
//                }
//                ')' -> {
//                    if (state != ReadState.NUMBER || first == null) {
//                        state = ReadState.LOOKING
//                        stack.clear()
//                    } else {
//                        if (currentNumber.isNotEmpty()) {
//                            second = currentNumber.toString().toInt()
//                            currentNumber.clear()
//                            stack.clear()
//
//                            found.add(Pair(first, second))
//                        } else {
//                            state = ReadState.LOOKING
//                            stack.clear()
//                        }
//                    }
//                }
//                else -> {
//                    state = ReadState.LOOKING
//                    currentNumber.clear()
//                    stack.clear()
//                }
//            }
//        }

        println("Found Pairs: $found")
        println("Found2 Pairs: $found2")
        println("Sum: ${found.sumOf { it.first * it.second }}")
        println("Sum2: ${found2.sumOf { it.first * it.second }}")
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day3().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}