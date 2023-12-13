package twentythree

import Day
import com.sun.nio.sctp.InvalidStreamException
import java.security.InvalidParameterException
import kotlin.math.max

class Day2 : Day("2023/day2.txt", true) {
    override fun processInput(lines: Int?) {
        val testRound = Round(12, 13, 14)
        val validGames = mutableListOf<Game>()
        var powerSum = 0

        for (line in input) {
            val gameAndBalls = line.split(":")
            if (gameAndBalls.size != 2)
                throw InvalidStreamException()

            val gameRegEx = """Game (\d+)""".toRegex()
            val redBallRegEx = """(\d+) red""".toRegex()
            val greenBallRegEx = """(\d+) green""".toRegex()
            val blueBallRegEx = """(\d+) blue""".toRegex()

            val gameID = gameRegEx.find(gameAndBalls[0])?.groups?.get(1)?.value ?: throw InvalidParameterException()

            val currentGame = Game(gameID.toInt())

            val rounds = gameAndBalls[1].split(";")
            for (round in rounds) {
                val redBalls = redBallRegEx.find(round)
                val greenBalls = greenBallRegEx.find(round)
                val blueBalls = blueBallRegEx.find(round)

                val red = redBalls?.groups?.get(1)?.value?.toInt()
                val green = greenBalls?.groups?.get(1)?.value?.toInt()
                val blue = blueBalls?.groups?.get(1)?.value?.toInt()

                val newRound = Round(red, green, blue)
                currentGame.addRound(newRound)
            }

            if (currentGame.validCase(testRound)) {
                currentGame.prettyPrint()
                println("Valid Game: ${currentGame.id}")
                validGames.add(currentGame)
            }

            val currentGamePower = currentGame.power()
            powerSum += currentGamePower
            println("Game power: $currentGamePower")
        }

        println("Final Sum: ${validGames.sumOf { it.id }}")
        println("Power Sum: $powerSum")
    }
}

class Game(val id: Int) {
    private val rounds: MutableList<Round> = mutableListOf()

    fun addRound(round: Round) {
        rounds.add(round)
    }

    fun prettyPrint() {
        println("Game ID: $id")
        for (round in rounds) {
            print("\tred:\t${round.red ?: 0}")
            print("\tgreen:\t${round.green ?: 0}")
            print("\tblue:\t${round.blue ?: 0}")
            println()
        }
    }

    fun validCase(given: Round): Boolean {
        var possible = true
        for (round in rounds) {
            possible = possible && (
                (given.red ?: 0) >= (round.red ?: 0) &&
                (given.green ?: 0) >= (round.green ?: 0) &&
                (given.blue ?: 0) >= (round.blue ?: 0)
            )
        }
        return possible
    }

    private fun minimumNecessary(): Round {
        var red = 0
        var green = 0
        var blue = 0
        for (round in rounds) {
            red = max(red, round.red ?: 0)
            green = max(green, round.green ?: 0)
            blue = max(blue, round.blue ?: 0)
        }
        return Round(red, green, blue)
    }

    fun power(): Int = minimumNecessary().power
}

data class Round(val red: Int?, val green: Int?, val blue: Int?) {
    val power = (red ?: 0) * (blue ?: 0) * (green ?: 0)
}

fun main(args: Array<String>) {
    Day2().processInput()
}
