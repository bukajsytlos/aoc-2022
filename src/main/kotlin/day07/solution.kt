package day07

import java.io.File

fun main() {
    val root = Node.Directory("")
    var currentDirectory: Node.Directory = root

    File("src/main/kotlin/day07/input.txt").useLines { seq ->
        val iterator = seq.iterator()
        while (iterator.hasNext()) {
            val lastCommand = iterator.next()
            when {
                lastCommand == "$ cd /" -> currentDirectory = root
                lastCommand.startsWith("$ cd ..") -> currentDirectory = currentDirectory.parent!!
                lastCommand.startsWith("$ cd ") -> currentDirectory =
                    currentDirectory.getDirectory(lastCommand.substringAfter("$ cd "))

                lastCommand.startsWith("$ ls") -> {
                    while (iterator.hasNext()) {
                        val output = iterator.next()
                        if (output.startsWith("$")) {
                            when {
                                output == "$ cd /" -> currentDirectory = root
                                output.startsWith("$ cd ..") -> currentDirectory = currentDirectory.parent!!
                                output.startsWith("$ cd ") -> currentDirectory =
                                    currentDirectory.getDirectory(output.substringAfter("$ cd "))
                            }
                            break
                        }
                        currentDirectory.add(output.toNode(currentDirectory))
                    }
                }
            }
        }
    }
    println(getSize(root))

    val freeSpace: Long = 30_000_000L - (70_000_000L - root.getFilesSize())
    println(getNodesOfAtLeastSize(root, freeSpace).minOf { it.getFilesSize() })
}

fun getSize(node: Node.Directory): Long {
    var totalSize = 0L
    val filesSize = node.getFilesSize()
    if (filesSize <= 100_000) {
        totalSize += filesSize
    }
    totalSize += node.getDirectories().sumOf { getSize(it) }
    return totalSize
}

fun getNodesOfAtLeastSize(node: Node.Directory, minSize: Long): List<Node.Directory> {
    val matchedNodes = mutableListOf<Node.Directory>()
    val filesSize = node.getFilesSize()
    if (filesSize >= minSize) {
        matchedNodes.add(node)
    }
    node.getDirectories().forEach {
        matchedNodes.addAll(getNodesOfAtLeastSize(it, minSize))
    }
    return matchedNodes
}

sealed class Node(val parent: Directory? = null) {
    abstract fun getFilesSize(): Int
    class Directory(
        val name: String,
        private val children: MutableList<Node> = mutableListOf(),
        parent: Directory? = null
    ) : Node(parent) {
        override fun getFilesSize(): Int = children.sumOf { it.getFilesSize() }
        fun add(node: Node) = children.add(node)
        fun getDirectories(): List<Directory> = children.filterIsInstance(Directory::class.java).toList()
        fun getDirectory(name: String) = getDirectories().first { it.name == name }
    }

    class File(
        val name: String, val size: Int,
        parent: Directory? = null
    ) : Node(parent) {
        override fun getFilesSize(): Int = size
    }
}

fun String.toNode(currentDirectory: Node.Directory): Node = when {
    startsWith("dir ") -> Node.Directory(name = substringAfter("dir "), parent = currentDirectory)
    else -> Node.File(name = substringAfter(" "), size = substringBefore(" ").toInt(), parent = currentDirectory)
}