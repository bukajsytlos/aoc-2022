package day15

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val sensors = File("src/main/kotlin/day15/input.txt").readLines()
        .map {
            it.split(": ").let {
                Sensor(
                    it[0].substringAfter("Sensor at ").toCoordinate(),
                    it[1].substringAfter("closest beacon is at ").toCoordinate()
                )
            }
        }
    val atY = 2_000_000
    val limitY = 4_000_000
    val reducedRanges = sensors.mapNotNull { it.sensorReachRangeAt(atY) }.reduce()
    val positionsWithoutBeaconAt = reducedRanges.sumOf { it.count() }
    val beaconsCountAt = sensors.map { it.closestBeacon }.distinct().count { b -> reducedRanges.any { it.contains(b.x) } && b.y == atY }
    val sensorsCountAt = sensors.map { it.coordinate }.distinct().count { s -> reducedRanges.any { it.contains(s.x) } && s.y == atY }
    println(positionsWithoutBeaconAt - beaconsCountAt - sensorsCountAt)
    val tuningFrequency = (0..limitY).asSequence()
        .map { i -> i to sensors.mapNotNull { it.sensorReachRangeAt(i) }.reduce() }
        .first { it.second.size > 1 }
        .let { (it.second.first().endInclusive + 1).toLong() * 4_000_000 + it.first }
    println(tuningFrequency)
}
data class Sensor(val coordinate: Coordinate, val closestBeacon: Coordinate) {
    fun sensorReachRangeAt(y: Int): IntRange? {
        val xHalfSize = closestBeaconDistance() - abs(coordinate.y - y)
        return if (xHalfSize > 0) {
            IntRange(coordinate.x - xHalfSize, coordinate.x + xHalfSize)
        } else null
    }

    private fun closestBeaconDistance() = coordinate.manhattanDistanceTo(closestBeacon)
}

fun List<IntRange>.reduce(): List<IntRange> {
    var reduced = sortedBy { it.first }
    val stack = ArrayDeque<IntRange>()
    stack.addLast(reduced.first())
    for (i in 1 until reduced.size) {
        val range = reduced[i]
        val range2 = stack.removeLast()
        stack.addAll(range2.merge(range))
    }
    return stack.toList()
}

fun IntRange.merge(other: IntRange?): List<IntRange> = if (other == null) {
    listOf(this)
} else if (start in other || endInclusive in other || other.start in this || other.endInclusive in this) {
    listOf(IntRange(min(start, other.first), max(endInclusive, other.last)))
} else listOf(this, other)

data class Coordinate(val x: Int, val y: Int) {
    fun manhattanDistanceTo(other: Coordinate): Int = abs(x - other.x) + abs(y - other.y)
}

fun String.toCoordinate() = split(", ").let {
    Coordinate(
        it[0].substringAfter("x=").toInt(),
        it[1].substringAfter("y=").toInt()
    )
}