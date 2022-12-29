package day25

import java.io.File

typealias Snafu = String

fun main() {
    val sum = File("src/main/kotlin/day25/input.txt").readLines()
        .sumOf { line -> line.toDec() }
    println(sum.toSnafu())
}

fun Snafu.toDec() = fold(0L) { acc, c ->
    5 * acc + when (c) {
        '=' -> -2
        '-' -> -1
        else -> c.digitToInt()
    }
}

fun Long.toSnafu(): String = buildString {
    var remainder = this@toSnafu
    while (remainder != 0L) {
        append("012=-"[remainder.mod(5)])
        remainder = (remainder + 2L).floorDiv(5)
    }
    reverse()
}