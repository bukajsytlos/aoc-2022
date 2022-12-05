package day05

import java.io.File

typealias Stack = ArrayDeque<Char>

fun main() {

    var stacks: List<Stack> = emptyList()
    var commands: List<Command> = emptyList()
    File("src/main/kotlin/day05/input.txt").useLines { seq ->
        var numberOfStacks = 0
        val iterator = seq.iterator()
        if (iterator.hasNext()) {
            val firstLine = iterator.next()
            numberOfStacks = (firstLine.length + 1) / 4
            stacks = List(numberOfStacks) { Stack() }
            stacks.fill(firstLine.toCrates(numberOfStacks))
        }
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (line.isBlank()) break
            if (line[1].isDigit()) continue
            stacks.fill(line.toCrates(numberOfStacks))
        }

        commands = iterator.asSequence().map { Command.from(it) }.toList()
    }

    val stacks2 = stacks.map { ArrayDeque(it) }

    commands.forEach {
        val fromStack = stacks[it.fromStack]
        val toStack = stacks[it.toStack]
        repeat(it.count) {
            toStack.addLast(fromStack.removeLast())
        }
    }
    println(stacks.map { it.last() }.joinToString(separator = ""))

    commands.forEach {
        val fromStack = stacks2[it.fromStack]
        val toStack = stacks2[it.toStack]
        val tempStack = ArrayDeque<Char>(it.count)
        repeat(it.count) {
            tempStack.addLast(fromStack.removeLast())
        }
        repeat(it.count) {
            toStack.addLast(tempStack.removeLast())
        }
    }
    println(stacks2.map { it.last() }.joinToString(separator = ""))
}

data class Command(val count: Int, val fromStack: Int, val toStack: Int) {
    companion object {
        private val commandPattern = Regex("""move (\d+) from (\d+) to (\d+)""")
        fun from(string: String): Command {
            val matchedGroups = commandPattern.matchEntire(string)!!.groupValues
            return Command(matchedGroups[1].toInt(), matchedGroups[2].toInt() - 1, matchedGroups[3].toInt() - 1)
        }
    }
}

fun String.toCrates(count: Int): List<Char> = (0 until count).map { this[it * 4 + 1] }
fun List<Stack>.fill(crates: List<Char>) = crates.forEachIndexed { index, c -> if (c != ' ') this[index].addFirst(c) }