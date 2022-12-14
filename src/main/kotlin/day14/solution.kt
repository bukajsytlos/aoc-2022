package day14

import java.io.File
import kotlin.math.max
import kotlin.math.min


fun main() {
    val rockLines = File("src/main/kotlin/day14/input.txt").readLines().flatMap {
        it.toRockLines()
    }

    val coordinates = rockLines.flatMap { listOf(it.from, it.to) }
    val minX = coordinates.minOf { it.x }
    val maxX = coordinates.maxOf { it.x }
    val maxY = coordinates.maxOf { it.y }

    val cave = Cave(SandSource(Coordinate(500, 0)), rockLines.flatMap { it.getTiles() }, minX, maxX, maxY)
    println(cave.pourSand())

    println(cave.pourSand2())
}

data class Cave(
    val sandSource: SandSource,
    val rockTiles: List<Coordinate>,
    val minX: Int,
    val maxX: Int,
    val maxY: Int
) {
    private val tiles = HashSet(rockTiles)

    fun pourSand(): Int {
        do {
            val sandUnit = sandSource.pour()
            when (val sandUnitState = evaluateSandUnitState(sandUnit)) {
                is SandUnitState.Rest -> tiles.add(sandUnitState.coordinate)
                is SandUnitState.Overflow -> break
            }
        } while (true)
        return sandSource.numberOfPouredSandUnits - 1
    }

    private fun evaluateSandUnitState(sandUnit: SandUnit): SandUnitState {
        val x = sandUnit.coordinate.x
        val y = sandUnit.coordinate.y
        return if (x !in (minX..maxX) || y > maxY) {
            SandUnitState.Overflow
        } else if (!tiles.contains(Coordinate(x, y + 1))) {
            evaluateSandUnitState(SandUnit(Coordinate(x, y + 1)))
        } else if (!tiles.contains(Coordinate(x - 1, y + 1))) {
            evaluateSandUnitState(SandUnit(Coordinate(x - 1, y + 1)))
        } else if (!tiles.contains(Coordinate(x + 1, y + 1))) {
            evaluateSandUnitState(SandUnit(Coordinate(x + 1, y + 1)))
        } else {
            SandUnitState.Rest(sandUnit.coordinate)
        }
    }

    fun pourSand2(): Int {
        do {
            val sandUnit = sandSource.pour()
            when (val sandUnitState = evaluateSandUnitState2(sandUnit)) {
                is SandUnitState.Rest -> {
                    if (sandUnitState.coordinate == sandSource.coordinate) {
                        break
                    }
                    tiles.add(sandUnitState.coordinate)
                }
                is SandUnitState.Overflow -> continue
            }
        } while (true)
        return sandSource.numberOfPouredSandUnits - 1
    }

    private fun evaluateSandUnitState2(sandUnit: SandUnit): SandUnitState {
        val x = sandUnit.coordinate.x
        val y = sandUnit.coordinate.y
        return if (!tiles.contains(Coordinate(x, y + 1)) && y + 1 < maxY + 2) {
            evaluateSandUnitState2(SandUnit(Coordinate(x, y + 1)))
        } else if (!tiles.contains(Coordinate(x - 1, y + 1)) && y + 1 < maxY + 2) {
            evaluateSandUnitState2(SandUnit(Coordinate(x - 1, y + 1)))
        } else if (!tiles.contains(Coordinate(x + 1, y + 1)) && y + 1 < maxY + 2) {
            evaluateSandUnitState2(SandUnit(Coordinate(x + 1, y + 1)))
        } else {
            SandUnitState.Rest(sandUnit.coordinate)
        }
    }
}

class SandSource(val coordinate: Coordinate) {
    var numberOfPouredSandUnits: Int = 0
    fun pour(): SandUnit {
        numberOfPouredSandUnits++
        return SandUnit(coordinate)
    }
}

class SandUnit(var coordinate: Coordinate)

sealed class SandUnitState {
    class Rest(val coordinate: Coordinate): SandUnitState()
    object Overflow: SandUnitState()
}

data class Coordinate(val x: Int, val y: Int)
data class RockLine(val from: Coordinate, val to: Coordinate) {
    fun getTiles(): List<Coordinate> = (min(from.x, to.x)..max(from.x, to.x)).flatMap { x ->
        (min(from.y, to.y)..max(from.y, to.y)).map { y -> Coordinate(x, y) } }
}

fun String.toRockLines(): List<RockLine> = split(" -> ")
    .map { Coordinate(it.substringBefore(",").toInt(), it.substringAfter(",").toInt()) }
    .windowed(2)
    .map { RockLine(it.first(), it.last()) }