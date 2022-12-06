package day06

import java.io.File

fun main() {

    val signal = File("src/main/kotlin/day06/input.txt").readLines().first()

    val packetMarkerPosition = signal.windowed(4).withIndex().first {
        it.value.toList().size == it.value.toSet().size
    }.index + 4
    println(packetMarkerPosition)

    val messageMarkerPosition = signal.windowed(14).withIndex().first {
        it.value.toList().size == it.value.toSet().size
    }.index + 14
    println(messageMarkerPosition)
}