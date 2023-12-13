package twentythree

import Day

class Day4 : Day("2023/day4.txt", true) {
    override fun processInput(lines: Int?) {
        val pattern = """\d+""".toRegex()
        val points = mutableListOf<Int>()
        val scratchCards = mutableListOf<ScratchCard>()

        for ((currentLine, line) in input.withIndex()) {
            if (line.isEmpty()) continue

            val currScratchCards = line.split("|")
            val firstPart = currScratchCards[0].split(":")
            val winningNumbers = pattern.findAll(firstPart[1]).map { it.value.toInt() }.toMutableList()
            val givenNumbers = pattern.findAll(currScratchCards[1]).map { it.value.toInt() }.toMutableList()

            scratchCards.add(ScratchCard(currentLine, winningNumbers, givenNumbers))
        }

        for (scratchCard in scratchCards) {
            val matches = scratchCard.matches()
            for (instance in 0 until scratchCard.count) {
                for (x in 1..matches) {
                    scratchCards[scratchCard.id + x].count++
                }
            }
            points.add(scratchCard.score())
        }
        println("Sum:\t\t\t${points.sum()}")
        println("Total Cards:\t${scratchCards.sumOf { scratchCard -> scratchCard.count }}")
    }

    data class ScratchCard(val id: Int, val winningNumbers: List<Int>, val gameNumbers: List<Int>) {
        var count = 1

        fun score(): Int {
            var pointSum = 0
            for (gameNumber in gameNumbers) {
                if (winningNumbers.contains(gameNumber)) {
                    pointSum = 1.coerceAtLeast(pointSum * 2)
                }
            }
            return pointSum
        }

        fun matches(): Int {
            var matches = 0
            for (gameNumber in gameNumbers) {
                if (winningNumbers.contains(gameNumber)) {
                    matches++
                }
            }
            return matches
        }
    }
}

fun main(args: Array<String>) {
    Day4().processInput()
}