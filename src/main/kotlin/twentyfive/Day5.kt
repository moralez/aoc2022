package twentyfive

import Day

/**
 * If you come across this, I'm not super proud of all the hoops I jumped through to get an answer
 */
class Day5: Day(fileName = "2025/day5", isResource = true) {

    override fun processInput(lines: Int?) {
        val ranges1: MutableMap<Long,Long> = mutableMapOf()
        val ranges: MutableMap<Long,Long> = mutableMapOf()
        val pairs: MutableList<Pair<Long,Long>> = mutableListOf()
        val ingredients: MutableList<Long> = mutableListOf()

        input.forEach { line ->
            val splits = line.split("-")

            if (splits.size > 1) {
                val first = splits[0].toLong()
                val last = splits[1].toLong()

                val currPair = first to last
                if (!pairs.contains(currPair)) {
                    pairs.add(first to last)
                }

                ranges[first] = last

                var added = false
                for ((k, v) in ranges1) {
                    if (first <= k && (last in k..v)) {
                        ranges1.remove(k)
                        ranges1[first] = v
                        added = true
                    } else if (last >= v && (first in k..v)) {
                        ranges1[k] = last
                        added = true
                    }

                    if (added) break
                }


                if (!added) {
                    ranges1[first] = last
                }
            } else if (splits.isNotEmpty()) {
                ingredients.add(line.toLong())
            }
        }

        var fresh = 0
        for (ingredient in ingredients) {
            for ((k, v) in ranges1) {
                if (ingredient in k..v) {
                    fresh++
                    break
                }
            }
        }

        println(" --- ")

        do {
            println(" Looping numRanges = ${ranges.size}")
            var modified = false
            for ((k, v) in ranges) {
                println("Checking $k to $v")
                for ((newK, newV) in ranges) {
                    if (k == newK && v == newV) continue
                    if (k in newK..newV) {
                        println("start overlap! $k overlaps $newK to $newV")
                        if (v > newV) {
                            println("New end range $v > $newV")
                            print("\t[$k, $v] + [$newK, $newV] -> [$newK, $v]")
                            ranges.remove(k)
                            ranges[newK] = v
                            println("\t Confirm [$newK, ${ranges[newK]}]")
                            modified = true
                            break
                        } else {
                            println("End range $v already accounted for")
                            ranges.remove(k)
                            modified = true
                            break
                        }
                    } else if (v in newK..newV) {
                        println("end overlap! $v overlaps $newK to $newV")
                        print("\t[$k, $v] + [$newK, $newV] -> [$k, $newV]")
                        ranges.remove(newK)
                        ranges[k] = newV
                        println("\t Confirm [$k, ${ranges[k]}]")
                        modified = true
                        break
                    }
                }

                if (modified) break
            }
        } while (modified)

        println("<------ PAIRS START ")

        val sortedPairs = pairs.sortedBy { it.first }
        for (pair in sortedPairs) {
            println("${pair.first}-${pair.second}")
        }

        var numValidIds = 0L
        var c_max = 0L
        for (pair in sortedPairs) {
            if (pair.second >= c_max) {
                numValidIds += (pair.second) - maxOf(pair.first, c_max) + 1
                c_max = pair.second + 1
            }
        }

        println("Reddit --- $numValidIds")

        do {
            println(" Pair Looping numRanges = ${pairs.size}")
            var modified = false
            for (pair in pairs) {
                val k = pair.first
                val v = pair.second
                println("Checking $k to $v")
                for (newPair in pairs) {
                    val newK = newPair.first
                    val newV = newPair.second
                    if (k == newK && v == newV) continue
                    if (k in newK..newV) {
                        println("start overlap! $k overlaps $newK to $newV")
                        if (v > newV) {
                            println("New end range $v > $newV")
                            print("\t[$k, $v] + [$newK, $newV] -> [$newK, $v]")
                            pairs.remove(newPair)
                            pairs.remove(pair)
                            pairs.add(newK to v)
                            modified = true
                            break
                        } else {
                            println("End range $v already accounted for")
                            pairs.remove(pair)
                            modified = true
                            break
                        }
                    } else if (v in newK..newV) {
                        println("end overlap! $v overlaps $newK to $newV")
                        print("\t[$k, $v] + [$newK, $newV] -> [$k, $newV]")
                        pairs.remove(newPair)
                        pairs.remove(pair)
                        pairs.add(k to newV)
                        modified = true
                        break
                    }
                }

                if (modified) break
            }
        } while (modified)

        println("\n\n---- mine")
        for (pair in pairs.sortedBy { it.first }) {
            println("${pair.first}-${pair.second}")
        }

        for ((k,v) in pairs) {
            for ((k1,v1) in pairs) {
                if (k == k1 && v == v1) continue
                if (k in k1..v1 || v in k1..v1) {
                    println("overlap $k to $v ---- $k1 to $v1")
                }
            }
        }

        println("-----> PAIRS DONE")

        var total = 0L
        for ((k, v) in ranges) {
            println("$v - $k + 1 = ${v - k + 1}")
            total += (v - k + 1)
        }
        println("Ingredients $fresh / ${ingredients.size}")
        println("Ingredients $total")

        total = 0L
        for ((k, v) in pairs) {
            println("$v - $k + 1 = ${v - k + 1}")
            total += (v - k + 1)
        }
        println("Pairs: ${pairs.size}")
        println("Pairs Total: $total")

        // 354051800155727
        // 381294976037323
        // 347468726696869 NO
        // 343988099691839 NO
        // 347468726696869 NO
        // 329911315341652 NO
        // 329911315341744 NO
        // 347468726696960 NO
        // 365828739373111 NO
        // 347468726696961 YES
    }
}

fun main() {
    val startTime = System.nanoTime()

    Day5().processInput()

    val endTime = System.nanoTime()
    val duration = endTime - startTime

    println("Execution time: ${duration / 1_000_000} ms")
}