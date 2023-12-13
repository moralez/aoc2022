import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonPrimitive
import java.util.LinkedList

fun main(args: Array<String>) {
    val day = Day13()
    day.processInput()
}
class Day13: Day(fileName = "D:\\Projects\\aoc2022\\src\\main\\resources\\day13.txt") {

    val gson = Gson()
    override fun processInput(lines: Int?) {
        log("Day 13...")
        partTwo()
    }

    fun partOne() {
        val pairs: MutableList<PacketPair> = mutableListOf()
        var currId = 1
        for (i in 0..input.size step 3) {
            val line1 = input[i]
            val line2 = input[i+1]

            val jsonArray1 = gson.fromJson(line1, JsonArray::class.java)
            val jsonArray2 = gson.fromJson(line2, JsonArray::class.java)

            val packetPair = PacketPair(currId++, Packet(jsonArray1), Packet(jsonArray2))
            pairs.add(packetPair)
        }

        var sum = 0
        for (pair in pairs) {
            val result = pair.compare()
            when (result) {
                Result.RIGHT -> {
                    println("\t\t- Left side is smaller, so inputs are in the right order")
                    sum += pair.id
                }
                Result.LEFT_TOO_FEW -> {
                    println("\t\t- Left side ran out of items, so inputs are in the right order")
                    sum += pair.id
                }
                else -> println("\t\t- Right side ran out of items, so inputs are not in the right order")
            }
        }

        println("Sum: $sum")
    }

    fun partTwo() { // 123 * 199 = 24477 <-- Answer
        val packets = LinkedList<Packet>()

        for (element in input) {
            if (element.isEmpty()) continue
            val jsonArray1 = gson.fromJson(element, JsonArray::class.java)
            val newPacket = Packet(jsonArray1)

            var addAtIndex = 0
            while (addAtIndex < packets.size) {
                val pair = PacketPair(0, packets[addAtIndex], newPacket)
                val result = pair.compare()
                if (result == Result.WRONG) {
                    println("Breaking [$addAtIndex]")
                    break
                }
                addAtIndex++
            }

            packets.add(addAtIndex, newPacket)
        }

        var curr = 1
        for (packet in packets) {
            println("Packet ${curr++}: ${packet.content}")
        }
    }

    class PacketPair(val id: Int, val left: Packet, val right: Packet) {
        fun compare(): Result {
            println("\n== Pair $id ==")
            println("- Compare ${left.content} vs ${right.content}")
            for (i in 0 until left.content.size()) {
                val leftNthElement = left.content[i]

                if (i >= right.content.size()) {
                    println("\t- Right side ran out of items, so inputs are not in the right order")
                    return Result.WRONG
                }

                val rightNthElement = right.content[i]

                println("\t- Compare $leftNthElement vs $rightNthElement")
                // Left Array, Right Array
                if (leftNthElement is JsonArray && rightNthElement is JsonArray) {
                    val result = compareValues(leftNthElement, rightNthElement, 1)
                    if (result != Result.SAME) return result
                }
                // Left Int, Right, Int
                else if (leftNthElement is JsonPrimitive && rightNthElement is JsonPrimitive) {
                    val result = compareValues(leftNthElement.asInt, rightNthElement.asInt, 1)
                    if (result != Result.SAME) return result
                } else if (leftNthElement is JsonArray && rightNthElement is JsonPrimitive) {
                    val result = convertAndCompare(leftNthElement, rightNthElement, 1)
//                    if (result == Result.RIGHT) return Result.RIGHT
                    if (result != Result.SAME) return result
                } else if (leftNthElement is JsonPrimitive && rightNthElement is JsonArray) {
                    val result = convertAndCompare(leftNthElement, rightNthElement, 1)
//                    if (result == Result.RIGHT) return Result.RIGHT
                    if (result != Result.SAME) return result
                }
            }

            return if (left.content.size() == right.content.size()) Result.SAME else Result.LEFT_TOO_FEW
        }

        fun compareValues(left: JsonArray, right: JsonArray, level: Int = 0): Result {
            println("${tabs(level)}- CompareArrays $left vs $right")
            for (i in 0 until left.size()) {
                val leftNth = left[i]
                if (i >= right.size()) {
                    println("${tabs(level)}- Right side ran out of items, so inputs are not in the right order")
                    return Result.WRONG
                }

                val rightNth = right[i]

                // Both Arrays
                if (leftNth is JsonArray && rightNth is JsonArray) {
                    val result = compareValues(leftNth, rightNth, level + 1)
                    return result
                }
                // Left Array, Right Primitive
                else if (leftNth is JsonArray && rightNth is JsonPrimitive) {
                    val result = convertAndCompare(leftNth, rightNth, level + 1)
                    return result
                }
                // Left Primitive, Right Array
                else if (leftNth is JsonPrimitive && rightNth is JsonArray) {
                    val result = convertAndCompare(leftNth, rightNth, level + 1)
                    return result
                }
                // Both Primitive
                else if (leftNth is JsonPrimitive && rightNth is JsonPrimitive) {
                    val result = compareValues(leftNth.asInt, rightNth.asInt, level + 1)
                    // This is the final result, we went to return if the ints were in the right order or the same
                    if (result != Result.SAME) return result
                }
                // What?
                else {
                    println("${tabs(level)}- Something is wrong. Probably an empty array")
                    return Result.WHAT
                }
            }

            return if (left.size() == right.size()) Result.SAME else Result.LEFT_TOO_FEW
        }

        fun compareValues(left: Int, right: Int, level: Int = 0): Result {
            println("${tabs(level)}- CompareInts $left vs $right")
            return when {
                left < right -> {
//                    println("${tabs(level)} Result.RIGHT")
                    Result.RIGHT
                }
                left > right -> {
//                    println("${tabs(level)} Result.WRONG")
                    Result.WRONG
                }
                else ->  {
//                    println("${tabs(level)} Result.SAME")
                    Result.SAME
                }
            }
        }

        fun convertAndCompare(left: JsonArray, right: JsonPrimitive, level: Int = 0): Result {
            println("${tabs(level)}- Mixed types; convert right to [${right.asInt}] and retry comparison")
            val rightArray = JsonArray().apply {
                add(right)
            }
            return compareValues(left, rightArray, level)
        }

        fun convertAndCompare(left: JsonPrimitive, right: JsonArray, level: Int = 0): Result {
            println("${tabs(level)}- Mixed types; convert left to [${left.asInt}] and retry comparison")
            val leftArray = JsonArray().apply {
                add(left)
            }
            return compareValues(leftArray, right, level)
        }

        fun tabs(level: Int): String {
            val sb = StringBuilder()
            for (i in 0..level) {
                sb.append("\t")
            }
            return sb.toString()
        }
    }

    class Packet(val content: JsonArray)

    enum class Result {
        RIGHT, WRONG, SAME, WHAT, LEFT_TOO_FEW
    }
}