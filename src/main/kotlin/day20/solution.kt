package day20

import java.io.File


fun main() {
    val sourceNumbers = File("src/main/kotlin/day20/input.txt").readLines().map { it.toLong() }

    val sourceNodes = sourceNumbers.map { Node(it) }
    sourceNodes.wire()
    sourceNodes.mix(1, 1)

    val zeroNode = sourceNodes.first { it.value == 0L }
    println(zeroNode.groveCoordinates(1))

    val decryptionKey = 811589153L
    val nodes2 = sourceNumbers.map { Node(it) }
    nodes2.wire()
    nodes2.mix(decryptionKey, 10)

    val zeroNode2 = nodes2.first { it.value == 0L }
    println(zeroNode2.groveCoordinates(decryptionKey))
}

fun Node.groveCoordinates(decryptionKey: Long): Long = (nextNth(1000).value + nextNth(2000).value + nextNth(3000).value) * decryptionKey

fun List<Node>.mix(decryptionKey: Long, times: Int) = repeat(times) {
    forEach {
        val shift = (it.value * decryptionKey).mod(this.size - 1)
        it.moveAfter(it.nextNth(shift))
    }
}

fun List<Node>.wire() {
    this.windowed(2).forEach {
        it[0].next = it[1]
        it[1].previous = it[0]
    }.also {
        this.first().previous = this.last()
        this.last().next = this.first()
    }
}

class Node(val value: Long) {
    lateinit var previous: Node
    lateinit var next: Node

    fun moveAfter(node: Node) {
        if (node == this) {
            return
        }

        previous.next = next
        next.previous = previous
        node.next.previous = this
        this.next = node.next
        node.next = this
        this.previous = node
    }

    fun nextNth(n: Int): Node {
        var node = this
        repeat(n) {
            node = node.next
        }
        return node
    }
}