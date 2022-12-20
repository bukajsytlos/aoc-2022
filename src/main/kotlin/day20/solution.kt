package day20

import java.io.File


fun main() {
    val sourceNumbers = File("src/main/kotlin/day20/input.txt").readLines().map { it.toLong() }

    val sourceNodes = sourceNumbers.map { Node(it) }
    sourceNodes.wire()
    sourceNodes.mix(1)

    val zeroNode = sourceNodes.first { it.value == 0L }
    println(zeroNode.groveCoordinates())

    val nodesWithDecryptionKey = sourceNumbers.map { Node(it * 811589153L) }
    nodesWithDecryptionKey.wire()
    nodesWithDecryptionKey.mix(10)

    val zeroNodeWithDecryptionKey = nodesWithDecryptionKey.first { it.value == 0L }
    println(zeroNodeWithDecryptionKey.groveCoordinates())
}

fun Node.groveCoordinates(): Long = nextNth(1000).value + nextNth(2000).value + nextNth(3000).value

fun List<Node>.mix(times: Int) = repeat(times) {
    forEach {
        val shift = it.value.mod(this.size - 1)
        it.nextNth(shift)
        it.swap(it.nextNth(shift))
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

    fun swap(node: Node) {
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