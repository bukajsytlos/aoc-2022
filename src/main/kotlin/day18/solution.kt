package day18

import java.io.File


fun main() {
    val lavaPoints = File("src/main/kotlin/day18/input.txt").readLines().map {
        val parts = it.split(",")
        Point3D(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
    }.toSet()

    val surfaceArea = lavaPoints.sumOf { it.neighbours().count { it !in lavaPoints } }
    println(surfaceArea)

    val xRange = lavaPoints.extendedRange { it.x }
    val yRange = lavaPoints.extendedRange { it.y }
    val zRange = lavaPoints.extendedRange { it.z }

    val toVisit = ArrayDeque<Point3D>().apply { add(Point3D(xRange.first, yRange.first, zRange.first)) }
    val visited = mutableSetOf<Point3D>()
    var exteriorSurfaceArea = 0
    while (toVisit.isNotEmpty()) {
        val cube = toVisit.removeFirst()
        if (cube !in visited) {
            visited += cube
            cube.neighbours()
                .filter { it.x in xRange && it.y in yRange && it.z in zRange }
                .forEach { neighbor ->
                    if (neighbor in lavaPoints) exteriorSurfaceArea++
                    else toVisit.add(neighbor)
                }
        }
    }
   println(exteriorSurfaceArea)
}
fun Set<Point3D>.extendedRange(filter: (Point3D) -> Int): IntRange = this.minOf(filter) - 1 .. this.maxOf(filter) + 1

data class Point3D(val x: Int, val y: Int, val z: Int) {
    fun neighbours(): Set<Point3D> = setOf(
        copy(x = x + 1),
        copy(x = x - 1),
        copy(y = y + 1),
        copy(y = y - 1),
        copy(z = z + 1),
        copy(z = z - 1),
    )
}