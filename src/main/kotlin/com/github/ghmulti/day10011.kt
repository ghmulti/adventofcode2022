package com.github.ghmulti

import kotlin.math.abs

@Suppress("unused")
private val testFile = """
1
2
-3
3
-2
0
4
""".trimIndent()

private data class IndWrapper(val value: Int, val index: Int)

private data class Node(
    var value: IndWrapper,
    var next: Node? = null,
    var prev: Node? = null,
)

private class LinkedList(
    val seq: List<IndWrapper>,
    val nodes: List<Node> = seq.map { Node(value = it) }.also {
        it.windowed(size = 2, step = 1).forEach { nodes ->
            val first = nodes.first()
            val last = nodes.last()
            first.next = last
            last.prev = first
        }
        it.last().next = it.first()
        it.first().prev = it.last()
    }
)

private fun LinkedList.nodeValues() = nodes.map { it.value }

private fun Node.swapRight() {
    val temp = next?.value ?: error("not expected")
    next?.value = value
    value = temp
}

private fun Node.swapLeft() {
    val temp = prev?.value ?: error("not expected")
    prev?.value = value
    value = temp
}

fun main() {
//    val seq = testFile.lines().mapIndexed { ind, i -> IndWrapper(index = ind, value = i.toInt()) }
    val seq = "day10011.txt".pathTo().toFile().readLines().mapIndexed { ind, i -> IndWrapper(ind, i.toInt()) }
    val decoded = seq.fold(LinkedList(seq)) { acc, el ->
        val movement = el.value
        if (movement == 0) {
            return@fold acc
        }

        (1..abs(movement)).forEach { _ ->
            val targetNode = acc.nodes.first { it.value.index == el.index }
            if (movement < 0) {
                targetNode.swapLeft()
            } else {
                targetNode.swapRight()
            }
        }

        acc
    }

    val decodedSeq = sequence { while(true) yieldAll(decoded.nodeValues()) }
    val res = listOf(
        decodedSeq.dropWhile { it.value != 0 }.drop(1000).first(),
        decodedSeq.dropWhile { it.value != 0 }.drop(2000).first(),
        decodedSeq.dropWhile { it.value != 0 }.drop(3000).first()
    )
    println(res.map { it.value })
    "The sum of the three numbers that form the grove coordinates is ${res.sumOf { it.value }}".cowsay("day 20")
}