package com.github.ghmulti

private fun List<String>.parseStacks(): Map<Int, ArrayDeque<String>> {
    val stackMap = mutableMapOf<Int, ArrayDeque<String>>()
    reversed().forEach { line ->
        line.windowed(size = 3, step = 4).forEachIndexed { index, block ->
            if (block.isBlank()) {
                return@forEachIndexed
            }
            stackMap.apply {
                val deque = getOrDefault(index+1, ArrayDeque())
                deque.addFirst(block)
                put(index+1, deque)
            }

        }
    }
    return stackMap
}

private val commandRegexp: Regex = Regex("move (\\d+) from (\\d+) to (\\d+)")

private fun String.parseCommand(): Command {
    return commandRegexp.find(this)?.groupValues?.takeLast(3)
        ?.let { Command(amount = it[0].toInt(), it[1].toInt(), it[2].toInt()) } ?: error("Invalid command")
}

private fun readDeques() = "day0101.txt".pathTo().toFile().useLines { lines ->
    val stacks = lines.takeWhile { it.isNotEmpty() }.toMutableList().apply { removeLast() }
    println("Given:")
    stacks.forEach { println(it) }
    val deques = stacks.parseStacks()
    println("First deque: ${deques[1]}, last deque: ${deques[9]}")
    return@useLines deques
}

fun day0101() {
    run {
        val deques = readDeques()
        "day0101.txt".pathTo().toFile().useLines { lines ->
            lines.dropWhile { !it.contains("move") }.forEachIndexed { index, line ->
                val command = line.parseCommand()
                //println("Command: $command [$index]")
                deques.apply {
                    val dequeFrom = getValue(command.from)
                    val dequeTo = getValue(command.to)
                    for (i in 1..command.amount) {
                        dequeTo.addFirst(dequeFrom.removeFirst())
                    }
                }
            }
        }

        (1..9).joinToString("") { deques.getValue(it).first() }.replace("[", "").replace("]", "")
            .let { result -> "Top of each stack after rearrangement: $result".cowsay("day5") }
    }

    run {
        val deques = readDeques()
        "day0101.txt".pathTo().toFile().useLines { lines ->
            lines.dropWhile { !it.contains("move") }.forEachIndexed { index, line ->
                val command = line.parseCommand()
                //println("Command: $command [$index]")
                deques.apply {
                    val dequeFrom = getValue(command.from)
                    val dequeTo = getValue(command.to)
                    val tempStack = ArrayDeque<String>()
                    for (i in 1..command.amount) {
                        tempStack.addLast(dequeFrom.removeFirst())
                    }
                    for (e in tempStack.reversed()) {
                        dequeTo.addFirst(e)
                    }
                }
            }
        }

        (1..9).joinToString("") { deques.getValue(it).first() }.replace("[", "").replace("]", "")
            .let { result -> "Top of each stack after rearrangement: $result".cowsay("day5") }
    }
}

private data class Command(
    val amount: Int,
    val from: Int,
    val to: Int
)