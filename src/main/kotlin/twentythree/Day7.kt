package twentythree

import Day
class Day7 : Day("2023/day7.txt", true) {
    companion object {
        val CARD_VALUE_MAP = mapOf(
            '2' to 2,
            '3' to 3,
            '4' to 4,
            '5' to 5,
            '6' to 6,
            '7' to 7,
            '8' to 8,
            '9' to 9,
            'T' to 10,
            'J' to 1,
            'Q' to 12,
            'K' to 13,
            'A' to 14,
        )
    }

    override fun processInput(lines: Int?) {
        val hands = mutableListOf<Hand>()
        for (line in input) {
            line.parseHand()?.let {
                hands.add(it)
            }
        }

        val handsArray = hands.toTypedArray().apply {
            bubbleSort(this, Hand::isBetterThan)
        }

        var totalWinnings = 0L
        handsArray.forEachIndexed { index, hand ->
            totalWinnings += (index + 1) * hand.wager
        }
        println("Total Winnings: $totalWinnings")
    }

    data class Hand(val cards: String, val wager: Int) {
        private val handMap: Map<Int, MutableList<Char>> = mapOf(
            1 to mutableListOf(),
            2 to mutableListOf(),
            3 to mutableListOf(),
            4 to mutableListOf(),
            5 to mutableListOf(),
            6 to mutableListOf(),
        )

        init {
            populateMap()
        }

        fun value(): Int {
            return when {
                fiveOfAKind() -> 100
                fourOfAKind() -> 90
                fullHouse() -> 80
                threeOfAKind() -> 70
                twoPair() -> 60
                pair() -> 50
                highCard() -> 40
                else -> 0
            }
        }

        fun fiveOfAKind(): Boolean = (numberOfQuintuples() == 1) ||
                (numberOfQuadruples() == 1 && numberOfJacks() == 1) ||
                (numberOfTriples() == 1 && numberOfJacks() == 2) ||
                (numberOfPairs() == 1 && numberOfJacks() == 3) ||
                (numberOfSingles() == 1 && numberOfJacks() == 4) ||
                (numberOfJacks() == 5)
        fun fourOfAKind(): Boolean = (numberOfQuadruples() == 1) ||
                (numberOfTriples() == 1 && numberOfJacks() == 1) ||
                (numberOfPairs() == 1 && numberOfJacks() == 2) ||
                (numberOfJacks() == 4)
        fun threeOfAKind(): Boolean = (numberOfTriples() == 1 && numberOfSingles() == 2) ||
                (numberOfPairs() == 1 && numberOfJacks() == 1) ||
                (numberOfSingles() >= 1 && numberOfJacks() == 2) ||
                (numberOfSingles() == 2 && numberOfJacks() == 3)
        fun fullHouse(): Boolean = (numberOfTriples() == 1 && numberOfPairs() == 1) ||
                (numberOfPairs() == 2 && numberOfJacks() == 1) ||
                (numberOfPairs() == 1 && numberOfJacks() == 2) ||
                (numberOfPairs() == 1 && numberOfJacks() == 3)
        fun twoPair(): Boolean = (numberOfPairs() == 2) ||
                (numberOfPairs() == 1 && numberOfJacks() == 1) ||
                (numberOfSingles() == 3 && numberOfJacks() == 2)
        fun pair(): Boolean = (numberOfPairs() == 1) ||
                (numberOfSingles() == 4 && numberOfJacks() == 1) ||
                (numberOfJacks() == 2)
        fun highCard(): Boolean = numberOfSingles() == 5

        fun numberOfJacks(): Int = handMap[6]?.size ?: 0
        fun numberOfQuintuples(): Int = handMap[5]?.size ?: 0
        fun numberOfQuadruples(): Int = handMap[4]?.size ?: 0
        fun numberOfTriples(): Int = handMap[3]?.size ?: 0
        fun numberOfPairs(): Int = handMap[2]?.size ?: 0
        fun numberOfSingles(): Int = handMap[1]?.size ?: 0

        private fun populateMap() {
            val values = "23456789TQKA"

            values.toCharArray().forEach { currValue ->
                val count = cards.count {
                    it == currValue
                }
                handMap[count]?.add(currValue)
            }

            val jCount = cards.count { it == 'J'}
            for (x in 0 until jCount) handMap[6]?.add('J')
        }

        fun isBetterThan(otherHand: Hand): Boolean {
            if (this.value() == otherHand.value()) {
                for (x in cards.indices) {
                    val thisCard = cards[x]
                    val otherCard = otherHand.cards[x]

                    val thisCardVal = CARD_VALUE_MAP[thisCard]!!
                    val otherCardVal = CARD_VALUE_MAP[otherCard]!!
                    if (thisCardVal > otherCardVal) {
                        return true
                    } else if (thisCardVal < otherCardVal) {
                        return false
                    }
                }
                return true
            } else if (this.value() > otherHand.value()) {
                return true
            } else {
                return false
            }
        }
    }
}

fun String.parseHand(): Day7.Hand? {
    val hand = """([23456789TJQKA]{5})\s(\d+)""".toRegex()
    hand.find(this)?.let {
        return Day7.Hand(it.groupValues[1], it.groupValues[2].toInt())
    }
    return null
}

fun <T> bubbleSort(array: Array<T>, compare: (T, T) -> Boolean) {
    val n = array.size
    for (i in 0 until n - 1) {
        for (j in 0 until n - i - 1) {
            if (compare(array[j], array[j + 1])) {
                val temp = array[j]
                array[j] = array[j + 1]
                array[j + 1] = temp
            }
        }
    }
}

fun main(args: Array<String>) {
    val startTime = System.currentTimeMillis()
    Day7().processInput()
    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
