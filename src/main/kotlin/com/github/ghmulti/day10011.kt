package com.github.ghmulti

import kotlin.math.abs

val testFile = """
1
2
-3
3
-2
0
4
""".trimIndent()

private class Node(
    var value: Int,
    var next: Node? = null,
    var prev: Node? = null,
)

private class LinkedList(
    val seq: List<Int>,
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
    val seq = testFile.lines().map { it.toInt() }
//    val seq = "day10011.txt".pathTo().toFile().readLines().map { it.toInt() }
//    println(seq)
//    val ll = LinkedList(seq)
//    val decoded = seq.fold(ll) { acc, el ->
////        println("Before moving $el: $acc")
//        val movement = el % (acc.nodes.size - 1)
//        if (movement == 0) {
//            return@fold  acc
//        }
//
//        (1..abs(movement)).forEach { _ ->
//            val targetNode = ll.nodes.first { it.value == el }
//            if (movement < 0) {
//                targetNode.swapLeft()
//            } else {
//                targetNode.swapRight()
//            }
//        }
////        println("After moving $el: ${acc.nodeValues()}")
//        acc
//    }

//    println(decoded)
//    val decodedSeq = sequence { while(true) yieldAll(decoded.nodeValues()) }
//    val res = listOf(
//        decodedSeq.dropWhile { it != 0 }.drop(1000).first(),
//        decodedSeq.dropWhile { it != 0 }.drop(2000).first(),
//        decodedSeq.dropWhile { it != 0 }.drop(3000).first()
//    )
//    println(res) // 5938 low
//    "The sum of the three numbers that form the grove coordinates is ${res.sum()}".cowsay("day 20")

    val size = seq.size - 1
    val decoded = seq.fold(seq.toMutableList()) { acc, el ->
        val movement = el % size
        if (movement == 0) {
            return@fold acc
        }
        val indexOfElement = acc.indexOf(el)
        acc.removeAt(indexOfElement)
        val newIndex = indexOfElement + movement
        if (newIndex in 0..size) {
            acc.add(newIndex, el)
        } else if (newIndex > size) {
            val n = newIndex - size
            acc.add(n, el)
        } else {
            val n = size + newIndex
            acc.add(n, el)
        }
        println("After moving $el: ${acc}")
        acc
    }

    val decodedSeq = sequence { while(true) yieldAll(decoded) }
    val res = listOf(
        decodedSeq.dropWhile { it != 0 }.drop(1000).first(),
        decodedSeq.dropWhile { it != 0 }.drop(2000).first(),
        decodedSeq.dropWhile { it != 0 }.drop(3000).first()
    )
    println(res) // 5938 low
    "The sum of the three numbers that form the grove coordinates is ${res.sum()}".cowsay("day 20")
}