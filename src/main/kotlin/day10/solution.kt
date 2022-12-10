package day10

import java.io.File
import kotlin.math.abs

fun main() {
    val instructions = File("src/main/kotlin/day10/input.txt").readLines().map {
        when {
            it.startsWith("addx ") -> Instruction.Add(it.substringAfter("addx ").toInt())
            else -> Instruction.Noop
        }
    }
    val instructionsQueue = ArrayDeque(instructions)

    var registerX = 1
    var ticks = 1
    var signalStrengths = 0
    var currentInstruction: Instruction? = null
    while (instructionsQueue.isNotEmpty()) {
        if ((ticks - 20) % 40 == 0) {
            signalStrengths += ticks * registerX
        }
        if (abs((ticks - 1) % 40 - registerX) < 2) {
            print("█")
        } else {
            print(" ")
        }
        if ((ticks) % 40 == 0) {
            println()
        }
        if (currentInstruction == null) {
            currentInstruction = instructionsQueue.removeFirst()
        }
        if (currentInstruction.cycles > 1) {
            currentInstruction.cycles--
        } else {
            if (currentInstruction is Instruction.Add) {
                registerX += currentInstruction.amount
            }
            currentInstruction = instructionsQueue.removeFirst()
        }
        ticks++
    }
    println()
    println(signalStrengths)
}

sealed class Instruction(var cycles: Int) {
    object Noop : Instruction(1)
    class Add(val amount: Int) : Instruction(2)
}