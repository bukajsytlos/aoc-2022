package day05

import java.io.File

fun main() {

    val stacks = List(9) {
        when (it) {
            0 -> ArrayDeque(listOf('N', 'R', 'G', 'P'))
            1 -> ArrayDeque(listOf('J', 'T', 'B', 'L', 'F', 'G', 'D', 'C'))
            2 -> ArrayDeque(listOf('M', 'S', 'V'))
            3 -> ArrayDeque(listOf('L', 'S', 'R', 'C', 'Z', 'P'))
            4 -> ArrayDeque(listOf('P', 'S', 'L', 'V', 'C', 'W', 'D', 'Q'))
            5 -> ArrayDeque(listOf('C', 'T', 'N', 'W', 'D', 'M', 'S'))
            6 -> ArrayDeque(listOf('H', 'D', 'G', 'W', 'P'))
            7 -> ArrayDeque(listOf('Z', 'L', 'P', 'H', 'S', 'C', 'M', 'V'))
            8 -> ArrayDeque(listOf('R', 'P', 'F', 'L', 'W', 'G', 'Z'))
            else -> ArrayDeque()
        }
    }
    val stacks2 = stacks.map { ArrayDeque(it) }


    val lines = File("src/main/kotlin/day05/input.txt").readLines()
    val commands = lines.map { Command.from(it) }
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
        val commandPattern = Regex("""move (\d+) from (\d+) to (\d+)""")
        fun from(string: String): Command {
            val patternMatch = commandPattern.matchEntire(string)!!
            return Command(patternMatch.groupValues[1].toInt(), patternMatch.groupValues[2].toInt() - 1, patternMatch.groupValues[3].toInt() - 1)
        }
    }
}