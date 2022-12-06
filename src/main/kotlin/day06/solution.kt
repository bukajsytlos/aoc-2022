package day06

import java.io.File

fun main() {

    val signal = File("src/main/kotlin/day06/input.txt").readLines().first()

    val packetMarkerPosition = signal.findMarker(4)
    println(packetMarkerPosition)

    val messageMarkerPosition = signal.findMarker(14)
    println(messageMarkerPosition)
}

private fun String.findMarker(packetLength: Int) = this.windowed(packetLength).withIndex().first {
    it.value.toList().size == it.value.toSet().size
}.index + packetLength