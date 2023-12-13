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

    override fun processInput(lines: Int?) {
        var currentMapType = MapType.SeedToSoil
        for (line in input) {
            when {
                line.contains("seeds") -> {
                    val seeds = processStartingRanges(line)
                    println("Starting Seeds: $seeds")
                    for (seedRange in seeds) {
                        maps[MapType.SeedToSoil]?.sourceRanges?.add(seedRange)
                        maps[MapType.SeedToSoil]?.destinationRanges?.add(seedRange)
                    }
                }
                line.contains("map") -> {
                    currentMapType = toggleMapType(line)
                }
                dstSrcLengthPattern.matches(line) -> {
                    val currSrcDstRange = processCurrRangeEntry(line)
                    maps[currentMapType]?.insertRange(currSrcDstRange)
                }
                else -> continue
            }
        }
    }

    private fun processStartingRanges(rawSeeds: String): MutableList<LongRange> {
        val splitSeeds = rawSeeds.split(":")
        return numberPairPattern.findAll(splitSeeds[1])
            .map { it.value.split(" ") }
            .map { Pair(it[0].toLong(), it[1].toLong()) }
            .map { LongRange(it.first, it.first + it.second - 1) }
            .toMutableList()
    }

    private fun processCurrRangeEntry(rawMap: String): Pair<LongRange, LongRange> {
        val test = numberPattern.findAll(rawMap).map { it.value.toLong() }
        val dst = test.elementAt(0)
        val src = test.elementAt(1)
        val len = test.elementAt(2)
        val srcRange = LongRange(src, src + len - 1)
        val dstRange = LongRange(dst, dst + len - 1)
        return Pair(srcRange, dstRange)
    }

    private fun toggleMapType(rawMapType: String): MapType {
        return MapType.fromId(rawMapType.split(" ")[0])
    }

    enum class GardenItem {
        SEED, SOIL, FERTILIZER, WATER, LIGHT, TEMPERATURE, HUMIDITY, LOCATION,
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

    data class Map(val type: MapType) {
        val destinationRanges: MutableList<LongRange> = mutableListOf()
        val sourceRanges: MutableList<LongRange> = mutableListOf()

        val srcDstRange: MutableList<Pair<LongRange, LongRange>> = mutableListOf()

        fun absoluteMin(): Long = sourceRanges.minOf { it.first }
        fun absoluteMax(): Long = sourceRanges.minOf { it.last }

        fun insertRange(otherRange: Pair<LongRange, LongRange>) {
            val otherSrc = otherRange.first
            val otherDst = otherRange.second
            for (range in sourceRanges) {
                if (range.encapsulatesRange(otherSrc)) {
                    println("Current Range $range encapsulates $otherSrc")
                    sourceRanges.remove(range)
                }
            }
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

}

fun LongRange.encapsulatesRange(other: LongRange): Boolean =
    (other.first in first..last && other.last in first.. last)

fun main(args: Array<String>) {
//    RetryDay5().processInput()

    val range1 = 0L..100L
    val range2 = 50L..75L

    val result = combineAndSplitRanges(range1, range2)
    result.forEach { println(it) }
}

fun combineAndSplitRanges(range1: LongRange, range2: LongRange): List<LongRange> {
    if (range1.isEmpty() || range2.isEmpty()) {
        return listOf(range1, range2).filterNot { it.isEmpty() }
    }

    val combinedStart = minOf(range1.first, range2.first)
    val combinedEnd = maxOf(range1.last, range2.last)

    val intersectionStart = maxOf(range1.first, range2.first)
    val intersectionEnd = minOf(range1.last, range2.last)

    return buildList {
        if (combinedStart < intersectionStart) {
            add(combinedStart until intersectionStart)
        }
        add(intersectionStart..intersectionEnd)
        if (intersectionEnd < combinedEnd) {
            add((intersectionEnd + 1)..combinedEnd)
        }
    }
}