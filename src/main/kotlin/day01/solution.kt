package day01

import java.io.File

fun main() {
    val calories = File("src/main/kotlin/day01/input.txt").readText()
        .split(System.lineSeparator() + System.lineSeparator())
        .map { it.lines().sumOf { it.toInt() } }

    println(calories.max())
    println(calories.sortedDescending().take(3).sum())
}