package day08

import java.io.File

fun main() {

    val lines = File("src/main/kotlin/day08/input.txt").readLines()
    val mapSize = lines.size
    val treeHeightMap: Array<IntArray> = Array(mapSize) { i -> lines[i].map { it.digitToInt() }.toIntArray() }

    val visibleTreeCoordinates = HashSet<Coordinates>()
    treeHeightMap.scanDirectionForVisibleTrees(visibleTreeCoordinates)
    println(visibleTreeCoordinates.size)

    val treeScenicScores = treeHeightMap.scanTreesForScenicScore()
    println(treeScenicScores.maxBy { it.value }.value)
}

fun Array<IntArray>.scanDirectionForVisibleTrees(
    alreadyVisibleTreeCoordinates: MutableSet<Coordinates>
): Set<Coordinates> {
    for (row in 0 until this.size) {
        var highestTreeSize = -1
        for (col in 0 until this[row].size) {
            val treeHeight = this[row][col]
            if (treeHeight > highestTreeSize) {
                highestTreeSize = treeHeight
                alreadyVisibleTreeCoordinates.add(Coordinates(col, row))
            }
        }
        highestTreeSize = -1
        for (col in this[row].size - 1 downTo 0) {
            val treeHeight = this[row][col]
            if (treeHeight > highestTreeSize) {
                highestTreeSize = treeHeight
                alreadyVisibleTreeCoordinates.add(Coordinates(col, row))
            }
        }
    }
    for (col in 0 until this[0].size) {
        var highestTreeSize = -1
        for (row in 0 until this.size) {
            val treeHeight = this[row][col]
            if (treeHeight > highestTreeSize) {
                highestTreeSize = treeHeight
                alreadyVisibleTreeCoordinates.add(Coordinates(col, row))
            }
        }
        highestTreeSize = -1
        for (row in this.size - 1 downTo 0) {
            val treeHeight = this[row][col]
            if (treeHeight > highestTreeSize) {
                highestTreeSize = treeHeight
                alreadyVisibleTreeCoordinates.add(Coordinates(col, row))
            }
        }
    }
    return alreadyVisibleTreeCoordinates
}

fun Array<IntArray>.scanTreesForScenicScore(): Map<Coordinates, Int> {
    val scenicScoreByTreeCoordinate = HashMap<Coordinates, Int>(this.size * this[0].size)
    for (row in indices) {
        for (col in 0 until this[row].size) {
            val treeHeight = this[row][col]
            val scenicScore = if (col == 0 || col == this[row].size - 1 || row == 0 || row == this.size - 1) 0 else {
                val leftScenicScore = (col - 1 downTo 0).map { this[row][it] }.indexOfFirst { it >= treeHeight }.let { if (it == -1) col else it + 1 }
                val rightScenicScore = (col + 1 until this[row].size).map { this[row][it] }.indexOfFirst { it >= treeHeight }.let { if (it == -1) this[row].size - 1 - col else it + 1 }
                val upScenicScore = (row - 1 downTo 0).map { this[it][col] }.indexOfFirst { it >= treeHeight }.let { if (it == -1) row else it + 1 }
                val downScenicScore = (row + 1 until this.size).map { this[it][col] }.indexOfFirst { it >= treeHeight }.let { if (it == -1) this.size - 1 - row else it + 1 }
                leftScenicScore * rightScenicScore * upScenicScore * downScenicScore
            }
            scenicScoreByTreeCoordinate[Coordinates(col, row)] = scenicScore
        }
    }
    return scenicScoreByTreeCoordinate
}

data class Coordinates(val x: Int, val y: Int)