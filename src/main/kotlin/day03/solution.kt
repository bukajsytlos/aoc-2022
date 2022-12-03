package day03

import java.io.File

typealias Item = Char

fun main() {
    val itemPriorities: Map<Item, Int> = (('a'..'z') + ('A'..'Z')).mapIndexed { index, c -> c to index + 1 }.toMap()

    val lines = File("src/main/kotlin/day03/input.txt").readLines()
    val rucksacks = lines.map { Rucksack(it.toList()) }

    val sumOfCommonRucksackItemPriorities = rucksacks
        .map { it.compartment1().intersect(it.compartment2().toSet()).first() }
        .sumOf { itemPriorities[it]!! }

    println(sumOfCommonRucksackItemPriorities)

    val badges = rucksacks.chunked(3)
        .map { group ->
            group.map { it.content }
                .reduce { acc, content -> acc.intersect(content.toSet()) }
                .first()
        }


    val sumOfBadgePriorities = badges
        .sumOf { itemPriorities[it]!! }

    println(sumOfBadgePriorities)
}

class Rucksack(val content: Iterable<Item>) {
    fun compartment1(): Iterable<Item> = content.take(content.count() / 2)
    fun compartment2(): Iterable<Item> = content.drop(content.count() / 2)
}