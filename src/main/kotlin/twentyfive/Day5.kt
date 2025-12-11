package twentyfive

import Day

class Day5: Day(fileName = "2025/day5", isResource = true) {

    override fun processInput(lines: Int?) {
        val ranges: MutableMap<Long,Long> = mutableMapOf()
        val ingredients: MutableList<Long> = mutableListOf()

        input.forEach { line ->
            val splits = line.split("-")

            if (splits.size > 1) {
                val first = splits[0].toLong()
                val last = splits[1].toLong()

                var added = false
                for ((k, v) in ranges) {
                    if (first <= k && (last in k..v)) {
                        ranges.remove(k)
                        ranges[first] = v
                        added = true
                    } else if (last >= v && (first in k..v)) {
                        ranges[k] = last
                        added = true
                    }

                    if (added) break
                }


                if (!added) {
                    ranges[first] = last
                }
            } else if (splits.isNotEmpty()) {
                ingredients.add(line.toLong())
            }
        }

        println("Ranges")
        for ((k, v) in ranges) {
            println("$k-$v")
        }

        println("")

        println("Ingredients ${ingredients.size}")
        for (ingredient in ingredients) {
            println("$ingredient")
        }

        var fresh = 0
        for (ingredient in ingredients) {
            for ((k, v) in ranges) {
                if (ingredient in k..v) {
                    println("$ingredient -> [$k, $v]")
                    fresh++
                    break
                }
            }
        }

        println("Ingredients $fresh / ${ingredients.size}")
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day5().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}