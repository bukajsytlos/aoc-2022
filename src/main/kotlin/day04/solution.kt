package day04

import java.io.File

fun main() {

    val lines = File("src/main/kotlin/day04/input.txt").readLines()
    val sectionAssignmentsPairs = lines.map { line ->
        line.split(",").map { pair ->
            pair.split("-").let {
                SectionAssignment(
                    it[0].toInt(),
                    it[1].toInt()
                )
            }
        }
    }
    val totalOverlapCount = sectionAssignmentsPairs.count { it[0].contains(it[1]) || it[0].isContainedBy(it[1]) }
    println(totalOverlapCount)

    val overlapCount = sectionAssignmentsPairs.count { it[0].overlap(it[1]) }
    println(overlapCount)
}

class SectionAssignment(private val start: Int, private val end: Int) {
    fun contains(other: SectionAssignment): Boolean = other.start >= start && other.end <= end
    fun isContainedBy(other: SectionAssignment): Boolean = start >= other.start && end <= other.end
    fun overlap(other: SectionAssignment): Boolean = start <= other.end && end >= other.start
}