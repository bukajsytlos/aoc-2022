package day23

import java.io.File
import kotlin.math.max
import kotlin.math.min


fun main() {
    val elves = File("src/main/kotlin/day23/input.txt").readLines()
        .flatMapIndexed { y: Int, line: String ->
            line.mapIndexedNotNull { x, c ->
                if (c == '#') Elf(Position(x, y)) else null
            }
        }.toSet()
    repeat(10) {
        elves.round(it)
    }
    var minX = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var minY = Int.MAX_VALUE
    var maxY = Int.MIN_VALUE
    elves.forEach {
        minX = min(minX, it.position.x)
        maxX = max(maxX, it.position.x)
        minY = min(minY, it.position.y)
        maxY = max(maxY, it.position.y)
    }
    println((maxX - minX + 1) * (maxY - minY + 1) - elves.size)

    var numberOfRounds = 10
    while (elves.round(numberOfRounds)) {
        numberOfRounds++
    }
    println(numberOfRounds + 1)
}

fun Set<Elf>.round(index: Int): Boolean {
    var hasAnyElfMoved = false
    val elvesPositions = this.map { it.position }.toSet()
    map { elf -> elf.proposeMove(index, elvesPositions) to elf }
        .groupBy({ it.first }, { it.second })
        .filter { it.value.size == 1 }
        .forEach {
            if (it.value[0].position != it.key) {
                it.value[0].position = it.key
                hasAnyElfMoved = true
            }
        }
    return hasAnyElfMoved
}

val directionGroups = listOf(
    setOf(Direction.NW, Direction.N, Direction.NE) to Direction.N,
    setOf(Direction.SW, Direction.S, Direction.SE) to Direction.S,
    setOf(Direction.NW, Direction.W, Direction.SW) to Direction.W,
    setOf(Direction.NE, Direction.E, Direction.SE) to Direction.E,
)

data class Elf(var position: Position) {
    fun proposeMove(directionGroupStartIndex: Int, elvesPositions: Set<Position>): Position {
        var newPosition: Position? = null
        var hasNeighbour = false
        for (i in directionGroups.indices) {
            val directionGroup = directionGroups[(directionGroupStartIndex + i) % 4]
            if (directionGroup.first.all { position.moveTo(it) !in elvesPositions }) {
                newPosition = newPosition ?: position.moveTo(directionGroup.second)
            } else {
                hasNeighbour = true
            }
        }
        return if (newPosition != null && hasNeighbour) newPosition else position
    }
}

data class Position(val x: Int, val y: Int) {
    fun moveTo(direction: Direction) = Position(x + direction.dx, y + direction.dy)
}

enum class Direction(val dx: Int, val dy: Int) {
    NW(-1, -1),
    N(0, -1),
    NE(1, -1),
    E(1, 0),
    SE(1, 1),
    S(0, 1),
    SW(-1, 1),
    W(-1, 0),
}