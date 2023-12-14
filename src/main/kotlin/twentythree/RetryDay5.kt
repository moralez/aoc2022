package twentythree

import Day
import java.security.InvalidParameterException

class RetryDay5 : Day("2023/day5.txt", true) {
    private val dstSrcLengthPattern = """\d+ \d+ \d+""".toRegex()
    private val numberPairPattern = """\d+ \d+""".toRegex()

    private val maps: MutableMap<MapType, Map> = mutableMapOf(
        MapType.SeedToSoil to Map(MapType.SeedToSoil),
        MapType.SoilToFertilizer to Map(MapType.SoilToFertilizer),
        MapType.FertilizerToWater to Map(MapType.FertilizerToWater),
        MapType.WaterToLight to Map(MapType.WaterToLight),
        MapType.LightToTemperature to Map(MapType.LightToTemperature),
        MapType.TemperatureToHumidity to Map(MapType.TemperatureToHumidity),
        MapType.HumidityToLocation to Map(MapType.HumidityToLocation),
    )
    val locations = mutableListOf<Long>()
    val conversionPatterns: MutableMap<GardenItem, Map> = mutableMapOf(
        GardenItem.SEED to maps[MapType.SeedToSoil]!!,
        GardenItem.SOIL to maps[MapType.SoilToFertilizer]!!,
        GardenItem.FERTILIZER to maps[MapType.FertilizerToWater]!!,
        GardenItem.WATER to maps[MapType.WaterToLight]!!,
        GardenItem.LIGHT to maps[MapType.LightToTemperature]!!,
        GardenItem.TEMPERATURE to maps[MapType.TemperatureToHumidity]!!,
        GardenItem.HUMIDITY to maps[MapType.HumidityToLocation]!!,
    )

    private val seeds: MutableList<LongRange> = mutableListOf()

    override fun processInput(lines: Int?) {
        var currentMapType = MapType.SeedToSoil
        for (line in input) {
            when {
                line.contains("seeds") -> {
                    seeds.addAll(processStartingRanges(line))
                    println("Starting Seeds: $seeds")
                }
                line.contains("map") -> {
                    currentMapType = toggleMapType(line)
                }
                dstSrcLengthPattern.matches(line) -> {
                    val currSrcDstRange = processCurrRangeEntry(line)
                    println("Adding to $currentMapType ${currSrcDstRange.source} ${currSrcDstRange.destination}")
                    maps[currentMapType]?.insertRange(currSrcDstRange)
                    println("${maps[currentMapType]} ${maps[currentMapType]?.sourceRanges} ${maps[currentMapType]?.destinationRanges}")
                }
                else -> continue
            }
        }

        for (seed in seeds) {
            convertRange(GardenItem.SEED, GardenItem.LOCATION, seed)
        }
        println("Location: ${locations.size} ${locations.min()}")
    }

    private fun processStartingRanges(rawSeeds: String): MutableList<LongRange> {
        val splitSeeds = rawSeeds.split(":")
        return numberPairPattern.findAll(splitSeeds[1])
            .map { it.value.split(" ") }
            .map { Pair(it[0].toLong(), it[1].toLong()) }
            .map { LongRange(it.first, it.first + it.second - 1) }
            .toMutableList()
    }

    private fun toggleMapType(rawMapType: String): MapType {
        val mapType = MapType.fromId(rawMapType.split(" ")[0])
        println("Changing to Map Type: $mapType")
        return mapType
    }

    private fun processCurrRangeEntry(rawMap: String): RangePattern {
        val test = numberPattern.findAll(rawMap).map { it.value.toLong() }
        val dst = test.elementAt(0)
        val src = test.elementAt(1)
        val len = test.elementAt(2)
        val srcRange = LongRange(src, src + len - 1)
        val dstRange = LongRange(dst, dst + len - 1)
        return RangePattern(srcRange, dstRange)
    }

    enum class GardenItem {
        SEED, SOIL, FERTILIZER, WATER, LIGHT, TEMPERATURE, HUMIDITY, LOCATION
    }

    enum class MapType(val id: String, val srcType: GardenItem, val dstType: GardenItem) {
        SeedToSoil("seed-to-soil", GardenItem.SEED, GardenItem.SOIL),
        SoilToFertilizer("soil-to-fertilizer", GardenItem.SOIL, GardenItem.FERTILIZER),
        FertilizerToWater("fertilizer-to-water", GardenItem.FERTILIZER, GardenItem.WATER),
        WaterToLight("water-to-light", GardenItem.WATER, GardenItem.LIGHT),
        LightToTemperature("light-to-temperature", GardenItem.LIGHT, GardenItem.TEMPERATURE),
        TemperatureToHumidity("temperature-to-humidity", GardenItem.TEMPERATURE, GardenItem.HUMIDITY),
        HumidityToLocation("humidity-to-location", GardenItem.HUMIDITY, GardenItem.LOCATION);

        companion object {
            fun fromId(id: String): MapType {
                return MapType.values().firstOrNull { it.id == id } ?: throw InvalidParameterException()
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

    data class RangePattern(val source: LongRange, val destination: LongRange)
    data class ConversionPattern(
        val sourceType: GardenItem,
        val destinationType: GardenItem,
        val patternRanges: List<RangePattern>,
    )
    data class Map(val type: MapType) {
        val destinationRanges: MutableList<LongRange> = mutableListOf()
        val sourceRanges: MutableList<LongRange> = mutableListOf()

        val patternRanges: MutableList<RangePattern> = mutableListOf()

        fun absoluteMin(): Long = sourceRanges.minOf { it.first }
        fun absoluteMax(): Long = sourceRanges.minOf { it.last }

        fun insertRange(rangePattern: RangePattern) {
            sourceRanges.add(rangePattern.source)
            destinationRanges.add(rangePattern.destination)

            patternRanges.add(rangePattern)
        }
    }

    fun mergeRanges(range1: LongRange, range2: LongRange): List<LongRange> {
        return when {
            range1.isEmpty() -> listOf(range2)
            range2.isEmpty() -> listOf(range1)
            range1.last < range2.first - 1 -> listOf(range1, range2)
            range2.last < range1.first - 1 -> listOf(range2, range1)
            else -> listOf(minOf(range1.first, range2.first)..maxOf(range1.last, range2.last))
        }
    }

    fun convertRange(sourceType: GardenItem, targetType: GardenItem, searchRange: LongRange) {
        println("Source Type: $sourceType, $targetType, $searchRange")
        if (sourceType == targetType) {
//            println("Adding to Locations: ${searchRange.first}")
            locations.add(searchRange.first)
            return
        }

        val conversionPattern = conversionPatterns[sourceType] ?: return
        println("Checking conversationPattern[$sourceType] ${conversionPattern.patternRanges}")
        val ranges = mutableListOf(searchRange)

        while (ranges.isNotEmpty()) {
            val currentRange = ranges.removeLast()
            println("Checking Range: $currentRange")
            var matchFound = false

            for (pattern in conversionPattern.patternRanges) {
                if (!overlap(pattern.source, currentRange)) continue

                matchFound = true

                // Make a range from before the overlap, if it exists
                val overlappingMin = maxOf(pattern.source.first, currentRange.first)
                if (overlappingMin > currentRange.first) {
                    val valuesBefore = LongRange(currentRange.first, overlappingMin - 1)
                    if (valuesBefore.contains(0L)) println("[BEFORE] Generated range containing 0: $valuesBefore")
                    else println("[BEFORE] Adding to Range: $currentRange - ${pattern.source} ${pattern.destination} -> $valuesBefore")
                    ranges.add(valuesBefore)
                }

                // Make a range from after the overlap, if it exists
                val overlappingMax = minOf(pattern.source.last, currentRange.last)
                if (overlappingMax < currentRange.last) {
                    val valuesAfter = LongRange(overlappingMax+1, currentRange.last)
                    if (valuesAfter.contains(0L)) println("[AFTER] Generated range containing 0: $valuesAfter")
                    else println("[AFTER] Adding to Range: $currentRange - ${pattern.source} ${pattern.destination} -> $valuesAfter")
                    ranges.add(valuesAfter)
                }

                // Now map the overlap, from source terms to destination terms
                // pattern source 10 - 20, 30 - 40
                val transformation = pattern.destination.first - pattern.source.first // 30 - 10 = 20
                val destinationMin = overlappingMin + transformation
                val destinationMax = overlappingMax + transformation
                val newDestination = LongRange(destinationMin, destinationMax)
                if (newDestination.contains(0L)) println("[DESTINATION] Generated range containing 0: $currentRange - ${pattern.source} ${pattern.destination} ${conversionPattern.type.dstType} $newDestination")
                else println("[DESTINATION] Adding to Range: $currentRange - ${pattern.source} ${pattern.destination} -> $newDestination")
                // Send converted range to next map
                convertRange(conversionPattern.type.dstType, targetType, newDestination)
            }

            // No match was found, send unconverted range to next map
            if (!matchFound) {
                convertRange(conversionPattern.type.dstType, targetType, currentRange)
            }
        }
    }

    fun overlap(one: LongRange, two: LongRange): Boolean {
        return one.last >= two.first && two.last >= one.first
    }
}

fun LongRange.encapsulatesRange(other: LongRange): Boolean =
    (other.first in first..last && other.last in first.. last)

fun main(args: Array<String>) {
    RetryDay5().processInput()
}

