package day24

import java.io.File
import java.util.PriorityQueue
import kotlin.math.abs


fun main() {
    val lines = File("src/main/kotlin/day24/input.txt").readLines()
    val obstacles = lines
        .flatMapIndexed { y: Int, line: String ->
        line.mapIndexedNotNull { x, c ->
            when (c) {
                '#' -> Obstacle.Wall(Position(x, y))
                '^' -> Obstacle.Blizzard(Position(x, y), Direction.N)
                'v' -> Obstacle.Blizzard(Position(x, y), Direction.S)
                '<' -> Obstacle.Blizzard(Position(x, y), Direction.W)
                '>' -> Obstacle.Blizzard(Position(x, y), Direction.E)
                else -> null
            }
        }
    }.toSet()

    val start = Position(lines.first().indexOf('.'), 0)
    val end = Position(lines.last().indexOf('.'), lines.indices.last)

    val basinWidth = obstacles.maxOf { it.position.x } + 1
    val basinHeight = obstacles.maxOf { it.position.y } + 1
    val tripPart1 = getShortestDistance(
        start,
        end,
        obstacles,
        basinWidth,
        basinHeight
    )
    println(tripPart1.first)
    val tripPart2 = getShortestDistance(
        end,
        start,
        tripPart1.second,
        basinWidth,
        basinHeight
    )
    val tripPart3 = getShortestDistance(
        start,
        end,
        tripPart2.second,
        basinWidth,
        basinHeight
    )
    println(tripPart1.first + tripPart2.first + tripPart3.first)
}

fun getShortestDistance(from: Position, to: Position, initObstacles: Set<Obstacle>, basinWidth: Int, basinHeight: Int): Pair<Int, Set<Obstacle>> {

    val xRange = 0 until basinWidth
    val yRange = 0 until basinHeight
    val queue = PriorityQueue<Triple<Position, Int, Set<Obstacle>>> { p1, p2 ->
        p1.first.manhattanDistanceTo(to) + p1.second - (p2.first.manhattanDistanceTo(to) + p2.second)
    }
    val seenStates = mutableSetOf<Pair<Position, Int>>()
    queue.offer(Triple(from, 0, initObstacles))
    while (queue.isNotEmpty()) {
        val (position, elapsedTime, obstacles) = queue.poll()
        if (position == to) {
            return elapsedTime to obstacles
        }
        if (position to elapsedTime in seenStates) continue
        seenStates.add(position to elapsedTime)
        val nextStateOfObstacles = obstacles.next(basinWidth, basinHeight)
        val obstaclePositions = nextStateOfObstacles.map { it.position }.toSet()
        (position.adjacents() + position).filter { it.x in xRange && it.y in yRange }.forEach {
            if (it !in obstaclePositions)
                queue.offer(Triple(it, elapsedTime + 1, nextStateOfObstacles))
        }
    }
    error("no path found")
}

fun Set<Obstacle>.next(basinWidth: Int, basinHeight: Int): Set<Obstacle> = map {
    when (it) {
        is Obstacle.Blizzard -> it.move(basinWidth, basinHeight)
        is Obstacle.Wall -> it
    }
}.toSet()

sealed class Obstacle(val position: Position) {
    class Wall(position: Position) : Obstacle(position)

    class Blizzard(position: Position, private val direction: Direction) : Obstacle(position) {
        fun move(basinWidth: Int, basinHeight: Int): Blizzard {
            val newPosition = when (direction) {
                Direction.N -> {
                    if (position.y + direction.dy < 1) Position(position.x, basinHeight - 2)
                    else position.adjacentIn(direction)
                }

                Direction.S -> {
                    if (position.y + direction.dy > basinHeight - 2) Position(position.x, 1)
                    else position.adjacentIn(direction)
                }

                Direction.W -> {
                    if (position.x + direction.dx < 1) Position(basinWidth - 2, position.y)
                    else position.adjacentIn(direction)
                }

                Direction.E -> {
                    if (position.x + direction.dx > basinWidth - 2) Position(1, position.y)
                    else position.adjacentIn(direction)
                }
            }
            return Blizzard(newPosition, direction)
        }
    }
}

data class Position(val x: Int, val y: Int) {
    fun adjacents() = Direction.values().map { adjacentIn(it) }.toSet()

    fun adjacentIn(direction: Direction) = Position(x + direction.dx, y + direction.dy)

    fun manhattanDistanceTo(other: Position): Int = abs(x - other.x) + abs(y - other.y)
}

enum class Direction(val dx: Int, val dy: Int) {
    N(0, -1),
    E(1, 0),
    S(0, 1),
    W(-1, 0),
}