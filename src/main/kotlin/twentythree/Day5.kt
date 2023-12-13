package twentythree

import Day
import java.security.InvalidParameterException

class Day5 : Day("2023/day5-sample.txt", true) {
    private val destSrcLengthPattern = """\d+ \d+ \d+""".toRegex()
    private val numberPairPattern = """\d+ \d+""".toRegex()
    override fun processInput(lines: Int?) {
        val rangesToCheck: MutableSet<MapRange> = mutableSetOf()
        var currMapType = MapType.SeedToSoil
        val maps: MutableMap<MapType, MutableList<Map>> = mutableMapOf()
        for (mapType in MapType.values()) {
            maps[mapType] = mutableListOf()
        }

        val newMaps: MutableMap<MapType, NewMap> = mutableMapOf(
            MapType.SeedToSoil to NewMap(MapType.SeedToSoil),
            MapType.SoilToFertilizer to NewMap(MapType.SoilToFertilizer),
            MapType.FertilizerToWater to NewMap(MapType.FertilizerToWater),
            MapType.WaterToLight to NewMap(MapType.WaterToLight),
            MapType.LightToTemperature to NewMap(MapType.LightToTemperature),
            MapType.TemperatureToHumidity to NewMap(MapType.TemperatureToHumidity),
            MapType.HumidityToLocation to NewMap(MapType.HumidityToLocation),
        )

        for (line in input) {
            when {
                line.contains("seeds") -> {
                    val seeds1 = processStartingRanges(line, partTwo = true)
                    println("Starting Seeds: $seeds1")
                    rangesToCheck.addAll(seeds1)

                }
                line.contains("map") -> {
                    currMapType = toggleMapType(line)
                }
                destSrcLengthPattern.matches(line) -> {
                    val currMap = processCurrMap(currMapType, line)
                    maps[currMapType]?.add(currMap)

                    val ranges = parseDstSrcRanges(currMapType, line)
                    newMaps[currMapType]?.let {
                        it.destinationRanges.add(ranges.first)
                        it.sourceRanges.add(ranges.second)
                    }
                }
                else -> continue
            }
        }

        // Part One
//        val path = mutableListOf<Long>()
//        val inputPath = mutableListOf<MapRange>()
//        val locations = mutableListOf<Long>()
//        for (input in rangesToCheck) {
//            path.add(input.start)
//
//            for (mapType in MapType.values()) {
//                var valueToAdd = path.last()
//
//                val listOfMaps = maps[mapType]
//                listOfMaps?.forEach {
//                    it.valueForSource(path.last())?.let { found ->
//                        valueToAdd = found
//                    }
//                }
//                path.add(valueToAdd)
//            }
//            println("Path: $path")
//            locations.add(path.last())
//            path.clear()
//        }
//        println("Min Location: $locations ${locations.minOrNull()}")

        // Start Overlap Tests
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 45L, 5L))
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 45L, 6L))
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 45L, 7L))

        // Encapsulated Test
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 55L, 7L))
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 55L, 1L))
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 50L, 43L))
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 99L, 1L))

        // End Overlap Test
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 99L, 7L))
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 55L, 1L))
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 50L, 43L))
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 99L, 1L))

        // Logen Test
//        rangesToCheck.add(MapRange(MapType.SeedToSoil, 40L, 100L))

//        while (rangesToCheck.firstOrNull { it.type != MapType.HumidityToLocation} != null) {
//            val mapRange = rangesToCheck.first { it.type != MapType.HumidityToLocation}
//            val removed = rangesToCheck.remove(mapRange)
//
//            println("Processing Input: $removed $mapRange")
//
//            newMaps[mapRange.type]?.let { newMap ->
//                val nextMapType = mapRange.type.next() //MapType.values()[mapRange.type.ordinal+1]
//                var found = false
//
//                if (mapRange.start <= newMap.absoluteMin() && mapRange.end >= newMap.absoluteMax()) {
//                    println("Input wraps all ranges")
//                    val listOfMapped = mutableListOf<MapRange>()
//                    val before = MapRange(nextMapType, mapRange.start, newMap.absoluteMin() - mapRange.start)
//                    val after = MapRange(nextMapType, newMap.absoluteMax()+1,  mapRange.end - newMap.absoluteMax())
//                    listOfMapped.add(before)
//                    listOfMapped.addAll(newMap.mappedRanges())
//                    listOfMapped.add(after)
//                    listOfMapped.forEach {
//                        println("\t$it")
//                    }
//                    found = true
//                }
//
//                for (x in 0 until newMap.sourceRanges.size) {
//                    if (found) break
//
//                    val sourceRange = newMap.sourceRanges[x]
//                    val destinationRange = newMap.destinationRanges[x]
//
//                    val newRanges = sourceRange.check(mapRange, destinationRange)
//                    if (newRanges.isNotEmpty()) {
//                        found = true
//                        println("Processed Ranges Added: $newRanges")
//                        rangesToCheck.addAll(newRanges)
//                    }
//                }
//
////                newMap.sourceRanges.forEachIndexed { index, sourceRange ->
////                    if (!found) {
////                        val newRanges = sourceRange.check(mapRange, newMap.destinationRanges[index])
////                        if (newRanges.isNotEmpty()) {
////                            found = true
////                            println("Processed Ranges Added: $newRanges")
////                            rangesToCheck.addAll(newRanges)
////                        }
////                    }
////                }
//
//                if (!found) {
//                    val fallbackMapping = MapRange(nextMapType, mapRange.start, mapRange.length)
//                    println("Fallback Ranges Added: $fallbackMapping")
//                    rangesToCheck.add(fallbackMapping)
//                }
//            }
//        }

        println("Ranges To Check:")
        rangesToCheck.forEach {
            println("\t$it")
        }
        println("Result: ${rangesToCheck.minOfOrNull { it.start }}")
    }

    private fun processSeeds(rawSeeds: String, partTwo: Boolean = false): MutableList<LongRange> {
        val splitSeeds = rawSeeds.split(":")
        return if (partTwo) {
            numberPairPattern.findAll(splitSeeds[1])
                .map { it.value.split(" ") }
                .map { Pair(it[0].toLong(), it[1].toLong()) }
                .map { LongRange(it.first, it.first + (it.second-1)) }
                .toMutableList()
        } else {
            numberPattern.findAll(splitSeeds[1])
                .map { it.value.toLong() }
                .map { LongRange(it, it) }
                .toMutableList()
        }
    }

    private fun processStartingRanges(rawSeeds: String, partTwo: Boolean = false): MutableList<MapRange> {
        val splitSeeds = rawSeeds.split(":")
        return if (partTwo) {
            numberPairPattern.findAll(splitSeeds[1])
                .map { it.value.split(" ") }
                .map { Pair(it[0].toLong(), it[1].toLong()) }
                .map { MapRange(MapType.SeedToSoil, it.first, it.second) }
                .toMutableList()
        } else {
            numberPattern.findAll(splitSeeds[1])
                .map { it.value.toLong() }
                .map { MapRange(MapType.SeedToSoil, it, 1) }
                .toMutableList()
        }
    }

    private fun toggleMapType(rawMapType: String): MapType {
        return MapType.fromId(rawMapType.split(" ")[0])
    }

    private fun processCurrMap(mapType: MapType, rawMap: String): Map {
        val test = numberPattern.findAll(rawMap).map { it.value.toLong() }
        return Map(mapType, test.elementAt(0), test.elementAt(1), test.elementAt(2))
    }

    private fun parseDstSrcRanges(type: MapType, rawRangeData: String): Pair<MapRange, MapRange> {
        val rangeData = numberPattern.findAll(rawRangeData).map { it.value.toLong() }
        val dst = rangeData.elementAt(0)
        val src = rangeData.elementAt(1)
        val len = rangeData.elementAt(2)
        val dstRange = MapRange(type, dst, len)
        val srcRange = MapRange(type, src, len)
        return Pair(dstRange, srcRange)
    }

    enum class GardenItem {
        SEED,
        SOIL,
        FERTILIZER,
        WATER,
        LIGHT,
        TEMPERATURE,
        HUMIDITY,
        LOCATION,
    }

    data class MapRange(val type: MapType, val start: Long, val length: Long) {
        val end = start + (length-1).coerceAtLeast(0)
        fun contains(value: Long): Boolean = value in start..end

        private fun encapsulatesRange(other: MapRange): Boolean =
            (other.start >= this.start && other.start <= this.end &&
                other.end <= this.end && other.end >= this.start)

        private fun encapsulatesRange(other: MapRange, destRange: MapRange): List<MapRange>? {
            if (other.start >= this.start && other.start <= this.end &&
                    other.end <= this.end && other.end >= this.start) {
                val newMapType = other.type.next() //MapType.values().first { it.sourceType == this.type.destType }
                val startIndex = other.start - this.start
                val currentMapValuesToUse = MapRange(other.type, other.start, other.length)
                val mappedDestinationValues = MapRange(newMapType, destRange.start + startIndex, other.length)
                println("Encapsulated Overlap: No need to split:\n" +
                        "\t$currentMapValuesToUse -> $mappedDestinationValues")
                return listOf(mappedDestinationValues)
            }
            return emptyList()
        }

        private fun startOverlap(other: MapRange, destRange: MapRange): List<MapRange>? {
            if (other.start < this.start && other.end <= this.end &&
                other.end >= this.start) {
                val newMapType = other.type.next() //MapType.values().first { it.sourceType == this.type.destType }

//                val indexOfOtherEnd = other.end - this.start
//                val numMapValuesToUse = indexOfOtherEnd + 1
//                println("New Length: $numMapValuesToUse")
//                println("Index Other End: $indexOfOtherEnd")

                val beforeLength = this.start - other.start
                val remainingLength = other.length - beforeLength
                println("Split Lengths: $beforeLength $remainingLength")

//                val before = MapRange(newMapType, other.start, other.length-numMapValuesToUse)
                val before = MapRange(newMapType, other.start, beforeLength)

                val currentMapValuesToUse = MapRange(this.type, this.start, remainingLength)
                val mappedDestinationValues = MapRange(newMapType, destRange.start, remainingLength)
                println("Start Overlap: Should split into:\n" +
                        "\t$before\n" +
                        "\t$currentMapValuesToUse -> $mappedDestinationValues")
                return listOf(before, mappedDestinationValues)
            }
            return emptyList()
        }

        private fun endOverlap(other: MapRange, destRange: MapRange): List<MapRange>? {
            if (other.start >= this.start && other.start <= this.end &&
                other.end >= this.end) {
                println()
                val newMapType = other.type.next() //MapType.values().first { it.sourceType == this.type.destType }

                val excessEnd = other.end
                val excessStart = this.end + 1
                val excessLength = excessEnd - excessStart
                println("Excess: $excessStart, $excessEnd, $excessLength")
                val excessRange = MapRange(newMapType, excessStart, excessLength)

                val currentStart = other.start
                val currentLen = this.end - other.start + 1
                println("Current: $excessStart, $excessEnd, $excessLength")
                val currentRange = MapRange(this.type, currentStart, currentLen)

                val startIndex = other.start - this.start
                println("Start Index: $startIndex")

                val mappedDestinationValues = MapRange(newMapType, destRange.start + startIndex, currentLen)
                println("End Overlap: Should split into:\n" +
                        "\t$currentRange -> $mappedDestinationValues\n" +
                        "\t$excessRange")
                return listOf(mappedDestinationValues, excessRange)
            }
            return emptyList()
        }

        override fun toString(): String {
            return "[$type] $start..$end [${length.toString().padStart(2, '0')}]"
        }

        fun check(input: MapRange, destRange: MapRange): List<MapRange> {
            println("Checking Input: $input v $this")
            if (input.end < this.start) {
//                println("Input is before everything")
                return emptyList()
            } else if (input.start > this.end) {
//                println("Input is after everything")
                return emptyList()
            }

            encapsulatesRange(input, destRange)?.let {
                if (it.isNotEmpty()) {
//                    println("$type Range $start..$end encapsulates ${input.start}..${input.end}")
                    println("\tENCAPSULATED Adding Ranges: $it")
                    return it
                }
            }

            startOverlap(input, destRange)?.let {
                if (it.isNotEmpty()) {
//                    println("$type Range $start..$end overlaps ${input.start}..${input.end}")
                    println("\tSTART_OVERLAP Adding Ranges: $it")
                    return it
                }
            }

            endOverlap(input, destRange)?.let {
                if (it.isNotEmpty()) {
                    println("\tEND_OVERLAP Adding Ranges: $it")
                    return emptyList()
                }
            }

            println("DANGER")
            return emptyList()
        }
    }

    data class NewMap(val type: MapType) {
        val destinationRanges: MutableList<MapRange> = mutableListOf()
        val sourceRanges: MutableList<MapRange> = mutableListOf()
        fun absoluteMin(): Long = sourceRanges.minOf { it.start }
        fun absoluteMax(): Long = sourceRanges.maxOf { it.end }

        fun mappedRanges(): List<MapRange> {
            val nextMapType = type.next() //MapType.values()[type.ordinal+1]
            val mapped = mutableListOf<MapRange>()
            for (destination in destinationRanges) {
                mapped.add(MapRange(nextMapType, destination.start, destination.length))
            }
            return mapped
        }

        fun insertRange(mapRange: MapRange) {
            println("Inserting: $mapRange")

            val rangesToCheck = mutableListOf(mapRange)
            val sortedRanges = sourceRanges.sortedBy { it.start }

            while (rangesToCheck.isNotEmpty()) {
                val checkRange = rangesToCheck.first()

                if (checkRange.end < absoluteMin()) {
                    println("Range before all of existing ranges")

                    sourceRanges.add(0, checkRange)
                    destinationRanges.add(0, checkRange)

                    rangesToCheck.remove(checkRange)
                } else if (mapRange.end > absoluteMax()) {
                    println("Range after all of existing ranges")

                    sourceRanges.add(mapRange)
                    destinationRanges.add(mapRange)

                    rangesToCheck.remove(checkRange)
                } else {
                    println("Checking Sorted Ranges")
                    sortedRanges.forEach { sourceRange ->
                        var subsetBefore: MapRange? = null
                        var subsetAfter: MapRange? = null
                        if (checkRange.contains(sourceRange.start)) {
                            println("Checked Range $checkRange contained {START} of $sourceRange")

                            subsetBefore = MapRange(type, checkRange.start, sourceRange.start)
                            println("Created Subset for {BEFORE} $subsetBefore")
                            rangesToCheck.add(subsetBefore)
                        }

                        if (checkRange.contains(sourceRange.end)) {
                            println("Checked Range $checkRange contained {END} of $sourceRange")
                            subsetAfter = MapRange(type, sourceRange.end + 1, checkRange.end - sourceRange.end)
                            println("Created Subset for {AFTER} $subsetAfter")
                            rangesToCheck.add(subsetAfter)
                        }

                        println("${subsetBefore?.length ?: 0} + ${subsetAfter?.length ?: 0} = ${checkRange.length}")

                        if (subsetBefore != null || subsetAfter != null) {
                            rangesToCheck.remove(checkRange)
                        }
                    }
                }

            }
        }
    }

    data class Map(val type: MapType, val destStart: Long, val sourceStart: Long, val length: Long) {
        val destinationRange: LongRange = LongRange(destStart, destStart+(length-1).coerceAtLeast(0))
        val sourceRange: LongRange = LongRange(sourceStart, sourceStart+(length-1).coerceAtLeast(0))

        fun valueForSource(source: Long): Long? {
            if (sourceRange.contains(source)) {
                val index = sourceRange.indexOf(source)
                return destinationRange.elementAt(index)
            }
            return null
        }
    }

    enum class MapType(val id: String, val sourceType: GardenItem, val destType: GardenItem) {
        SeedToSoil("seed-to-soil", GardenItem.SEED, GardenItem.SOIL),
        SoilToFertilizer("soil-to-fertilizer", GardenItem.SOIL, GardenItem.FERTILIZER),
        FertilizerToWater("fertilizer-to-water", GardenItem.FERTILIZER, GardenItem.WATER),
        WaterToLight("water-to-light", GardenItem.WATER, GardenItem.LIGHT),
        LightToTemperature("light-to-temperature", GardenItem.LIGHT, GardenItem.TEMPERATURE),
        TemperatureToHumidity("temperature-to-humidity", GardenItem.TEMPERATURE, GardenItem.HUMIDITY),
        HumidityToLocation("humidity-to-location", GardenItem.HUMIDITY, GardenItem.LOCATION);

        companion object {
            fun fromId(id: String): MapType {
                return values().firstOrNull { it.id == id } ?: throw InvalidParameterException()
            }
        }

        fun next(): MapType {
            return when (this) {
                SeedToSoil -> SoilToFertilizer
                SoilToFertilizer -> FertilizerToWater
                FertilizerToWater -> WaterToLight
                WaterToLight -> LightToTemperature
                LightToTemperature -> TemperatureToHumidity
                TemperatureToHumidity -> HumidityToLocation
                HumidityToLocation -> throw ArrayIndexOutOfBoundsException()
            }
        }
    }
}

fun main(args: Array<String>) {
    Day5().processInput()
}